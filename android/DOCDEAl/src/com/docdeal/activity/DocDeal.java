package com.docdeal.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.global.Const;
import com.global.MathUtil;
import com.global.StringUtil;
import com.docdeal.R;
import com.docdeal.adapter.DealPeopleAdapter;
import com.docdeal.adapterview.TitleBar;
import com.docdeal.bean.Action;
import com.docdeal.bean.Flow;
import com.docdeal.bean.User;
import com.docdeal.util.ActivityUtil;
import com.docdeal.webservice.WebService;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class DocDeal extends Activity implements OnClickListener {
	private String methodName = "execDealGW";
	private Spinner stepSpinner;
	private ListView vChooseList, vChoosedList;
	private DealPeopleAdapter chooseAdapter, choosedAdapter;
	private Button btn_toLeft, btn_toRight, btn_submit, btn_back;
	private List<User> dealPeopleList = new LinkedList<User>();
	private List<User> dealdPeopleList = new LinkedList<User>();
	private Handler handler = new Handler();
	private List<String> selectList = new ArrayList<String>();// 下拉列表
	// private DocumentDetail docDetail;
	private Flow flow;
	private Document dealXml;
	private LinearLayout showPeopleList,showPeopleTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TitleBar.title="公文处理";
		ActivityUtil.getInstance().initActivity(this,R.layout.doc_deal);
		initView();
		initData();
		if(selectList.contains("处理返回")){
			showPeopleList.setVisibility(View.INVISIBLE);
			showPeopleTitle.setVisibility(View.INVISIBLE);
		}

	}

	private void initView() {
		showPeopleList=(LinearLayout) findViewById(R.id.showPeopleList);
		showPeopleTitle=(LinearLayout) findViewById(R.id.showPeopleTitle);
		vChooseList = (ListView) findViewById(R.id.people_choose);
		vChoosedList = (ListView) findViewById(R.id.people_choosed);
		vChooseList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);// 设置为多选
		vChoosedList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		chooseAdapter = new DealPeopleAdapter(this, LayoutInflater.from(this),
				dealPeopleList);
		choosedAdapter = new DealPeopleAdapter(this, LayoutInflater.from(this),
				dealdPeopleList);
		vChooseList.setAdapter(chooseAdapter);
		vChooseList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterview, View v,
					int position, long id) {
				if (Const.actionSerial) {
					chooseAdapter.clearChoosed();
					// chooseAdapter.clear();
				}
				chooseAdapter.modifyStates(position);// 根据点击的位置改变该行的选择状态
				chooseAdapter.getView(position, v, vChooseList);
				chooseAdapter.notifyDataSetChanged();
			}
		});
		vChoosedList.setAdapter(choosedAdapter);
		vChoosedList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterview, View v,
					int position, long id) {

				choosedAdapter.modifyStates(position);// 根据点击的位置改变该行的选择状态
				choosedAdapter.getView(position, v, vChoosedList);
				choosedAdapter.notifyDataSetChanged();

			}
		});
		btn_toRight = (Button) findViewById(R.id.toright);
		btn_toRight.setOnClickListener(toRight);
		btn_toLeft = (Button) findViewById(R.id.toleft);
		btn_toLeft.setOnClickListener(toLeft);
		stepSpinner = (Spinner) findViewById(R.id.dealstep);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		// ArrayAdapter<CharSequence> stepAdapter = ArrayAdapter
		// .createFromResource(this, R.array.dealstep_array,
		// android.R.layout.simple_spinner_item);
		// stepAdapter
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// stepSpinner.setAdapter(stepAdapter);

	}

	private OnClickListener toRight = new OnClickListener() {

		@Override
		public void onClick(View v) {
			List<Integer> chooseList = chooseAdapter.getChooseList();
			if (chooseList.size() > 0) {
				Integer[] indexes = new Integer[chooseList.size()];
				indexes = MathUtil.orderDesc(chooseList.toArray(indexes));
				for (int i = 0; i < indexes.length; i++) {
					if (Const.actionSerial) {
						for (int j = 0; j < choosedAdapter.getCount(); j++) {
							chooseAdapter.addItem((User) choosedAdapter
									.getItem(j));
							choosedAdapter.removeItem(j);
						}
						choosedAdapter.notifyDataSetChanged();
						chooseAdapter.notifyDataSetChanged();
						choosedAdapter.clearChoosed();
					}
					choosedAdapter.addItem((User) chooseAdapter
							.getItem(indexes[i].intValue()));
					chooseAdapter.removeItem(indexes[i].intValue());
				}
				choosedAdapter.notifyDataSetChanged();
				chooseAdapter.notifyDataSetChanged();

			}
		}
	};

	private OnClickListener toLeft = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			List<Integer> chooseList = choosedAdapter.getChooseList();
			if (chooseList.size() > 0) {
				Integer[] indexes = new Integer[chooseList.size()];
				indexes = MathUtil.orderDesc(chooseList.toArray(indexes));
				for (int i = 0; i < indexes.length; i++) {
					chooseAdapter.addItem((User) choosedAdapter
							.getItem(indexes[i].intValue()));
					choosedAdapter.removeItem(indexes[i].intValue());
				}
				choosedAdapter.notifyDataSetChanged();
				chooseAdapter.notifyDataSetChanged();

			}
		}

	};

	private void initData() {
		flow = (Flow) this.getIntent().getSerializableExtra("flow");
		String dealDoc = this.getIntent().getStringExtra("dealXml");
		// Document dealDoc = null;
		try {
			dealXml = DocumentHelper.parseText(dealDoc);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dealWebserviceData();
		// Thread thread = new Thread(new DataThread());
		// thread.start();
	}

	private void dealWebserviceData() {
		// flow = docDetail.getFlow();
		List<Action> actionList = flow.getList();
		for (int i = 0; i < actionList.size(); i++) {
			selectList.add(actionList.get(i).getName());
		}
		ArrayAdapter<String> stepAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, selectList);
		stepAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stepSpinner.setAdapter(stepAdapter);
		stepSpinner
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						choosedAdapter.clear();
						chooseAdapter.clear();
						Action ac = flow.getList().get(position);
						if (!StringUtil.isEmpty(ac.getActiontype())
								&& ac.getActiontype().equals("0")) {
							Const.actionSerial = true;

						} else {
							Const.actionSerial = false;
						}

						List<User> userList = ac.getUserList();
						chooseAdapter.addAllItem(userList);
						chooseAdapter.notifyDataSetChanged();
						choosedAdapter.notifyDataSetChanged();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

	}

	private class DataThread implements Runnable {

		@Override
		public void run() {
			String result = getData();
			dealResult(result);
			handler.post(new Runnable() {

				@Override
				public void run() {
					chooseAdapter.notifyDataSetChanged();

				}
			});
		}
	}

	private String getData() {
		String url = Const.url + "";
		String result = "";
		try {
			HttpGet request = new HttpGet(url);
			HttpResponse response = new DefaultHttpClient().execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "[{\"id\":\"110101194703203396\",\"name\":\"吉广\"},{\"id\":\"110101194703123564\",\"name\":\"桑晕怂\"},{\"id\":\"110101194703310511\",\"name\":\"归劳\"},{\"id\":\"110101194109121179\",\"name\":\"戚伏\"},{\"id\":\"110101194109120096\",\"name\":\"利敦\"},{\"id\":\"110101194703203396\",\"name\":\"吉广\"},{\"id\":\"110101194703123564\",\"name\":\"桑晕怂\"},{\"id\":\"110101194703310511\",\"name\":\"归劳\"},{\"id\":\"110101194109121179\",\"name\":\"戚伏\"},{\"id\":\"110101194109120096\",\"name\":\"利敦\"},{\"id\":\"110101194703203396\",\"name\":\"吉广\"},{\"id\":\"110101194703123564\",\"name\":\"桑晕怂\"},{\"id\":\"110101194703310511\",\"name\":\"归劳\"},{\"id\":\"110101194109121179\",\"name\":\"戚伏\"},{\"id\":\"110101194109120096\",\"name\":\"利敦\"}]";
	}

	private void dealResult(String result) {
		try {
			JSONArray ja = new JSONArray(result);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jtemp = ja.optJSONObject(i);
				User dealPeople = new User();
				dealPeople.setId(jtemp.getString("id"));
				dealPeople.setName(jtemp.getString("name"));
				chooseAdapter.addItem(dealPeople);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_submit:
			new AlertDialog.Builder(this)
			.setTitle("确定提交？")
			.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							creatDealXml();
							setResult(Const.DEALCODE);
							finish();
						}
					}).setNegativeButton("取消", null).show();
			break;
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}

	}

	private void creatDealXml() {

		Element gw = (Element) dealXml.selectSingleNode("/root/公文");
		Element dealflow = DocumentHelper.createElement("流程相关");
		gw.add(dealflow);
		Element action = DocumentHelper.createElement("Action");
		dealflow.add(action);
		int location = stepSpinner.getSelectedItemPosition();
		Action ac = flow.getList().get(location);
		action.addAttribute("actionid", ac.getActionid());
		action.addAttribute("actiontype", ac.getActiontype());
		action.addAttribute("type", ac.getType());
		action.addAttribute("totaskid", ac.getTotaskid());
		for (User user : dealdPeopleList) {
			Element u = DocumentHelper.createElement("User");
			u.addAttribute("id", user.getId());
			action.add(u);
		}

		Thread t = new Thread(new SubmitDealResult());
		t.start();
	}

	private class SubmitDealResult implements Runnable {
		public void run() {
			LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
			parameters.put("dealgwxml", dealXml.asXML());
			WebService wb = new WebService(methodName, parameters);
			wb.getData();
		}
	}

}
