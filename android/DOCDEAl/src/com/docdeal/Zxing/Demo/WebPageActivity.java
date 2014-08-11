package com.docdeal.Zxing.Demo;

import com.docdeal.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebPageActivity extends Activity {
	private WebView webView;
	private Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webpage);
		intent = this.getIntent();
		// 获得布局中的控件
		webView = (WebView) findViewById(R.id.webView01);
		String strURL = intent.getStringExtra("url");
		strURL = strURL.trim();	
		webView.loadUrl(strURL);
		finish();
	}
}
