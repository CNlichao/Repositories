package com.docdeal.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
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

import com.global.Const;
import com.global.FileUtil;
import com.global.OpenFileUtil;
import com.global.RUtil;
import com.global.StringUtil;
import com.docdeal.R;
import com.docdeal.bean.DocumentDetail;
import com.docdeal.bean.Form;
import com.docdeal.bean.NodeInfo;
import com.docdeal.parsexml.DocumentDetailXml;
import com.docdeal.webservice.WebService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 发文明细
 * 
 * @author 李超
 * 
 */
public class SendDocDetail extends Activity implements OnClickListener {

	private String methodName = "getMobileGWInfo";
	private String returnProperty = "getMobileGWInfoReturn";

	private TextView laiwendanwei, laiwenzihao, tab_shouwen_miji, title,
			laiwen_date, banjieriqi, yeshu, shouwenniban_yijian,
			personaladvice_ldps, personaladvice_bljg, tab_fawen_huanji;
	private Button btnDetail, btnAttachment, btnSubmit, btnBack;
	private Bundle bundle = new Bundle();
	private String sessionId;
	private String docId;
	private Map<String, String> map = new HashMap<String, String>();
	private String[] fromData = { "socialSecurityId", "townName",
			"startedType", "cityName", "provinceName", "picture" };
	private String[] toData = { "laiwendanwei", "laiwenzihao",
			"tab_shouwen_miji", "title", "laiwen_date", "laiwen_date" };
	private Form docForm;
	DocumentDetail detail;
	private Document dealXml;

	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recieve_doc_detail);
		initView();
		initData();
		// setViewData();
	}

	private void initView() {

		laiwendanwei = (TextView) findViewById(R.id.laiwendanwei);// 来文单位
		laiwenzihao = (TextView) findViewById(R.id.laiwenzihao);// 来文字号
		tab_shouwen_miji = (TextView) findViewById(R.id.tab_shouwen_miji);// 收文密级
		title = (TextView) findViewById(R.id.title);// 标题
		laiwen_date = (TextView) findViewById(R.id.laiwen_date);// 来文日期
		banjieriqi = (TextView) findViewById(R.id.banjieriqi);// 办结日期
		yeshu = (TextView) findViewById(R.id.yeshu);// 页数
		shouwenniban_yijian = (TextView) findViewById(R.id.shouwenniban_yijian);// 拟办意见
		personaladvice_ldps = (TextView) findViewById(R.id.personaladvice_ldps);// 领导批示
		personaladvice_bljg = (TextView) findViewById(R.id.personaladvice_bljg);// 办理结果
		tab_fawen_huanji = (TextView) findViewById(R.id.tab_fawen_huanji);// 发文缓急

		btnDetail = (Button) findViewById(R.id.btn_docDetail);
		btnAttachment = (Button) findViewById(R.id.btn_attachment);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		btnDetail.setOnClickListener(this);
		btnAttachment.setOnClickListener(this);
		sessionId = this.getIntent().getStringExtra(Const.header);
		docId = this.getIntent().getStringExtra("docId");
		bundle.putString(Const.Cookie, sessionId);
	}

	private void setViewData() {
		laiwendanwei.setText(docForm.getTextByName("laiwendanwei"));
		laiwenzihao.setText(docForm.getTextByName("laiwenzihao"));
		tab_shouwen_miji.setText(docForm.getTextByName("tab_shouwen_miji"));
		title.setText(detail.getBasic().getOfficialDocTitle());
		laiwen_date.setText(docForm.getTextByName("laiwen_date"));
		banjieriqi.setText(docForm.getTextByName("banjieriqi"));
		yeshu.setText(docForm.getTextByName("yeshu"));
		tab_fawen_huanji.setText(docForm.getTextByName("tab_fawen_huanji"));
		shouwenniban_yijian.setText(docForm
				.getTextByName("shouwenniban_yijian"));
		personaladvice_ldps.setText(docForm
				.getTextByName("departmentadvice_ldps")
				+ docForm.getTextByName("personaladvice_ldps"));
		personaladvice_bljg.setText(docForm
				.getTextByName("departmentadvice_bljg")
				+ docForm.getTextByName("personaladvice_bljg"));
		if (docForm.getCanWrite("shouwenniban_yijian")) {
			shouwenniban_yijian.setOnClickListener(this);
			shouwenniban_yijian.setBackgroundResource(R.drawable.setbar_bg1);
		}
		if (docForm.getCanWrite("personaladvice_ldps")) {
			personaladvice_ldps.setOnClickListener(this);
			personaladvice_ldps.setBackgroundResource(R.drawable.setbar_bg1);
		}
		if (docForm.getCanWrite("personaladvice_bljg")) {
			personaladvice_bljg.setOnClickListener(this);
			personaladvice_bljg.setBackgroundResource(R.drawable.setbar_bg1);
		}
	}

	/**
	 * 页面点击事件
	 */
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {

		case R.id.shouwenniban_yijian:// 拟办意见
			startActivityOfText(v.getId());
			break;
		case R.id.personaladvice_ldps:// 领导批示
			startActivityOfText(v.getId());
			break;
		case R.id.personaladvice_bljg:// 办理结果
			startActivityOfText(v.getId());
			break;
		case R.id.btn_docDetail:
			Thread thread = new Thread(new openDocDetail());
			thread.start();
			break;
		case R.id.btn_attachment:
			break;
		case R.id.btn_submit:
			intent = new Intent(SendDocDetail.this, DocDeal.class);
			Bundle db = new Bundle();
			db.putSerializable("flow", detail.getFlow());
			db.putString("dealXml", dealXml.asXML());
			intent.putExtras(db);
			startActivityForResult(intent, R.id.btn_submit);
			break;
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 获取表单中可写内容并拼写成xml （最后处理表单提交的xml）
	 * 
	 * @param bundle
	 */
	private void getDealXml() {
		// TODO Auto-generated method stub
		// List<NodeInfo> nodeList = docForm.getCanWriteNode();
		Element el = DocumentHelper.createElement("root");

		dealXml = DocumentHelper.createDocument(el);
		dealXml.setXMLEncoding("GB2312");
		Element submit = el.addElement("公文");
		Element basic = submit.addElement("基本信息");
		Element form = submit.addElement("表单");
		basic.addElement("公文标识").setText(docId);
		basic.addElement("发送时间").setText(sendTime());
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
		root.selectSingleNode("/root/公文/表单");
		root.selectSingleNode("/公文/表单");
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
		/*
		 * File sss = new File("E:\\sss.txt"); int a;
		 * 
		 * FileWriter fw; try { fw = new FileWriter("/sdcard/myDoc/sss.txt",
		 * true);
		 * 
		 * fw.write(result); fw.close(); } catch (IOException e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); } catch (Exception
		 * e) { e.printStackTrace(); } finally {
		 * 
		 * }
		 */

		return result;
	}

	/**
	 * 解析返回的xml文件
	 * 
	 * @param xml
	 */
	private void dealWebServiceData(String xml) {

		DocumentDetailXml dxml = new DocumentDetailXml();
		try {
			detail = dxml.parserXml(xml);
			docForm = detail.getForm();
			// System.out.print("sss");
			handler.post(new Runnable() {

				@Override
				public void run() {
					setViewData();
				}
			});
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	private String getDocDetail() {
		String result = "";
		try {
			String url = Const.url
					+ "do/control/InsurerDo/updateUI?id=110101196805204823";
			HttpGet get = new HttpGet(url);

			HttpResponse respons = new DefaultHttpClient().execute(get);
			if (respons.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(respons.getEntity());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private class GetDocFromServer implements Runnable {

		@Override
		public void run() {
			String result = getDocDetail();
			dealResult(result);
			handler.post(new Runnable() {

				@Override
				public void run() {
					setViewData();
				}
			});
		}
	}

	private void dealResult(String result) {
		try {
			JSONObject js = new JSONObject(result);
			for (int i = 0; i < fromData.length; i++) {
				map.put(toData[i], js.getString(fromData[i]));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
