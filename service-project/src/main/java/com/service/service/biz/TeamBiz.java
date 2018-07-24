package com.service.service.biz;

import com.ace.cache.annotation.CacheClear;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.Query;
import com.service.service.Constants;
import com.service.service.entity.TeamModel;
import com.service.service.entity.UserModel;
import com.service.service.mapper.TeamModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.service.service.Constants.AccountType.LOCAL;

/**
 * 业务逻辑类
 * 描述：实现在增删改中对事务的管理和回滚，所有需要实现事物的方法均要放到这里
 *
 * @author dk
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TeamBiz extends BaseBiz<TeamModelMapper, TeamModel> {
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
     * 修改用户所属团队
     *
     * @param userId
     * @param teamIds
     */
    public boolean updateTeams(Integer userId, String teamIds){
        mapper.deleteTeamsByUserId(userId);
        if (!StringUtils.isEmpty(teamIds)) {
            String[] teams = teamIds.split(",");
            for (String t : teams) {
                mapper.insertTeamsByUserId(userId, Integer.parseInt(t));
            }
        }
        return true;
    }
    /**
     * 根据userid查询参加的team
     *
     * @param query
     * @return
     */

    public TableResultResponse<TeamModel> selectTeamByUserId(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<TeamModel> list = mapper.selectTeamByUserId(query.getCurrentUserId());
        return new TableResultResponse<TeamModel>(result.getTotal(), list);
    }

    public Map<String, TeamModel> loadTeams() {
        Map<String, TeamModel> teams = new ConcurrentHashMap<String, TeamModel>();
        // load the teams
        Set<String> teamnames = new HashSet<String>(mapper.getTeamIds());
        for (String teamname : teamnames) {
            TeamModel team = new TeamModel(teamname);
            team.setCanAdmin(team.isCanAdmin());
            team.setCanFork(team.isCanFork());
            team.setCanCreate(team.isCanCreate());
            team.setAccountType(LOCAL);

            if (!team.isCanAdmin()) {
                // non-admin team, read permissions
                team.addRepositoryPermissions(mapper.getTeamPermissions(team.getId()));
            }
            team.addUsers(mapper.getUserIdsByTeamId(team.getId()));

            teams.put(String.valueOf(team.getId()), team);
        }
        return teams;
    }

}
