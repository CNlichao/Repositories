package com.hrcx.selfphotovideocompare.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.hrcx.selfphotovideocompare.R;
import com.hrcx.selfphotovideocompare.dao.net.NetProtocol;
import com.hrcx.selfphotovideocompare.utils.Constant;

public class WaitingActivity extends BasicActivity {

	private TextView tv_waiting;
	private TextView tv_remain_seconds;
	private TextView tv_data_connecting;
	private TextView tv_picture_on_loading;
	private Thread timeThread;
	private boolean isTiming = true;
	private String imageFilePath;
	// private String sfzh;
	// private String validation;
	View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/* setContentView(R.layout.activity_waiting); */
		LayoutInflater mInflater = LayoutInflater.from(this);
		view = mInflater.inflate(R.layout.activity_waiting, null);
		setContentView(view);
		findView();
		// 开启一个计时线程
		startThread();
	}

	private void startThread() {
		timeThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isTiming) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (isTiming) {
						Message msg = new Message();
						handler_time.sendMessage(msg);
					}
				}
			}
		});
		timeThread.start();
	}

	// 时间handler
	private Handler handler_time = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (Constant.waitTime <= 0) {
				isTiming = false;
				Constant.waitTime = 0;
				tv_waiting.setVisibility(View.GONE);
				tv_remain_seconds.setVisibility(View.GONE);
				tv_data_connecting.setVisibility(View.VISIBLE);
				tv_picture_on_loading.setVisibility(View.GONE);
				view.postInvalidate();
				// 访问服务端
				new ValidationIsOkTask().execute();

			} else {
				Constant.waitTime--;
				tv_remain_seconds.setText("还剩" + Constant.waitTime + "秒");
				view.postInvalidate();
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void findView() {
		Intent intent = getIntent();
		imageFilePath = intent.getExtras().getString("curenImageUrl");
		// sfzh = intent.getExtras().getString("sfzh");
		// validation = intent.getExtras().getString("validation");
		tv_waiting = (TextView) findViewById(R.id.tv_waiting);
		tv_remain_seconds = (TextView) findViewById(R.id.tv_remain_seconds);
		tv_remain_seconds.setText("还剩" + Constant.waitTime + "秒");
		tv_data_connecting = (TextView) findViewById(R.id.tv_data_connecting);
		tv_picture_on_loading = (TextView) findViewById(R.id.tv_picture_on_loading);
	}

	class ValidationIsOkTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(WaitingActivity.this).isSelfValidationOk();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				pd.dismiss();
				Toast.makeText(WaitingActivity.this,
						"验证失败，请重新拍照", Toast.LENGTH_SHORT).show();
				Intent itUpdate = new Intent(Constant.RECEIVER_SESSION_EXPIRE);
				sendBroadcast(itUpdate);
				Intent it = new Intent(WaitingActivity.this,
						CameraImage.class);
				startActivity(it);
				finish();
				return;

			}
			super.onPostExecute(result);
			if (result) {
				if (Constant.waitTime <= 0) {

					Toast.makeText(WaitingActivity.this,
							R.string.data_request_success, Toast.LENGTH_SHORT)
							.show();
					tv_waiting.setVisibility(View.GONE);
					tv_remain_seconds.setVisibility(View.GONE);
					tv_data_connecting.setVisibility(View.GONE);
					tv_picture_on_loading.setVisibility(View.VISIBLE);
					view.postInvalidate();
					new GetPhotoesTask().execute();
				}
			} else {
				Toast.makeText(WaitingActivity.this,
						R.string.data_request_success, Toast.LENGTH_SHORT)
						.show();
				pd.dismiss();
				tv_waiting.setVisibility(View.VISIBLE);
				tv_remain_seconds.setVisibility(View.VISIBLE);
				tv_data_connecting.setVisibility(View.GONE);
				tv_picture_on_loading.setVisibility(View.GONE);
				view.postInvalidate();
				isTiming = true;
				startThread();
			}
		}
	}

	class GetPhotoesTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			pd.dismiss();
			if (!result) {
				// Toast.makeText(WaitingActivity.this,
				// R.string.photo_download_fail, Toast.LENGTH_SHORT)
				// .show();
				return;
			}
			super.onPostExecute(result);
			
//			Intent intent = new Intent(WaitingActivity.this,
//					CameraRecordActivity.class);
			Intent intent = new Intent(WaitingActivity.this,
					RecommendedActivity.class);
			intent.putExtra("curenImageUrl", imageFilePath);
			// intent.putExtra("sfzh", sfzh);
			// intent.putExtra("validation", validation);
			startActivity(intent);
			finish();
		}
	}

}
