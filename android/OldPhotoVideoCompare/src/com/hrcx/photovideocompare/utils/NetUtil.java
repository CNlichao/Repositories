package com.hrcx.photovideocompare.utils;

import com.hrcx.photovideocompare.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class NetUtil {
	public static boolean checkNet(Context context) {
		// ��ȡ�ֻ��������ӹ�����󣨰�����wi-fi,net�����ӵĹ���
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// ��ȡ�������ӹ���Ķ���
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) { 
					// �жϵ�ǰ�����Ƿ��Ѿ�����
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
	 * wifi �Զ���
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

