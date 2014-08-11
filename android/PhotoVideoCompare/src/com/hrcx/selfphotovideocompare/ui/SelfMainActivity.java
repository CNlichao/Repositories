package com.hrcx.selfphotovideocompare.ui;

import com.hrcx.selfphotovideocompare.R;
import com.hrcx.selfphotovideocompare.dao.net.NetProtocol;
import com.hrcx.selfphotovideocompare.utils.Constant;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SelfMainActivity extends Activity implements OnClickListener {
	private TextView currentStateTv;
	private ImageButton authenticationHistoryBtn;
	private ImageButton authenticationStartBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_main);
		Constant.mainActivity=this;
		findView();
	}

	private void findView() {
		currentStateTv = (TextView) findViewById(R.id.currentState);
		if (Constant.loginState == 0) {
			currentStateTv.setText("认证通过");
		} else if (Constant.loginState == 1) {
			currentStateTv.setText("未认证");
		} else if (Constant.loginState == 2) {
			currentStateTv.setText("等待审核");
		}
		authenticationHistoryBtn = (ImageButton) findViewById(R.id.authenticationHistory);
		authenticationHistoryBtn.setOnClickListener(this);
		authenticationStartBtn = (ImageButton) findViewById(R.id.authenticationStart);
		authenticationStartBtn.setOnClickListener(this);
		if (Constant.loginState != 1) {
			authenticationStartBtn.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.authenticationHistory:
			new ActivityTask().execute();
			break;
		case R.id.authenticationStart:
			Intent intent = new Intent(this, CameraImage.class);
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}

	}

	class ActivityTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(SelfMainActivity.this)
					.getAuthenticationHistory();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (!result) {
				return;
			}

			Toast.makeText(SelfMainActivity.this, R.string.login_success,
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(SelfMainActivity.this,
					SelfAusHistory.class);
			startActivityForResult(intent, 1);
			// new ActivityTask2().execute();
		}
	}

}
