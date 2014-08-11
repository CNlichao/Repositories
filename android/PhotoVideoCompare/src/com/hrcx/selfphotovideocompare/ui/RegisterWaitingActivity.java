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

public class RegisterWaitingActivity extends BasicActivity {

	private TextView tv_waiting;
	private TextView tv_remain_seconds;
	private Thread timeThread;
	private boolean isTiming = true;
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
				view.postInvalidate();
				// 访问服务端
				new ValidationIsOkTask().execute();
				/*
				 * Intent intent = new Intent(WaitingActivity.this,
				 * CameraRecordActivity.class); startActivity(intent);
				 */
			} else {
				Constant.waitTime--;
				tv_remain_seconds.setText("还剩" + Constant.waitTime + "秒");
				view.postInvalidate();
			}
		}
	};


	private void findView() {
		tv_waiting = (TextView) findViewById(R.id.tv_waiting);
		tv_remain_seconds = (TextView) findViewById(R.id.tv_remain_seconds);
		tv_remain_seconds.setText("还剩" + Constant.waitTime + "秒");
	}

	class ValidationIsOkTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(RegisterWaitingActivity.this).registerIsOk();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (Constant.waitTime <= 0) {
					tv_waiting.setVisibility(View.GONE);
					tv_remain_seconds.setVisibility(View.GONE);
					view.postInvalidate();
					if (result) {
						Toast.makeText(RegisterWaitingActivity.this,
								"照片验证成功", Toast.LENGTH_SHORT)
								.show();
						Intent intent = new Intent(RegisterWaitingActivity.this,
								RegisterInfo.class);
						startActivity(intent);
						finish();
					}else{
						Toast.makeText(RegisterWaitingActivity.this,
								"照片验证失败", Toast.LENGTH_SHORT)
								.show();
						finish();
					}
				
			} else {
				pd.dismiss();
				tv_waiting.setVisibility(View.VISIBLE);
				tv_remain_seconds.setVisibility(View.VISIBLE);
				view.postInvalidate();
				isTiming = true;
				startThread();
			}
		}
	}

}
