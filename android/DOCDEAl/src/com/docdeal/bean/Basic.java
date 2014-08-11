package com.docdeal.bean;

import java.io.Serializable;

public class Basic implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8971441199257699826L;
	private String officialDocID;
	private String hostDepartment;
	private String streamType;
	private String officialDocTitle;
	private String sendTime;
	private String sender;
	private String transactor;
	
	
	
	public Basic(String officialDocID, String hostDepartment,
			String streamType, String officialDocTitle, String sendTime,
			String sender, String transactor) {
		super();
		this.officialDocID = officialDocID;
		this.hostDepartment = hostDepartment;
		this.streamType = streamType;
		this.officialDocTitle = officialDocTitle;
		this.sendTime = sendTime;
		this.sender = sender;
		this.transactor = transactor;
	}
	
	
	public String getOfficialDocID() {
		return officialDocID;
	}
	public void setOfficialDocID(String officialDocID) {
		this.officialDocID = officialDocID;
	}

	public String getHostDepartment() {
		return hostDepartment;
	}
	public void setHostDepartment(String hostDepartment) {
		this.hostDepartment = hostDepartment;
	}
	public String getStreamType() {
		return streamType;
	}
	public void setStreamType(String streamType) {
		this.streamType = streamType;
	}
	public String getOfficialDocTitle() {
		return officialDocTitle;
	}
	public void setOfficialDocTitle(String officialDocTitle) {
		this.officialDocTitle = officialDocTitle;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getTrasactor() {
		return transactor;
	}
	public void setTrasactor(String trasactor) {
		this.transactor = trasactor;
	}
}
