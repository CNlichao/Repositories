package com.docdeal.bean;

import java.io.Serializable;

public class NodeInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8597649123868140612L;
	private String tagName;
	private String canwrite;
	private String name;
	private String text;

	public NodeInfo(String tagName, String canwrite, String name, String text) {
		super();
		this.tagName = tagName;
		this.canwrite = canwrite;
		this.name = name;
		this.text = text;
	}

	public String getCanwrite() {
		return canwrite;
	}

	public void setCanwrite(String canwrite) {
		this.canwrite = canwrite;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

}
