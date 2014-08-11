package com.docdeal.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Action implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5202695552658347280L;
	private String name;
	private String totaskid;
	private String type;
	private String actiontype;
	private String actionid;
	private List<User> userList=new ArrayList<User>();
	
	public Action(String name, String totaskid, String type, String actiontype,
			String actionid) {
		super();
		this.name = name;
		this.totaskid = totaskid;
		this.type = type;
		this.actiontype = actiontype;
		this.actionid = actionid;
	}
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTotaskid() {
		return totaskid;
	}
	public void setTotaskid(String totaskid) {
		this.totaskid = totaskid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getActiontype() {
		return actiontype;
	}
	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}
	public String getActionid() {
		return actionid;
	}
	public void setActionid(String actionid) {
		this.actionid = actionid;
	}

	public void addUser(User user) {
		userList.add(user);
	}

}
