package com.workhub.z.servicechat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.util.Objects;

public class IworkWsMsgHandler implements IWsMsgHandler {
    private static Logger log = LoggerFactory.getLogger(IworkWsMsgHandler.class);

    public static IworkWsMsgHandler me = new IworkWsMsgHandler();

    private IworkWsMsgHandler() {

    }

    /**
     * 握手时走这个方法，业务可以在这里获取cookie，request参数等
     */
    @Override
    public HttpResponse handshake(HttpRequest request, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        String clientip = request.getClientIp();
//        String name=request.getParam("name");
//        String userid=request.getParam("token");
        String userid=request.getParam("userid");

        //前端 参数 获取，绑定信息
        Aio.bindUser(channelContext,userid);
//        Aio.bindGroup(channelContext, Const.GROUP_ID);
//        Aio.bindGroup(channelContext,Const.GROUP_ID);
        log.info("收到来自{}的ws握手包\r\n{}", clientip, request.toString());
        return httpResponse;
    }

    /**
     * 字节消息（binaryType = arraybuffer）过来后会走这个方法
     */
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    /**
     * 当客户端发close flag时，会走这个方法
     */
    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        Aio.remove(channelContext, "receive close flag");
        return null;
    }

    /*
     * 字符消息（binaryType = blob）过来后会走这个方法
     */
    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
        WsSessionContext wsSessionContext = (WsSessionContext) channelContext.getAttribute();
        HttpRequest httpRequest = wsSessionContext.getHandshakeRequestPacket();//获取websocket握手包
        String name=httpRequest.getParam("name");
        String userid=httpRequest.getParam("id");
        String type=httpRequest.getParam("type");
        String r=httpRequest.getParam("recipient");

        Aio.bindUser(channelContext,userid);
        Aio.bindGroup(channelContext, Const.GROUP_ID);

        //获取前端消息 展示
        if (log.isDebugEnabled()) {
            log.debug("握手包:{}", httpRequest);
        }

        log.info("收到ws消息:{}", text);

        if (Objects.equals("心跳内容", text)) {
            return null;
        }
        System.out.println(text);
//      String msg = channelContext.getClientNode().toString() + " 说：" + text;
        String msg = text;
        //用tio-websocket，服务器发送到客户端的Packet都是WsResponse
        WsResponse wsResponse = WsResponse.fromText(msg, IworkServerConfig.CHARSET);
        //群发
        Aio.bSendToGroup(channelContext.getGroupContext(), Const.GROUP_ID, wsResponse);
        //系统消息
//      Aio.sendToAll(channelContext.getGroupContext(),wsResponse);
//      Aio.sendToUser(channelContext.getGroupContext(),"123",wsResponse);
        //返回值是要发送给客户端的内容，一般都是返回null
        return null;
    }
}