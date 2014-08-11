package com.hrcx.selfphotovideocompare.utils;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;

public class FileUtils {

	public static final String USERPATH = Environment.getExternalStorageDirectory() + "/DreamTime/user/";
	public static final String BABYPATH = Environment.getExternalStorageDirectory() + "/DreamTime/baby/";
	public static final String TEMPLATEPATH = Environment.getExternalStorageDirectory() + "/DreamTime/template";
	public static final String MUSICPATH = Environment.getExternalStorageDirectory() + "/DreamTime/music";
	public static final String EXPORTPATH = Environment.getExternalStorageDirectory() + "/export";
	/**
	 * 导出文件
	 * @param drArray
	 * @param con
	 * @return
	 */
	public static boolean saveRecords2Public(JSONArray drArray,Context con){
		File rFile = null;
		if(drArray != null){ 
			try {
				if(!new File(EXPORTPATH).exists()){
					new File(EXPORTPATH).mkdirs();
				}
				for(int i = 0 ;!drArray.isNull(i);i++){ //遍历导出
					JSONObject drJs = drArray.getJSONObject(i);
					int type = drJs.getInt("type");
					String fileName = drJs.getString("fileName");
					rFile= new File(USERPATH + "/" + type, fileName);
					if(rFile.exists()){
						rFile.renameTo(new File(EXPORTPATH,fileName));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 处理图片尺寸
	 */
	public static Bitmap ResizeBitmap(Bitmap bitmap, int newHeight) {
	     int width = bitmap.getWidth();
	     int height = bitmap.getHeight();
	     float temp = ((float) width) / ((float) height);
	     int newWidth = (int) ((newHeight) * temp);
	     float scaleWidth = ((float) newWidth) / width;
	     float scaleHeight = ((float) newHeight) / height;
	     Matrix matrix = new Matrix();
	     matrix.postScale(scaleWidth, scaleHeight);
	     Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	     return resizedBitmap;
	}
}

