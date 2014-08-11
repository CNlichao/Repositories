package com.hrcx.selfphotovideocompare.utils;

import java.util.ArrayList;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;
import android.os.Process;

import com.baidu.mapapi.*;

public class BMapApiDemoApp extends Application {
	
	static BMapApiDemoApp mDemoApp;
	
	//百度MapAPI的管理类
	BMapManager mBMapMan = null;
	
	// 授权Key
	// TODO: 请输入您的Key,
	// 申请地址：http://dev.baidu.com/wiki/static/imap/key/
	String mStrKey = "7E8C8D31A48545316493CB548990724F388498D9";
	boolean m_bKeyRight = true;	// 授权Key正确，验证通过
	
	private static ArrayList<Activity> mActivityList = new ArrayList<Activity>();
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			Log.d("MyGeneralListener", "onGetNetworkState error is "+ iError);
			Toast.makeText(BMapApiDemoApp.mDemoApp.getApplicationContext(), "您的网络出错啦！",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onGetPermissionState(int iError) {
			Log.d("MyGeneralListener", "onGetPermissionState error is "+ iError);
			if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(BMapApiDemoApp.mDemoApp.getApplicationContext(), 
						"请在BMapApiDemoApp.java文件输入正确的授权Key！",
						Toast.LENGTH_LONG).show();
				BMapApiDemoApp.mDemoApp.m_bKeyRight = false;
			}
		}
	}

	@Override
    public void onCreate() {
		Log.v("BMapApiDemoApp", "onCreate");
		mDemoApp = this;
		mBMapMan = new BMapManager(this);
		mBMapMan.init(this.mStrKey, new MyGeneralListener());
		mBMapMan.getLocationManager().setNotifyInternal(10, 5);
//		if (mBMapMan != null) {
//			mBMapMan.destroy();
//			mBMapMan = null;
//		}
		
		super.onCreate();
	}

	@Override
	//建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}

	public static void addActivity(Activity activity) {
		mActivityList.add(activity);
	}

	public static void removeActivity(Activity activity) {
		mActivityList.remove(activity);
	}

	public static void exit() {
		for (Activity activity : mActivityList) {
			activity.finish();
		}

		Process.killProcess(Process.myPid());
	}

	public static ArrayList<Activity> getActivityList() {
		return mActivityList;
	}
}
