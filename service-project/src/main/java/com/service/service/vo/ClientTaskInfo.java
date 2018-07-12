package com.service.service.vo;

import io.swagger.models.auth.In;

/**
 * 客户端任务信息实体类
 * 描述：服务端发送到客户端的任务信息实体
 *
 * @author dk
 */
public class ClientTaskInfo {
    private Integer taskId;
    private String taskDes;
    private String taskName;
    private Byte taskState;
    private Byte taskType;
    private String path;
    private String attr1;
    private String attr2;
}
