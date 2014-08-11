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
	 * 将文件解析为docmentdetail对象
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	public DocumentDetail parserXml(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		DocumentDetailXml xml2Object = new DocumentDetailXml();
		Element officialDocElement = document.getRootElement().element("公文");
		DocumentDetail docDetail = xml2Object
				.parseofficialDocument(officialDocElement);
		return docDetail;
	}

	// 解析公文
	public DocumentDetail parseofficialDocument(Element officialDocElement) {
		DocumentDetail offDoc = new DocumentDetail();
		// 解析基本信息
		Element basicElement = officialDocElement.element("基本信息");
		if (basicElement != null) {
			Basic basic = parseBasicInfo(basicElement);
			offDoc.setBasic(basic);
		}

		// 解析表单元素
		Element formElement = officialDocElement.element("表单");
		if (formElement != null) {
			Form form = parseFormInfo(formElement);
			offDoc.setForm(form);
		}
		// 解析正文
		Element bodyElement = officialDocElement.element("正文");
		if (bodyElement != null) {
			Body body = parseBody(bodyElement);
			offDoc.setBody(body);
		}
		// 解析附件部分
		Element attachmentsElement = officialDocElement.element("附件部分");
		if (attachmentsElement != null) {
			Attachments attachments = parseAttachments(attachmentsElement);
			offDoc.setAttachments(attachments);
		}

		// 解析流程相关
		Element flowElement = officialDocElement.element("流程相关");
		if (flowElement != null) {
			Flow flow = parseFlow(flowElement);
			offDoc.setFlow(flow);
		}

		return offDoc;
	}

	// 解析流程相关
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

	// 解析action
	public Action parseAction(Element actionElement) {
		String name = actionElement.attributeValue("name");
		String totaskid = actionElement.attributeValue("totaskid");
		String type = actionElement.attributeValue("type");
		String actiontype = actionElement.attributeValue("actiontype");
		String actionid = actionElement.attributeValue("actionid");
		Action action = new Action(name, totaskid, type, actiontype, actionid);
		// 将user依次加入action对象中
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

	// 解析全部附件
	public Attachments parseAttachments(Element attachmentsElement) {
		Attachments attachments = new Attachments();
		List list = attachmentsElement.elements();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Element attachmentElement = (Element) it.next();
			String title = attachmentElement.element("标题").getText();
			// String body = attachmentElement.element("内容").getText();
			String body = "";
			String charset = attachmentElement.element("内容").attributeValue(
					"编码方式");
			Attachment attachment = new Attachment(title, body, charset);
			attachments.addAttachment(attachment);
		}
		return attachments;
	}

	// 解析正文
	public Body parseBody(Element bodyElement) {
		String charset = bodyElement.attributeValue("编码方式");
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

	// 解析表单元素
	public Form parseFormInfo(Element formElement) {
		Form formInfo = new Form();
		// 获取xml原始数据
		List list = formElement.elements();
		Iterator it = list.iterator();
		// 遍历节点元素，将每一个节点放入formInfo对象中去
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

	// 解析基本信息
	public Basic parseBasicInfo(Element basicElement) {
		// TODO Auto-generated method stub
		String officialDocID = basicElement.element("公文标识").getText();
		String hostDepartment = basicElement.element("主办单位").getText();
		String streamType = basicElement.element("流程类型").getText();
		String officialDocTitle = basicElement.element("公文标题").getText();
		String sendTime = basicElement.element("发送时间").getText();
		String sender = basicElement.element("发送人").getText();
		String transactor = basicElement.element("办理人").getText();
		Basic basicInfo = new Basic(officialDocID, hostDepartment, streamType,
				officialDocTitle, sendTime, sender, transactor);
		return basicInfo;
	}

}
