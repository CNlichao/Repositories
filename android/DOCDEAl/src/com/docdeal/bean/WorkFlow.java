package com.docdeal.bean;

import java.util.ArrayList;
import java.util.List;

public class WorkFlow {
	private String id;
	private String name;
	private List<Condition> listConditions = new ArrayList<Condition>();

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

	public List<Condition> getListConditions() {
		return listConditions;
	}

	public void setListConditions(List<Condition> listConditions) {
		this.listConditions = listConditions;
	}

}
