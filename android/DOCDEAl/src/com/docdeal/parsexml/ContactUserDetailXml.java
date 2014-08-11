package com.docdeal.parsexml;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.docdeal.bean.ContactUser;

public class ContactUserDetailXml {
	public static void xmlToObj(String str, ContactUser cu) {
		if (str == null && str.equals("")) {
			return;
		}
		try {
			Document document = DocumentHelper.parseText(str);

			Element root = document.getRootElement();

			for (Iterator iter = root.elementIterator(); iter.hasNext();) {
				Element element = (Element) iter.next();
				if (element.getName().equals("userinfo")) {
					for (Iterator iter2 = element.elementIterator(); iter2
							.hasNext();) {
						Element element2 = (Element) iter2.next();
						if (element2.getName().equals("职务")) {
							cu.setJob(element2.getText());
						}
						if (element2.getName().equals("内线电话")) {
							cu.setHousePhone(element2.getText());
						}
						if (element2.getName().equals("外线电话")) {
							cu.setOutsidePhone(element2.getText());
						}
						if (element2.getName().equals("集团短号")) {
							cu.setCornet(element2.getText());
						}
						if (element2.getName().equals("办公电话")) {
							cu.setPhoneForWork(element2.getText());
						}
						if (element2.getName().equals("传真")) {
							cu.setFax(element2.getText());
						}
						if (element2.getName().equals("电子邮件")) {
							cu.setMail(element2.getText());
						}
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
