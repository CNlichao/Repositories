package com.docdeal.parsexml;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.global.Base64Util;
import com.docdeal.bean.Action;
import com.docdeal.bean.Attachment;
import com.docdeal.bean.Attachments;
import com.docdeal.bean.Basic;
import com.docdeal.bean.Body;
import com.docdeal.bean.DocumentDetail;
import com.docdeal.bean.Flow;
import com.docdeal.bean.Form;
import com.docdeal.bean.NodeInfo;
import com.docdeal.bean.User;

public class DocumentDetailXml {

	/**
	 * ���ļ�����Ϊdocmentdetail����
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	public DocumentDetail parserXml(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		DocumentDetailXml xml2Object = new DocumentDetailXml();
		Element officialDocElement = document.getRootElement().element("����");
		DocumentDetail docDetail = xml2Object
				.parseofficialDocument(officialDocElement);
		return docDetail;
	}

	// ��������
	public DocumentDetail parseofficialDocument(Element officialDocElement) {
		DocumentDetail offDoc = new DocumentDetail();
		// ����������Ϣ
		Element basicElement = officialDocElement.element("������Ϣ");
		if (basicElement != null) {
			Basic basic = parseBasicInfo(basicElement);
			offDoc.setBasic(basic);
		}

		// ������Ԫ��
		Element formElement = officialDocElement.element("��");
		if (formElement != null) {
			Form form = parseFormInfo(formElement);
			offDoc.setForm(form);
		}
		// ��������
		Element bodyElement = officialDocElement.element("����");
		if (bodyElement != null) {
			Body body = parseBody(bodyElement);
			offDoc.setBody(body);
		}
		// ������������
		Element attachmentsElement = officialDocElement.element("��������");
		if (attachmentsElement != null) {
			Attachments attachments = parseAttachments(attachmentsElement);
			offDoc.setAttachments(attachments);
		}

		// �����������
		Element flowElement = officialDocElement.element("�������");
		if (flowElement != null) {
			Flow flow = parseFlow(flowElement);
			offDoc.setFlow(flow);
		}

		return offDoc;
	}

	// �����������
	public Flow parseFlow(Element flowElement) {
		Flow flow = new Flow();
		List<Element> actionElements = flowElement.elements();
		Iterator<Element> itAction = actionElements.iterator();
		while (itAction.hasNext()) {
			Element actionElement = (Element) itAction.next();
			Action action = parseAction(actionElement);
			flow.addAction(action);
		}
		return flow;

	}

	// ����action
	public Action parseAction(Element actionElement) {
		String name = actionElement.attributeValue("name");
		String totaskid = actionElement.attributeValue("totaskid");
		String type = actionElement.attributeValue("type");
		String actiontype = actionElement.attributeValue("actiontype");
		String actionid = actionElement.attributeValue("actionid");
		Action action = new Action(name, totaskid, type, actiontype, actionid);
		// ��user���μ���action������
		List<Element> userElements = actionElement.elements();
		Iterator<Element> iterator = userElements.iterator();
		while (iterator.hasNext()) {
			Element userElement = (Element) iterator.next();
			String deptName = userElement.attributeValue("deptName");
			String id = userElement.attributeValue("id");
			String text = userElement.getText();
			User user = new User(deptName, id, text);
			action.addUser(user);
		}
		return action;
	}

	// ����ȫ������
	public Attachments parseAttachments(Element attachmentsElement) {
		Attachments attachments = new Attachments();
		List list = attachmentsElement.elements();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Element attachmentElement = (Element) it.next();
			String title = attachmentElement.element("����").getText();
			// String body = attachmentElement.element("����").getText();
			String body = "";
			String charset = attachmentElement.element("����").attributeValue(
					"���뷽ʽ");
			Attachment attachment = new Attachment(title, body, charset);
			attachments.addAttachment(attachment);
		}
		return attachments;
	}

	// ��������
	public Body parseBody(Element bodyElement) {
		String charset = bodyElement.attributeValue("���뷽ʽ");
		String canwrite = bodyElement.attributeValue("canwrite");
		String text = bodyElement.getText();
		// try {
		// text = Base64Utils.decodeBASE64(text);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		Body bodyInfo = new Body(charset, canwrite, text);
		return bodyInfo;
	}

	// ������Ԫ��
	public Form parseFormInfo(Element formElement) {
		Form formInfo = new Form();
		// ��ȡxmlԭʼ����
		List list = formElement.elements();
		Iterator it = list.iterator();
		// �����ڵ�Ԫ�أ���ÿһ���ڵ����formInfo������ȥ
		while (it.hasNext()) {
			Element element = (Element) it.next();
			String tagName = element.getName();
			String canwrite = element.attributeValue("canwrite");
			String name = element.attributeValue("name");
			String text = element.getText();
			NodeInfo nodeInfo = new NodeInfo(tagName, canwrite, name, text);
			formInfo.addNodeInfo(nodeInfo);
		}
		return formInfo;
	}

	// ����������Ϣ
	public Basic parseBasicInfo(Element basicElement) {
		// TODO Auto-generated method stub
		String officialDocID = basicElement.element("���ı�ʶ").getText();
		String hostDepartment = basicElement.element("���쵥λ").getText();
		String streamType = basicElement.element("��������").getText();
		String officialDocTitle = basicElement.element("���ı���").getText();
		String sendTime = basicElement.element("����ʱ��").getText();
		String sender = basicElement.element("������").getText();
		String transactor = basicElement.element("������").getText();
		Basic basicInfo = new Basic(officialDocID, hostDepartment, streamType,
				officialDocTitle, sendTime, sender, transactor);
		return basicInfo;
	}

}
