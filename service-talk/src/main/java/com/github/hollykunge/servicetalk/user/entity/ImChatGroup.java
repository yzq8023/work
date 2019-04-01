package com.github.hollykunge.servicetalk.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.github.hollykunge.servicetalk.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 群组
 *
 * @author: holly
 * @since: 2019/2/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ImChatGroup extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    /**
     * 群名称
     */
    private String name;

    /**
     * 群头像
     */
    private String avatar;

    /**
     * 群主
     */
    private String master;




}
