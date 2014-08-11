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
	// 标题栏关联的activity
	private Activity activity;
	// 设置按钮
	private Button settingButton;
	// 弹出菜单
	private PopupWindow popMenu;
	// 菜单项内容
	private List<Map<String, Object>> menuItemsContent;
	// 菜单项视图
	private ListView menuItemsView;
	//菜单名称
	public static String title="公文处理";

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
		// 设置标题栏左上角的textview
		text = (TextView) activity.findViewById(R.id.text);
		text.setText(title);
		text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
		
		// 设置标题栏右边的设置按钮
				settingButton = (Button) activity.findViewById(R.id.btn_setting);
				settingButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (popMenu.isShowing()) {
							// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
							popMenu.dismiss();
						} else {
							// 显示窗口
							popMenu.showAsDropDown(v);
						}
					}
				});
			
		// 设置弹出菜单
		View popWindowView = ((LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.titlemenu, null);
		popWindowView.setFocusableInTouchMode(true);
		// 设置弹出菜单的菜单选项
		menuItemsView = (ListView) popWindowView.findViewById(R.id.menuList);
		// 初始化菜单选项的名字
		initMenu();
		// 将菜单选项绑定到弹出菜单中
		SimpleAdapter adapter = new SimpleAdapter(activity,
				getMenuItemsContent(), R.layout.titlemenuitem,
				new String[] { "menuItem" }, new int[] { R.id.menuItem });
		menuItemsView.setAdapter(adapter);
		//设置菜单选项的点击事件
		menuItemsView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toast.makeText(DocumentsHandle.this, "nihao", 2000).show();
				switch (position) {
				case 0:
					Toast.makeText(activity, "扫一扫", Toast.LENGTH_SHORT).show();
					activity.startActivity(new Intent(activity,CaptureActivity.class));
					break;
				case 1:
					Toast.makeText(activity, "系统设置", Toast.LENGTH_SHORT).show();
					activity.startActivity(new Intent(activity,NotificationSettingsActivity.class));
					break;
				case 2:
					Toast.makeText(activity, "退出系统", Toast.LENGTH_SHORT).show();
					// 具体的操作代码
					new AlertDialog.Builder(activity)
							.setTitle("完全退出后将注销用户登陆")
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									})
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											// 用户注销后重置状态
											SharedPreferences preferences1 = activity
													.getSharedPreferences(
															"login", 0);
											SharedPreferences.Editor editor1 = preferences1
													.edit();
											editor1.putString("login", "");
											editor1.commit();
											// 结束推送服务
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
		//设置弹出菜单在activity中的样式和属性
		popMenu = new PopupWindow(popWindowView, (int) (getWindowWidth() * 0.3),
				LayoutParams.WRAP_CONTENT);
		popMenu.setTouchable(true);
		popMenu.setOutsideTouchable(true);
		popMenu.setFocusable(true);
		popMenu.setBackgroundDrawable(new BitmapDrawable());

		}

	// 初始化菜单项
	private void initMenu() {
		menuItemsContent = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menuItem", "扫一扫");
		// map.put("img", R.drawable.grid1);
		menuItemsContent.add(map);

		map = new HashMap<String, Object>();
		map.put("menuItem", "系统设置");
		// map.put("img", R.drawable.grid1);
		menuItemsContent.add(map);

		map = new HashMap<String, Object>();
		map.put("menuItem", "退出系统");
		// map.put("img", R.drawable.grid2);
		menuItemsContent.add(map);

	}

	private int getWindowWidth() {
		WindowManager wm = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		// 屏幕宽度
		float screenWidth = display.getWidth();
		return (int) screenWidth;
	}

	private int getWindowHeight() {
		WindowManager wm = (WindowManager) activity
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		// 屏幕高度
		float screenHeight = display.getHeight();
		return (int) screenHeight;
	}

}
