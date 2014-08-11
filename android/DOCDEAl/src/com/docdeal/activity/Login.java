package com.docdeal.activity;

import java.util.LinkedHashMap;
import com.global.ConnectivityUtil;
import com.docdeal.R;
import com.docdeal.util.ActivityUtil;
import com.docdeal.webservice.WebService;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	private Button loginButton;
	private EditText userNameET, passWordET;
	private ProgressDialog proDialog;
	private String methodName = "getUserExist";
	private String returnProperty = "getUserExistReturn";
	private LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
	private String USERSIGN;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences preferences2=getSharedPreferences("login", 0);
		String s=preferences2.getString("login", "");
		
		//如果状态为已登录则跳转到主菜单
		if(!s.equals("")){
			Intent intent = new Intent(Login.this, MenuMain.class);
			startActivity(intent);
			finish();
		}
		ActivityUtil.getInstance().initActivity(this,R.layout.login);
		ActivityUtil.getInstance().getTitleBar().getSettingButton().setVisibility(View.INVISIBLE);
		Drawable drawableLeft= getResources().getDrawable(R.drawable.toppic);
		drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
		ActivityUtil.getInstance().getTitleBar().getText().setCompoundDrawables(drawableLeft, null,null,null);
		initView();
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				proDialog = ProgressDialog.show(Login.this, "请稍等", "正在登陆……",
						true, true);
				Thread login = new Thread(new WebDataThread());
				login.start();
			}
		});
	}

	private class WebDataThread implements Runnable {

		@Override
		public void run() {
			String result = getWebServiceData();
			if (result.equals("0")) {
				if (proDialog != null) {
					proDialog.dismiss();
				}
				Intent intent = new Intent(Login.this, MenuMain.class);	
				//登陆后记录当前用户状态！
				SharedPreferences preferences1=getSharedPreferences("login", 0);
				SharedPreferences.Editor editor1=preferences1.edit();
				editor1.putString("login", USERSIGN);
				editor1.commit();
				
				startActivity(intent);
				
			    SharedPreferences preferences = getSharedPreferences("service", 0); 
			       //获得修改器 
			       SharedPreferences.Editor editor = preferences.edit();
			       editor.putString("USERSIGN", intent.getStringExtra("USERSIGN"));
			       editor.commit();  
			       //启动服务，监听消息更新推送
				//startService(new Intent(Login.this,MessageService.class).putExtra("USERSIGN", USERSIGN));
				finish();
			} else {
				Message message = new Message();
				Bundle bd = new Bundle();
				bd.putString("isLogin", result);
				message.setData(bd);
				loginHandler.sendMessage(message);
			}
		}

	}

	private String getWebServiceData() {
		USERSIGN = userNameET.getText().toString();
		String userPassword = passWordET.getText().toString();
		parameters.put("userEmail", USERSIGN);
		parameters.put("userPassword", userPassword);
		WebService web = new WebService(methodName, parameters, returnProperty);
		String result = web.getData();
		return result;
	}

	/**
	 * 处理登录信息
	 */
	private Handler loginHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Bundle bundle = msg.getData();
			String isLogin = bundle.getString("isLogin");
			boolean isNet = ConnectivityUtil.isNetworkAvailable(Login.this);
			if (proDialog != null) {
				proDialog.dismiss();
			}
			if (!isNet) {
				Toast.makeText(Login.this, "网络连接不可用", Toast.LENGTH_SHORT)
						.show();
			}
			if (null == isLogin) {
				Toast.makeText(Login.this, "服务器连接失败", Toast.LENGTH_SHORT)
						.show();
			}
			if (isLogin.equals("1")) {
				Toast.makeText(Login.this, "用户名错误", Toast.LENGTH_SHORT).show();
			} else if (isLogin.equals("2")) {
				Toast.makeText(Login.this, "密码错误", Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}
	};

	private void initView() {
		loginButton = (Button) findViewById(R.id.btn_login);
		userNameET = (EditText) findViewById(R.id.edit_loginName);
		
		passWordET = (EditText) findViewById(R.id.edit_loginPawd);
		userNameET.setText("xululu");
		passWordET.setText("gt123123");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
             super.onCreateOptionsMenu(menu);
             MenuInflater menuInflater = getMenuInflater();
             menuInflater.inflate(R.menu.menu, menu);
             return true;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (proDialog != null) {
			proDialog.dismiss();
		}
	}

}
