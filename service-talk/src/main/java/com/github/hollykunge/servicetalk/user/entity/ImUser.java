package com.github.hollykunge.servicetalk.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class ImUser  extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    private String avatar;

    private String name;

    private String sign;

    private String mobile;

    private String email;

    private String password;

    @TableField("login_name")
    private String loginName;

    @TableField("default_group_id")
    private String defaultGroupId;
}
