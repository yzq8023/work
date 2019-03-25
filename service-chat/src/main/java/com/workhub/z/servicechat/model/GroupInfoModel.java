package com.workhub.z.servicechat.model;

import java.util.Date;

public class GroupInfoModel {

	private String groupId;
	private String groupName;
	private String groupDescribe;
	private Date createTime;
	private String creator;
	private String creatorName;
	private Date updateTime;
	private String updator;
	private String isdelete;
	private String pname;
	private String scop; // 参与范围
	private Integer peopleCount;// 讨论组内人数
	private String ispublic; // 是否为公共组
	private String isclose; // 是否关闭
	private String levels; // 是否关闭

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

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public Integer getPeopleCount() {
		return peopleCount;
	}

	public void setPeopleCount(Integer peopleCount) {
		this.peopleCount = peopleCount;
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
