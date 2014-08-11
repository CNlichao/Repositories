package com.docdeal.parsexml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class ContactGroupXml {
	public static void xmlToList(String str, List<Map<String, String>> list) {
		if (str == null && str.equals("")) {
			return;
		}
		try {
			Document document = DocumentHelper.parseText(str);

			Element root = document.getRootElement();

			for (Iterator iter = root.elementIterator(); iter.hasNext();) {
				Element element = (Element) iter.next();
				Map<String, String> oh = new HashMap<String, String>();
				if (element.getName().equals("orginfo")) {
					for (Iterator iter2 = element.elementIterator(); iter2
							.hasNext();) {
						Element element2 = (Element) iter2.next();
						if (element2.getName().equals("×éÃû³Æ")) {
							oh.put("name", element2.getText());
						}
						if (element2.getName().equals("×éID")) {
							oh.put("id", element2.getText());
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
