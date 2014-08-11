package com.docdeal.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.docdeal.R;
import com.docdeal.adapterview.TitleBar;
import com.docdeal.bean.Condition;
import com.docdeal.bean.WorkFlow;
import com.docdeal.bean.WorkFlows;
import com.docdeal.parsexml.SearchConditon;
import com.docdeal.util.ActivityUtil;
import com.docdeal.webservice.WebService;
import com.global.MapUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class DocSearchCondition extends Activity {
	private String methodName = "getSearchCondition";
	private String returnProperty = "getSearchConditionReturn";
	private String USERSIGN = "";
	private LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
	private LinearLayout commonLine, specialLine;
	private Button submit;
	private WorkFlows workFlows;
	private int scrWidth;
	private Spinner spinner;
	private List<Condition> commonList = new ArrayList<Condition>();
	private List<WorkFlow> flowList = new ArrayList<WorkFlow>();
	private List<Condition> specialList = new ArrayList<Condition>();
	private Map<String, String> commonMap = new HashMap<String, String>();
	private Map<String, String> specialMap = new HashMap<String, String>();
	private Document searchXml;
	private Handler handle = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TitleBar.title="公文查询";
		ActivityUtil.getInstance().initActivity(this,R.layout.doc_search_conditon);

		WindowManager windowManager = this.getWindowManager();
		scrWidth = windowManager.getDefaultDisplay().getWidth();
		initView();
		initData();
	}

	private void initData() {
		Thread t = new Thread(new WebDataThread());
		t.run();

	}

	private class WebDataThread implements Runnable {

		@Override
		public void run() {
			String result = getWebServiceData();
			dealReult(result);
			// System.out.println(result);
		}

		private void dealReult(String result) {
			SearchConditon se = new SearchConditon();
			workFlows = se.paserXml(result);
			commonList = workFlows.getListConditions();
			flowList = workFlows.getListWorkFlows();
			handle.post(new Runnable() {

				@Override
				public void run() {
					createCommonView();
					bandingViewData();
				}

			});

		}
	}

	/**
	 * 绑定试图数据
	 */
	private void bandingViewData() {
		String[] workArray = new String[flowList.size()];
		for (int i = 0; i < flowList.size(); i++) {
			workArray[i] = flowList.get(i).getName();
		}
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, workArray);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				specialList = flowList.get(position).getListConditions();
				specialLine.removeAllViews();
				MapUtil.removeAll(specialMap);
				createSpecialView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	private String getWebServiceData() {
		parameters.put("userSign", USERSIGN);
		WebService wb = new WebService(methodName, parameters, returnProperty);
		String result = wb.getData();
		return result;
	}

	private void initView() {
		commonLine = (LinearLayout) findViewById(R.id.common_condition);
		specialLine = (LinearLayout) findViewById(R.id.special_condition);
		spinner = (Spinner) findViewById(R.id.spinner);
		submit = (Button) findViewById(R.id.btn_submit);
		submit.setOnClickListener(submitAction);
		SharedPreferences preferences2=getSharedPreferences("login", 0);
		String s=preferences2.getString("login", "");
		USERSIGN = s;

	}

	private void createCommonView() {

		for (final Condition cd : commonList) {
			LinearLayout line = new LinearLayout(this);
			line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			commonLine.addView(line);
			TextView tvTemp = new TextView(this);
			tvTemp.setLayoutParams(new LayoutParams(scrWidth / 3,
					LayoutParams.MATCH_PARENT));
			tvTemp.setText(cd.getText());
			tvTemp.setGravity(Gravity.CENTER);
			tvTemp.setBackgroundResource(R.drawable.view_yuan_morelist);
			line.addView(tvTemp);
			if (cd.getType().equals("text")) {

				final EditText et = new EditText(this);
				et.setLayoutParams(new LayoutParams(scrWidth * 2 / 3,
						LayoutParams.WRAP_CONTENT));
				et.setBackgroundResource(R.drawable.tablestyle);
				et.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						// cd.getFieldname();
						// et.getText();
						commonMap.put(cd.getFieldname(), et.getText()
								.toString());

					}
				});

				line.addView(et);
			} else if (cd.getType().equals("date")) {
				final EditText et = new EditText(this);
				et.setLayoutParams(new LayoutParams(scrWidth * 2 / 3,
						LayoutParams.WRAP_CONTENT));
				et.setBackgroundResource(R.drawable.tablestyle);
				final Calendar calendar = Calendar.getInstance();
				Date date = new Date();
				calendar.setTime(date);
				et.setFocusable(false);
				et.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						new DatePickerDialog(DocSearchCondition.this,
								new OnDateSetListener() {
									public void onDateSet(DatePicker view,
											int year, int monthOfYear,
											int dayOfMonth) {
										et.setText(year
												+ "-"
												+ ((monthOfYear + 1) > 10 ? (monthOfYear + 1)
														: ("0" + (monthOfYear + 1)))
												+ "-"
												+ (dayOfMonth > 10 ? dayOfMonth
														: ("0" + dayOfMonth)));
										commonMap.put(cd.getFieldname(), et
												.getText().toString());
									}
								}, calendar.get(Calendar.YEAR), calendar
										.get(Calendar.MONTH), calendar
										.get(Calendar.DAY_OF_MONTH)).show();
					}
				});

				line.addView(et);
			}
		}
	}

	private void createSpecialView() {
		for (final Condition cd : specialList) {
			LinearLayout line = new LinearLayout(this);
			line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			specialLine.addView(line);
			TextView tvTemp = new TextView(this);
			tvTemp.setLayoutParams(new LayoutParams(scrWidth / 3,
					LayoutParams.MATCH_PARENT));
			tvTemp.setBackgroundResource(R.drawable.view_yuan_morelist);
			tvTemp.setGravity(Gravity.CENTER);
			tvTemp.setText(cd.getText());
			line.addView(tvTemp);
			if (cd.getType().equals("text")) {
				final EditText et = new EditText(this);
				et.setLayoutParams(new LayoutParams(scrWidth * 2 / 3,
						LayoutParams.WRAP_CONTENT));
				et.setBackgroundResource(R.drawable.tablestyle);
				et.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						specialMap.put(cd.getFieldname(), et.getText()
								.toString());
					}
				});

				line.addView(et);
			} else if (cd.getType().equals("date")) {
				final EditText et = new EditText(this);
				et.setLayoutParams(new LayoutParams(scrWidth * 2 / 3,
						LayoutParams.WRAP_CONTENT));
				et.setBackgroundResource(R.drawable.tablestyle);
				final Calendar calendar = Calendar.getInstance();
				Date date = new Date();
				calendar.setTime(date);
				et.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						new DatePickerDialog(DocSearchCondition.this,
								new OnDateSetListener() {
									public void onDateSet(DatePicker view,
											int year, int monthOfYear,
											int dayOfMonth) {

										et.setText(year
												+ "-"
												+ ((monthOfYear + 1) > 10 ? (monthOfYear + 1)
														: ("0" + (monthOfYear + 1)))
												+ "-"
												+ (dayOfMonth > 10 ? dayOfMonth
														: ("0" + dayOfMonth)));
										specialMap.put(cd.getFieldname(), et
												.getText().toString());
									}
								}, calendar.get(Calendar.YEAR), calendar
										.get(Calendar.MONTH), calendar
										.get(Calendar.DAY_OF_MONTH)).show();
					}
				});

				line.addView(et);

			}
		}
	}

	private OnClickListener submitAction = new OnClickListener() {

		@Override
		public void onClick(View v) {
			getSearchXml();
			Intent intent = new Intent(DocSearchCondition.this,
					SearchDocList.class);
			intent.putExtra("searchXml", searchXml.asXML());
			intent.putExtra("USERSIGN", USERSIGN);
			startActivityForResult(intent, v.getId());
		}
	};

	/**
	 * 获取查询提交的Xml
	 */
	private void getSearchXml() {
		Element root = DocumentHelper.createElement("root");
		searchXml = DocumentHelper.createDocument(root);
		searchXml.setXMLEncoding("GB2312");
		Element usersign = DocumentHelper.createElement("usersign");
		Element workflows = DocumentHelper.createElement("workflows");
		root.add(usersign);
		usersign.add(DocumentHelper.createText(USERSIGN));// 用户信息
		root.add(workflows);
		createConditionXml(workflows, commonList, commonMap);// 通用查询条件
		int position = spinner.getSelectedItemPosition();
		WorkFlow wf = flowList.get(position);
		Element wfNode = DocumentHelper.createElement("workflow");
		wfNode.addAttribute("id", wf.getId());
		workflows.add(wfNode);
		createConditionXml(wfNode, specialList, specialMap);// 特殊查询条件
		// String ssss = searchXml.asXML();

	}

	private void createConditionXml(Element root, List<Condition> list,
			Map<String, String> map) {
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Condition cd = this.findCondition(list, key);
			if (cd != null) {
				Element el = DocumentHelper.createElement("condition");
				el.addAttribute("fieldname", cd.getFieldname());
				el.addAttribute("type", cd.getType());
				el.add(DocumentHelper.createText(map.get(key)));
				root.add(el);
			}
		}
	}

	private Condition findCondition(List<Condition> list, String key) {
		for (int i = 0; i < list.size(); i++) {
			Condition cd = list.get(i);
			if (cd.getFieldname().equals(key)) {
				return cd;
			}
		}
		return null;
	}
}
