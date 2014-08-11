package com.docdeal.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import android.util.Base64;
import android.util.Log;

public class TestXml {
	public static void test(){
		try {
			FileReader fr=new FileReader("C:/Users/Administrator/Documents/Tencent Files/630871666/FileRecv/a.xml");
			BufferedReader rd=new BufferedReader(fr);
			StringBuffer sb=new StringBuffer();
			String line;
			line=rd.readLine();
			while(line!=null){
				sb.append(line);
				line=rd.readLine();
			}
			
			String base64Code=sb.toString();
			System.out.println(base64Code);
			byte[] bytes = Base64.decode(base64Code, Base64.DEFAULT);
			String code=new String(bytes);
			Log.e("¸½¼þword", code);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
