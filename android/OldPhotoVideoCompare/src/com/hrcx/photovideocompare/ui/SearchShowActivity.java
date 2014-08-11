package com.hrcx.photovideocompare.ui;

import com.hrcx.photovideocompare.R;
import com.hrcx.photovideocompare.entity.UserInfo;
import com.hrcx.photovideocompare.utils.Constant;
import com.hrcx.photovideocompare.utils.PublicUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SearchShowActivity extends BasicActivity implements
		OnClickListener {

	private ListView lv_user_info;
	private UserInfoAdapter userInfoAdapter;
	TextView tv_no_result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		/* LayoutInflater mInflater = LayoutInflater.from(this.getParent()); */
		LayoutInflater mInflater = LayoutInflater.from(this);
		View view = mInflater.inflate(R.layout.activity_search_show, null);
		setContentView(view);
		findView();
	}

	private void findView() {		
		if (Constant.searchUserInfoList.size() <= 0) {
			tv_no_result = (TextView) findViewById(R.id.tv_no_result);
			tv_no_result.setVisibility(View.VISIBLE);
		} else {
			lv_user_info = (ListView) findViewById(R.id.lv_user_info);
			userInfoAdapter = new UserInfoAdapter(this);
			lv_user_info.setAdapter(userInfoAdapter);
		}
	}

	@Override
	public void onClick(View arg0) {

	}

	class UserInfoAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public UserInfoAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return Constant.searchUserInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_user_info, null);
			}
			final UserInfo userInfo = Constant.searchUserInfoList.get(position);
			TextView tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			TextView tv_sfzh = (TextView) convertView
					.findViewById(R.id.tv_sfzh);
			TextView tv_area = (TextView) convertView
					.findViewById(R.id.tv_area);
			Button btn_verification = (Button) convertView
					.findViewById(R.id.btn_verification);
			btn_verification.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent it = new Intent(SearchShowActivity.this,
							CameraImage.class);
					it.putExtra("sfzh", userInfo.getSfzh());
					it.putExtra("validation", userInfo.getValidation());
					startActivity(it);
					finish();
				}
			});
			tv_name.setText(userInfo.getName());
			tv_sfzh.setText(userInfo.getSfzh());
			int color = PublicUtil.getSfzhDrawColor(userInfo.getValidation(), userInfo.getCheckYear());
			switch (color) {
			case Constant.RED:
				tv_sfzh.setTextColor(SearchShowActivity.this.getResources()
						.getColor(R.color.red));
				btn_verification.setVisibility(View.VISIBLE);
				break;
			case Constant.BLUE:
				tv_sfzh.setTextColor(SearchShowActivity.this.getResources()
						.getColor(R.color.blue));
				btn_verification.setVisibility(View.VISIBLE);
				break;
			case Constant.GREEN:
				tv_sfzh.setTextColor(SearchShowActivity.this.getResources()
						.getColor(R.color.green));
				btn_verification.setVisibility(View.GONE);
				break;
			}
			tv_area.setText(userInfo.getArea());
			return convertView;
		}
	}

}
