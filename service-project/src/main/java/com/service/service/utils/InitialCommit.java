package com.service.service.utils;

import com.service.service.Constants;
import com.service.service.Keys;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.UserModel;
import com.service.service.managers.IRepositoryManager;
import com.service.service.managers.IRuntimeManager;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheBuilder;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * @author workhub
 */
public class InitialCommit {

    IRuntimeManager runtimeManager;
    IRepositoryManager repositoryManager;

    public boolean initialCommit(TaskEntity repositoryModel,
                                    UserModel user) {
        boolean success = false;
        Repository db = repositoryManager.getRepository(repositoryModel.getTaskName());
        ObjectInserter odi = db.newObjectInserter();

        try {
            String email = Optional.ofNullable(user.getEmailAddress()).orElse(user.getUserId() + "@" + "workhub");
            PersonIdent author = new PersonIdent(user.getDisplayName(), email);

            DirCache newIndex = DirCache.newInCore();
            DirCacheBuilder indexBuilder = newIndex.builder();

            // insert a README
            String title = StringUtils.stripDotGit(StringUtils.getLastPathElement(repositoryModel.getTaskName()));
            String description = repositoryModel.getTaskDes() == null ? "" : repositoryModel.getTaskDes();
            String readme = String.format("## %s\n\n%s\n\n", title, description);
            byte [] bytes = readme.getBytes(Constants.ENCODING);

            DirCacheEntry entry = new DirCacheEntry("README.md");
            entry.setLength(bytes.length);
            entry.setLastModified(System.currentTimeMillis());
            entry.setFileMode(FileMode.REGULAR_FILE);
            entry.setObjectId(odi.insert(org.eclipse.jgit.lib.Constants.OBJ_BLOB, bytes));

            indexBuilder.add(entry);

            /*
            //这里的bytes和entry用之前的
            File dir = runtimeManager.getFileOrFolder(Keys.git.gitignoreFolder, "${baseFolder}/gitignore");
            File file = new File(dir, ".gitignore");
            if (file.exists() && file.length() > 0) {
                bytes = FileUtils.readContent(file);
                if (!ArrayUtils.isEmpty(bytes)) {
                    entry = new DirCacheEntry(".gitignore");
                    entry.setLength(bytes.length);
                    entry.setLastModified(System.currentTimeMillis());
                    entry.setFileMode(FileMode.REGULAR_FILE);
                    entry.setObjectId(odi.insert(org.eclipse.jgit.lib.Constants.OBJ_BLOB, bytes));

                    indexBuilder.add(entry);
                }
            }


            Config config = new Config();
            config.setString("gitflow", null, "masterBranch", Constants.MASTER);
            config.setString("gitflow", null, "developBranch", Constants.DEVELOP);
            config.setString("gitflow", null, "featureBranchPrefix", "feature/");
            config.setString("gitflow", null, "releaseBranchPrefix", "release/");
            config.setString("gitflow", null, "hotfixBranchPrefix", "hotfix/");
            config.setString("gitflow", null, "supportBranchPrefix", "support/");
            config.setString("gitflow", null, "versionTagPrefix", "");

            bytes = config.toText().getBytes(Constants.ENCODING);

            entry = new DirCacheEntry(".gitflow");
            entry.setLength(bytes.length);
            entry.setLastModified(System.currentTimeMillis());
            entry.setFileMode(FileMode.REGULAR_FILE);
            entry.setObjectId(odi.insert(org.eclipse.jgit.lib.Constants.OBJ_BLOB, bytes));

            indexBuilder.add(entry);
            */

            //结束
            indexBuilder.finish();
            if (newIndex.getEntryCount() == 0) {
                return false;
            }

            ObjectId treeId = newIndex.writeTree(odi);

            // Create a commit object
            CommitBuilder commit = new CommitBuilder();
            commit.setAuthor(author);
            commit.setCommitter(author);
            commit.setEncoding(Constants.ENCODING);
            commit.setMessage("Initial commit");
            commit.setTreeId(treeId);

            // Insert the commit into the repository
            ObjectId commitId = odi.insert(commit);
            odi.flush();

            // set the branch refs
            try (RevWalk revWalk = new RevWalk(db)) {
                // set the master branch
                RevCommit revCommit = revWalk.parseCommit(commitId);
                RefUpdate masterRef = db.updateRef(Constants.R_MASTER);
                masterRef.setNewObjectId(commitId);
                masterRef.setRefLogMessage("commit: " + revCommit.getShortMessage(), false);
                RefUpdate.Result masterRC = masterRef.update();
                switch (masterRC) {
                    case NEW:
                        success = true;
                        break;
                    default:
                        success = false;
                }

                /*
                // set the develop branch for git-flow
                RefUpdate developRef = db.updateRef(Constants.R_DEVELOP);
                developRef.setNewObjectId(commitId);
                developRef.setRefLogMessage("commit: " + revCommit.getShortMessage(), false);
                RefUpdate.Result developRC = developRef.update();
                switch (developRC) {
                    case NEW:
                        success = true;
                        break;
                    default:
                        success = false;
                }
                */
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException" + e.getMessage());
        } finally {
            odi.close();
            db.close();
        }
        return success;
    }
}
