package com.docdeal.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.global.Const;
import com.docdeal.R;
import com.docdeal.adapter.SearchDocAdapter;
import com.docdeal.adapterview.TitleBar;
import com.docdeal.bean.OfficialDocument;
import com.docdeal.parsexml.DocumentsListXml;
import com.docdeal.util.ActivityUtil;
import com.docdeal.webservice.WebService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchDocList extends Activity {
	private String methodName = "getSearchResult";
	private String returnProperty = "getSearchResultReturn";
	private LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
	private String searchXml;
	private List<OfficialDocument> docList = new ArrayList<OfficialDocument>();
	private String sessionId;
	private Bundle bundle = new Bundle();
	private DefaultHttpClient defaultClient = new DefaultHttpClient();;
	private SearchDocAdapter docAdapter;
	private ListView docListView;
	private Handler handler;

	private StringBuffer pages = new StringBuffer();// 总共多少页
	private StringBuffer rows = new StringBuffer();// 总共多少条
	// private String USERSIGN = "duangjian";
	private String USERSIGN = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TitleBar.title="公文列表";
		ActivityUtil.getInstance().initActivity(this,R.layout.searchdoclist_main);
        
		initView();
		loadData();

	}

	private void initView() {
		Intent intent = this.getIntent();
		sessionId = intent.getStringExtra(Const.header);
		bundle.putString(Const.Cookie, sessionId);
		docAdapter = new SearchDocAdapter(this, LayoutInflater.from(this),
				docList);
		docListView = (ListView) findViewById(R.id.doclist);
		docListView.setAdapter(docAdapter);
		docListView.setOnItemClickListener(new onItemClickListener());
		searchXml = this.getIntent().getStringExtra("searchXml");
		USERSIGN = this.getIntent().getStringExtra("USERSIGN");
		handler = new Handler();

	}

	private class onItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Toast.makeText(DocumentsHandle.this, "nihao", 2000).show();
			ListView listView = (ListView) parent;
			OfficialDocument doc = (OfficialDocument) parent
					.getItemAtPosition(position);
			String docId = doc.getId();
			bundle.putString("docId", docId);
			Intent intent = null;
			// if (doc.getType().equals("收文")) {
			// intent = new Intent(DocumentList.this, RecieveDocDetail.class);
			// } else if (doc.getType().equals("发文")) {
			// intent = new Intent(DocumentList.this, SendDocDetail.class);
			// }
			intent = new Intent(SearchDocList.this, SearchDocDetail.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, 100);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100) {
			// Toast.makeText(this, "返回成功", Toast.LENGTH_SHORT).show();
		}
	};

	private void loadData() {
		Thread loadData = new Thread(new WebDataThread());
		loadData.start();
	}

	private class DataThread implements Runnable {

		@Override
		public void run() {
			String result = getDocList();
			dealResult(result);
			if (docList.size() > 0) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						docAdapter.notifyDataSetChanged();
						// docListView
						// .setOnItemClickListener(new onItemClickListener());

					}
				});

			}
		}
	}

	/**
	 * webservice取文件列表
	 * 
	 * @author Administrator
	 * 
	 */
	private class WebDataThread implements Runnable {

		@Override
		public void run() {
			String result = getWebServiceData();
			dealWebServiceData(result);
			if (docList.size() > 0) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						docAdapter.notifyDataSetChanged();
						// docListView
						// .setOnItemClickListener(new onItemClickListener());

					}
				});
			}

		}
	}

	private String getWebServiceData() {

		// parameters.put("", value)
		parameters.put("searchconditionxml", searchXml);

		WebService wb = new WebService(methodName, parameters, returnProperty);
		String result = wb.getData();
		// System.out.println(result);
		return result;
	}

	private String getDocList() {
		String url = Const.url + "do/control/InsurerDo/mobileList?userId="
				+ 31718 + "&validation=" + 0 + "&rows=" + 10 + "&page=" + 1;
		String result = "";

		try {
			HttpGet request = new HttpGet(url);
			request.setHeader("Cookie", sessionId);
			HttpResponse response = defaultClient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
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

	private void dealResult(String result) {
		try {
			JSONObject jo = new JSONObject(result);
			JSONArray ja = jo.getJSONArray("rows");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jTemp = ja.optJSONObject(i);
				// Document doc = new Document();
				// doc.setDocName(jTemp.getString("name"));
				// doc.setSendTime(jTemp.getString("areaName"));
				// docList.add(doc);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void dealWebServiceData(String xml) {
		DocumentsListXml dxml = new DocumentsListXml();
		List<OfficialDocument> olist = dxml.parserXml(xml, pages, rows);
		docList.addAll(olist);
	}

	/**
	 * 点击返回键弹出确定窗口 选择退出
	 */
	// @Override
	// public boolean dispatchKeyEvent(KeyEvent event) {
	// if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
	// && event.getAction() == KeyEvent.ACTION_DOWN
	// && event.getRepeatCount() == 0) {
	// // 具体的操作代码
	// new AlertDialog.Builder(this)
	// .setTitle("确定退出程序么")
	// .setNegativeButton("取消",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// }
	// })
	// .setPositiveButton("确定",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int whichButton) {
	// finish();
	// System.exit(0);
	// }
	// }).show();
	// return true;
	// }
	// return super.dispatchKeyEvent(event);
	// }
}
