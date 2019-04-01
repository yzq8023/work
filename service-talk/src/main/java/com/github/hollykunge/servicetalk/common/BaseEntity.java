package com.github.hollykunge.servicetalk.common;

import lombok.Data;

import java.util.Date;

/**
 * 基础类
 *
 * @author: holly
 * @since: 2019/2/15
 */
@Data
public class BaseEntity {

    /**
     * 说明
     */
    private String remarks;

    private Date createDate;

    private String createBy;

    private Date updateDate;

    private String updateBy;

    private String delFlag;

    public void preInsert() {
        this.createDate = new Date();
        this.updateDate = new Date();
        this.delFlag = "0";
    }
}
