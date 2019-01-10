package com.chat.client;

import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Aio;
import org.tio.core.Node;

/**
 * @author 忠
 * @program ace-security
 * @Description
 * @date 2019-01-07 15:47
 */
public class IworkClientStarter {
    //服务器节点
    public static Node serverNode = new Node(Const.SERVER, Const.PORT);

    //handler, 包括编码、解码、消息处理
    public static ClientAioHandler aioClientHandler = new IworkClientAioHandler();

    //事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
    public static ClientAioListener aioListener = null;

    //断链后自动连接的，不想自动连接请设为null
    private static ReconnConf reconnConf = new ReconnConf(5000L);

    //一组连接共用的上下文对象
    public static ClientGroupContext clientGroupContext = new ClientGroupContext(aioClientHandler, aioListener, reconnConf);

    public static AioClient aioClient = null;
    public static ClientChannelContext clientChannelContext = null;

    /**
     * 启动程序入口
     */
    public static void startClient() throws Exception {
        clientGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
        aioClient = new AioClient(clientGroupContext);
        clientChannelContext = aioClient.connect(serverNode);
        //连上后，发条消息玩玩
//        send();
    }

    public static void sendMessage(String content) throws Exception {
        IworkPacket packet = new IworkPacket();
        packet.setBody("hello world".getBytes(IworkPacket.CHARSET));
        Aio.send(clientChannelContext, packet);
    }
}
