package com.service.service.enumeration.impl;

import com.service.service.enumeration.BaseEnum;

import java.io.Serializable;

public enum CommentType implements Serializable,BaseEnum<CommentType,Integer> {

    评论(0,"COMMENT"),
    关闭(1,"CLOSE"),
    打开(2,"OPEN");

    private static final long serialVersionUID = 1L;

    private final Integer code;
    private final String status;


    CommentType(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public static CommentType get(int v) {
        String str = String.valueOf(v);
        return get(str);
    }

    public static CommentType get(String str) {
        for (CommentType e : values()) {
            if(e.getCode().toString().equals(str)) {
                return e;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "CommentType{" +
                "code=" + code +
                ", status='" + status + '\'' +
                '}';
    }
}
