package com.docdeal.adapterview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidpn.client.NotificationService;
import org.androidpn.client.NotificationSettingsActivity;
import org.androidpn.client.ServiceManager;

import com.docdeal.R;
import com.docdeal.Zxing.Demo.CaptureActivity;
import com.docdeal.activity.Login;
import com.docdeal.activity.MenuMain;
import com.docdeal.activity.MessageService;
import com.docdeal.util.SysApplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TitleBar {
	private TextView text;
	// ������������activity
	private Activity activity;
	// ���ð�ť
	private Button settingButton;
	// �����˵�
	private PopupWindow popMenu;
	// �˵�������
	private List<Map<String, Object>> menuItemsContent;
	// �˵�����ͼ
	private ListView menuItemsView;
	//�˵�����
	public static String title="���Ĵ���";

	public TitleBar(Activity activity) {
		this.activity = activity;
		setTitleBar();
	}

	public TextView getText() {
		return text;
	}

	public void setText(TextView text) {
		this.text = text;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public PopupWindow getPopMenu() {
		return popMenu;
	}

	public void setPopMenu(PopupWindow popMenu) {
		this.popMenu = popMenu;
	}

	public Button getSettingButton() {
		return settingButton;
	}

	public void setSettingButton(Button settingButton) {
		this.settingButton = settingButton;
	}

	public List<Map<String, Object>> getMenuItemsContent() {
		return menuItemsContent;
	}

	public void setMenuItemsContent(List<Map<String, Object>> menuItemsContent) {
		this.menuItemsContent = menuItemsContent;
	}

	public ListView getMenuItemsView() {
		return menuItemsView;
	}

	public void setMenuItemsView(ListView menuItemsView) {
		this.menuItemsView = menuItemsView;
	}

	private void setTitleBar() {
		// ���ñ��������Ͻǵ�textview
		text = (TextView) activity.findViewById(R.id.text);
		text.setText(title);
		text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
		
		// ���ñ������ұߵ����ð�ť
				settingButton = (Button) activity.findViewById(R.id.btn_setting);
				settingButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (popMenu.isShowing()) {
							// ���ش��ڣ���������˵��������Сʱ������Ҫ�˷�ʽ����
							popMenu.dismiss();
						} else {
							// ��ʾ����
							popMenu.showAsDropDown(v);
						}
					}
				});
			
		// ���õ����˵�
		View popWindowView = ((LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.titlemenu, null);
		popWindowView.setFocusableInTouchMode(true);
		// ���õ����˵��Ĳ˵�ѡ��
		menuItemsView = (ListView) popWindowView.findViewById(R.id.menuList);
		// ��ʼ���˵�ѡ�������
		initMenu();
		// ���˵�ѡ��󶨵������˵���
		SimpleAdapter adapter = new SimpleAdapter(activity,
				getMenuItemsContent(), R.layout.titlemenuitem,
				new String[] { "menuItem" }, new int[] { R.id.menuItem });
		menuItemsView.setAdapter(adapter);
		//���ò˵�ѡ��ĵ���¼�
		menuItemsView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toast.makeText(DocumentsHandle.this, "nihao", 2000).show();
				switch (position) {
				case 0:
					Toast.makeText(activity, "ɨһɨ", Toast.LENGTH_SHORT).show();
					activity.startActivity(new Intent(activity,CaptureActivity.class));
					break;
				case 1:
					Toast.makeText(activity, "ϵͳ����", Toast.LENGTH_SHORT).show();
					activity.startActivity(new Intent(activity,NotificationSettingsActivity.class));
					break;
				case 2:
					Toast.makeText(activity, "�˳�ϵͳ", Toast.LENGTH_SHORT).show();
					// ����Ĳ�������
					new AlertDialog.Builder(activity)
							.setTitle("��ȫ�˳���ע���û���½")
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
											// �û�ע��������״̬
											SharedPreferences preferences1 = activity
													.getSharedPreferences(
															"login", 0);
											SharedPreferences.Editor editor1 = preferences1
													.edit();
											editor1.putString("login", "");
											editor1.commit();
											// �������ͷ���
											if (MenuMain.serviceManager != null)
												MenuMain.serviceManager
														.stopService();
											activity.stopService(new Intent(
													activity,
													MessageService.class));
											SysApplication.getInstance().exit();
											System.exit(0);
										}
									}).show();
					break;
				default:
					Toast.makeText(activity, String.valueOf(position),
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
		//���õ����˵���activity�е���ʽ������
		popMenu = new PopupWindow(popWindowView, (int) (getWindowWidth() * 0.3),
				LayoutParams.WRAP_CONTENT);
		popMenu.setTouchable(true);
		popMenu.setOutsideTouchable(true);
		popMenu.setFocusable(true);
		popMenu.setBackgroundDrawable(new BitmapDrawable());

		}

	// ��ʼ���˵���
	private void initMenu() {
		menuItemsContent = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menuItem", "ɨһɨ");
		// map.put("img", R.drawable.grid1);
		menuItemsContent.add(map);

		map = new HashMap<String, Object>();
		map.put("menuItem", "ϵͳ����");
		// map.put("img", R.drawable.grid1);
		menuItemsContent.add(map);

		map = new HashMap<String, Object>();
		map.put("menuItem", "�˳�ϵͳ");
		// map.put("img", R.drawable.grid2);
		menuItemsContent.add(map);

	}

	private int getWindowWidth() {
		WindowManager wm = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		// ��Ļ���
		float screenWidth = display.getWidth();
		return (int) screenWidth;
	}

	private int getWindowHeight() {
		WindowManager wm = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		// ��Ļ�߶�
		float screenHeight = display.getHeight();
		return (int) screenHeight;
	}

}
