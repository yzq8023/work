package com.github.wxiaoqi.security.common.util;


import com.github.wxiaoqi.security.common.constant.PageConstants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 *
 * @author hollykunge
 */
public class Query extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    private int page = 1;
    private int limit = 10;

    private Integer crtUser = null;

    private Integer taskExecutorId = null;

    private Integer projectId = null;

    public Query(Map<String, Object> params) {
        this.putAll(params);
        //分页参数
        if (params.get(PageConstants.PAGE_NUM) != null) {
            this.page = Integer.parseInt(params.get("page").toString());
        }
        if (params.get(PageConstants.PAGE_LIMIT) != null) {
            this.limit = Integer.parseInt(params.get("limit").toString());
        }
        if (params.get(PageConstants.TASK_EXE_ID) != null) {
            this.crtUser = Integer.parseInt(params.get(PageConstants.TASK_EXE_ID).toString());
        }
        if (params.get(PageConstants.PROJECT_ID) != null) {
            this.projectId = Integer.parseInt(params.get(PageConstants.PROJECT_ID).toString());
        }
        if (params.get(PageConstants.TASK_EXE_ID) != null) {
            this.taskExecutorId = Integer.parseInt(params.get(PageConstants.TASK_EXE_ID).toString());
        }
        this.remove(PageConstants.PAGE_NUM);
        this.remove(PageConstants.PAGE_LIMIT);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Integer getCrtUser() {
        return crtUser;
    }

    public void setCrtUser(Integer crtUser) {
        this.crtUser = crtUser;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getTaskExecutorId() {
        return taskExecutorId;
    }

    public void setTaskExecutorId(Integer taskExecutorId) {
        this.taskExecutorId = taskExecutorId;
    }
}
