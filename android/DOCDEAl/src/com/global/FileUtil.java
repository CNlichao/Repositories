package com.global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

import org.textmining.text.extraction.WordExtractor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

public class FileUtil {

	public static String readword(String wfile) {
		String temp = "";
		try {

			FileInputStream fileinputstream = new FileInputStream(wfile);
			WordExtractor extractor = new WordExtractor();
			temp = extractor.extractText(fileinputstream);
			fileinputstream.close();
		} catch (Exception ex) {
			System.out.println("FileNotFoundException error" + ex.getMessage());
		}
		return temp;

	}

	public static String writeStringToTxt(String filePath, String fileContent) {
		String path = filePath.substring(0, filePath.lastIndexOf(".")) + ".txt";
		// FileWriter fw;
		try {
			FileOutputStream out = new FileOutputStream(path);
			out.write(fileContent.getBytes("GB2312"));
			out.close();
			// fw = new FileWriter(path, true);
			// fw.write(fileContent);
			// fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}

	@SuppressLint("SdCardPath")
	public static String saveFileToSdkcard(InputStream input, String type) {
		String fileName;
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			Log.i("TestFile", "SD card is not avaiable/writeable right now.");
			return "";
		}
		String name = new DateFormat().format("yyyyMMdd_hhmmss",
				Calendar.getInstance(Locale.CHINA))
				+ "." + type;

		FileOutputStream b = null;
		File file = new File("/sdcard/myDoc/");
		file.mkdirs();// 创建文件夹
		fileName = "/sdcard/myDoc/" + name;

		int a;

		try {
			b = new FileOutputStream(fileName);
			while ((a = input.read()) != -1) {
				b.write(a);
			}
			b.flush();
			b.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileName;
	}

	

}
