package com.docdeal.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.docdeal.adapterview.AlphaView;
import com.docdeal.adapterview.TitleBar;
import com.docdeal.adapterview.AlphaView.*;
import com.docdeal.bean.ContactItem;
import com.docdeal.R;
import com.docdeal.parsexml.ContactUserXml;
import com.docdeal.util.ActivityUtil;
import com.docdeal.webservice.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DocContactForPublicList extends Activity implements
		OnAlphaChangedListener {
	private ListView listView;
	private AlphaView alphaView;
	private TextView overlay;
	private Intent intent;

	private WindowManager windowManager;
	private List<ContactItem> list;
	private ListAdapter adapter;
	private HashMap<String, Integer> alphaIndexer; // 存放存在的汉语拼音首字母和与之对应的列表位置
	private OverlayThread overlayThread;

	private String type;
	private LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
	private String orgid;
	
	private ProgressDialog progressDialog;
	
	private LayoutInflater inflater;
	
	private Handler handler = new Handler();

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
		orgid = intent.getStringExtra("orgid");
		ActivityUtil.getInstance().initActivity(this, R.layout.main);
		list = new ArrayList<ContactItem>();
		alphaIndexer = new HashMap<String, Integer>();
		overlayThread = new OverlayThread();
		adapter = new ListAdapter();
		intitWidget();
		initOverlay();
		progressDialog = ProgressDialog.show(DocContactForPublicList.this, "请稍后",
				"数据访问中……", true, false);
		Thread t=new Thread(new Runnable() {
			public void run() {
				String s = getWebServiceData();
				ContactUserXml.xmlToList(s, list);
				handler.post(new Runnable() {
					@Override
					public void run() {	
						adapter = new ListAdapter();
						intitWidget();
						initOverlay();
						adapter.notifyDataSetChanged();	
						progressDialog.dismiss();
					}
				});
				
			}
		});
		t.start();
	}

	@Override
	protected void onStop() {
		try {
			windowManager.removeViewImmediate(overlay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onStop();
	}

	// 初始化控件
	private void intitWidget() {
		listView = (ListView) findViewById(R.id.list_view);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bundle bundle = new Bundle();
				bundle.putString("userId",
						String.valueOf(list.get(arg2).getId()));
				bundle.putString("phone",
						String.valueOf(list.get(arg2).getNumber()));
				bundle.putString("name",
						String.valueOf(list.get(arg2).getName()));
				bundle.putString("contactType",
						intent.getStringExtra("contactType"));
				if (type.equals("0"))
					startActivity(new Intent(DocContactForPublicList.this,
							DocContactDetail2.class).putExtras(bundle));
				else
					startActivity(new Intent(DocContactForPublicList.this,
							DocContactDetail.class).putExtras(bundle));
			}
		});
		alphaView = (AlphaView) findViewById(R.id.alphaView);
		alphaView.setOnAlphaChangedListener(this);
	}

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	private class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public ListAdapter() {
			this.inflater = LayoutInflater.from(DocContactForPublicList.this);
			for (int i = 0; i < list.size(); i++) {
				// 当前汉语拼音首字母
				String currentAlpha = list.get(i).getAlpha();
				// 上一个汉语拼音首字母，如果不存在为“ ”
				String previewAlpha = (i - 1) >= 0 ? list.get(i - 1).getAlpha()
						: " ";
				if (!previewAlpha.equals(currentAlpha)) {
					String alpha = list.get(i).getAlpha();
					alphaIndexer.put(alpha, i);
				}
			}
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ContactItem item = list.get(position);
			holder.name.setText(item.getName());
			holder.number.setText(item.getNumber());

			String currentAlpha = list.get(position).getAlpha();
			String previewAlpha = (position - 1) >= 0 ? list.get(position - 1)
					.getAlpha() : " ";
			if (!previewAlpha.equals(currentAlpha)) {
				holder.alpha.setVisibility(View.VISIBLE);
				holder.alpha.setText(currentAlpha);
			} else {
				holder.alpha.setVisibility(View.GONE);
			}
			return convertView;
		}

	}

	private final class ViewHolder {
		TextView alpha;
		TextView name;
		TextView number;

		public ViewHolder(View v) {
			alpha = (TextView) v.findViewById(R.id.alpha_text);
			name = (TextView) v.findViewById(R.id.name);
			number = (TextView) v.findViewById(R.id.number);
		}
	}

	

	// 设置overlay不可见
	private class OverlayThread implements Runnable {

		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}

	@Override
	public void OnAlphaChanged(String s, int index) {
		if (s != null && s.trim().length() > 0) {
			overlay.setText(s);
			overlay.setVisibility(View.VISIBLE);
			handler.removeCallbacks(overlayThread);
			handler.postDelayed(overlayThread, 700);
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				listView.setSelection(position);
			}
		}
	}

	private String getWebServiceData() {
		parameters.put("orgId", orgid);
		Log.d("name", MenuMain.username);
		parameters.put("bookType", type);
		Log.d("type", type);

		WebService wb = new WebService("getUserByOrgidAndBooktype", parameters,
				"getUserByOrgidAndBooktypeReturn");
		String result = wb.getData();
		return result;
	}
}
