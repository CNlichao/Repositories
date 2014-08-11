package com.hrcx.photovideocompare.dao.net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hrcx.photovideocompare.utils.Constant;
import com.hrcx.photovideocompare.utils.DTLogger;
import com.hrcx.photovideocompare.utils.FileUtils;

import android.content.Context;
import android.content.Intent;

/**
 * 协议基类
 * 
 * @author Nestor
 * 
 */
public class BaseProtocol {

	protected static Context mContext;

	public BaseProtocol(Context con) {
		if (mContext == null)
			mContext = con;
	}

	/**
	 * 发送请求的方法
	 * 
	 * @param path
	 * @param params
	 * @return
	 */
	protected JSONObject parseJson(String path, HashMap<String, String> params) {
		try {
			// 获得相应数据
			InputStream inputStream = HttpReq.sendPOSTRequest(path, params);
			if (inputStream != null) {
				StringBuilder jsonBuilder = new StringBuilder();
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				for (String json = br.readLine(); json != null; json = br.readLine()) {
					jsonBuilder.append(json);
				}
				DTLogger.NetProLog(jsonBuilder.toString());
				JSONObject jObject = new JSONObject(jsonBuilder.toString());
				DTLogger.NetProLog("new JO:" + jObject.toString());
				return jObject;
			} else {
				DTLogger.NetProLog("inputStream==null:");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	protected JSONObject parserJson(InputStream inputStream) {
		try {
			if (inputStream != null) {
				StringBuilder jsonBuilder = new StringBuilder();
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				for (String json = br.readLine(); json != null; json = br.readLine()) {
					jsonBuilder.append(json);
				}
				DTLogger.NetProLog(jsonBuilder.toString());
				JSONObject jObject = new JSONObject(jsonBuilder.toString());
				DTLogger.NetProLog("back Js:" + jObject.toString());
				return jObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * 从输入流中获取数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readInputSream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
//		inStream.close();
		return outStream.toByteArray();
	}

	protected void toast(Object msg) {
		Intent i = new Intent();
		i.setAction(Constant.RECEVIER_TOAST);
		i.putExtra("msg", String.valueOf(msg));
		mContext.sendBroadcast(i);
	}
	
	public void getTemplateElements(JSONArray temArray) {
		for (int i = 0; !temArray.isNull(i); i++) {
			try {
				File temFile = null;
				JSONObject element = temArray.getJSONObject(i);
				String fileName = element.getString("fn");
				String path = Constant.SERVER_PATH_ROOT + element.getString("p") + fileName;
				int type = element.getInt("t");
				InputStream in = HttpReq.sendGETRequest(path, element.getString("u"), element.getString("s"));
				if (in != null) {
					if (type == 1)
						temFile = new File(FileUtils.TEMPLATEPATH, fileName);
					else if (type == 2)
						temFile = new File(FileUtils.TEMPLATEPATH, fileName); //更改为templatepath,原为Musicpath
					if (!temFile.exists())
						temFile.getParentFile().mkdirs();
					FileOutputStream fileOut = new FileOutputStream(temFile);
					byte[] buffer = new byte[1024*8];
					for (int j = 0; (j = in.read(buffer)) != -1;) {
						fileOut.write(buffer, 0, j);
					}
					fileOut.flush();
					in.close();
					fileOut.close();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

