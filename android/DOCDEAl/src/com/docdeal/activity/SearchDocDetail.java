package com.docdeal.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.json.JSONException;
import org.json.JSONObject;

import com.global.Base64Util;
import com.global.Const;
import com.global.FileUtil;
import com.global.OpenFileUtil;
import com.global.RUtil;
import com.global.StringUtil;
import com.docdeal.R;
import com.docdeal.adapter.DocAdapter;
import com.docdeal.adapter.DocDetailAdapter;
import com.docdeal.adapterview.TitleBar;
import com.docdeal.bean.Attachments;
import com.docdeal.bean.Body;
import com.docdeal.bean.DocumentDetail;
import com.docdeal.bean.Form;
import com.docdeal.bean.NodeInfo;
import com.docdeal.parsexml.DocumentDetailXml;
import com.docdeal.util.ActivityUtil;
import com.docdeal.webservice.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 收文明细处理
 * 
 * @author 李超
 * 
 */
public class SearchDocDetail extends Activity {

	private String methodName = "getMobileGWInfo";
	private String returnProperty = "getMobileGWInfoReturn";
	private Bundle bundle = new Bundle();
	private String docId;
	private Form docForm;
	private DocumentDetail detail;
	private Document dealXml;
	private Handler handler = new Handler();
	private List<Object> nodeList = new ArrayList<Object>();
	private LinearLayout detailList;
	private DocDetailAdapter adapter;
	private WindowManager dm;
	private Map<String, String> dealMap = new HashMap<String, String>();
	private ProgressDialog proDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TitleBar.title="公文详细";
		ActivityUtil.getInstance().initActivity(this,R.layout.searchdocdetail_main);
		dm = this.getWindowManager();
		initView();
		initData();

		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		// setViewData();
	}

	private void initView() {

		docId = this.getIntent().getStringExtra("docId");
		detailList = (LinearLayout) findViewById(R.id.doc_detail);
		adapter = new DocDetailAdapter(this, LayoutInflater.from(this),
				nodeList, dm, dealMap, true, docId);
		proDialog = ProgressDialog.show(SearchDocDetail.this, "请稍后", "数据访问中",
				true, true);

	}

	private void setViewData() {
		adapter.addAll(nodeList);
		adapter.getView(detailList);

	}

	/**
	 * 解析webservice返回的xml文件
	 * 
	 * @param xml
	 */
	private void dealWebServiceData(String xml) {

		DocumentDetailXml dxml = new DocumentDetailXml();
		try {
			detail = dxml.parserXml(xml);
			docForm = detail.getForm();
			nodeList = docForm.getNodeList();
			Body body = detail.getBody();
			Attachments attach = detail.getAttachments();
			nodeList.add(body);
			nodeList.add(attach);
			handler.post(new Runnable() {

				@Override
				public void run() {
					setViewData();
					if (proDialog != null) {
						proDialog.dismiss();
					}
				}
			});
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取表单中可写内容并拼写成xml （最后处理表单提交的xml）
	 * 
	 * @param bundle
	 */
	private void getDealXml() {
		// TODO Auto-generated method stub
		// List<Object> nodeList = docForm.getCanWriteNode();
		Element el = DocumentHelper.createElement("root");

		dealXml = DocumentHelper.createDocument(el);
		dealXml.setXMLEncoding("GB2312");
		Element submit = el.addElement("公文");
		Element basic = submit.addElement("基本信息");
		Element form = submit.addElement("表单");
		basic.addElement("公文标识").setText(docId);
		basic.addElement("发送时间").setText(sendTime());
		addWriteField(dealMap);
	}

	/**
	 * 生成提交时上传的xml
	 * 
	 * @param id
	 * @param opinion
	 */
	private void addWriteField(int id, String opinion) {
		if (dealXml == null) {
			getDealXml();
		}
		String fieldName = RUtil.getNameById(id);
		Element root = dealXml.getRootElement();

		Element parent = (Element) root.selectSingleNode("/root/公文/表单/"
				+ fieldName);
		if (parent != null) {
			CDATA cOpinion = DocumentHelper.createCDATA(opinion);
			parent.add(cOpinion);
		} else {

			parent = (Element) root.selectSingleNode("/root/公文/表单");
			Element newFiled = DocumentHelper.createElement(fieldName);
			CDATA cOpinion = DocumentHelper.createCDATA(opinion);
			newFiled.add(cOpinion);
			parent.add(newFiled);
		}
		// System.out.println(dealXml.asXML());

	}

	private void addWriteField(Map<String, String> dealMap) {
		Element root = dealXml.getRootElement();
		Iterator<String> it = dealMap.keySet().iterator();
		while (it.hasNext()) {

			String fieldName = it.next();

			Element parent = (Element) root.selectSingleNode("/root/公文/表单/"
					+ fieldName);
			if (parent != null) {
				CDATA cOpinion = DocumentHelper.createCDATA(dealMap
						.get(fieldName));
				parent.add(cOpinion);
			} else {

				parent = (Element) root.selectSingleNode("/root/公文/表单");
				Element newFiled = DocumentHelper.createElement(fieldName);
				CDATA cOpinion = DocumentHelper.createCDATA(dealMap
						.get(fieldName));
				newFiled.add(cOpinion);
				parent.add(newFiled);
			}
		}
	}

	/**
	 * 处理人与时间
	 * 
	 * @return
	 */
	private String getNameAndDate() {

		java.text.DateFormat format1 = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		String time = format1.format(new Date());
		time = time.replaceFirst("-", "年").replaceFirst("-", "月").concat("日");
		detail.getBasic().getTrasactor();
		return detail.getBasic().getTrasactor() + time;

	}

	private String sendTime() {

		java.text.DateFormat format1 = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:MM");
		String time = format1.format(new Date());
		return time;
	}

	private void initData() {
		Thread dataThread = new Thread(new WebDataThread());
		dataThread.start();
	}

	private class WebDataThread implements Runnable {

		@Override
		public void run() {
			String result = getWebServiceData();
			dealWebServiceData(result);
		}
	}

	private String getWebServiceData() {
		LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
		parameters.put("gwSign", docId);
		WebService wb = new WebService(methodName, parameters, returnProperty);
		String result = wb.getData();
		return result;
	}

	/**
	 * 打开文件
	 * 
	 * @author Administrator
	 * 
	 */
	private class openDocDetail implements Runnable {

		@Override
		public void run() {

			String filePath = getDocInput();
			// TODO Auto-generated method stub
		}
	}

	private String getDocInput() {
		String url = Const.url + "do/control/WordFileGetTest/getFile";
		String path = "";
		try {
			URL uri = new URL(url);
			HttpURLConnection urlCon = (HttpURLConnection) uri.openConnection();
			InputStream input = urlCon.getInputStream();
			String sss = FileUtil.saveFileToSdkcard(input, "doc");
			OpenFileUtil.openFile(this, sss);
			// Intent intent = OpenFileUtil.openFile(sss);
			// startActivity(intent);
			// Log.i("1", sss);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * 根据填写意见框 跳转至意见填写页面
	 * 
	 * @param requestCode
	 */
	private void startActivityOfText(int viewId) {
		Intent intent = new Intent(this, DealOpinion.class);
		String temp = "";
		String oldWithnew = "";
		switch (viewId) {
		case R.id.shouwenniban_yijian:// 拟办意见
			temp = docForm.getTextByName("shouwenniban_yijian");
			oldWithnew = ((TextView) findViewById(viewId)).getText().toString();
			bundle.putString("opinion",
					oldWithnew.substring(oldWithnew.indexOf(temp)));
			break;
		case R.id.personaladvice_ldps:// 领导批示
			temp = docForm.getTextByName("departmentadvice_ldps");
			oldWithnew = ((TextView) findViewById(viewId)).getText().toString();
			bundle.putString(
					"opinion",
					oldWithnew.substring(oldWithnew.indexOf(temp)
							+ temp.length()));
			break;
		case R.id.personaladvice_bljg:// 办理结果
			temp = docForm.getTextByName("departmentadvice_bljg");
			oldWithnew = ((TextView) findViewById(viewId)).getText().toString();
			bundle.putString("opinion",
					oldWithnew.substring(oldWithnew.indexOf(temp)));
			break;
		default:
			break;
		}
		bundle.putInt("requestCode", viewId);
		intent.putExtras(bundle);
		startActivityForResult(intent, viewId);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case R.id.shouwenniban_yijian:// 拟办意见
			setEditOpinion(resultCode, data.getStringExtra("opinion"));
			break;
		case R.id.personaladvice_ldps:// 领导批示
			setEditOpinion(resultCode, data.getStringExtra("opinion"));
			break;
		case R.id.personaladvice_bljg:// 办理结果
			setEditOpinion(resultCode, data.getStringExtra("opinion"));
			break;
		default:
			break;
		}
	}

	/**
	 * 处理意见
	 * 
	 * @param viewId
	 * @param opinion
	 */
	private void setEditOpinion(int viewId, String opinion) {
		TextView view = (TextView) findViewById(viewId);
		String temp = "";
		switch (viewId) {
		case R.id.shouwenniban_yijian:// 拟办意见
			temp = docForm.getTextByName("shouwenniban_yijian");
			setTextViewWithDate(view, temp, opinion);
			break;
		case R.id.personaladvice_ldps:// 领导批示
			temp = docForm.getTextByName("departmentadvice_ldps");
			setTextViewWithDate(view, temp, opinion);
			break;
		case R.id.personaladvice_bljg:// 办理结果
			temp = docForm.getTextByName("departmentadvice_bljg");
			setTextViewWithDate(view, temp, opinion);
			break;
		default:
			temp = docForm.getTextByName(RUtil.getNameById(viewId));
			setTextViewWithDate(view, temp, opinion);
			break;
		}

	}

	private void setTextViewWithDate(TextView tv, String temp, String opinion) {
		StringBuffer lastO = new StringBuffer();
		if (StringUtil.isEmpty(temp)) {
			lastO = lastO.append("");
		} else {
			lastO = lastO.append(temp);
			// lastO.append("\n");
		}
		lastO.append(opinion);
		lastO.append("  ");
		lastO.append(getNameAndDate());
		lastO.append("\n");
		tv.setText(lastO.toString());
		addWriteField(tv.getId(), lastO.toString().replaceAll("\n", ""));
	}

}
