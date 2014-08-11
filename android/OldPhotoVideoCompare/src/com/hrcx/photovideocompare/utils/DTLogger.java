package com.hrcx.photovideocompare.utils;

import android.util.Log;

public class DTLogger {
	
	private static boolean openlog = true;
	
	//相册导入
	public static void MediaImportLog(String desc){
		if(openlog)
			Log.e("MediaImportLog", desc);
	}
	
	//拍照
	public static void PictureLog(String desc){
		if(openlog)
			Log.e("PictureLog", desc);
	}
	
	//主页
	public static void MainActivityLog(String desc){
		if(openlog)
			Log.e("MainActivityLog", desc);
	}
	
	//记录列表
	public static void RecordsActivityLog(String desc){
		if(openlog)
			Log.e("RecordsActivityLog", desc);
	}
	
	//NetUtil
	public static void NetUtilLog(String desc){
		if(openlog)
			Log.e("NetUtilLog", desc);
	}
	
	//NetImpl
	public static void NetProLog(String desc){
		if(openlog)
			Log.e("NetImplLog", desc);
	}
	
	//DownQueue
	public static void DownQueueLog(String desc){
		if(openlog)
			Log.e("DownQueueLog", desc);
	}
	
	//UpdateAndDownloadDaoImpl
	public static void UpdateAndDownloadDaoImplLog(String desc){
		if(openlog)
			Log.e("UpdateAndDownloadDaoImplLog", desc);
	}
	
	//BBSQLiteOpenHelper
	public static void BBSQLiteOpenHelperLog(String desc){
		if(openlog)
			Log.e("BBSQLiteOpenHelperLog", desc);
	}
	
	//登陆
	public static void LoginLog(String desc){
		if(openlog)
			Log.e("LoginLog", desc);
	}
	
	//MediaExport
	public static void MediaExportLog(String desc){
		if(openlog)
			Log.e("MediaExportLog", desc);
	}
	
	//Regist
	public static void RegistLog(String desc){
		if(openlog)
			Log.e("RegistLog", desc);
	}
	
	//SocketHttpRequester
	public static void socketHttpRequesterLog(String desc){
		if(openlog)
			Log.e("SocketHttpRequesterLog", desc);
	}
	//reg telNumber 
	public static void telNumberRegexLog(String desc){
		if(openlog)
			Log.e("telNumberRegexLog", desc);
	}
	
	public static void d(String desc){
		if(openlog)
			Log.d("DreamTime::D", desc);
	}
	
	public static void e(String desc){
		if(openlog)
			Log.e("DreamTime::E", desc);
	}
}

