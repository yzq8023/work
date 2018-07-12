package com.service.service.biz;

import com.ace.cache.annotation.CacheClear;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.Query;
import com.service.service.entity.TeamEntity;
import com.service.service.mapper.TeamEntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 业务逻辑类
 * 描述：实现在增删改中对事务的管理和回滚，所有需要实现事物的方法均要放到这里
 * @author dk
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TeamBiz extends BaseBiz<TeamEntityMapper, TeamEntity> {
    /**
     * 修改小组人员
     *
     * @param teamId
     * @param members
     */
    @CacheClear(pre = "permission")
    public void modifiyTeamUsers(Integer teamId, String members) {
        mapper.deleteTeamUsersById(teamId);
        if (!StringUtils.isEmpty(members)) {
            String[] mem = members.split(",");
            for (String m : mem) {

                mapper.insertTeamUsersById(teamId, Integer.parseInt(m));
            }
        }
    }

    /**
     * 根据userid查询参加的team
     *
     * @param query
     * @return
     */

    public TableResultResponse<TeamEntity> selectTeamByUserId(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPage(),query.getLimit());
        List<TeamEntity> list = mapper.selectTeamByUserId(query.getCrtUser());
        return new TableResultResponse<TeamEntity>(result.getTotal(), list);
    }
}
