package com.docdeal.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.androidpn.client.ServiceManager;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.docdeal.R;
import com.docdeal.adapterview.TitleBar;
import com.docdeal.bean.CalendarApointment;
import com.docdeal.util.ActivityUtil;
import com.docdeal.util.SysApplication;
import com.docdeal.webservice.WebService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class MenuMain extends Activity {
	private String texts[] = null;
	private int images[] = null;
	Intent intent;
	public static ServiceManager serviceManager;
	private PopupWindow popupWindowForContact;
	private View view;
	private RadioGroup lv_group;
	private LayoutInflater inflater;
	private Bundle bundle = new Bundle();
	private ProgressDialog progressDialog;
	public static String username;
	private ContentResolver cr;

	public void onCreate(Bundle savedInstanceState) {
		cr =getContentResolver();
		SharedPreferences preferences2 = getSharedPreferences("login", 0);
		String s = preferences2.getString("login", "");
		username = s;
		super.onCreate(savedInstanceState);
		TitleBar.title = "���˵�";
		ActivityUtil.getInstance().initActivity(this, R.layout.menu_main);
		
		Drawable drawableLeft= getResources().getDrawable(R.drawable.toppic);
		drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
		ActivityUtil.getInstance().getTitleBar().getText().setCompoundDrawables(drawableLeft, null,null,null);
		
		intent = this.getIntent();
		inflater = this.getLayoutInflater();
		images = new int[] { R.drawable.menu1, R.drawable.menu2,
				R.drawable.menu3, R.drawable.menu6 };
		texts = new String[] { "���İ���", "���Ĳ�ѯ", "ͨѶ¼", "�ճ̹���" };

		GridView gridview = (GridView) findViewById(R.id.gridview);
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 4; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", images[i]);
			map.put("itemText", texts[i]);
			lstImageItem.add(map);
		}

		SimpleAdapter saImageItems = new SimpleAdapter(this, lstImageItem,// ����Դ
				R.layout.menu_item,// ��ʾ����
				new String[] { "itemImage", "itemText" }, new int[] {
						R.id.itemImage, R.id.itemText });
		gridview.setAdapter(saImageItems);
		gridview.setOnItemClickListener(new ItemClickListener());
		// �������ͷ���
		serviceManager = new ServiceManager(this);
		serviceManager.setNotificationIcon(R.drawable.menu6);
		serviceManager.startService();
	}

	class ItemClickListener implements OnItemClickListener {
		/**
		 * �����ʱ�����¼�
		 * 
		 * @param parent
		 *            �������������AdapterView
		 * @param view
		 *            ��AdapterView�б��������ͼ(������adapter�ṩ��һ����ͼ)��
		 * @param position
		 *            ��ͼ��adapter�е�λ�á�
		 * @param rowid
		 *            �����Ԫ�ص���id��
		 */
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long rowid) {
			HashMap<String, Object> item = (HashMap<String, Object>) parent
					.getItemAtPosition(position);
			// ��ȡ����Դ������ֵ
			String itemText = (String) item.get("itemText");
			Object object = item.get("itemImage");
			Toast.makeText(MenuMain.this, itemText, Toast.LENGTH_LONG).show();

			// ����ͼƬ������Ӧ����ת
			switch (images[position]) {
			case R.drawable.menu1:
				startActivityForResult(new Intent(MenuMain.this,
						DocumentList.class), 0);
				break;
			case R.drawable.menu2:
				startActivityForResult(new Intent(MenuMain.this,
						DocSearchCondition.class), 1);
				break;
			case R.drawable.menu3:
				showWindowForContact(view);
				break;
			case R.drawable.menu6:
				progressDialog = ProgressDialog.show(MenuMain.this, "���Ժ�",
						"���ݷ����С���", true, false);
		    		  new getCalendarTask().execute("lixiaotao");
				break;
			default:
				break;
			}

		}
	}

	/**
	 * ������ؼ�����ȷ������ ѡ���˳�
	 */
	/*
	 * @Override public boolean dispatchKeyEvent(KeyEvent event) { if
	 * (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() ==
	 * KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) { // ����Ĳ������� new
	 * AlertDialog.Builder(this) .setTitle("ȷ���˳�����ô") .setNegativeButton("ȡ��",
	 * new DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) { } })
	 * .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { public
	 * void onClick(DialogInterface dialog, int whichButton) { //�û�ע��������״̬
	 * SharedPreferences preferences1=getSharedPreferences("login", 0);
	 * SharedPreferences.Editor editor1=preferences1.edit();
	 * editor1.putString("login", ""); editor1.commit(); //�������ͷ���
	 * serviceManager.stopService(); finish(); //stopService(new
	 * Intent(MenuMain.this,MessageService.class)); System.exit(0); } }).show();
	 * return true; } return super.dispatchKeyEvent(event); }
	 */

	private void showWindowForContact(View parent) {
		int width = getWindowManager().getDefaultDisplay().getWidth();
		RadioButton tv1 = new RadioButton(this);
		RadioButton tv2 = new RadioButton(this);
		RadioButton tv3 = new RadioButton(this);
		if (popupWindowForContact == null) {
			view = inflater.inflate(R.layout.group_list2, null);
			lv_group = (RadioGroup) view.findViewById(R.id.raGroup);

			// ��������

			tv1.setText("����ͨѶ¼");
			tv1.setBackgroundResource(R.drawable.view_yuan_morelist);
			tv1.setWidth(width * 1 / 2);
			tv1.setGravity(Gravity.CENTER);

			tv2.setText("��λͨѶ¼");
			tv2.setBackgroundResource(R.drawable.view_yuan_morelist);
			tv2.setWidth(width * 1 / 2);
			tv2.setGravity(Gravity.CENTER);

			tv3.setText("����ͨѶ¼");
			tv3.setBackgroundResource(R.drawable.view_yuan_morelist);
			tv3.setWidth(width * 1 / 2);
			tv3.setGravity(Gravity.CENTER);

			lv_group.addView(tv1);
			lv_group.addView(tv2);
			lv_group.addView(tv3);

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
				bundle.putString("contactType", "2");
				startActivityForResult(new Intent(MenuMain.this,
						DocContactForPublic.class).putExtras(bundle), 2);

			}
		});
		tv2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				bundle.putString("contactType", "0");
				startActivityForResult(new Intent(MenuMain.this,
						DocContactForPublic.class).putExtras(bundle), 2);
			}
		});
		tv3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				bundle.putString("contactType", "1");
				startActivityForResult(new Intent(MenuMain.this,
						DocContactForPublic.class).putExtras(bundle), 2);
			}
		});
	}



		
	  private class getCalendarTask extends AsyncTask<String, Integer, Long> {
		  private ArrayList<String> CalendarIds;
		  @Override
		protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		CalendarIds =new ArrayList<String>();
		Cursor cur=new CursorLoader(MenuMain.this, Calendars.CONTENT_URI, new String[]{"_id","name"}, null, null, null).loadInBackground();
		if (cur.moveToFirst() == true)
		{
			do {
				int nameColumnIndex = cur.getColumnIndex(Calendars._ID);
				 CalendarIds.add(cur.getString(nameColumnIndex));
			} while (cur.moveToNext());
		}
		 //ɾ�����ճ�
		  
		  int deleteNum=cr.delete(Events.CONTENT_URI,"organizer=='DOCDEAL'", null);
		
		}

			protected Long doInBackground(String... param) {
				//�ӷ������˻�ȡxml����
		    	  String methodName = "getUserAppointment";
					LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
					parameters.put("userName", username);
					String returnProperty = "getUserAppointmentReturn";
		    	  WebService web = new WebService(methodName, parameters, returnProperty);
		  		String result = web.getData();
		  		List<CalendarApointment> apointmentList = null;
		  		//����xmlΪ�ճ̶���
		    	  try {
		    		  apointmentList=parseCalendarXml(result);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	  long eventID = 0;
		    	  //��������
		    	  long lastStartMillis=-1;
		    	  long lastEventId=-1;
		    	  //ɾ�����ճ�
	    		  //ContentResolver cr = getContentResolver();
	    		  //int deleteNum=cr.delete(Events.CONTENT_URI,"organizer=='DOCDEAL'", null);
		    	 
	    		  for (String CalendarId : CalendarIds) {
		    		 if(apointmentList!=null){
			    		  //�����ճ�д��������
			    		  Iterator iterator = apointmentList.iterator();
			    		  
			    		 for (int i=1; i<=apointmentList.size();i++) {
							CalendarApointment appointment = (CalendarApointment) iterator
									.next();
							ContentValues values = new ContentValues();
							
							//��ʼ����ʼ����
							String [] sdate=appointment.getStartDate().split("-");
							Calendar beginTime = Calendar.getInstance();
							beginTime.set(Integer.valueOf(sdate[0]), Integer.valueOf(sdate[1])-1, Integer.valueOf(sdate[2]), Integer.valueOf(sdate[3]), Integer.valueOf(sdate[4]));
							long startMillis = beginTime.getTimeInMillis();
							values.put(Events.DTSTART, startMillis);
							//����ʱ��
							Calendar endTime = Calendar.getInstance();
							endTime.set(Integer.valueOf(sdate[0]), Integer.valueOf(sdate[1])-1, Integer.valueOf(sdate[2]), Integer.valueOf(sdate[3]), Integer.valueOf(sdate[4])+Integer.valueOf(appointment.getDuration()));
							long endMillis = endTime.getTimeInMillis();
							values.put(Events.DTEND,endMillis);
							//����
							values.put(Events.TITLE, appointment.getTitle());
							//����
							values.put(Events.DESCRIPTION, appointment.getContent());
							//�ص�
							values.put(Events.EVENT_LOCATION, appointment.getPlace());
							
							values.put(Events.CALENDAR_ID, CalendarId);
							values.put(Events.ORGANIZER, "DOCDEAL");
							 values.put(Events.EVENT_TIMEZONE, "Asia/ShangHai");
							 Uri uri = cr.insert(Events.CONTENT_URI, values);
								eventID = Long.parseLong(uri.getLastPathSegment());
								//��ȡҪ��ʾ�Ŀ�ʼʱ����¼�ID
								if(lastStartMillis<startMillis){
									lastStartMillis=startMillis;
									lastEventId=Long.parseLong(uri.getLastPathSegment());
								}
								//��������ʱ��
								ContentValues values1 = new ContentValues();
								values1.put(Reminders.MINUTES, 30);
								values1.put(Reminders.EVENT_ID, eventID);
								values1.put(Reminders.METHOD, Reminders.METHOD_ALERT);
								cr.insert(Reminders.CONTENT_URI, values1);	
								//publishProgress(i*100/apointmentList.size());
						}
				} 	 
		      }
				return lastStartMillis;
			}
/*		      protected void onProgressUpdate(Integer... progress) {
		    	  progressDialog.setMessage( "���ݼ���"+progress[0]+"%");
		      }
		 */
		      protected void onPostExecute(Long result) {
		    	  progressDialog.dismiss();
		    	 //��ָ�����ڵ�����
		    	  long startMillis;
		    	  if(result==-1){
		    		  startMillis=Calendar.getInstance().getTimeInMillis();
		    	  }else{
		    		  startMillis=result;
		    	  }
		    	  
					Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
					builder.appendPath("time");
					ContentUris.appendId(builder, startMillis);
					Uri uri=builder.build();
					Intent intent = new Intent(Intent.ACTION_VIEW)
					    .setData(uri);
					try{
					startActivity(intent);
					}catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(MenuMain.this, "��ȥ�����в鿴", Toast.LENGTH_LONG)
						.show();
						new AlertDialog.Builder(MenuMain.this)
						.setTitle("��ȥ�����в鿴")
						.setNegativeButton("ȡ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialog,
											int which) {
									}
								})
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int whichButton) {
										finish();
									}
								}).show();
					}
					
					
			  //��ָ���ճ̰��� 
			 /*long eventID=result; 
			 Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID); Intent
			  intent = new Intent(Intent.ACTION_VIEW) .setData(uri);
			 startActivity(intent);*/
			 
					
					//�Զ����ճ̽���
					//startActivity(new Intent(MenuMain.this, CalendarManager.class).putExtras(bundle));
		      }
		  }

	public List<CalendarApointment> parseCalendarXml(String result) throws DocumentException {
		// TODO Auto-generated method stub
		
			Document document = DocumentHelper.parseText(result);

			Element root = document.getRootElement();
			List xmlList = root.elements("appointment");
			List<CalendarApointment> apointmentList = new ArrayList<CalendarApointment>();
			for (int i = 0; i < xmlList.size(); i++) {
				CalendarApointment apoint = new CalendarApointment();
				Element xmlApoint = (Element) xmlList.get(i);
				apoint.setTitle(xmlApoint.elementText("����"));
				apoint.setContent(xmlApoint.elementText("����"));
				apoint.setDuration(xmlApoint.elementText("ʱ��"));
				apoint.setStartDate(xmlApoint.elementText("��ʼʱ��"));
				apoint.setPlace(xmlApoint.elementText("�ص�"));
				apointmentList.add(apoint);
		} 
			return apointmentList;
	}

}
