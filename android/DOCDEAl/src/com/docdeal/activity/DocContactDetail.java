package com.docdeal.activity;

import java.util.LinkedHashMap;

import com.docdeal.R;
import com.docdeal.adapterview.TitleBar;
import com.docdeal.bean.ContactUser;
import com.docdeal.parsexml.ContactUserDetailXml;
import com.docdeal.parsexml.ContactUserXml;
import com.docdeal.util.ActivityUtil;
import com.docdeal.webservice.WebService;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class DocContactDetail extends Activity {
	private TextView phone;
	private TextView phoneForWork;
	private TextView name;
	private TextView job;
	private TextView fax;
	private TextView mail;
	private Button bt1;
	private Button bt2;
	private PopupWindow popupWindowForContact;
	private View view;
	private LayoutInflater inflater;
	private RadioGroup lv_group;
	private Intent intent;

	private String type;
	private LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();

	private ContactUser cu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		type = intent.getStringExtra("contactType");
		if (type.equals("0")) {
			TitleBar.title = "单位通讯录";
		} else if (type.equals("1")) {
			TitleBar.title = "公共通讯录";
		} else if (type.equals("2")) {
			TitleBar.title = "个人通讯录";
		}
		ActivityUtil.getInstance()
				.initActivity(this, R.layout.doccontactdetail);
		inflater = this.getLayoutInflater();
		cu = new ContactUser(intent.getStringExtra("userId"),
				intent.getStringExtra("phone"), intent.getStringExtra("name"));

		String s = getWebServiceData();
		Log.d("xml", s);
		ContactUserDetailXml.xmlToObj(s, cu);

		phone = (TextView) findViewById(R.id.DoccontactDetailPhone);
		phoneForWork = (TextView) findViewById(R.id.DoccontactDetailPhoneForWork);
		name = (TextView) findViewById(R.id.DoccontactDetailName);
		job = (TextView) findViewById(R.id.DoccontactDetailJob);
		fax = (TextView) findViewById(R.id.DoccontactDetailFax);
		mail = (TextView) findViewById(R.id.DoccontactDetailMail);

		
		phone.setText(cu.getPhone());
		phoneForWork.setText(cu.getPhoneForWork());
		name.setText(cu.getName());
		job.setText(cu.getJob());
		fax.setText(cu.getFax());
		mail.setText(cu.getMail());

		bt1 = (Button) findViewById(R.id.DoccontactDetailBt1);
		bt2 = (Button) findViewById(R.id.DoccontactDetailBt2);
		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showWindowForContact(arg0);
			}
		});

		bt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String phone_number = phone.getText().toString();
				Uri smsToUri = Uri.parse("smsto:" + phone_number);
				Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
				startActivity(intent);
			}
		});
	}

	private void showWindowForContact(View parent) {
		int width = getWindowManager().getDefaultDisplay().getWidth();
		RadioButton tv1 = new RadioButton(this);
		RadioButton tv2 = new RadioButton(this);
		if (popupWindowForContact == null) {
			view = inflater.inflate(R.layout.group_list2, null);
			lv_group = (RadioGroup) view.findViewById(R.id.raGroup);

			// 加载数据

			tv1.setText("手机号码");
			tv1.setBackgroundResource(R.drawable.view_yuan_morelist);
			tv1.setWidth(width * 1 / 2);
			tv2.setText("办公电话");
			lv_group.addView(tv1);
			lv_group.addView(tv2);
			tv2.setBackgroundResource(R.drawable.view_yuan_morelist);
			tv2.setWidth(width * 1 / 2);
			// 创建一个PopuWidow对象
			popupWindowForContact = new PopupWindow(view, width * 1 / 2,
					LayoutParams.WRAP_CONTENT);
		}
		// 使其聚集
		popupWindowForContact.setFocusable(true);
		// 设置允许在外点击消失
		popupWindowForContact.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindowForContact.setBackgroundDrawable(new BitmapDrawable());
		popupWindowForContact.showAtLocation((View) parent, Gravity.CENTER, 0,
				0);
		tv1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dial(phone);
			}
		});

		tv2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dial(phoneForWork);
			}
		});
	}

	public void dial(TextView tv) {
		String phone_number = tv.getText().toString();

		phone_number = phone_number.trim();// 删除字符串首部和尾部的空格

		if (phone_number != null && !phone_number.equals("")) {

			// 调用系统的拨号服务实现电话拨打功能
			// 封装一个拨打电话的intent，并且将电话号码包装成一个Uri对象传入
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ phone_number));
			DocContactDetail.this.startActivity(intent);// 内部类
		}
	}

	private String getWebServiceData() {
		parameters.put("userId", cu.getId());
		parameters.put("bookType", type);

		WebService wb = new WebService("getInfoByUserId", parameters,
				"getInfoByUserIdReturn");
		String result = wb.getData();
		return result;
	}
}