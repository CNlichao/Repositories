package com.docdeal.bean;

import java.util.ArrayList;
import java.util.List;

public class WorkFlows {
	private List<Condition> listConditions = new ArrayList<Condition>();
	private List<WorkFlow> listWorkFlows = new ArrayList<WorkFlow>();

	public List<Condition> getListConditions() {
		return listConditions;
	}

	public void setListConditions(List<Condition> listConditions) {
		this.listConditions = listConditions;
	}

	public List<WorkFlow> getListWorkFlows() {
		return listWorkFlows;
	}

	public void setListWorkFlows(List<WorkFlow> listWorkFlows) {
		this.listWorkFlows = listWorkFlows;
	}

}
