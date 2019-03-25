package com.workhub.z.servicechat.model;

public class PrivateMsgModel {

	private String msgId;
	private String msgSender;
	private String senderName;
	private String msgReceiver;
	private String receiverName;
	private String sendTime;
	private String receiverTime;
	private String isRead;
	private String msg;
	private String msgType;
	private String msgPath;
	private String isDelete;
	private String isSystem;
	private String head;
	private String levels;

	public PrivateMsgModel() {
	}

	public PrivateMsgModel(String msgSender, String msgReceiver, String isRead) {
		this.msgSender = msgSender;
		this.msgReceiver = msgReceiver;
		this.isRead = isRead;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgSender() {
		return msgSender;
	}

	public void setMsgSender(String msgSender) {
		this.msgSender = msgSender;
	}

	public String getMsgReceiver() {
		return msgReceiver;
	}

	public void setMsgReceiver(String msgReceiver) {
		this.msgReceiver = msgReceiver;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getReceiverTime() {
		return receiverTime;
	}

	public void setReceiverTime(String receiverTime) {
		this.receiverTime = receiverTime;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgPath() {
		return msgPath;
	}

	public void setMsgPath(String msgPath) {
		this.msgPath = msgPath;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String toString() {
		return "msgId=" + msgId + ";msg=" + msg;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(String isSystem) {
		this.isSystem = isSystem;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getLevels() {
		return levels;
	}

	public void setLevels(String levels) {
		this.levels = levels;
	}

}
