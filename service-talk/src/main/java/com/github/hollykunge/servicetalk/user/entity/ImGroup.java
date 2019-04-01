package com.github.hollykunge.servicetalk.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.github.hollykunge.servicetalk.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * ${描述}
 *
 * @author: holly
 * @since: 2019/2/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ImGroup extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    private String userId;

    private String name;

    @TableField(exist = false)
    private List<ImUser> userList;

    @TableField(exist = false)
    private boolean expansion = false;
}
