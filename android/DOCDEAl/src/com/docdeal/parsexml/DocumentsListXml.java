package com.docdeal.parsexml;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.docdeal.bean.OfficialDocument;

/**
 * �����ļ��б�Xml
 * 
 * @author �
 */
public class DocumentsListXml {

	public List<OfficialDocument> parserXml(String xml, StringBuffer pages,
			StringBuffer rows) {
		List<OfficialDocument> oList = new ArrayList<OfficialDocument>();
		try {
			Document document = DocumentHelper.parseText(xml);

			Element root = document.getRootElement();
			pages = pages.delete(0, pages.length()).append(root.attributeValue("��ҳ��"));
			rows = rows.delete(0, rows.length()).append(root.attributeValue("������"));
			List docList = root.elements("����");

			for (int i = 0; i < docList.size(); i++) {
				OfficialDocument od = new OfficialDocument();
				Element doc = (Element) docList.get(i);
				od.setTitle(doc.elementText("����"));
				od.setSendTime(doc.elementText("����ʱ��"));
				od.setSender(doc.elementText("������"));
				od.setType(doc.elementText("�ļ�����"));
				od.setId(doc.attributeValue("��ʶ��"));
				od.setReturnCode(doc.attributeValue("��д��ʶ"));
				oList.add(od);
			}

		} catch (DocumentException e) {
			System.out.println(e.getMessage());
		}
		// System.out.println("dom4j parserXml");
		return oList;

	}

	public void createXml(String fileName) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		DocumentsListXml ds = new DocumentsListXml();
		ds.createXml("E:\\aaa.txt");
		// ds.parserXml("C:\\Users\\Administrator\\Desktop\\OA�ƶ��칫\\�ƶ��칫v3.0\\�ļ���Ϣ.xml");
		// ds.parserXml("C:\\Users\\Administrator\\Desktop\\OA�ƶ��칫\\�ƶ��칫v3.0\\�����б�.xml");

	}

}
