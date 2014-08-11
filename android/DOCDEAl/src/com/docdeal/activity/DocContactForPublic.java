package com.docdeal.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.docdeal.R;
import com.docdeal.adapterview.TitleBar;
import com.docdeal.parsexml.ContactGroupXml;
import com.docdeal.util.ActivityUtil;
import com.docdeal.webservice.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DocContactForPublic extends Activity {
	private ListView listView;
	private SimpleAdapter spa;
	private List<Map<String, String>> list;
	private Bundle bundle;
	private Intent intent;
	private String type;
	private LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
	private TextView tv;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		type = intent.getStringExtra("contactType");
		if (type.equals("0")) {
			TitleBar.title = "单位通讯录";
		} else if (type.equals("1")) {
			TitleBar.title = "公共通讯录";
		} else if (type.equals("2")) {
			TitleBar.title = "个人通讯录";
		}
		ActivityUtil.getInstance()
				.initActivity(this, R.layout.contactforpublic);
		list = new ArrayList<Map<String, String>>();
		spa = new SimpleAdapter(this, list, R.layout.contactforpublicitem,
				new String[] { "name", },
				new int[] { R.id.contactforpublicitems });
		progressDialog = ProgressDialog.show(DocContactForPublic.this, "请稍后",
				"数据访问中……", true, false);
		listView = (ListView) findViewById(R.id.contactForPublicItem);
		listView.setAdapter(spa);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String orgid = list.get(arg2).get("id");
				bundle = new Bundle();
				bundle.putString("orgid", orgid);
				bundle.putString("contactType",
						intent.getStringExtra("contactType"));
				startActivity(new Intent(DocContactForPublic.this,
						DocContactForPublicList.class).putExtras(bundle));

			}
		});
		tv = (TextView) findViewById(R.id.contactforpublicitemALL);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				bundle = new Bundle();
				bundle.putString("orgid", MenuMain.username + ":allusers");
				bundle.putString("contactType",
						intent.getStringExtra("contactType"));
				startActivity(new Intent(DocContactForPublic.this,
						DocContactForPublicList.class).putExtras(bundle));
			}
		});
		Thread t = new Thread(new Runnable() {

			private Handler handler = new Handler();

			public void run() {
				String res = getWebServiceData();
				ContactGroupXml.xmlToList(res, list);
				handler.post(new Runnable() {
					@Override
					public void run() {
						spa.notifyDataSetChanged();
						progressDialog.dismiss();
					}
				});
			}
		});
		t.start();
	}

	private String getWebServiceData() {
		parameters.put("userId", MenuMain.username);
		parameters.put("booktype", type);

		WebService wb = new WebService("getDeptIdByUseridAndBooktype",
				parameters, "getDeptIdByUseridAndBooktypeReturn");
		String result = wb.getData();
		return result;
	}
}
