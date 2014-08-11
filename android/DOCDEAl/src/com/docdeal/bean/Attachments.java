package com.docdeal.bean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Attachments implements Serializable{
	
	private static final long serialVersionUID = -4202890763300167503L;
	private List<Attachment> list=new ArrayList<Attachment>();

	public List<Attachment> getList() {
		return list;
	}

	public void setList(List<Attachment> list) {
		this.list = list;
	}	
	public void addAttachment(Attachment attachment) {
		list.add(attachment);
	}
}
