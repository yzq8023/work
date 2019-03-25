package com.workhub.z.servicechat.mapper;

import com.workhub.z.servicechat.model.GroupMsgModel;
import com.workhub.z.servicechat.model.PrivateMsgModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消息处理Mapper
 * @author hanxu
 */
public interface MessageMapper {

	
	/**
	 * 移动私聊消息到历史表
	 */
	Integer movePrivateMsg(Map<String, Object> parameter);
	
	/**
	 * 删除已经移动到历史表中的私聊信息
	 * @return
	 */
	Integer removeRuPrivateMsg(Map<String, Object> parameter);

	/**
	 * 查询获得ru表中未读消息大于10的私聊对象组(使用is_read字段存储未读的总数)
	 * @return
	 */
	List<PrivateMsgModel> findRuPrivateRedundant(int count);
	
	/**
	 * 移动讨论组当天消息到历史表中
	 * @return
	 */
	Integer moveGroupMsg();
	
	/**
	 * 删除讨论组中当天的已读消息
	 * @return
	 */
	Integer removeGroupMsg();
	
	/**
	 * 获得讨论组中未读消息大于10条的讨论组消息（使用msg字段存储未读的总数）
	 * @param count
	 * @return
	 */
	List<GroupMsgModel> findRuGroupRedundant(int count);

	/**
	 * 移动讨论组中超过10条的未读消息到历史表中
	 * @param parameter
	 * @return
	 */
	Integer moveGroupMsgMore10(Map<String, Object> parameter);
	
	/**
	 * 移除讨论组中超过10条的未读消息
	 * @param parameter
	 * @return
	 */
	Integer removeGroupMsgMore10(Map<String, Object> parameter);
	
	/**
	 * 查询讨论组消息
	 * @param systemId	：讨论组id
	 * @param start		：分页开始位置
	 * @param row		：每页显示数量
	 * @return
	 */
	List<GroupMsgModel> groupRuMsg(@Param("systemId") String systemId,
                                   @Param("start") Integer start, @Param("row") Integer row);

	/**
	 * 获取全部组信息
	 * @param msg
	 * @param groupId
	 * @return
	 */
	List<?> queryAllHisGroupMsg(@Param("msg") String msg,
                                @Param("groupId") String groupId);

	/**
	 * 查询私聊信息
	 * @param userId	:用户id
	 * @param chatId	:聊天对象id
	 * @param start		:分页开始位置
	 * @param row		:每页显示数量
	 * @return
	 */
	List<PrivateMsgModel> privateRuMsg(@Param("userId") String userId,
                                       @Param("chatId") String chatId, @Param("start") Integer start, @Param("row") int row);

	/**
	 * 更新用户最后读取讨论组消息时间
	 * @param msgId		:消息id
	 * @param userId	：用户id
	 * @param groupId	：讨论组id
	 */
	void updateEndTime(@Param("msgId") String msgId, @Param("userId") String userId, @Param("groupId") String groupId);

	/**
	 * 更新用户和聊天对象全部聊天消息为已读
	 * @param chatId	：聊天对象
	 * @param userId	：用户id
	 */
	void updateIsRead(@Param("chatId") String chatId, @Param("userId") String userId);

	/**
	 * 获得最近联系人的未读消息
	 * @param userId	:用户id
	 * @return
	 */
	List<Map<String, Object>> latelyNotRead(String userId);

	/**
	 * 获得用户列表的未读消息
	 * @param userId	:用户id
	 * @return
	 */
	List<Map<String, Object>> userNotRead(String userId);

	/**
	 * 获得讨论组的未读消息
	 * @param userId	:用户id
	 * @return
	 */
	List<Map<String, Object>> groupNotRead(String userId);

	/**
	 * 保存聊天文件
	 * @param chatFile	：文件对象
	 * @return
	 */
//	int saveFile(ChatFileModel chatFile);

