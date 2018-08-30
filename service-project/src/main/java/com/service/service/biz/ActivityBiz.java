package com.service.service.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.Query;
import com.service.service.Keys;
import com.service.service.entity.Activity;
import com.service.service.entity.ActivityEntity;
import com.service.service.entity.TaskEntity;
import com.service.service.entity.UserModel;
import com.service.service.managers.IWorkHub;
import com.service.service.mapper.ActivityEntityMapper;
import com.service.service.utils.ActivityUtils;
import com.service.service.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: hollykunge
 * @Description:
 * @Date: 创建于 2018/8/13
 * @Modified:
 */
@Service
public class ActivityBiz extends BaseBiz<ActivityEntityMapper, ActivityEntity> {

    private IWorkHub workHub;
    List<TaskEntity> repositoryModels = new ArrayList<TaskEntity>();

    @Autowired
    public ActivityBiz(IWorkHub workHub) {
        this.workHub = workHub;

    }

    /**
     * 根据用户id获取map_user_project中的项目
     *
     * @param query
     * @@return TableResultResponse
     */
    public TableResultResponse<Activity> getActivity(Query query) {
        String objectId = null;
        int daysBack = query.getDayBack();
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<TaskEntity> models = getRepositories(query);
        List<Activity> recentActivity = ActivityUtils.getRecentActivity(
                workHub.getSettings(),
                workHub,
                models,
                daysBack,
                objectId,
                getTimeZone());
        return new TableResultResponse<Activity>(result.getTotal(), recentActivity);
    }

    protected List<TaskEntity> getRepositoryModels(Integer userId) {
        if (repositoryModels.isEmpty()) {
            final UserModel user = workHub.getUserModel(userId);
            List<TaskEntity> repositories = workHub.getRepositoryModels(user);
            repositoryModels.addAll(repositories);
            Collections.sort(repositoryModels);
        }
        return repositoryModels;
    }

    protected List<TaskEntity> getRepositories(Query query) {
        if (query == null) {
            return null;
        }

        boolean hasParameter = false;
        String projectName = query.getProjectName();
        Integer crtUser = query.getCrtUser();
        if (StringUtils.isEmpty(projectName)) {
            if (crtUser != null) {
                //可自行创建独立于项目外的任务库
//				projectName = ModelUtils.getPersonalPath(userId);
            }
        }
        String repositoryName = query.getTaskName();
//		String set = WicketUtils.getSet(params);
//		String regex = WicketUtils.getRegEx(params);
//		String team = WicketUtils.getTeam(params);
        int daysBack = 0;
        int maxDaysBack = workHub.getSettings().getInteger(Keys.web.activityDurationMaximum, 30);

        List<TaskEntity> availableModels = getRepositoryModels(query.getCrtUser());
        Set<TaskEntity> models = new HashSet<TaskEntity>();

        if (!StringUtils.isEmpty(repositoryName)) {
            // 尝试命名存储库
            hasParameter = true;
            for (TaskEntity model : availableModels) {
                if (model.getTaskName().equalsIgnoreCase(repositoryName)) {
                    models.add(model);
                    break;
                }
            }
        }

        if (!StringUtils.isEmpty(projectName)) {
            // try named project
            hasParameter = true;
            if (projectName.equalsIgnoreCase(workHub.getSettings().getString(Keys.web.repositoryRootGroupName, "main"))) {
                // root project/group
                for (TaskEntity model : availableModels) {
                    if (model.getTaskName().indexOf('/') == -1) {
                        models.add(model);
                    }
                }
            } else {
                // named project/group
                String group = projectName.toLowerCase() + "/";
                for (TaskEntity model : availableModels) {
                    if (model.getTaskName().toLowerCase().startsWith(group)) {
                        models.add(model);
                    }
                }
            }
        }

//		if (!StringUtils.isEmpty(regex)) {
//			// filter the repositories by the regex
//			hasParameter = true;
//			Pattern pattern = Pattern.compile(regex);
//			for (TaskEntity model : availableModels) {
//				if (pattern.matcher(model.name).find()) {
//					models.add(model);
//				}
//			}
//		}

//		if (!StringUtils.isEmpty(set)) {
//			// filter the repositories by the specified sets
//			hasParameter = true;
//			List<String> sets = triSngUtils.getStringsFromValue(set, ",");
//			for (TaskEntity model : availableModels) {
//				for (String curr : sets) {
//					if (model.federationSets.contains(curr)) {
//						models.add(model);
//					}
//				}
//			}
//		}

//		if (!StringUtils.isEmpty(team)) {
//			// filter the repositories by the specified teams
//			hasParameter = true;
//			List<String> teams = StringUtils.getStringsFromValue(team, ",");
//
//			// need TeamModels first
//			List<TeamModel> teamModels = new ArrayList<TeamModel>();
//			for (String name : teams) {
//				TeamModel teamModel = app().users().getTeamModel(name);
//				if (teamModel != null) {
//					teamModels.add(teamModel);
//				}
//			}
//
//			// brute-force our way through finding the matching models
//			for (TaskEntity repositoryModel : availableModels) {
//				for (TeamModel teamModel : teamModels) {
//					if (teamModel.hasRepositoryPermission(repositoryModel.getTaskName())) {
//						models.add(repositoryModel);
//					}
//				}
//			}
//		}

        if (!hasParameter) {
            models.addAll(availableModels);
        }

        // time-filter the list
        if (daysBack > 0) {
            if (maxDaysBack > 0 && daysBack > maxDaysBack) {
                daysBack = maxDaysBack;
            }
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.add(Calendar.DATE, -1 * daysBack);
            Date threshold = cal.getTime();
            Set<TaskEntity> timeFiltered = new HashSet<TaskEntity>();
            for (TaskEntity model : models) {
                if (model.getLastChange().after(threshold)) {
                    timeFiltered.add(model);
                }
            }
            models = timeFiltered;
        }

        List<TaskEntity> list = new ArrayList<TaskEntity>(models);
        Collections.sort(list);
        return list;
    }

    protected TimeZone getTimeZone() {
        return workHub.getTimezone();
    }

    @Override
    protected String getPageName() {
        return null;
    }

    public void updateActivity(TaskEntity taskEntity, String method) {

        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setContent(taskEntity.getTaskDes());
        activityEntity.setIsPrivate(true);
//        activityEntity.setOpType();
//        activityEntity.setRefName();
//        activityEntity.setRepoId();
//        activityEntity.setRepoUserName();
//        activityEntity.setRepoName();
//        activityEntity.setUserId();
//
//        super.insertSelective(taskEntity);
    }
}
