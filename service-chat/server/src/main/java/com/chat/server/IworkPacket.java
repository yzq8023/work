package com.chat.server;


import org.tio.core.intf.Packet;

/**
 * @author 忠
 * @program ace-security
 * @Description
 * @date 2019-01-07 14:35
 */
public class IworkPacket extends Packet {

    private static final long serialVersionUID = -172060606924066412L;
    public static final int HEADER_LENGHT = 4;//消息头的长度
    public static final String CHARSET = "utf-8";
    private byte[] body;

    public byte[] getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(byte[] body) {
        this.body = body;
    }
}