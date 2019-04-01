package com.github.hollykunge.servicetalk.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.hollykunge.servicetalk.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * ${描述}
 *
 * @author: holly
 * @since: 2019/2/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("im_user_friend")
public class ImUserFriend extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 好友ID
     */
    private String friendId;

    /**
     * 用户分组
     */
    private String userGroupId;

    /**
     * 好友分组
     */
    private String friendGroupId;


}
