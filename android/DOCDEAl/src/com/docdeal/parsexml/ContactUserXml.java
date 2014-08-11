package com.docdeal.parsexml;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.docdeal.bean.ContactItem;
import com.docdeal.util.PinYin;

public class ContactUserXml {
	public static void xmlToList(String str, List<ContactItem> list) {
		if (str == null && str.equals("")) {
			return;
		}
		try {
			Document document = DocumentHelper.parseText(str);

			Element root = document.getRootElement();

			for (Iterator iter = root.elementIterator(); iter.hasNext();) {
				Element element = (Element) iter.next();
				ContactItem oh = new ContactItem();
				if (element.getName().equals("userinfo")) {
					for (Iterator iter2 = element.elementIterator(); iter2
							.hasNext();) {
						Element element2 = (Element) iter2.next();
						if (element2.getName().equals("姓名")) {
							oh.setName(element2.getText());
							oh.setAlpha(PinYin.getPinYin(element2.getText()));
						}
						if (element2.getName().equals("用户ID")) {
							oh.setId(element2.getText());
						}
						if (element2.getName().equals("手机号码")) {
							oh.setNumber(element2.getText());
						}
					}
					list.add(oh);
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
