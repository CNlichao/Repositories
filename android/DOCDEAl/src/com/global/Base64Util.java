package com.global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;

public class Base64Util {
	/**
	 * Base64�ֽ���ת��Ϊָ��tye�����ļ� �ļ���ʱ���������
	 * 
	 * @param base64Code
	 * @param �ļ�����
	 * @return �����ļ�·��
	 */
	public static String decoderBase64File(String base64Code, String type) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
			Log.i("TestFile", "SD card is not avaiable/writeable right now.");
			return "unavailable";
		}
		File file = new File("/sdcard/myDoc/");
		if (!file.exists()) {
			file.mkdirs();
		}
		String name = "tempfile." + type;
		String filePath = "/sdcard/myDoc/" + name;
		byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
		try {
			FileOutputStream out = new FileOutputStream(filePath);
			out.write(buffer);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}

	/**
	 * Base64�ֽ���ת��Ϊָ�������ļ�
	 * 
	 * @param base64Code
	 * @param �ļ���
	 * @return �����ļ�·��
	 */
	public static String decoderBase64FileWithFileName(String base64Code,
			String fileName) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
			Log.i("TestFile", "SD card is not avaiable/writeable right now.");
			return "unavailable";
		}
		File file = new File("/sdcard/myDoc/");
		if (!file.exists()) {
			file.mkdirs();
		}
		String filePath = "/sdcard/myDoc/" + fileName;
		byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
		try {
			FileOutputStream out = new FileOutputStream(filePath);
			out.write(buffer);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}
}
