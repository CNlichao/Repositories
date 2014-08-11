package com.docdeal.parsexml;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.docdeal.bean.Condition;
import com.docdeal.bean.WorkFlow;
import com.docdeal.bean.WorkFlows;

public class SearchConditon {
	public WorkFlows paserXml(String xml) {
		WorkFlows workFlows = null;
		try {
			Document dom = DocumentHelper.parseText(xml);
			Element workFlowsElement = dom.getRootElement()
					.element("workflows");

			workFlows = parseWorkFlows(workFlowsElement);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workFlows;
	}

	/**
	 * @param args
	 * @throws DocumentException
	 */
	public static void main(String[] args) throws DocumentException {
		// TODO Auto-generated method stub
		File inputXml = new File("F:/Workspaces/xml/src/文件查询条件.xml");
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(inputXml);
		Element workFlowsElement = document.getRootElement().element(
				"workflows");

		SearchConditon parse = new SearchConditon();
		WorkFlows workFlows = parse.parseWorkFlows(workFlowsElement);
		System.out.println("");
	}

	private WorkFlows parseWorkFlows(Element workFlowsElement) {

		WorkFlows workFlows = new WorkFlows();
		List<Element> list1 = workFlowsElement.elements("condition");
		Iterator<Element> it1 = list1.iterator();
		while (it1.hasNext()) {
			Element conditionElement = (Element) it1.next();
			Condition condition = parseCondition(conditionElement);
			workFlows.getListConditions().add(condition);
		}

		List list2 = workFlowsElement.elements("workflow");
		Iterator<Element> it2 = list2.iterator();
		while (it2.hasNext()) {
			Element workflowElement = (Element) it2.next();
			WorkFlow workflow = parseWorkflow(workflowElement);
			workFlows.getListWorkFlows().add(workflow);
		}
		return workFlows;
	}

	private WorkFlow parseWorkflow(Element workflowElement) {
		WorkFlow workFlow = new WorkFlow();

		String id = workflowElement.attributeValue("id");
		workFlow.setId(id);
		String name = workflowElement.element("name").getText();
		workFlow.setName(name);

		List<Element> list1 = workflowElement.elements("condition");
		Iterator<Element> it1 = list1.iterator();
		while (it1.hasNext()) {
			Element conditionElement = (Element) it1.next();
			Condition condition = parseCondition(conditionElement);
			workFlow.getListConditions().add(condition);
		}
		return workFlow;
	}

	private Condition parseCondition(Element conditionElement) {
		Condition condition = new Condition();

		String fieldname = conditionElement.attributeValue("fieldname");
		condition.setFieldname(fieldname);
		String text = conditionElement.getText();
		condition.setText(text);
		String type = conditionElement.attributeValue("type");
		condition.setType(type);
		return condition;

	}

}
