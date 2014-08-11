package com.hrcx.selfphotovideocompare.ui;

import com.hrcx.selfphotovideocompare.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class RecommendedActivity extends Activity {
	private ImageButton nextStep;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend);
		nextStep = (ImageButton) findViewById(R.id.nextStep);
		nextStep.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RecommendedActivity.this,
						CameraRecordActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
