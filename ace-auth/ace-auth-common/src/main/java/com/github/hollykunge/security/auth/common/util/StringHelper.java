package com.github.hollykunge.security.auth.common.util;

/**
 * Created by 协同设计小组 on 2017/9/10.
 */
public class StringHelper {
    public static String getObjectValue(Object obj){
        return obj==null?"":obj.toString();
    }
}
