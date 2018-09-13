package com.service.service.biz;

import com.github.wxiaoqi.security.api.vo.user.UserInfo;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.service.service.entity.IssueEntity;
import com.service.service.feign.IUserFeignClient;
import com.service.service.mapper.IssueEntityMapper;
import com.service.service.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 *
 * @author
 * @date 2018-08-20 16:05:04
 */
@Service
public class IssueBiz extends BaseBiz<IssueEntityMapper,IssueEntity> {


    private IUserFeignClient userFeignClient;

    @Autowired
    public IssueBiz(IUserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    public void modifyLabelsInIssue(Integer issueId, String labels) {
        mapper.deleteIssueLabelsById(issueId);
        if (!StringUtils.isEmpty(labels)) {
            String[] label = labels.split(",");
            for (String t : label) {
                mapper.insertIssueLabelsById(issueId, Integer.parseInt(t));
            }
        }
    }

    public List<UserInfo> getJoinedUsersFromIssue(Integer taskId,Integer issueId){

        List<Integer> userIds = mapper.getJoinedUsersFromIssue(taskId,issueId);
        List<UserInfo> userInfos = new ArrayList<>();

        for(Integer userId:userIds){
            mapper.insertIssueUsersById(issueId,userId);
            UserInfo userInfo = userFeignClient.info(userId);
            userInfos.add(userInfo);
        }
        return userInfos;
    }

    @Override
    protected String getPageName() {
        return null;
    }
}