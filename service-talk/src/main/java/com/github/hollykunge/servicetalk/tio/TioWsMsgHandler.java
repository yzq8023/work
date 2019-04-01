package com.github.hollykunge.servicetalk.tio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.common.HttpResponseStatus;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.server.handler.IWsMsgHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * websocket 处理函数
 *
 * @author: holly
 * @since: 2019/2/15
 */
public class TioWsMsgHandler implements IWsMsgHandler {

    private static Logger log = LoggerFactory.getLogger(TioWsMsgHandler.class);

    @Resource
    private DefaultTokenServices defaultTokenServices;

    @Resource
    @Qualifier(value = "imUserService")
    private IImUserService imUserService;

    @Resource
    @Qualifier(value = "iImMessageService")
    private IImMessageService iImMessageService;

    /**
     * 握手时走这个方法，业务可以在这里获取cookie，request参数等
     *
     * @param httpRequest    httpRequest
     * @param httpResponse   httpResponse
     * @param channelContext channelContext
     * @return HttpResponse
     */
    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        String token = request.getParam("token");
        try {
            OAuth2Authentication auth2Authentication = defaultTokenServices.loadAuthentication(token);
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth2Authentication.getUserAuthentication().getPrincipal();
            String userId = imUserService.getByLoginName(user.getUsername()).getId();
            //绑定用户
            Tio.bindUser(channelContext, userId);
            //绑定群组
            List<ImChatGroup> groups = imUserService.getChatGroups(userId);
            for (ImChatGroup group : groups) {
                Tio.bindGroup(channelContext, group.getId());
            }
        } catch (AuthenticationException | InvalidTokenException e) {
            e.printStackTrace();
            httpResponse.setStatus(HttpResponseStatus.getHttpStatus(401));
        }
        return httpResponse;
    }

    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {

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
        Tio.remove(channelContext, "receive close flag");
        return null;
    }

    /**
     * 字符消息（binaryType = blob）过来后会走这个方法
     *
     * @param wsRequest      wsRequest
     * @param s              s
     * @param channelContext channelContext
     * @return obj
     */
    @Override
    public Object onText(WsRequest wsRequest, String s, ChannelContext channelContext) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SendInfo sendInfo = objectMapper.readValue(text, SendInfo.class);
            //心跳检测包
            if (ChatUtils.MSG_PING.equals(sendInfo.getCode())) {
                WsResponse wsResponse = WsResponse.fromText(text, TioServerConfig.CHARSET);
                Tio.send(channelContext, wsResponse);
            }
            //真正的消息
            else if (ChatUtils.MSG_MESSAGE.equals(sendInfo.getCode())) {
                Message message = sendInfo.getMessage();
                message.setMine(false);
                WsResponse wsResponse = WsResponse.fromText(objectMapper.writeValueAsString(sendInfo), TioServerConfig.CHARSET);
                //单聊
                if (ChatUtils.FRIEND.equals(message.getType())) {
                    SetWithLock<ChannelContext> channelContextSetWithLock = Tio.getChannelContextsByUserid(channelContext.groupContext, message.getId());
                    //用户没有登录，存储到离线文件
                    if (channelContextSetWithLock == null || channelContextSetWithLock.size() == 0) {
                        saveMessage(message, ChatUtils.UNREAD);
                    } else {
                        Tio.sendToUser(channelContext.groupContext, message.getId(), wsResponse);
                        //入库操作
                        saveMessage(message, ChatUtils.READED);
                    }
                } else {
                    Tio.sendToGroup(channelContext.groupContext, message.getId(), wsResponse);
                    //入库操作
                    saveMessage(message, ChatUtils.READED);
                }
            }
            //准备就绪，需要发送离线消息
            else if (ChatUtils.MSG_READY.equals(sendInfo.getCode())) {
                //未读消息
                sendOffLineMessage(channelContext, objectMapper);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回值是要发送给客户端的内容，一般都是返回null
        return null;
    }

    /**
     * 未读消息
     *
     * @param channelContext channelContext
     * @param objectMapper   objectMapper
     * @throws IOException 抛出异常
     */
    private void sendOffLineMessage(ChannelContext channelContext, ObjectMapper objectMapper) throws IOException {
        List<ImMessage> imMessageList = iImMessageService.getUnReadMessage(channelContext.userid);
        for (ImMessage imMessage : imMessageList) {
            Message message = new Message();
            message.setId(imMessage.getToId());
            message.setMine(false);
            message.setType(imMessage.getType());
            ImUser imUser = imUserService.getById(imMessage.getFromId());
            message.setAvatar(imUser.getAvatar());
            message.setUsername(imUser.getName());
            message.setFromid(imMessage.getFromId());
            message.setCid(String.valueOf(imMessage.getId()));
            message.setContent(imMessage.getContent());
            message.setTimestamp(imMessage.getSendTime());
            SendInfo sendInfo1 = new SendInfo();
            sendInfo1.setCode(ChatUtils.MSG_MESSAGE);
            sendInfo1.setMessage(message);
            WsResponse wsResponse = WsResponse.fromText(objectMapper.writeValueAsString(sendInfo1), TioServerConfig.CHARSET);
            Tio.sendToUser(channelContext.groupContext, message.getId(), wsResponse);
        }
    }

    /**
     * 保存信息
     *
     * @param message    信息
     * @param readStatus 是否已读
     */
    private void saveMessage(Message message, String readStatus) {
        ImMessage imMessage = new ImMessage();
        imMessage.setToId(message.getId());
        imMessage.setFromId(message.getFromid());
        imMessage.setSendTime(message.getTimestamp());
        imMessage.setContent(message.getContent());
        imMessage.setReadStatus(readStatus);
        imMessage.setType(message.getType());
        iImMessageService.saveMessage(imMessage);
    }
}
