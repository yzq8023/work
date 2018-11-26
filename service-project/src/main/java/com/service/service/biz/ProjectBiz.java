package com.service.service.biz;

import com.ace.cache.annotation.CacheClear;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.service.service.entity.ProjectEntity;
import com.service.service.mapper.ProjectEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * 业务逻辑类
 * 描述：实现在增删改中对事务的管理和回滚
 *
 * @author dk
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProjectBiz extends BaseBiz<ProjectEntityMapper, ProjectEntity> {

    @Autowired
    ProjectEntityMapper mapper;

    /**
     * 更改项目关联团队
     *
     * @param projectId
     * @param teams
     */
    public void modifiyProTeam(Integer projectId, String teams) {
        mapper.deleteProTeamsById(projectId);
        if (!StringUtils.isEmpty(teams)) {
            String[] team = teams.split(",");
            for (String t : team) {
                mapper.insertProTeamsById(projectId, Integer.parseInt(t));
            }
        }
    }

    /**
     * 根据用户id获取map_user_project中的项目
     *
     * @param query
     * @@return  TableResultResponse
     */
    public TableResultResponse<ProjectEntity> getJoinedProject(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<ProjectEntity> list = mapper.selectProjectByUserId(query.getCrtUser());
        return new TableResultResponse<ProjectEntity>(result.getTotal(), list);
    }

    @Override
    protected String getPageName() {
        return "ProjectBiz";
    }
}
