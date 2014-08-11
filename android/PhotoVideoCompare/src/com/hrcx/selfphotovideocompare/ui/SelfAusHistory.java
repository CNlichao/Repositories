package com.hrcx.selfphotovideocompare.ui;

import java.text.SimpleDateFormat;
import org.json.JSONException;
import org.json.JSONObject;
import com.hrcx.selfphotovideocompare.R;
import com.hrcx.selfphotovideocompare.entity.UserInfo;
import com.hrcx.selfphotovideocompare.utils.Constant;
import com.hrcx.selfphotovideocompare.utils.PublicUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SelfAusHistory extends Activity {
	ListView ausHistoryListView;

	AusHistoryAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aus_history);
		findView();
	}

	private void findView() {
		ausHistoryListView = (ListView) findViewById(R.id.ausHistoryList);
		adapter = new AusHistoryAdapter(this);
		ausHistoryListView.setAdapter(adapter);

	}

	@SuppressLint("SimpleDateFormat")
	private class AusHistoryAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public AusHistoryAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Constant.ausHistoryList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return Constant.ausHistoryList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.item_aus_history, null);
			}
			final JSONObject userInfo = Constant.ausHistoryList.get(position);

			TextView ausTime = (TextView) convertView
					.findViewById(R.id.ausTime);
			TextView ausAddress = (TextView) convertView
					.findViewById(R.id.ausAddress);
			TextView ausResult = (TextView) convertView
					.findViewById(R.id.ausResult);

			try {
				ausTime.setText(new SimpleDateFormat("yy年M月d日").format(new SimpleDateFormat("yyyyMMdd").parse(userInfo.getString("date"))));

				ausAddress.setText(userInfo.getString("address").equals("null") ? ""
						: userInfo.getString("address"));
				String result = userInfo.getString("result");
				String validationType = userInfo.getString("validationType");
				if (validationType.equals("1")) {
					validationType = "手机验证";
				} else if (validationType.equals("2")) {
					validationType = "排查验证";
				} else if (validationType.equals("3")) {
					validationType = "抽查验证";
				} else if (validationType.equals("4")) {
					validationType = "自我验证";
				} else {
					validationType = "";
				}

				if (result.equals("null") || null == result || result == "") {
					result = "";
				} else if (result.equals("0") || result.equals("3")) {
					result = "通过";
				} else {
					result = "不通过";
				}
				ausResult.setText(validationType + result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}
	}

}
