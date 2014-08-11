package com.docdeal.bean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Flow implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2156494846458632278L;
	private List<Action> list=new ArrayList<Action>();

	public List<Action> getList() {
		return list;
	}

	public void setList(List<Action> list) {
		this.list = list;
	}
	public void addAction(Action action) {
		list.add(action);
	}

}
