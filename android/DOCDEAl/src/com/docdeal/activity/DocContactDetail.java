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
			TitleBar.title = "��λͨѶ¼";
		} else if (type.equals("1")) {
			TitleBar.title = "����ͨѶ¼";
		} else if (type.equals("2")) {
			TitleBar.title = "����ͨѶ¼";
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

			// ��������

			tv1.setText("�ֻ�����");
			tv1.setBackgroundResource(R.drawable.view_yuan_morelist);
			tv1.setWidth(width * 1 / 2);
			tv2.setText("�칫�绰");
			lv_group.addView(tv1);
			lv_group.addView(tv2);
			tv2.setBackgroundResource(R.drawable.view_yuan_morelist);
			tv2.setWidth(width * 1 / 2);
			// ����һ��PopuWidow����
			popupWindowForContact = new PopupWindow(view, width * 1 / 2,
					LayoutParams.WRAP_CONTENT);
		}
		// ʹ��ۼ�
		popupWindowForContact.setFocusable(true);
		// ����������������ʧ
		popupWindowForContact.setOutsideTouchable(true);
		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı���
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

		phone_number = phone_number.trim();// ɾ���ַ����ײ���β���Ŀո�

		if (phone_number != null && !phone_number.equals("")) {

			// ����ϵͳ�Ĳ��ŷ���ʵ�ֵ绰������
			// ��װһ������绰��intent�����ҽ��绰�����װ��һ��Uri������
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ phone_number));
			DocContactDetail.this.startActivity(intent);// �ڲ���
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