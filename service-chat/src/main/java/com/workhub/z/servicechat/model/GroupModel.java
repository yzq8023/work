package com.workhub.z.servicechat.model;

import java.util.Date;

/**
 * pf_user_group
 * 
 * @author zouct
 */
public class GroupModel {

	private String groupId; // 用户组id
	private String groupName; // 组名称
	private String groupDescribe; // 组描述
	private String isdelete; // 是否已经解散
	private Date createTime; // 创建时间
	private String creator; // 创建人
	private Date updateTime; // 更新时间
	private String updator; // 更新人
	private String pname; // 项目名称
	private String scop; // 参与范围
	private String ispublic; // 是否为公共组
	private String isclose; // 是否关闭
	private String levels; // 讨论组密级


	public String getLevels() {
		return levels;
	}

	public void setLevels(String levels) {
		this.levels = levels;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescribe() {
		return groupDescribe;
	}

	public void setGroupDescribe(String groupDescribe) {
		this.groupDescribe = groupDescribe;
	}

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getScop() {
		return scop;
	}

	public void setScop(String scop) {
		this.scop = scop;
	}

	public String getIspublic() {
		return ispublic;
	}

	public void setIspublic(String ispublic) {
		this.ispublic = ispublic;
	}

	public String getIsclose() {
		return isclose;
	}

	public void setIsclose(String isclose) {
		this.isclose = isclose;
	}

}
