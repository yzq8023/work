package com.service.service.utils;

import com.service.service.IStoredSettings;
import com.service.service.Keys;
import com.service.service.managers.IRepositoryManager;
import com.service.service.entity.Activity;
import com.service.service.entity.RefModel;
import com.service.service.entity.RepositoryCommit;
import com.service.service.entity.TaskEntity;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 从存储库构建活动信息的实用程序类。
 *
 * @author James Moger
 *
 */
public class ActivityUtils {

	/**
	 * 从存储库中获取最近几天在指定分支上的活动。
	 *
	 * @param settings
	 *            the runtime settings
	 * @param repositoryManager
	 *            the repository manager
	 * @param models
	 *            the list of repositories to query
	 * @param daysBack
	 *            the number of days back from Now to collect
	 * @param objectId
	 *            the branch to retrieve. If this value is null or empty all
	 *            branches are queried.
	 * @param timezone
	 *            the timezone for aggregating commits
	 * @return
	 */
	public static List<Activity> getRecentActivity(
					IStoredSettings settings,
					IRepositoryManager repositoryManager,
					List<TaskEntity> models,
					int daysBack,
					String objectId,
					TimeZone timezone) {

		// 活动面板显示了所有存储库的活动的最后一天。
		Date thresholdDate = new Date(System.currentTimeMillis() - daysBack * TimeUtils.ONEDAY);

		// 在指定的阈值日期，从可用的存储库构建一个每日活动的地图。
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setTimeZone(timezone);
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(timezone);

		// 总作者除外
		Set<String> authorExclusions = new TreeSet<String>();
		authorExclusions.addAll(settings.getStrings(Keys.web.metricAuthorExclusions));
		for (TaskEntity model : models) {
			if (!ArrayUtils.isEmpty(model.getMetricAuthorExclusions())) {
				authorExclusions.addAll(model.getMetricAuthorExclusions());
			}
		}

		Map<String, Activity> activity = new HashMap<String, Activity>();
		for (TaskEntity model : models) {
			if (!model.isShowActivity()) {
				// 跳过当前的库
				continue;
			}
			if (model.isHasCommits() && model.getLastChange().after(thresholdDate)) {
				if (model.isCollectingGarbage()) {
					continue;
				}
				Repository repository = repositoryManager.getRepository(model.getTaskName());
				List<String> branches = new ArrayList<String>();
				if (StringUtils.isEmpty(objectId)) {
					for (RefModel local : JGitUtils.getLocalBranches(
							repository, true, -1)) {
			        	if (!local.getDate().after(thresholdDate)) {
							// 分支不是最近的更新
			        		continue;
			        	}
						branches.add(local.getName());
					}
				} else {
					branches.add(objectId);
				}

				for (String branch : branches) {
					String shortName = branch;
					if (shortName.startsWith(Constants.R_HEADS)) {
						shortName = shortName.substring(Constants.R_HEADS.length());
					}
					List<RepositoryCommit> commits = CommitCache.instance().getCommits(model.getTaskName(), repository, branch, thresholdDate);
					if (model.getMaxActivityCommits() > 0 && commits.size() > model.getMaxActivityCommits()) {
						//纵倾提交最大计数
						commits = commits.subList(0,  model.getMaxActivityCommits());
					}
					for (RepositoryCommit commit : commits) {
						Date date = commit.getCommitDate();
						String dateStr = df.format(date);
						if (!activity.containsKey(dateStr)) {
							// Normalize the date to midnight
							cal.setTime(date);
							cal.set(Calendar.HOUR_OF_DAY, 0);
							cal.set(Calendar.MINUTE, 0);
							cal.set(Calendar.SECOND, 0);
							cal.set(Calendar.MILLISECOND, 0);
							Activity a = new Activity(cal.getTime());
							a.excludeAuthors(authorExclusions);
							activity.put(dateStr, a);
						}
						activity.get(dateStr).addCommit(commit);
					}
				}

				// close the repository
				repository.close();
			}
		}

		List<Activity> recentActivity = new ArrayList<Activity>(activity.values());
		return recentActivity;
	}
}
