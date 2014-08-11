package com.hrcx.photovideocompare.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;

/**
 * @author Administrator
 *
 */
public class BasicActivity extends Activity {

	ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//StrictMode Controller
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.permitAll()
		.penaltyLog()
		.build());
		
		pd = new ProgressDialog(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}

