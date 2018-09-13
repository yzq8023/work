package com.service.service.mapper;


import com.service.service.entity.IssueEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 
 * 
 * @author Mr.AG
 * @email 463540703@qq.com
 * @date 2018-08-20 16:05:04
 */

public interface IssueEntityMapper extends Mapper<IssueEntity> {
    public void deleteIssueLabelsById(@Param("issueId") Integer issueId);
    public void insertIssueLabelsById(@Param("issueId") Integer issueId, @Param("labelId") Integer labelId);
    public List<Integer> getJoinedUsersFromIssue(@Param("taskId") Integer taskId,@Param("issueId") Integer issueId);
    public void insertIssueUsersById(@Param("issueId") Integer issueId, @Param("userId") Integer userId);
}
