package com.hrcx.photovideocompare.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 用户状态相关方法工具类
 *
 */
public class UserState {
	/**
	 * 设备唯一id
	 */
	private static String equipmentId;
	
	/**
	 * 获取设备唯一id
	 * @param con Context
	 * @return 设备id
	 */
	public static String getEquipmentId(Context con){
		if(equipmentId!=null)
			return equipmentId;
		else{
			equipmentId = getDeviceId(con);
			return equipmentId;
		}
	}

	/**
	 * 获取设备唯一id
	 * @param con Context
	 * @return 设备唯一id
	 */
	private static String getDeviceId(Context con){
		TelephonyManager tm = (TelephonyManager)con.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
}

