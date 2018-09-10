/*
 * Copyright 2013 gitblit.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.service.service.git;

import com.github.wxiaoqi.security.common.context.BaseContextHandler;
import com.service.service.Constants;
import com.service.service.Constants.AccessRestrictionType;
import com.service.service.IStoredSettings;
import com.service.service.Keys;
import com.service.service.async.PushAsync;
import com.service.service.entity.TaskEntity;
import com.service.service.extensions.ReceiveHook;
import com.service.service.entity.TicketModel;
import com.service.service.entity.TicketModel.Change;
import com.service.service.entity.TicketModel.Field;
import com.service.service.entity.TicketModel.Status;
import com.service.service.entity.TicketModel.TicketLink;
import com.service.service.entity.UserModel;
import com.service.service.managers.IWorkHub;
import com.service.service.tickets.ITicketService;
import com.service.service.tickets.TicketNotifier;
import com.service.service.utils.*;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.PostReceiveHook;
import org.eclipse.jgit.transport.PreReceiveHook;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.ReceiveCommand.Result;
import org.eclipse.jgit.transport.ReceivePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

import static org.eclipse.jgit.transport.BasePackPushConnection.CAPABILITY_SIDE_BAND_64K;


/**
 * GitblitReceivePack 进程接受命令.  执行Groovy pre-
 * 和 post- receive hooks.
 * <p>
 * The general execution flow is:
 * <ol>
 * <li>onPreReceive()</li>
 * <li>executeCommands()</li>
 * <li>onPostReceive()</li>
 * </ol>
 *
 * @author James Moger
 */
