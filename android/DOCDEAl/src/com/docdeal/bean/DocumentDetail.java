package com.docdeal.bean;

import java.io.Serializable;

public class DocumentDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2337937881709830076L;
	private Basic basic;
	private Form form;
	private Body body;
	private Attachments attachments;
	private Flow flow;

	public Basic getBasic() {
		return basic;
	}

	public void setBasic(Basic basic) {
		this.basic = basic;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Attachments getAttachments() {
		return attachments;
	}

	public void setAttachments(Attachments attachments) {
		this.attachments = attachments;
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}
}
