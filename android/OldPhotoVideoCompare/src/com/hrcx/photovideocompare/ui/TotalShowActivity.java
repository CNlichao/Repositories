package com.hrcx.photovideocompare.ui;

import com.hrcx.photovideocompare.R;
import com.hrcx.photovideocompare.dao.net.NetProtocol;
import com.hrcx.photovideocompare.entity.UserInfo;
import com.hrcx.photovideocompare.utils.Constant;
import com.hrcx.photovideocompare.utils.PublicUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 * 
 */
public class TotalShowActivity extends BasicActivity implements
		View.OnClickListener {

	private EditText et_sfz;
	private Button btn_search;
	private ListView lv_user_info;
	private UserInfoAdapter userInfoAdapter;
	private String searchSfzh;
	private TextView tv_total_record;
	private TextView tv_curpage_totalpage;
	private Button btn_lastpage;
	private Button btn_nextpage;
	private Button btn_skip;
	private AlertDialog dialog;
	private int skipPageValue;
	private UpdateBroadcast updateReceiver = null;
	ProgressDialog pd;
	boolean isAccess = false;
	View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		LayoutInflater mInflater = LayoutInflater.from(this.getParent());
		view = mInflater.inflate(R.layout.activity_total_show, null);
		setContentView(view);
		registerUpdate();
		findView();
	}

	private void findView() {
		pd = new ProgressDialog(this.getParent());
		et_sfz = (EditText) findViewById(R.id.et_sfz);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);
		lv_user_info = (ListView) findViewById(R.id.lv_user_info);
		userInfoAdapter = new UserInfoAdapter(this);
		lv_user_info.setAdapter(userInfoAdapter);
		tv_total_record = (TextView) findViewById(R.id.tv_total_record);
		tv_total_record.setText("共" + Constant.totalRows + "条记录");
		tv_curpage_totalpage = (TextView) findViewById(R.id.tv_curpage_totalpage);
		tv_curpage_totalpage.setText(Constant.totalCurPage + "/"
				+ Constant.totalPages);
		btn_lastpage = (Button) findViewById(R.id.btn_lastpage);
		btn_lastpage.setOnClickListener(this);
		if (Constant.totalCurPage == 1) {
			btn_lastpage.setClickable(false);
		}
		btn_nextpage = (Button) findViewById(R.id.btn_nextpage);
		btn_nextpage.setOnClickListener(this);
		if (Constant.totalCurPage == Constant.totalPages) {
			btn_nextpage.setClickable(false);
		}
		btn_skip = (Button) findViewById(R.id.btn_skip);
		btn_skip.setOnClickListener(this);
	}

	private void registerUpdate() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.RECEIVER_VERIFICATION);
		filter.addAction(Constant.RECEIVER_SESSION_EXPIRE);
		updateReceiver = new UpdateBroadcast();
		registerReceiver(updateReceiver, filter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Constant.totalUserInfoList.size() <= 0) {
			pd.setMessage(TotalShowActivity.this.getParent().getResources()
					.getString(R.string.fyi_loading));
			pd.show();
			new DownloadActivity().execute();
		}
		view.postInvalidate();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:
			if (!isAccess) {
				searchSfzh = et_sfz.getText().toString();
				if (searchSfzh.length() != 15 && searchSfzh.length() != 18) {
					Toast.makeText(TotalShowActivity.this,
							R.string.sfzh_below_15, Toast.LENGTH_SHORT).show();
				} else {
					isAccess = true;
					pd.setMessage(TotalShowActivity.this.getParent()
							.getResources().getString(R.string.fyi_loading));
					pd.show();
					new ActivityTask().execute(searchSfzh);
				}
			}
			break;
		case R.id.btn_lastpage:
			if (!isAccess) {
				isAccess = true;
				pd.setMessage(TotalShowActivity.this.getParent().getResources()
						.getString(R.string.fyi_loading));
				pd.show();
				new LastPageActivityTask().execute((Constant.totalCurPage - 1)
						+ "");
			}
			break;
		case R.id.btn_nextpage:
			if (!isAccess) {
				isAccess = true;
				pd.setMessage(TotalShowActivity.this.getParent().getResources()
						.getString(R.string.fyi_loading));
				pd.show();
				new NextPageActivityTask().execute((Constant.totalCurPage + 1)
						+ "");
			}
			break;
		case R.id.btn_skip:
			// 弹出对话框
			showSkipDialog();
			break;
		}
	}

	class UpdateBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				return;
			}
			String action = intent.getAction();
			if (Constant.RECEIVER_VERIFICATION.equals(action)) {
				new UpdateActivity().execute();
				view.postInvalidate();
			} else if (Constant.RECEIVER_SESSION_EXPIRE.equals(action)) {
				finish();
			}
		}
	}

	// 加载设置密码对话框
	private void showSkipDialog() {
		AlertDialog.Builder builder = new Builder(this.getParent());
		final View view = View.inflate(this.getParent(), R.layout.item_skip,
				null);
		builder.setView(view);
		builder.setCancelable(false);
		final EditText et_skip_to = (EditText) view
				.findViewById(R.id.et_skip_to);
		Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String skipPage = et_skip_to.getText().toString();
				if (skipPage != null && skipPage.length() > 0) {
					skipPageValue = Integer.parseInt(skipPage, 10);
					if (skipPageValue <= 0
							|| skipPageValue > Constant.totalPages) {
						Toast.makeText(TotalShowActivity.this,
								R.string.input_valid_pagenum,
								Toast.LENGTH_SHORT).show();
					} else {
						new SkipPageActivityTask().execute(skipPageValue + "");
						dialog.dismiss();
					}
				} else {
					Toast.makeText(TotalShowActivity.this,
							R.string.input_skip_pagenum, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (updateReceiver != null) {
			unregisterReceiver(updateReceiver);
		}
	}

	class ActivityTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(TotalShowActivity.this).search(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			pd.dismiss();
			if (!result) {
				if (Constant.isSessionExpire) {
					Toast.makeText(TotalShowActivity.this,
							R.string.session_expire, Toast.LENGTH_SHORT).show();
					Intent itUpdate = new Intent(
							Constant.RECEIVER_SESSION_EXPIRE);
					sendBroadcast(itUpdate);
					Intent it = new Intent(TotalShowActivity.this,
							LoginActivity.class);
					startActivity(it);
					finish();
					return;
				} else {
					Toast.makeText(TotalShowActivity.this,
							R.string.search_fail, Toast.LENGTH_SHORT).show();
					return;
				}
			}
			super.onPostExecute(result);
			isAccess = false;
			Toast.makeText(TotalShowActivity.this, R.string.search_success,
					Toast.LENGTH_SHORT).show();
			Intent it = new Intent(TotalShowActivity.this,
					SearchShowActivity.class);
			startActivity(it);
		}
	}

	class LastPageActivityTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(TotalShowActivity.this)
					.syncTotalData(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				pd.dismiss();
				if (Constant.isSessionExpire) {
					Toast.makeText(TotalShowActivity.this,
							R.string.session_expire, Toast.LENGTH_SHORT).show();
					Intent itUpdate = new Intent(
							Constant.RECEIVER_SESSION_EXPIRE);
					sendBroadcast(itUpdate);
					Intent it = new Intent(TotalShowActivity.this,
							LoginActivity.class);
					startActivity(it);
					finish();
					return;
				} else {
					return;
				}
			}
			super.onPostExecute(result);
			Constant.totalCurPage--;
			// 更新相关控件显示
			updateRelativeControlShow();
			pd.dismiss();
			isAccess = false;
		}
	}

	class NextPageActivityTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(TotalShowActivity.this)
					.syncTotalData(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				pd.dismiss();
				if (Constant.isSessionExpire) {
					Toast.makeText(TotalShowActivity.this,
							R.string.session_expire, Toast.LENGTH_SHORT).show();
					Intent itUpdate = new Intent(
							Constant.RECEIVER_SESSION_EXPIRE);
					sendBroadcast(itUpdate);
					Intent it = new Intent(TotalShowActivity.this,
							LoginActivity.class);
					startActivity(it);
					finish();
					return;
				} else {
					return;
				}
			}
			super.onPostExecute(result);
			Constant.totalCurPage++;
			// 更新相关控件显示
			updateRelativeControlShow();
			pd.dismiss();
			isAccess = false;
		}
	}

	class SkipPageActivityTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(TotalShowActivity.this)
					.syncTotalData(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				/* pd.dismiss(); */
				if (Constant.isSessionExpire) {
					Toast.makeText(TotalShowActivity.this,
							R.string.session_expire, Toast.LENGTH_SHORT).show();
					Intent itUpdate = new Intent(
							Constant.RECEIVER_SESSION_EXPIRE);
					sendBroadcast(itUpdate);
					Intent it = new Intent(TotalShowActivity.this,
							LoginActivity.class);
					startActivity(it);
					finish();
					return;
				} else {
					return;
				}
			}
			super.onPostExecute(result);
			/* pd.dismiss(); */
			Constant.totalCurPage = skipPageValue;
			// 更新相关控件显示
			updateRelativeControlShow();
		}
	}

	class UpdateActivity extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(TotalShowActivity.this)
					.syncTotalData(Constant.totalCurPage + "");
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				if (Constant.isSessionExpire) {
					Toast.makeText(TotalShowActivity.this,
							R.string.session_expire, Toast.LENGTH_SHORT).show();
					Intent itUpdate = new Intent(
							Constant.RECEIVER_SESSION_EXPIRE);
					sendBroadcast(itUpdate);
					Intent it = new Intent(TotalShowActivity.this,
							LoginActivity.class);
					startActivity(it);
					finish();
					return;
				} else {
					return;
				}
			}
			super.onPostExecute(result);
			userInfoAdapter.notifyDataSetChanged();
		}
	}

	class DownloadActivity extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(TotalShowActivity.this)
					.syncTotalData(Constant.totalCurPage + "");
		}

		@Override
		protected void onPostExecute(Boolean result) {
			pd.dismiss();
			if (!result) {
				if (Constant.isSessionExpire) {
					Toast.makeText(TotalShowActivity.this,
							R.string.session_expire, Toast.LENGTH_SHORT).show();
					Intent itUpdate = new Intent(
							Constant.RECEIVER_SESSION_EXPIRE);
					sendBroadcast(itUpdate);
					Intent it = new Intent(TotalShowActivity.this,
							LoginActivity.class);
					startActivity(it);
					finish();
					return;
				} else {
					return;
				}
			}
			super.onPostExecute(result);
			// 更新相关控件显示
			updateRelativeControlShow();
			tv_total_record.setText("共" + Constant.totalRows + "条记录");
		}
	}

	public void setButtonClickable() {
		if (Constant.totalCurPage == 1) {
			btn_lastpage.setClickable(false);
		} else {
			btn_lastpage.setClickable(true);
		}

		if (Constant.totalCurPage == Constant.totalPages) {
			btn_nextpage.setClickable(false);
		} else {
			btn_nextpage.setClickable(true);
		}
	}

	// 更新相关控件显示
	public void updateRelativeControlShow() {
		setButtonClickable();
		userInfoAdapter.notifyDataSetChanged();
		tv_curpage_totalpage.setText(Constant.totalCurPage + "/"
				+ Constant.totalPages);
	}

	class UserInfoAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public UserInfoAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return Constant.totalUserInfoList.size();
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
			final UserInfo userInfo = Constant.totalUserInfoList.get(position);
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
					Intent it = new Intent(TotalShowActivity.this,
							CameraImage.class);
					it.putExtra("sfzh", userInfo.getSfzh());
					it.putExtra("validation", userInfo.getValidation());
					startActivity(it);
				}
			});
			tv_name.setText(userInfo.getName());
			tv_sfzh.setText(userInfo.getSfzh());
			int color = PublicUtil.getSfzhDrawColor(userInfo.getValidation(),
					userInfo.getCheckYear());
			switch (color) {
			case Constant.RED:
				tv_sfzh.setTextColor(TotalShowActivity.this.getResources()
						.getColor(R.color.red));
				btn_verification.setVisibility(View.VISIBLE);
				break;
			case Constant.BLUE:
				tv_sfzh.setTextColor(TotalShowActivity.this.getResources()
						.getColor(R.color.blue));
				btn_verification.setVisibility(View.VISIBLE);
				break;
			case Constant.GREEN:
				tv_sfzh.setTextColor(TotalShowActivity.this.getResources()
						.getColor(R.color.green));
				btn_verification.setVisibility(View.GONE);
				break;
			}
			tv_area.setText(userInfo.getArea());
			return convertView;
		}
	}
}
