package com.github.hollykunge.servicetalk.user.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 群组用户
 *
 * @author: holly
 * @since: 2019/2/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ImChatGroupUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 群id
     */
    private String chatGroupId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 入群时间
     */
    private Date createDate;


}
