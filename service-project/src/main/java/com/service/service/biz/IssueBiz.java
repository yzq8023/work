package com.service.service.biz;

import com.github.hollykunge.security.api.vo.user.UserInfo;
import com.github.hollykunge.security.common.biz.BaseBiz;
import com.service.service.entity.IssueEntity;
import com.service.service.feign.IUserFeignClient;
import com.service.service.mapper.IssueEntityMapper;
import com.service.service.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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

    public List<UserInfo> getJoinedUsersFromIssue(Integer issueId,Integer taskId){

        List<Integer> userIds = mapper.getJoinedUsersFromIssue(taskId,issueId);
        List<UserInfo> userInfos = new ArrayList<>();
        UserInfo userInfo = new UserInfo();

        if (taskId != null) {
            List<Integer> issueUserByIssueId = mapper.getIssueUserByIssueId(issueId);
                for(Integer userId:userIds){
                    if(issueUserByIssueId .size() == 0){
                        mapper.insertIssueUsersById(issueId,userId);
                    }
                    userInfo = userFeignClient.info(userId);
                    userInfos.add(userInfo);
                }
        }
        return userInfos;
    }

    @Override
    protected String getPageName() {
        return null;
    }
}