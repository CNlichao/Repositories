package com.hrcx.photovideocompare.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * �û�״̬��ط���������
 *
 */
public class UserState {
	/**
	 * �豸Ψһid
	 */
	private static String equipmentId;
	
	/**
	 * ��ȡ�豸Ψһid
	 * @param con Context
	 * @return �豸id
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
	 * ��ȡ�豸Ψһid
	 * @param con Context
	 * @return �豸Ψһid
	 */
	private static String getDeviceId(Context con){
		TelephonyManager tm = (TelephonyManager)con.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
}

