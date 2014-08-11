package com.docdeal.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.global.StringUtil;

public class Form implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6037912378507082922L;
	private List list = new ArrayList();

	public void addNodeInfo(NodeInfo node) {
		list.add(node);
	}

	public NodeInfo getNode(String tagName) {
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			NodeInfo node = (NodeInfo) iterator.next();
			if (node.getTagName().equals(tagName)) {
				return node;
			}
		}
		return null;
	}

	public List<Object> getNodeList() {

		return list;
	}

	public NodeInfo getNodeByName(String name) {
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			NodeInfo node = (NodeInfo) iterator.next();
			if (node.getName().equals(name)) {
				return node;
			}
		}
		return null;
	}

	public String getTextByName(String name) {
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			NodeInfo node = (NodeInfo) iterator.next();
			if (node.getName().equals(name)) {
				return node.getText();
			}
		}
		return "";
	}

	public boolean getCanWrite(String name) {
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			NodeInfo node = (NodeInfo) iterator.next();
			if (node.getName().equals(name)
					&& node.getCanwrite().equals("true")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 返回页面表单中可编辑项
	 * 
	 * @return
	 */
	public List<Object> getCanWriteNode() {
		Iterator iterator = list.iterator();
		List<Object> list = new ArrayList<Object>();
		while (iterator.hasNext()) {
			NodeInfo node = (NodeInfo) iterator.next();
			if (node.getCanwrite().equals("true")
					&& !StringUtil.isEmpty(node.getText())) {

				list.add(node);
			}
		}
		return list;
	}
	/*
	 * private NodeInfo qianfaren_self;//签发人 private NodeInfo
	 * departmentadvice_qianfa;//签发意见汇总 private NodeInfo
	 * personaladvice_qianfa;//个人签发意见 private NodeInfo nigaoren_self;//拟稿人
	 * private NodeInfo tab_fawen_zihao;//tab_fawen_zihao private NodeInfo
	 * niandu_year;//niandu_year private NodeInfo dayin_self ;//dayin_self
	 * private NodeInfo ri_day ;//ri_day private NodeInfo
	 * nigaodanwei_selfdept;//拟稿单位 private NodeInfo
	 * tab_fawen_gongkai;//tab_fawen_gongkai 发文是否公开 private NodeInfo
	 * tab_fawen_miji;//密级 private NodeInfo biaoti;//标题 private NodeInfo
	 * fawen_bianhao;//fawen_bianhao 发文标号 private NodeInfo
	 * tab_fawen_wenzhong;//文种 private NodeInfo add_fujianlist;//add_fujianlist
	 * private NodeInfo jiaodui_self;//jiaodui_self private NodeInfo
	 * fenshu;//fenshu private NodeInfo lxrnames;//lxrnames private NodeInfo
	 * yue_month ;//yue_month private NodeInfo departmentadvice_1
	 * ;//departmentadvice_1 private NodeInfo tab_fawen_huanji;//缓急 private
	 * NodeInfo zhusong_sendto;//zhusong_sendto private NodeInfo
	 * gongwenzhutici;//gongwenzhutici private NodeInfo
	 * personaladvice_1;//personaladvice_1 private NodeInfo
	 * chaosong_sendto;//chaosong_sendto private NodeInfo
	 * bangongshi_yijian;//办公室意义 private NodeInfo chushi_yijian;//chushi_yijian
	 * private NodeInfo lxdianhuas;//lxdianhuas private NodeInfo
	 * nian_year;//nian_year public NodeInfo getQianfaren_self() { return
	 * qianfaren_self; } public void setQianfaren_self(NodeInfo qianfaren_self)
	 * { this.qianfaren_self = qianfaren_self; } public NodeInfo
	 * getDepartmentadvice_qianfa() { return departmentadvice_qianfa; } public
	 * void setDepartmentadvice_qianfa(NodeInfo departmentadvice_qianfa) {
	 * this.departmentadvice_qianfa = departmentadvice_qianfa; } public NodeInfo
	 * getPersonaladvice_qianfa() { return personaladvice_qianfa; } public void
	 * setPersonaladvice_qianfa(NodeInfo personaladvice_qianfa) {
	 * this.personaladvice_qianfa = personaladvice_qianfa; } public NodeInfo
	 * getNigaoren_self() { return nigaoren_self; } public void
	 * setNigaoren_self(NodeInfo nigaoren_self) { this.nigaoren_self =
	 * nigaoren_self; } public NodeInfo getTab_fawen_zihao() { return
	 * tab_fawen_zihao; } public void setTab_fawen_zihao(NodeInfo
	 * tab_fawen_zihao) { this.tab_fawen_zihao = tab_fawen_zihao; } public
	 * NodeInfo getNiandu_year() { return niandu_year; } public void
	 * setNiandu_year(NodeInfo niandu_year) { this.niandu_year = niandu_year; }
	 * public NodeInfo getDayin_self() { return dayin_self; } public void
	 * setDayin_self(NodeInfo dayin_self) { this.dayin_self = dayin_self; }
	 * public NodeInfo getRi_day() { return ri_day; } public void
	 * setRi_day(NodeInfo ri_day) { this.ri_day = ri_day; } public NodeInfo
	 * getNigaodanwei_selfdept() { return nigaodanwei_selfdept; } public void
	 * setNigaodanwei_selfdept(NodeInfo nigaodanwei_selfdept) {
	 * this.nigaodanwei_selfdept = nigaodanwei_selfdept; } public NodeInfo
	 * getTab_fawen_gongkai() { return tab_fawen_gongkai; } public void
	 * setTab_fawen_gongkai(NodeInfo tab_fawen_gongkai) { this.tab_fawen_gongkai
	 * = tab_fawen_gongkai; } public NodeInfo getTab_fawen_miji() { return
	 * tab_fawen_miji; } public void setTab_fawen_miji(NodeInfo tab_fawen_miji)
	 * { this.tab_fawen_miji = tab_fawen_miji; } public NodeInfo getBiaoti() {
	 * return biaoti; } public void setBiaoti(NodeInfo biaoti) { this.biaoti =
	 * biaoti; } public NodeInfo getFawen_bianhao() { return fawen_bianhao; }
	 * public void setFawen_bianhao(NodeInfo fawen_bianhao) { this.fawen_bianhao
	 * = fawen_bianhao; } public NodeInfo getTab_fawen_wenzhong() { return
	 * tab_fawen_wenzhong; } public void setTab_fawen_wenzhong(NodeInfo
	 * tab_fawen_wenzhong) { this.tab_fawen_wenzhong = tab_fawen_wenzhong; }
	 * public NodeInfo getAdd_fujianlist() { return add_fujianlist; } public
	 * void setAdd_fujianlist(NodeInfo add_fujianlist) { this.add_fujianlist =
	 * add_fujianlist; } public NodeInfo getJiaodui_self() { return
	 * jiaodui_self; } public void setJiaodui_self(NodeInfo jiaodui_self) {
	 * this.jiaodui_self = jiaodui_self; } public NodeInfo getFenshu() { return
	 * fenshu; } public void setFenshu(NodeInfo fenshu) { this.fenshu = fenshu;
	 * } public NodeInfo getLxrnames() { return lxrnames; } public void
	 * setLxrnames(NodeInfo lxrnames) { this.lxrnames = lxrnames; } public
	 * NodeInfo getYue_month() { return yue_month; } public void
	 * setYue_month(NodeInfo yue_month) { this.yue_month = yue_month; } public
	 * NodeInfo getDepartmentadvice_1() { return departmentadvice_1; } public
	 * void setDepartmentadvice_1(NodeInfo departmentadvice_1) {
	 * this.departmentadvice_1 = departmentadvice_1; } public NodeInfo
	 * getTab_fawen_huanji() { return tab_fawen_huanji; } public void
	 * setTab_fawen_huanji(NodeInfo tab_fawen_huanji) { this.tab_fawen_huanji =
	 * tab_fawen_huanji; } public NodeInfo getZhusong_sendto() { return
	 * zhusong_sendto; } public void setZhusong_sendto(NodeInfo zhusong_sendto)
	 * { this.zhusong_sendto = zhusong_sendto; } public NodeInfo
	 * getGongwenzhutici() { return gongwenzhutici; } public void
	 * setGongwenzhutici(NodeInfo gongwenzhutici) { this.gongwenzhutici =
	 * gongwenzhutici; } public NodeInfo getPersonaladvice_1() { return
	 * personaladvice_1; } public void setPersonaladvice_1(NodeInfo
	 * personaladvice_1) { this.personaladvice_1 = personaladvice_1; } public
	 * NodeInfo getChaosong_sendto() { return chaosong_sendto; } public void
	 * setChaosong_sendto(NodeInfo chaosong_sendto) { this.chaosong_sendto =
	 * chaosong_sendto; } public NodeInfo getBangongshi_yijian() { return
	 * bangongshi_yijian; } public void setBangongshi_yijian(NodeInfo
	 * bangongshi_yijian) { this.bangongshi_yijian = bangongshi_yijian; } public
	 * NodeInfo getChushi_yijian() { return chushi_yijian; } public void
	 * setChushi_yijian(NodeInfo chushi_yijian) { this.chushi_yijian =
	 * chushi_yijian; } public NodeInfo getLxdianhuas() { return lxdianhuas; }
	 * public void setLxdianhuas(NodeInfo lxdianhuas) { this.lxdianhuas =
	 * lxdianhuas; } public NodeInfo getNian_year() { return nian_year; } public
	 * void setNian_year(NodeInfo nian_year) { this.nian_year = nian_year; }
	 */
}
