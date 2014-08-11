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
import com.docdeal.adapter.DocAdapter;
import com.docdeal.adapterview.TitleBar;
import com.docdeal.bean.OfficialDocument;
import com.docdeal.parsexml.DocumentsListXml;
import com.docdeal.util.ActivityUtil;
import com.docdeal.webservice.WebService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DocumentList extends Activity {
	private String methodName = "getWaitTodo";
	private String returnProperty = "getWaitTodoReturn";
	LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
	private List<OfficialDocument> docList = new ArrayList<OfficialDocument>();
	private Bundle bundle = new Bundle();
	private DefaultHttpClient defaultClient = new DefaultHttpClient();;
	private DocAdapter docAdapter;
	private ListView docListView;
	private Button btnNext, btnPre;
	private Button pageInfo;
	private Handler handler;
	private StringBuffer pages = new StringBuffer();// �ܹ�����ҳ
	private StringBuffer rows = new StringBuffer();// �ܹ�������
	// private String USERSIGN = "hebingfei";
	private String USERSIGN = "";
	private int page = 1;
	private ProgressDialog progressDialog;

	private View.OnClickListener clickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TitleBar.title="�����б�";
		ActivityUtil.getInstance().initActivity(this,R.layout.doclist_main);
		initView();
		progressDialog = ProgressDialog
				.show(this, "���Ժ�", "���ݷ����С���", true, true);
		loadData();
	}

	private void initView() {
		SharedPreferences preferences2=getSharedPreferences("login", 0);
		String s=preferences2.getString("login", "");
		USERSIGN =s;
		bundle.putString(USERSIGN, USERSIGN);
		docAdapter = new DocAdapter(this, LayoutInflater.from(this), docList);
		docListView = (ListView) findViewById(R.id.doclist);
		docListView.setAdapter(docAdapter);
		docListView.setOnItemClickListener(new onItemClickListener());
		pageInfo = (Button) findViewById(R.id.pageinfo);

		btnNext = (Button) this.findViewById(R.id.btnNext);
		btnPre = (Button) this.findViewById(R.id.btnPre);
		clickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btnPre:
					preView();
					break;
				case R.id.btnNext:
					nextView();
					break;
				}
			}
		};
		btnPre.setOnClickListener(clickListener);
		btnNext.setOnClickListener(clickListener);
		handler = new Handler();

	}

	private void preView() {
		page--;
		loadData();
		// checkButton();
	}

	private void nextView() {
		page++;
		loadData();

	}

	private void checkButton() {
		if (page <= 1) {
			btnPre.setEnabled(false);
		} else {
			btnPre.setEnabled(true);
		}
		if (page < Integer.valueOf(pages.toString())) {
			btnNext.setEnabled(true);
		} else {
			btnNext.setEnabled(false);
		}

		pageInfo.setText("�ܹ�" + rows + "����¼  " + page + "/" + pages + "ҳ");

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
			// if (doc.getType().equals("����")) {
			// intent = new Intent(DocumentList.this, RecieveDocDetail.class);
			// } else if (doc.getType().equals("����")) {
			// intent = new Intent(DocumentList.this, SendDocDetail.class);
			// }
			intent = new Intent(DocumentList.this, DocDetail.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, 100);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Const.DEALCODE) {
			loadData();
		}
		if (requestCode == 100) {
			Toast.makeText(this, "���سɹ�", Toast.LENGTH_SHORT).show();
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
	 * webserviceȡ�ļ��б�
	 * 
	 * @author Administrator
	 * 
	 */
	private class WebDataThread implements Runnable {

		@Override
		public void run() {
			String result = getWebServiceData();
			dealWebServiceData(result);
			
				handler.post(new Runnable() {
					@Override
					public void run() {
						docAdapter.notifyDataSetChanged();
						checkButton();
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
						// docListView
						// .setOnItemClickListener(new onItemClickListener());

					}
				});
			

		}
	}

	private String getWebServiceData() {

		// parameters.put("", value)
		parameters.put("userSign", USERSIGN);
		parameters.put("offset", "" + page);
		parameters.put("rows", "10");

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
			// request.setHeader("Cookie", sessionId);
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
		docList.clear();
		docList.addAll(olist);
	}

	/**
	 * ������ؼ�����ȷ������ ѡ���˳�
	 */
	// @Override
	// public boolean dispatchKeyEvent(KeyEvent event) {
	// if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
	// && event.getAction() == KeyEvent.ACTION_DOWN
	// && event.getRepeatCount() == 0) {
	// // ����Ĳ�������
	// new AlertDialog.Builder(this)
	// .setTitle("ȷ���˳�����ô")
	// .setNegativeButton("ȡ��",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// }
	// })
	// .setPositiveButton("ȷ��",
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