	/**
	 * 查询私聊消息
	 * @param content	：消息内容
	 * @param chatId	：聊天对象id
	 * @param userId	：当前用户id
	 * @param start		：开始位置
	 * @param row		：显示行数
	 * @param startDate	：开始时间
	 * @param endDate	：结束时间
	 * @param maxTotal	：查询结束位置
	 * @return
	 */
//	List<?> privateHiMsg(@Param("content") String content, @Param("chatId") String chatId, @Param("userId") String userId,
//                         @Param("start") Integer start, @Param("row") Integer row,
//                         @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	/**
	 * 查询私聊消息总数
	 * @param content	：消息内容
	 * @param chatId	：聊天对象id
	 * @param userId	：当前用户id
	 * @param startDate	：开始时间
	 * @param endDate	：结束时间
	 * @return
	 */
	int privateHiMsgCount(@Param("content") String content, @Param("chatId") String chatId, @Param("userId") String userId,
                          @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	/**
	 * 查询讨论组消息
	 * @param content	：消息内容
	 * @param chatId	：讨论组id
	 * @param start		：开始位置
	 * @param row		：显示行数
	 * @param startDate	：开始时间
	 * @param endDate	：结束时间
	 * @return
	 */
	List<?> groupHiMsg(@Param("content") String content, @Param("chatId") String chatId,
                       @Param("start") Integer start, @Param("row") Integer row,
                       @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	/**
	 * 查询讨论组消息总数
	 * @param content	：消息内容
	 * @param chatId	：讨论组id
	 * @param startDate	：开始时间
	 * @param endDate	：结束时间
	 * @return
	 */
	int groupHiMsgCount(@Param("content") String content, @Param("chatId") String chatId,
                        @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	/**
	 * 保存一条讨论组消息
	 * @param msgId			：消息id
	 * @param msgSender		：消息发送者
	 * @param msgReceiver	：消息接收者
	 * @param sendTime		：发送时间
	 * @param msg			：消息内容
	 * @param msgType		：消息类型
	 * @return
	 */
	int saveGroupMsg(@Param("msgId") String msgId, @Param("msgSender") String msgSender, @Param("msgReceiver") String msgReceiver,
                     @Param("sendTime") Date sendTime, @Param("msg") String msg, @Param("msgType") String msgType);

	/**
	 * 保存一条私聊消息
	 * @param msgId			：消息id
	 * @param msgSender		：消息发送者
	 * @param msgReceiver	：消息接收者
	 * @param sendTime		：发送时间
	 * @param msg			：消息内容
	 * @param msgType		：消息类型
	 * @return
	 */
	int savePrivateMsg(@Param("msgId") String msgId, @Param("msgSender") String msgSender, @Param("msgReceiver") String msgReceiver,
                       @Param("sendTime") Date sendTime, @Param("msg") String msg, @Param("msgType") String msgType);


	/**
	 * 查询系统消息
	 * @param startTime	：时间范围开始时间
	 * @param endTime	：时间范围结束时间
	 * @param title		：消息title
	 * @param start		：分页开始位置
	 * @param row		：每页显示数量
	 * @return	:List<SystemNotificationModel>
	 */
//	List<SystemNotificationModel> querySystemNotification(@Param("isRead") String isRead, @Param("startTime") Date startTime, @Param("endTime") Date endTime,
//                                                          @Param("title") String title, @Param("start") Integer start, @Param("row") Integer row);

	/**
	 * 查询系统消息总数
	 * @param startTime
	 * @param endTime
	 * @param title
	 * @return
	 */
	int querySystemNotificationCount(@Param("isRead") String isRead, @Param("startTime") Date startTime, @Param("endTime") Date endTime,
                                     @Param("title") String title, @Param("userId") String userId);

	/**
	 * 保存系统通知
	 * @param id	：id
	 * @param sender	:发送者
	 * @param title		：消息title
	 * @param type		：消息类型
	 * @param content	：消息内容
	 * @return
	 */
	int saveSystemNotification(@Param("id") String id, @Param("sender") String sender, @Param("title") String title,
                               @Param("type") String type, @Param("content") String content, @Param("isRead") String isRead, @Param("receiver") String receiver);

	/**
	 * 更新聊天文件的读取路径，转码线程调用
	 * @param readPath	:读取路径
	 * @param fileId	：文件id
	 * @return
	 */
	int updateChatFile(@Param("readPath") String readPath, @Param("fileId") String fileId, @Param("updator") Long updator);

	/**
	 * 通过文件id获取聊天文件的转码后的路径
	 * @param fileId	:文件id
	 * @return
	 */
	String queryFileById(String fileId);

	/**
	 *  获得聊天文件
	 * @param fileId	:文件id
	 * @return
	 */
	String queryFile(String fileId);

	/**
	 *  获得聊天文件(组)
	 * @param fileId	:文件id
	 * @return
	 */
	String qgFlie(String fileId);

	/**
	 * 分页查询获得私聊消息
	 * @param msg	：消息
	 * @param startTime	：时间范围
	 * @param endTime	：时间范围
	 * @param sendUser	：消息发送人
	 * @param receiverUser	：消息接收人
	 * @param start	：分页开始位置
	 * @param rows	：显示行数
	 * @return
	 */
	List<PrivateMsgModel> queryPrivateMsg(@Param("msg") String msg, @Param("startTime") Date startTime,
                                          @Param("endTime") Date endTime, @Param("sendUser") String sendUser,
                                          @Param("receiverUser") String receiverUser, @Param("start") Integer start, @Param("rows") Integer rows);

	/**
	 * 分页查询私聊消息时获得总数
	 * @param msg	：消息
	 * @param startTime	：时间范围
	 * @param endTime	：时间范围
	 * @param sendUser	：消息发送人
	 * @param receiverUser	：消息接收人
	 * @return
	 */
	int queryPrivateMsgCount(@Param("msg") String msg, @Param("startTime") Date startTime,
                             @Param("endTime") Date endTime, @Param("sendUser") String sendUser,
                             @Param("receiverUser") String receiverUser);

	/**
	 *  更新系统信息已读\未读标识
	 */
	public int updateSysMsgFlag(String notificationId);

	/**
	 * 查询个人私聊消息
	 * @param privateMsgModel
	 * @return
	 */
	int queryPrivateUserMessage(PrivateMsgModel privateMsgModel);

	/**
	 * 获取群组文件数量
	 * @param fileName
	 * @param sendUser
	 * @param fileLevels
	 * @return
	 */
	int queryGCount(@Param("fileName") String fileName, @Param("sendUser") String sendUser, @Param("fileLevels") String fileLevels);

	/**
	 * 根据ID，获取交换文件数量
	 * @param sender
	 * @param receiver
	 * @return
	 */
	int queryMsgFileCount(@Param("sender") String sender, @Param("receiver") String receiver);

	/**
	 * 删除被@人员
	 * @param groupId
	 * @param userId
	 */
	void delAtUser(@Param("groupId") String groupId, @Param("userId") String userId);

	/**
	 * 获取最近聊天历史消息(群组)
	 * @param start
	 * @param row
	 * @param chatUserId
	 * @return
	 */
	List<GroupMsgModel> queryGroupHisMsgs(@Param("start") Integer start, @Param("row") Integer row, @Param("chatUserId") String chatUserId);

	/**
	 * 获取最近聊天历史消息数量(群组)
	 * @param chatUserId
	 * @return
	 */
	int queryGroupHisMsgsCount(String chatUserId);

	/**
	 * 获取最近聊天历史消息(私聊)
	 * @param start
	 * @param row
	 * @param chatUserId
	 * @return
	 */
	List<PrivateMsgModel> queryPrivateHisMsgs(@Param("start") Integer start, @Param("row") Integer row, @Param("chatUserId") String chatUserId);

	/**
	 * 获取最近聊天历史消息数量(私聊)
	 * @param chatUserId
	 * @return
	 */
	int queryPrivateHisMsgsCount(String chatUserId);

}






