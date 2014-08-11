package com.docdeal.activity;

import com.docdeal.R;
import com.docdeal.adapterview.TitleBar;
import com.docdeal.util.ActivityUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DocText extends Activity{
	private TextView textV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TitleBar.title="¹«ÎÄÄÚÈÝ";
		ActivityUtil.getInstance().initActivity(this,R.layout.doctext);
		
        textV=(TextView) findViewById(R.id.doctext);
		String s = this.getIntent().getStringExtra("text");
		textV.setText(s);
	}

}
