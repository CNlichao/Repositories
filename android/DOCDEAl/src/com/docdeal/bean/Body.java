package com.docdeal.bean;

import java.io.Serializable;

public class Body  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1562767417845290228L;
	private String charset;
	private String canwrite;
	private String text;
		
	public Body(String charset, String canwrite, String text) {
		super();
		this.charset = charset;
		this.canwrite = canwrite;
		this.text = text;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getCanwrite() {
		return canwrite;
	}
	public void setCanwrite(String canwrite) {
		this.canwrite = canwrite;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}	
}
