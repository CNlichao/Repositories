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
						if (element2.getName().equals("ְ��")) {
							cu.setJob(element2.getText());
						}
						if (element2.getName().equals("���ߵ绰")) {
							cu.setHousePhone(element2.getText());
						}
						if (element2.getName().equals("���ߵ绰")) {
							cu.setOutsidePhone(element2.getText());
						}
						if (element2.getName().equals("���Ŷ̺�")) {
							cu.setCornet(element2.getText());
						}
						if (element2.getName().equals("�칫�绰")) {
							cu.setPhoneForWork(element2.getText());
						}
						if (element2.getName().equals("����")) {
							cu.setFax(element2.getText());
						}
						if (element2.getName().equals("�����ʼ�")) {
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
