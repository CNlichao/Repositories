package com.docdeal.bean;

import java.io.Serializable;

public class Attachment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2149480488683892351L;
	private String title;
	private String body;
	private String charset;
		
	public Attachment(String title, String body, String charset) {
		super();
		this.title = title;
		this.body = body;
		this.charset = charset;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}	
}
