package com.hrcx.selfphotovideocompare.ui;

import com.hrcx.selfphotovideocompare.R;
import com.hrcx.selfphotovideocompare.dao.net.NetProtocol;
import com.hrcx.selfphotovideocompare.utils.Constant;
import com.hrcx.selfphotovideocompare.utils.NetUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class RegisterIdValidateActivity extends Activity implements OnClickListener {

	private ImageButton btn_next;
	private EditText idET;
	String mobile;
	ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register_idvalidate);
		init();
	}


	private void init() {
		pd = new ProgressDialog(this);
		btn_next = (ImageButton) findViewById(R.id.button_register_id_next);
		btn_next.setOnClickListener(this);
		idET = (EditText) findViewById(R.id.editText_register_id);
		idET.setText("110101194502020016");
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_register_id_next:
			
			if (idET.length()==18) {
				Constant.registerid = idET.getText().toString();
				if (NetUtil.checkNet(RegisterIdValidateActivity.this)) {
					pd.setMessage("正在验证该身份证");
					pd.show();
					pd.setCancelable(true);
					new ActivityTask().execute(Constant.registerid);
				} else {
					NetUtil.showToastNoNet(RegisterIdValidateActivity.this);
				}
			} else {				
					Toast.makeText(this,
							"请输入18位身份证号",
							Toast.LENGTH_SHORT).show();
					return;
			}
			break;
		default:
			break;
		}
	}

	class ActivityTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(RegisterIdValidateActivity.this).validateId(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				Toast.makeText(RegisterIdValidateActivity.this, "该身份证已注册或非法",
						Toast.LENGTH_SHORT).show();
				Intent it = new Intent(RegisterIdValidateActivity.this, LoginActivity.class);
				it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it);
				return;
			}
			super.onPostExecute(result);
			Toast.makeText(RegisterIdValidateActivity.this,"可以注册",
					Toast.LENGTH_SHORT).show();
			Constant.registerid=idET.getText().toString();
			Intent it = new Intent(RegisterIdValidateActivity.this, RegisterTakePhoto.class);
			startActivity(it);
			pd.dismiss();
		}
	}

}
