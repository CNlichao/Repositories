package com.docdeal.bean;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3014656263298890521L;
	private String deptName;
	private String id;
	private String name;

	public User(String deptName, String id, String name) {
		super();
		this.deptName = deptName;
		this.id = id;
		this.name = name;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
