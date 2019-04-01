package com.workhub.z.servicechat.mapper;

import com.workhub.z.servicechat.model.*;
import com.github.hollykunge.security.api.vo.user.UserInfo;
import com.workhub.z.servicechat.model.GroupMsgModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface GroupMapper {

	/**
	 * 获得讨论组成员信息
	 * @param groupId	:讨论组Id
	 * @return
	 */
	public List<UserInfo> queryGroupUser(String groupId);

	/**
	 * 获得讨论组密级
	 * @param groupid	:讨论组Id
	 * @return
	 */
	public String queryGroupLevelsByGroupId(String groupid);

	/**
	 * 获得重要信息
	 * @return
	 */
	public List<GroupMsgModel> queryImportantInfo(String groupId);
	
	/**
	 * 导出讨论组聊天信息
	 * @return
	 */
	public List<GroupMsgModel> queryGInfo(String groupId);
	
	/**
	 * 获得讨论组信息
	 * @return
	 */
	public List<GroupModel> queryGroup();
	
	/**
	 * 获得讨论组信息
	 * @param groupId	:讨论组Id
	 * @return
	 */
	public GroupInfoModel queryGroupInfo(String groupId);
	
	/**
	 * 分页获得讨论组文件
	 * @param fileName	：文件名称
	 * @param groupId	：讨论组id
	 * @param start		: 起始位置
	 * @param end		：接收位置
	 * @return
	 */
//	public List<GroupFileModel> queryGroupFile(@Param("fileName") String fileName, @Param("groupId") String groupId,
//                                               @Param("start") int start, @Param("end") int end);

	/**
	 * 获得讨论组文件总数
	 * @param fileName	：文件名称
	 * @param groupId	：讨论组id
	 * @return
	 */
	public int queryGroupFileCount(@Param("fileName") String fileName, @Param("groupId") String groupId);

	/**
	 * 保存讨论组文件
	 * @param groupFile	:讨论组文件对象
	 * @return
	 */
//	public int saveGroupFile(GroupFileModel groupFile);

	/**
	 * 新增讨论组
	 * @param groupInfo	:新增讨论组
	 * @return
	 */
	public int saveGroupInfo(GroupInfoModel groupInfo);

	/**
	 * 新增讨论组成员
	 * @param groupUser	:讨论组人员关联对象
	 * @return
	 */
//	public int saveGroupUser(GroupUserRefModel groupUser);

	/**
	 * 删除讨论组成员
	 * @param groupId	：讨论组id
	 * @param userId	：用户id
	 * @return
	 */
	public int deleteGroupUser(String groupId, String userId);

	/**
	 * 更新讨论组信息
	 * @param groupInfo	：讨论组信息
	 * @return
	 */
	public int updateGroupInfo(GroupInfoModel groupInfo);

	/**
	 * 删除讨论组全部成员
	 * @param groupId	：讨论组id
	 * @return
	 */
	public int deleteGroupAllUser(String groupId);

	/**
	 * 删除讨论组文件
	 * @param groupId	：讨论组id
	 * @param fileId	：文件id
	 * @return
	 */
	public int deleteGroupFile(@Param("groupId") String groupId, @Param("fileId") String fileId);

	/**
	 * 移除重要信息标记
	  * @param msgId
	 *            :信息Id
	 * @param msg
	 *            ：信息内容
	 * @return
	 */
	public int removeImportantInfo(@Param("msgId") String msgId, @Param("msg") String msg);

	/**
	 * 逻辑删除讨论组
	 * @param groupId	:讨论组id
	 * @param updator
	 * @return
	 */
	public int deleteGroup(@Param("groupId") String groupId, @Param("updator") String updator);

	/**
	 * 删除讨论组对应的最近联系人列表
	 * @param groupId	:讨论组id
	 * @return
	 */
	public int deleteGroupLatelyLinkman(@Param("groupId") String groupId);

	/**
	 * 通过文件id获得文件信息
	 * @param fileId	：文件id
	 * @return
	 */
//	public GroupFileModel queryGroupFileById(@Param("fileId") String fileId);

	/**
	 * 更新讨论组文件转码路径，线程调用更新
	 * @param readPath	：转码后的路径
	 * @param fileId	：文件id
	 * @param updator	：更新人
	 * @return
	 */
	public int updateGroupFile(@Param("readPath") String readPath, @Param("fileId") String fileId, @Param("updator") String updator);

	/**
	 * 添加为重要信息
	 */
	public void updateImportantFlag(String meg);


	/**
	 * 获得所有讨论组
	 * @return
	 */
	public List<GroupInfoModel> queryAllGroup();

	/**
	 * 分页获得讨论组
	 * @return
	 */
	public List<GroupInfoModel> queryGroupPage(@Param("groupName") String groupName, @Param("isDelete") String isDelete,
                                               @Param("startTime") Date startTime, @Param("endTime") Date endTime,
                                               @Param("start") int start, @Param("rows") int rows);

	/**
	 * 分页查询时获得总数
	 * @return
	 */
	public int queryGroupPageCount(@Param("groupName") String groupName, @Param("isDelete") String isDelete,
                                   @Param("startTime") Date startTime, @Param("endTime") Date endTime);


	/**
	 * 退出群组
	 * @param userId
	 * @param groupId
	 * @return
	 */

	public void outGroup(@Param("userId") String userId, @Param("groupId") String groupId);

	/**
	 * 根据groupID获得讨论组名称
	 * @param receiver
	 * @return
	 */
	public GroupModel queryGroupInfoById(String receiver);

	/**
	 * 关闭讨论组
	 * @param groupId
	 * @return
	 */
	public int closedGroup(String groupId);

	/**
	 * 模糊查询用户的组
	 * @param parm
	 * @return
	 */
	List<Map<String, Object>> queryGroupByUserAndGroupName(Map parm);

	/**
	 * -根据人员ID，查找最佳讨论组（消息最多）
	 * @param userid
	 * @return
	 */
//	List<StatisticsModel> queryGroupMsgByUserId(String userid);

	/**
	 * -根据群组ID，返回群组文件数
	 * @param groupId
	 * @return
	 */
	int queryGroupFilenum(String groupId);

	/**
	 * 全部群组消息
	 * @param start		：分页开始位置
	 * @param row		：每页显示数量
	 * @param msgName
	 * @param sendUser
	 * @param msgLevels
	 * @return	:List<GroupMsgModel>
	 */
	public List<GroupMsgModel> groupMsgsList(@Param("start") Integer start, @Param("row") Integer row,
											 @Param("msgName") String msgName, @Param("sendUser") String sendUser, @Param("msgLevels") String msgLevels);
	
	/**
	 * 获取群组消息数量
	 * @param msgName
	 * @param sendUser
	 * @param msgLevels
	 * @return
	 */
	int groupMsgsListCount(@Param("msgName") String msgName, @Param("sendUser") String sendUser, @Param("msgLevels") String msgLevels);

}



