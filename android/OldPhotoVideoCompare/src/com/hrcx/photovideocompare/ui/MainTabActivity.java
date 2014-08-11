package com.hrcx.photovideocompare.ui;

import com.hrcx.photovideocompare.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

public class MainTabActivity extends TabActivity {

	private TabHost mTabHost;
	private RadioGroup mainbtGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_tab);
		getWindow()
		.setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
		mTabHost = this.getTabHost();

		TabSpec tabTotalShow = mTabHost.newTabSpec("ȫ��");
		tabTotalShow.setIndicator("ȫ��");
		Intent totalShowIntent = new Intent(this,
				TotalShowActivityGroup.class);
		totalShowIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		tabTotalShow.setContent(totalShowIntent);

		TabSpec tabVerificationShow = mTabHost.newTabSpec("��֤");
		tabVerificationShow.setIndicator("��֤");
		Intent verificationShowIntent = new Intent(this,
				VerificationShowActivityGroup.class);
		verificationShowIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		tabVerificationShow.setContent(verificationShowIntent);

		TabSpec tabNoVerificationShow = mTabHost.newTabSpec("δ��֤");
		tabNoVerificationShow.setIndicator("δ��֤");
		Intent noVerificationShowIntent = new Intent(this, NoVerificationShowActivityGroup.class);
		noVerificationShowIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		tabNoVerificationShow.setContent(noVerificationShowIntent);

		mTabHost.addTab(tabTotalShow);
		mTabHost.addTab(tabVerificationShow);
		mTabHost.addTab(tabNoVerificationShow);
		initView();
	}

	/**
	 * ��ʼ��
	 */
	public void initView() {
		this.mainbtGroup = (RadioGroup) this.findViewById(R.id.main_radio);
		mTabHost.setCurrentTabByTag("δ��֤");
		// ����ײ���ť,�л�����
		mainbtGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.menu_tab_product:
					mTabHost.setCurrentTabByTag("ȫ��");
					break;
				case R.id.menu_tab_history:
					mTabHost.setCurrentTabByTag("��֤");
					break;
				case R.id.menu_tab_analyse:
					mTabHost.setCurrentTabByTag("δ��֤");
					break;
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*getMenuInflater().inflate(R.menu.activity_main, menu);*/
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

