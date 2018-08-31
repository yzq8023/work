package com.service.service.handler.impl;

import com.service.service.enumeration.BaseEnum;
import com.service.service.enumeration.impl.CommentType;
import com.service.service.handler.BaseEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;


@MappedTypes(value = {CommentType.class})
public class EnumTypeHandler<E extends BaseEnum> extends BaseEnumTypeHandler<E> {

    public EnumTypeHandler(Class<E> type) {

        super(type);
    }
}
