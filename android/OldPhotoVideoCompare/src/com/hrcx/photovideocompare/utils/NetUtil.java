package com.hrcx.photovideocompare.utils;

import com.hrcx.photovideocompare.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class NetUtil {
	public static boolean checkNet(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) { 
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) { 
						return true;
					}
				}
			}
		} catch (Exception e) {
			DTLogger.NetUtilLog(e.toString());
		}
		return false;
	}
	
	public static void showToastNoNet(Context context) {
		Toast.makeText(context, context.getResources().getString(R.string.toast_network_error),
				Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * wifi 自动打开
	 * @param con
	 */
	public static boolean wifiAuto(Context con) {
		WifiManager wifi = (WifiManager) con
				.getSystemService(Context.WIFI_SERVICE);
		if (wifi.isWifiEnabled()) {
			return true;
		} else {
			return false;
		}
	}
}

