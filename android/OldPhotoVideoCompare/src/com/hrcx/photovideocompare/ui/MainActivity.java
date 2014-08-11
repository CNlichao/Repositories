package com.hrcx.photovideocompare.ui;

import com.hrcx.photovideocompare.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent it = new Intent(this, LoginActivity.class);
		startActivity(it);
		finish();
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		finish();
		System.exit(0);
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
}
