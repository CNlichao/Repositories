package com.docdeal.bean;

public class OfficialDocument {
	private String id;
	private String title;
	private String returnCode;
	private String sender;
	private String sendTime;
	private String type;

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;

	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setType(String type) {
		this.type = type;

	}

	public String getType() {
		return type;
	}
}