public class GitblitReceivePack extends ReceivePack implements PreReceiveHook, PostReceiveHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitblitReceivePack.class);

    protected final TaskEntity repository;

    protected final UserModel user;

    protected final File groovyDir;

    protected String gitblitUrl;

    protected final IStoredSettings settings;

    protected final IWorkHub gitblit;

    protected final ITicketService ticketService;

    protected final TicketNotifier ticketNotifier;

    protected GroovyScriptEngine gse;

    private PushAsync pushAsync;

    @Autowired
    public GitblitReceivePack(
            IWorkHub gitblit,
            Repository db,
            TaskEntity repository,
            UserModel user) {

        super(db);
        this.settings = gitblit.getSettings();
        this.gitblit = gitblit;
        this.repository = repository;
        this.user = user;
        this.groovyDir = gitblit.getHooksFolder();
        // set Grape root
        File grapeRoot = gitblit.getGrapesFolder();
        grapeRoot.mkdirs();
        System.setProperty("grape.root", grapeRoot.getAbsolutePath());

        this.ticketService = null;
        this.ticketNotifier = null;

        // set advanced ref permissions
        setAllowCreates(user.canCreateRef(repository));
        setAllowDeletes(user.canDeleteRef(repository));
        setAllowNonFastForwards(user.canRewindRef(repository));

        int maxObjectSz = settings.getInteger(Keys.git.maxObjectSizeLimit, -1);
        if (maxObjectSz >= 0) {
            setMaxObjectSizeLimit(maxObjectSz);
        }
        int maxPackSz = settings.getInteger(Keys.git.maxPackSizeLimit, -1);
        if (maxPackSz >= 0) {
            setMaxPackSizeLimit(maxPackSz);
        }
        setCheckReceivedObjects(settings.getBoolean(Keys.git.checkReceivedObjects, true));
        setCheckReferencedObjectsAreReachable(settings.getBoolean(Keys.git.checkReferencedObjectsAreReachable, true));

        // setup pre and post receive hook
        setPreReceiveHook(this);
        setPostReceiveHook(this);

        this.pushAsync = pushAsync;
    }

    /**
     * 如果用户被允许将接收命令应用到该命令，则返回true存储库。
     *
     * @param commands
     * @return true if the user may push these commands
     */
    protected boolean canPush(Collection<ReceiveCommand> commands) {
        // TODO 考虑支持分支权限 (issue-36)
        // Not sure if that should be Gerrit-style, refs/meta/config, or
        // gitolite-style, permissions in users.conf
        //
        // How could commands be empty?
        //
        // Because a subclass, like PatchsetReceivePack, filters receive
        // commands before this method is called.  This makes it possible for
        // this method to test an empty list.  In this case, we assume that the
        // subclass receive pack properly enforces push restrictions. for the
        // ref.
        //
        // The empty test is not explicitly required, it's written here to
        // clarify special-case behavior.

        return commands.isEmpty() ? true : user.canPush(repository);
    }

    /**
     * 插装点，传入的推送事件已经被解析，经过验证，创建的对象和refs都没有更新。
     * 可能会使用此方法来强制执行分支-写权限模型。
     */
    @Override
    public void onPreReceive(ReceivePack rp, Collection<ReceiveCommand> commands) {

        if (commands.size() == 0) {
            // no receive commands to process
            // this can happen if receive pack subclasses intercept and filter
            // the commands
            LOGGER.debug("skipping pre-receive processing, no refs created, updated, or removed");
            return;
        }

        if (repository.isMirror()) {
            // repository is a mirror
            for (ReceiveCommand cmd : commands) {
                sendRejection(cmd, "Gitblit does not allow pushes to \"{0}\" because it is a mirror!", repository.getTaskName());
            }
            return;
        }

        if (repository.isFrozen()) {
            // repository is frozen/readonly
            for (ReceiveCommand cmd : commands) {
                sendRejection(cmd, "Gitblit does not allow pushes to \"{0}\" because it is frozen!", repository.getTaskName());
            }
            return;
        }

        if (!repository.isBare()) {
            // repository has a working copy
            for (ReceiveCommand cmd : commands) {
                sendRejection(cmd, "Gitblit does not allow pushes to \"{0}\" because it has a working copy!", repository.getTaskName());
            }
            return;
        }

        if (!canPush(commands)) {
            // user does not have push permissions
            for (ReceiveCommand cmd : commands) {
                sendRejection(cmd, "User \"{0}\" does not have push permissions for \"{1}\"!", user.getUserId(), repository.getTaskName());
            }
            return;
        }

        if (repository.getAccessRestriction().atLeast(AccessRestrictionType.PUSH) && repository.isVerifyCommitter()) {
            // enforce committer verification
            if (StringUtils.isEmpty(user.getEmailAddress())) {
                // reject the push because the pushing account does not have an email address
                for (ReceiveCommand cmd : commands) {
                    sendRejection(cmd, "Sorry, the account \"{0}\" does not have an email address set for committer verification!", user.getUserId());
                }
                return;
            }

            // Optionally enforce that the committer of first parent chain
            // match the account being used to push the commits.
            //
            // This requires all merge commits are executed with the "--no-ff"
            // option to force a merge commit even if fast-forward is possible.
            // This ensures that the chain first parents has the commit
            // identity of the merging user.
            boolean allRejected = false;
            for (ReceiveCommand cmd : commands) {
                String firstParent = null;
                try {
                    List<RevCommit> commits = JGitUtils.getRevLog(rp.getRepository(), cmd.getOldId().name(), cmd.getNewId().name());
                    for (RevCommit commit : commits) {

                        if (firstParent != null) {
                            if (!commit.getName().equals(firstParent)) {
                                // ignore: commit is right-descendant of a merge
                                continue;
                            }
                        }

                        // update expected next commit id
                        if (commit.getParentCount() == 0) {
                            firstParent = null;
                        } else {
                            firstParent = commit.getParents()[0].getId().getName();
                        }

                        PersonIdent committer = commit.getCommitterIdent();
                        if (!user.is(committer.getName(), committer.getEmailAddress())) {
                            // verification failed
                            String reason = MessageFormat.format("{0} by {1} <{2}> was not committed by {3} ({4}) <{5}>",
                                    commit.getId().name(), committer.getName(), StringUtils.isEmpty(committer.getEmailAddress()) ? "?" : committer.getEmailAddress(), user.getDisplayName(), user.getUserId(), user.getEmailAddress());
                            LOGGER.warn(reason);
                            cmd.setResult(Result.REJECTED_OTHER_REASON, reason);
                            allRejected &= true;
                            break;
                        } else {
                            allRejected = false;
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to verify commits were made by pushing user", e);
                }
            }

            if (allRejected) {
                // all ref updates rejected, abort
                return;
            }
        }

        for (ReceiveCommand cmd : commands) {
            String ref = cmd.getRefName();
            if (ref.startsWith(Constants.R_HEADS)) {
                switch (cmd.getType()) {
                    case UPDATE_NONFASTFORWARD:
                    case DELETE:
                        // reset branch commit cache on REWIND and DELETE
                        CommitCache.instance().clear(repository.getTaskName(), ref);
                        break;
                    default:
                        break;
                }
            }
//            else if (ref.equals(BranchTicketServiceTemp.BRANCH)) {
//                // ensure pushing user is an administrator OR an owner
//                // i.e. prevent ticket tampering
//                boolean permitted = user.canAdmin() || repository.isOwner(user.getUserId());
//                if (!permitted) {
//                    sendRejection(cmd, "{0} is not permitted to push to {1}", user.getUserId(), ref);
//                }
//            }
            else if (ref.startsWith(Constants.R_FOR)) {
                // prevent accidental push to refs/for
                sendRejection(cmd, "{0} is not configured to receive patchsets", repository.getTaskName());
            }
        }

        // call pre-receive plugins
        for (ReceiveHook hook : gitblit.getExtensions(ReceiveHook.class)) {
            try {
                hook.onPreReceive(this, commands);
            } catch (Exception e) {
                LOGGER.error("Failed to execute extension", e);
            }
        }

        Set<String> scripts = new LinkedHashSet<String>();
        scripts.addAll(gitblit.getPreReceiveScriptsInherited(repository));
        if (!ArrayUtils.isEmpty(repository.getPreReceiveScripts())) {
            scripts.addAll(repository.getPreReceiveScripts());
        }
        runGroovy(commands, scripts);
        for (ReceiveCommand cmd : commands) {
            if (!Result.NOT_ATTEMPTED.equals(cmd.getResult())) {
                LOGGER.warn(MessageFormat.format("{0} {1} because \"{2}\"", cmd.getNewId()
                        .getName(), cmd.getResult(), cmd.getMessage()));
            }
        }
    }

    /**
     * 钩子，在整个push过程完结以后运行，可以用来更新其他系统服务或者通知用户。
     */
    @Override
    public void onPostReceive(ReceivePack rp, Collection<ReceiveCommand> commands) {
        if (commands.size() == 0) {
            LOGGER.debug("skipping post-receive processing, no refs created, updated, or removed");
            return;
        }

        logRefChange(commands);
        updateIncrementalPushTags(commands);
        updateGitblitRefLog(commands);

        // 检查推送到BranchTicketService分支的更新，如果BranchTicketService是活动的，它将根据适当的方式重新索引
//        for (ReceiveCommand cmd : commands) {
//            if (Result.OK.equals(cmd.getResult())
//                    && BranchTicketServiceTemp.BRANCH.equals(cmd.getRefName())) {
//                rp.getRepository().fireEvent(new ReceiveCommandEvent(repository, cmd));
//            }
//        }
        Change change = new Change(BaseContextHandler.getUserID());
        change.setField(Field.title, "请求合并");
        change.setField(Field.body, "来自客户端的合并请求");
        change.setField(Field.mergeTo, "master");

        gitblit.getTicketService().createTicket(gitblit.getRepositoryModel(repository.getTaskName()), 0L, change);
        // 调用post-receive插件
        for (ReceiveHook hook : gitblit.getExtensions(ReceiveHook.class)) {
            try {
                hook.onPostReceive(this, commands);
            } catch (Exception e) {
                LOGGER.error("Failed to execute extension", e);
            }
        }

        // run Groovy hook scripts
        Set<String> scripts = new LinkedHashSet<String>();
        scripts.addAll(gitblit.getPostReceiveScriptsInherited(repository));
        if (!ArrayUtils.isEmpty(repository.getPostReceiveScripts())) {
            scripts.addAll(repository.getPostReceiveScripts());
        }
        runGroovy(commands, scripts);
    }

    /**
     * 在容器日志中记录ref更改。
     *
     * @param commands
     */
    protected void logRefChange(Collection<ReceiveCommand> commands) {
        boolean isRefCreationOrDeletion = false;

        // log ref changes
        for (ReceiveCommand cmd : commands) {

            if (Result.OK.equals(cmd.getResult())) {
                // add some logging for important ref changes
                switch (cmd.getType()) {
                    case DELETE:
                        LOGGER.info(MessageFormat.format("{0} DELETED {1} in {2} ({3})", user.getUserId(), cmd.getRefName(), repository.getTaskName(), cmd.getOldId().name()));
                        isRefCreationOrDeletion = true;
                        break;
                    case CREATE:
                        LOGGER.info(MessageFormat.format("{0} CREATED {1} in {2}", user.getUserId(), cmd.getRefName(), repository.getTaskName()));
                        isRefCreationOrDeletion = true;
                        break;
                    case UPDATE:
                        LOGGER.info(MessageFormat.format("{0} UPDATED {1} in {2} (from {3} to {4})", user.getUserId(), cmd.getRefName(), repository.getTaskName(), cmd.getOldId().name(), cmd.getNewId().name()));
                        break;
                    case UPDATE_NONFASTFORWARD:
                        LOGGER.info(MessageFormat.format("{0} UPDATED NON-FAST-FORWARD {1} in {2} (from {3} to {4})", user.getUserId(), cmd.getRefName(), repository.getTaskName(), cmd.getOldId().name(), cmd.getNewId().name()));
                        break;
                    default:
                        break;
                }
            }
        }

        if (isRefCreationOrDeletion) {
            gitblit.resetRepositoryCache(repository.getTaskName());
        }
    }

    /**
     * 可以选择更新增量推送标记。
     *
     * @param commands
     */
    protected void updateIncrementalPushTags(Collection<ReceiveCommand> commands) {
        if (!repository.isUseIncrementalPushTags()) {
            return;
        }

        // tag each pushed branch tip
        String emailAddress = user.getEmailAddress() == null ? getRefLogIdent().getEmailAddress() : user.getEmailAddress();
        PersonIdent userIdent = new PersonIdent(user.getDisplayName(), emailAddress);

        for (ReceiveCommand cmd : commands) {
            if (!cmd.getRefName().startsWith(Constants.R_HEADS)) {
                // only tag branch ref changes
                continue;
            }

            if (!ReceiveCommand.Type.DELETE.equals(cmd.getType())
                    && ReceiveCommand.Result.OK.equals(cmd.getResult())) {
                String objectId = cmd.getNewId().getName();
                String branch = cmd.getRefName().substring(Constants.R_HEADS.length());
                // get translation based on the server's locale setting
//				String template = Translation.get("gb.incrementalPushTagMessage");
                String msg = MessageFormat.format("", branch);
                String prefix;
                if (StringUtils.isEmpty(repository.getIncrementalPushTagPrefix())) {
                    prefix = settings.getString(Keys.git.defaultIncrementalPushTagPrefix, "r");
                } else {
                    prefix = repository.getIncrementalPushTagPrefix();
                }

                JGitUtils.createIncrementalRevisionTag(
                        getRepository(),
                        objectId,
                        userIdent,
                        prefix,
                        "0",
                        msg);
            }
        }
    }

    /**
     * Update Gitblit's internal reflog.
     *
     * @param commands
     */
//	protected void updateGitblitRefLog(Collection<ReceiveCommand> commands) {
//		try {
//			RefLogUtils.updateRefLog(user, getRepository(), commands);
//			LOGGER.debug(MessageFormat.format("{0} reflog updated", repository.name));
//		} catch (Exception e) {
//			LOGGER.error(MessageFormat.format("Failed to update {0} reflog", repository.name), e);
//		}
//	}

    /**
     * 执行命令以更新引用。
     */
    @Override
    protected void executeCommands() {
        List<ReceiveCommand> toApply = filterCommands(Result.NOT_ATTEMPTED);
        if (toApply.isEmpty()) {
            return;
        }

        ProgressMonitor updating = NullProgressMonitor.INSTANCE;
        boolean sideBand = isCapabilityEnabled(CAPABILITY_SIDE_BAND_64K);
//		if (sideBand) {
//			SideBandProgressMonitor pm = new SideBandProgressMonitor(msgOut);
//			pm.setDelayStart(250, TimeUnit.MILLISECONDS);
//			updating = pm;
//		}

        BatchRefUpdate batch = getRepository().getRefDatabase().newBatchUpdate();
        batch.setAllowNonFastForwards(isAllowNonFastForwards());
        batch.setRefLogIdent(getRefLogIdent());
        batch.setRefLogMessage("push", true);

        for (ReceiveCommand cmd : toApply) {
            if (Result.NOT_ATTEMPTED != cmd.getResult()) {
                // Already rejected by the core receive process.
                continue;
            }
            batch.addCommand(cmd);
        }

        if (!batch.getCommands().isEmpty()) {
            try {
                batch.execute(getRevWalk(), updating);
            } catch (IOException err) {
                for (ReceiveCommand cmd : toApply) {
                    if (cmd.getResult() == Result.NOT_ATTEMPTED) {
                        sendRejection(cmd, "lock error: {0}", err.getMessage());
                    }
                }
            }
        }

        //
        // if there are ref update receive commands that were
        // successfully processed and there is an active ticket service for the repository
        // then process any referenced tickets
        //
        if (ticketService != null) {
            List<ReceiveCommand> allUpdates = ReceiveCommand.filter(batch.getCommands(), Result.OK);
            if (!allUpdates.isEmpty()) {
                int ticketsProcessed = 0;
                for (ReceiveCommand cmd : allUpdates) {
                    switch (cmd.getType()) {
                        case CREATE:
                        case UPDATE:
                            if (cmd.getRefName().startsWith(Constants.R_HEADS)) {
                                Collection<TicketModel> tickets = processReferencedTickets(cmd);
                                ticketsProcessed += tickets.size();
                                for (TicketModel ticket : tickets) {
                                    ticketNotifier.queueMailing(ticket);
                                }
                            }
                            break;

                        case UPDATE_NONFASTFORWARD:
                            if (cmd.getRefName().startsWith(Constants.R_HEADS)) {
                                String base = JGitUtils.getMergeBase(getRepository(), cmd.getOldId(), cmd.getNewId());
                                List<TicketLink> deletedRefs = JGitUtils.identifyTicketsBetweenCommits(getRepository(), settings, base, cmd.getOldId().name());
                                for (TicketLink link : deletedRefs) {
                                    link.isDelete = true;
                                }
                                Change deletion = new Change(user.getUserId());
                                deletion.pendingLinks = deletedRefs;
                                ticketService.updateTicket(repository, 0, deletion);

                                Collection<TicketModel> tickets = processReferencedTickets(cmd);
                                ticketsProcessed += tickets.size();
                                for (TicketModel ticket : tickets) {
                                    ticketNotifier.queueMailing(ticket);
                                }
                            }
                            break;
                        case DELETE:
                            //Identify if the branch has been merged
                            SortedMap<Integer, String> bases = new TreeMap<Integer, String>();
                            try {
                                ObjectId dObj = cmd.getOldId();
                                Collection<Ref> tips = getRepository().getRefDatabase().getRefs(Constants.R_HEADS).values();
                                for (Ref ref : tips) {
                                    ObjectId iObj = ref.getObjectId();
                                    String mergeBase = JGitUtils.getMergeBase(getRepository(), dObj, iObj);
                                    if (mergeBase != null) {
                                        int d = JGitUtils.countCommits(getRepository(), getRevWalk(), mergeBase, dObj.name());
                                        bases.put(d, mergeBase);
                                        //All commits have been merged into some other branch
                                        if (d == 0) {
                                            break;
                                        }
                                    }
                                }

                                if (bases.isEmpty()) {
                                    //TODO: Handle orphan branch case
                                } else {
                                    if (bases.firstKey() > 0) {
                                        //Delete references from the remaining commits that haven't been merged
                                        String mergeBase = bases.get(bases.firstKey());
                                        List<TicketLink> deletedRefs = JGitUtils.identifyTicketsBetweenCommits(getRepository(),
                                                settings, mergeBase, dObj.name());

                                        for (TicketLink link : deletedRefs) {
                                            link.isDelete = true;
                                        }
                                        Change deletion = new Change(user.getUserId());
                                        deletion.pendingLinks = deletedRefs;
                                        ticketService.updateTicket(repository, 0, deletion);
                                    }
                                }

                            } catch (IOException e) {
                                LOGGER.error(null, e);
                            }
                            break;

                        default:
                            break;
                    }
                }

                if (ticketsProcessed == 1) {
                    sendInfo("1 ticket updated");
                } else if (ticketsProcessed > 1) {
                    sendInfo("{0} tickets updated", ticketsProcessed);
                }
            }

            // reset the ticket caches for the repository
            ticketService.resetCaches(repository);
        }
    }

    protected void setGitblitUrl(String url) {
        this.gitblitUrl = url;
    }

    public void sendRejection(final ReceiveCommand cmd, final String why, Object... objects) {
        String text;
        if (ArrayUtils.isEmpty(objects)) {
            text = why;
        } else {
            text = MessageFormat.format(why, objects);
        }
        cmd.setResult(Result.REJECTED_OTHER_REASON, text);
        LOGGER.error(text + " (" + user.getUserId() + ")");
    }

    public void sendHeader(String msg, Object... objects) {
        sendInfo("--> ", msg, objects);
    }

    public void sendInfo(String msg, Object... objects) {
        sendInfo("    ", msg, objects);
    }

    private void sendInfo(String prefix, String msg, Object... objects) {
        String text;
        if (ArrayUtils.isEmpty(objects)) {
            text = msg;
            super.sendMessage(prefix + msg);
        } else {
            text = MessageFormat.format(msg, objects);
            super.sendMessage(prefix + text);
        }
        if (!StringUtils.isEmpty(msg)) {
            LOGGER.info(text + " (" + user.getUserId() + ")");
        }
    }

    public void sendError(String msg, Object... objects) {
        String text;
        if (ArrayUtils.isEmpty(objects)) {
            text = msg;
            super.sendError(msg);
        } else {
            text = MessageFormat.format(msg, objects);
            super.sendError(text);
        }
        if (!StringUtils.isEmpty(msg)) {
            LOGGER.error(text + " (" + user.getUserId() + ")");
        }
    }

    /**
     * 更新Gitblit的内部reflog。
     *
     * @param commands
     */
    protected void updateGitblitRefLog(Collection<ReceiveCommand> commands) {
        try {
            RefLogUtils.updateRefLog(user, getRepository(), commands);
            LOGGER.debug(MessageFormat.format("{0} reflog updated", repository.getTaskName()));
        } catch (Exception e) {
            LOGGER.error(MessageFormat.format("Failed to update {0} reflog", repository.getTaskName()), e);
        }
    }

    public IWorkHub getGitblit() {
        return gitblit;
    }

    public TaskEntity getRepositoryModel() {
        return repository;
    }

    public UserModel getUserModel() {
        return user;
    }

    /**
     * 自动关闭Ticket,并在提交信息中增加对Ticket的参考。
     *
     * @param cmd
     */
    private Collection<TicketModel> processReferencedTickets(ReceiveCommand cmd) {
        Map<Long, TicketModel> changedTickets = new LinkedHashMap<Long, TicketModel>();

        final RevWalk rw = getRevWalk();
        try {
            rw.reset();
            rw.markStart(rw.parseCommit(cmd.getNewId()));
            if (!ObjectId.zeroId().equals(cmd.getOldId())) {
                rw.markUninteresting(rw.parseCommit(cmd.getOldId()));
            }

            RevCommit c;
            while ((c = rw.next()) != null) {
                rw.parseBody(c);
                List<TicketLink> ticketLinks = JGitUtils.identifyTicketsFromCommitMessage(getRepository(), settings, c);
                if (ticketLinks == null) {
                    continue;
                }

                for (TicketLink link : ticketLinks) {

                    TicketModel ticket = ticketService.getTicket(repository, link.targetTicketId);
                    if (ticket == null) {
                        continue;
                    }

                    Change change = null;
                    String commitSha = c.getName();
                    String branchName = Repository.shortenRefName(cmd.getRefName());

                    switch (link.action) {
                        case Commit: {
                            //A commit can reference a ticket in any branch even if the ticket is closed.
                            //This allows developers to identify and communicate related issues
                            change = new Change(user.getUserId());
                            change.referenceCommit(commitSha);
                        }
                        break;

                        case Close: {
                            // As this isn't a patchset theres no merging taking place when closing a ticket
                            if (ticket.isClosed()) {
                                continue;
                            }

                            change = new Change(user.getUserId());
                            change.setField(Field.status, Status.Fixed);

                            if (StringUtils.isEmpty(ticket.getResponsible())) {
                                // unassigned tickets are assigned to the closer
                                change.setField(Field.responsible, user.getUserId());
                            }
                        }

                        default: {
                            //No action
                        }
                        break;
                    }

                    if (change != null) {
                        ticket = ticketService.updateTicket(repository, ticket.getNumber(), change);
                    }

                    if (ticket != null) {
                        sendInfo("");
                        sendHeader("#{0,number,0}: {1}", ticket.getNumber(), StringUtils.trimString(ticket.getTitle(), Constants.LEN_SHORTLOG));

                        switch (link.action) {
                            case Commit: {
                                sendInfo("referenced by push of {0} to {1}", commitSha, branchName);
                                changedTickets.put(ticket.getNumber(), ticket);
                            }
                            break;

                            case Close: {
                                sendInfo("closed by push of {0} to {1}", commitSha, branchName);
                                changedTickets.put(ticket.getNumber(), ticket);
                            }
                            break;

                            default: {
                            }
                        }

                        sendInfo(ticketService.getTicketUrl(ticket));
                        sendInfo("");
                    } else {
                        switch (link.action) {
                            case Commit: {
                                sendError("FAILED to reference ticket {0} by push of {1}", link.targetTicketId, commitSha);
                            }
                            break;

                            case Close: {
                                sendError("FAILED to close ticket {0} by push of {1}", link.targetTicketId, commitSha);
                            }
                            break;

                            default: {
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            LOGGER.error("Can't scan for changes to reference or close", e);
        } finally {
            rw.reset();
        }

        return changedTickets.values();
    }


    /**
     * 运行指定的Groovy钩子脚本。
     *
     * @param commands
     * @param scripts
     */
    private void runGroovy(Collection<ReceiveCommand> commands, Set<String> scripts) {
        if (scripts == null || scripts.size() == 0) {
            // no Groovy scripts to execute
            return;
        }

        Binding binding = new Binding();
        binding.setVariable("gitblit", gitblit);
        binding.setVariable("repository", repository);
        binding.setVariable("receivePack", this);
        binding.setVariable("user", user);
        binding.setVariable("commands", commands);
        binding.setVariable("url", gitblitUrl);
        binding.setVariable("logger", LOGGER);
        binding.setVariable("clientLogger", new ClientLogger(this));
        for (String script : scripts) {
            if (StringUtils.isEmpty(script)) {
                continue;
            }
            // allow script to be specified without .groovy extension
            // this is easier to read in the settings
            File file = new File(groovyDir, script);
            if (!file.exists() && !script.toLowerCase().endsWith(".groovy")) {
                file = new File(groovyDir, script + ".groovy");
                if (file.exists()) {
                    script = file.getName();
                }
            }
            try {
                Object result = gse.run(script, binding);
                if (result instanceof Boolean) {
                    if (!((Boolean) result)) {
                        LOGGER.error(MessageFormat.format(
                                "Groovy script {0} has failed!  Hook scripts aborted.", script));
                        break;
                    }
                }
            } catch (Exception e) {
                LOGGER.error(
                        MessageFormat.format("Failed to execute Groovy script {0}", script), e);
            }
        }
    }
}
