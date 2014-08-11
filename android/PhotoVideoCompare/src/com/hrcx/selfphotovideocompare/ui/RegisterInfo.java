package com.hrcx.selfphotovideocompare.ui;

import com.hrcx.selfphotovideocompare.R;
import com.hrcx.selfphotovideocompare.dao.net.NetProtocol;
import com.hrcx.selfphotovideocompare.utils.Constant;
import com.hrcx.selfphotovideocompare.utils.DataCleanManager;
import com.hrcx.selfphotovideocompare.utils.NetUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegisterInfo extends Activity implements OnClickListener {

	private ImageButton btn_submit;
	private LinearLayout ll;
	private EditText phoneET;
	private EditText addressET;
	private EditText passwordET;
	ProgressDialog pd;
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register_info);
		findView();
	}

	private void findView() {
		pd = new ProgressDialog(this);
		ll = (LinearLayout) findViewById(R.id.l_ll);
		btn_submit = (ImageButton) findViewById(R.id.button_register_info_next);
		btn_submit.setOnClickListener(this);
		phoneET = (EditText) findViewById(R.id.editText_register_phone);
		addressET = (EditText) findViewById(R.id.editText_register_address);
		passwordET = (EditText) findViewById(R.id.editText_register_password);

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_register_info_next:
			String phone,address,password;
			phone = phoneET.getText().toString();
			address = addressET.getText().toString();
			password = passwordET.getText().toString();
			if (phone.length() > 0 && password.length() > 0&address.length() > 0) {
				if (NetUtil.checkNet(RegisterInfo.this)) {
					pd.setMessage(this.getResources().getString(
							R.string.fyi_loading));
					pd.show();
					DataCleanManager.cleanApplicationData(this);
					new ActivityTask().execute(Constant.registerid,password,phone,address);
				} else {
					NetUtil.showToastNoNet(RegisterInfo.this);
				}
			} else {
				ll.startAnimation(AnimationUtils.loadAnimation(
						RegisterInfo.this, R.anim.shake));
				if (phone.length() <= 0) {
					Toast.makeText(this,
							"ÇëÊäÈëµç»°ºÅÂë",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (address.length() <= 0) {
					Toast.makeText(this,
							"ÇëÊäÈëµØÖ·",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (password.length() <= 0) {
					Toast.makeText(this,
							"ÇëÊäÈëÃÜÂë",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
			break;
		default:
			break;
		}
	}

	class ActivityTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(RegisterInfo.this).register(params[0],
					params[1], params[2], params[3]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				Toast.makeText(RegisterInfo.this, "×¢²áÊ§°Ü",
						Toast.LENGTH_SHORT).show();
				return;
			}
			super.onPostExecute(result);
			Toast.makeText(RegisterInfo.this, "×¢²á³É¹¦",
					Toast.LENGTH_SHORT).show();
			Intent it = new Intent(RegisterInfo.this, LoginActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(it);
		}
	}


}
