package com.docdeal.activity;

import com.global.StringUtil;
import com.docdeal.R;
import com.docdeal.util.ActivityUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class DealOpinion extends Activity implements OnClickListener {
	private Spinner linkWordSpinner, signSpinner, honorSpinner,
			positionSpinner, daylyOpinionSpinner;
	private Button submitBtn, backBtn, addaylyOpinionBtn, addHonorBtn,
			addSignBtn, addPositionBtn, addLinkWordBtn;
	private EditText opinion;
	private Bundle bundle = new Bundle();
	private String backString = "";
	private int requestCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityUtil.getInstance().initActivity(this,R.layout.deal_opinion);
		initView();
	}

	private void initView() {
		opinion = (EditText) findViewById(R.id.opinion);
		String comeOpinion = this.getIntent().getStringExtra("opinion");
		if (StringUtil.isEmpty(comeOpinion)) {
			opinion.setText("");
		} else {
			opinion.setText(comeOpinion.substring(0,
					comeOpinion.lastIndexOf("  ")).replaceAll("\n", ""));
		}

		submitBtn = (Button) findViewById(R.id.btn_submit);
		submitBtn.setOnClickListener(this);
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);
		addaylyOpinionBtn = (Button) findViewById(R.id.addaylyopinion);
		addaylyOpinionBtn.setOnClickListener(this);
		addHonorBtn = (Button) findViewById(R.id.addhonor);
		addHonorBtn.setOnClickListener(this);
		addSignBtn = (Button) findViewById(R.id.addsign);
		addSignBtn.setOnClickListener(this);
		addPositionBtn = (Button) findViewById(R.id.addposition);
		addPositionBtn.setOnClickListener(this);
		addLinkWordBtn = (Button) findViewById(R.id.addlinkword);
		addLinkWordBtn.setOnClickListener(this);
		requestCode = this.getIntent().getIntExtra("requestCode", -1);

		linkWordSpinner = (Spinner) findViewById(R.id.linkword);
		ArrayAdapter<CharSequence> linkWordAdapter = ArrayAdapter
				.createFromResource(this, R.array.linkword_array,
						android.R.layout.simple_spinner_item);
		linkWordAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		linkWordSpinner.setAdapter(linkWordAdapter);
		signSpinner = (Spinner) findViewById(R.id.sign);
		ArrayAdapter<CharSequence> signAdapter = ArrayAdapter
				.createFromResource(this, R.array.sign_array,
						android.R.layout.simple_spinner_item);
		signAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		signSpinner.setAdapter(signAdapter);
		honorSpinner = (Spinner) findViewById(R.id.honor);
		ArrayAdapter<CharSequence> honorAdapter = ArrayAdapter
				.createFromResource(this, R.array.honor_array,
						android.R.layout.simple_spinner_item);
		honorAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		honorSpinner.setAdapter(honorAdapter);
		positionSpinner = (Spinner) findViewById(R.id.position);
		ArrayAdapter<CharSequence> positionAdapter = ArrayAdapter
				.createFromResource(this, R.array.position_array,
						android.R.layout.simple_spinner_item);
		positionAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		positionSpinner.setAdapter(positionAdapter);
		daylyOpinionSpinner = (Spinner) findViewById(R.id.daylyopinion);
		ArrayAdapter<CharSequence> opinionAdapter = ArrayAdapter
				.createFromResource(this, R.array.opinion_array,
						android.R.layout.simple_spinner_item);
		opinionAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		daylyOpinionSpinner.setAdapter(opinionAdapter);
	}

	@Override
	public void onClick(View v) {
		String temp = "";
		switch (v.getId()) {
		case R.id.btn_submit:
			Intent intent = new Intent(this, RecieveDocDetail.class);
			backString = opinion.getText().toString();
			bundle.putString("opinion", "\n" + backString);
			intent.putExtras(bundle);
			setResult(requestCode, intent);
			finish();
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.addaylyopinion:
			temp = daylyOpinionSpinner.getSelectedItem().toString();
			opinion.setText(opinion.getText().toString() + temp);
			break;
		case R.id.addhonor:
			temp = honorSpinner.getSelectedItem().toString();
			opinion.setText(opinion.getText().toString() + temp);
			break;
		case R.id.addlinkword:
			temp = linkWordSpinner.getSelectedItem().toString();
			opinion.setText(opinion.getText().toString() + temp);
			break;
		case R.id.addposition:
			temp = positionSpinner.getSelectedItem().toString();
			opinion.setText(opinion.getText().toString() + temp);
			break;
		case R.id.addsign:
			temp = signSpinner.getSelectedItem().toString();
			opinion.setText(opinion.getText().toString() + temp);
			break;
		default:
			break;
		}
	}
}
