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
 * 解析文件列表Xml
 * 
 * @author 李超
 */
public class DocumentsListXml {

	public List<OfficialDocument> parserXml(String xml, StringBuffer pages,
			StringBuffer rows) {
		List<OfficialDocument> oList = new ArrayList<OfficialDocument>();
		try {
			Document document = DocumentHelper.parseText(xml);

			Element root = document.getRootElement();
			pages = pages.delete(0, pages.length()).append(root.attributeValue("总页数"));
			rows = rows.delete(0, rows.length()).append(root.attributeValue("总行数"));
			List docList = root.elements("公文");

			for (int i = 0; i < docList.size(); i++) {
				OfficialDocument od = new OfficialDocument();
				Element doc = (Element) docList.get(i);
				od.setTitle(doc.elementText("标题"));
				od.setSendTime(doc.elementText("发送时间"));
				od.setSender(doc.elementText("发送人"));
				od.setType(doc.elementText("文件类型"));
				od.setId(doc.attributeValue("标识符"));
				od.setReturnCode(doc.attributeValue("回写标识"));
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
		// ds.parserXml("C:\\Users\\Administrator\\Desktop\\OA移动办公\\移动办公v3.0\\文件信息.xml");
		// ds.parserXml("C:\\Users\\Administrator\\Desktop\\OA移动办公\\移动办公v3.0\\待办列表.xml");

	}

}
