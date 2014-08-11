package com.hrcx.selfphotovideocompare.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class SocketHttpRequester {

	private final static int BUFFER = 24;

	public static JSONObject post(String path, Map<String, String> params,
			List<FormFile> files) throws Exception {
		final String BOUNDARY = "---------------------------7da2137580612";
		final String endline = "--" + BOUNDARY + "--\r\n";

		int fileDataLength = 0;
		for (FormFile uploadFile : files) {
			StringBuilder fileExplain = new StringBuilder();
			fileExplain.append("--");
			fileExplain.append(BOUNDARY);
			fileExplain.append("\r\n");
			String fileName = uploadFile.getFilname();
			fileExplain.append("Content-Disposition: form-data;name=\""
					+ uploadFile.getParameterName() + "\";filename=\""
					+ fileName + "\"\r\n");
			fileExplain.append("Content-Type: " + uploadFile.getContentType()
					+ "\r\n\r\n");
			fileExplain.append("\r\n");
			fileDataLength += fileExplain.length();
			if (uploadFile.getInStream() != null) {
				fileDataLength += uploadFile.getFile().length();
			} else {
				fileDataLength += uploadFile.getData().length;
			}
		}
		StringBuilder textEntity = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			textEntity.append("--");
			textEntity.append(BOUNDARY);
			textEntity.append("\r\n");
			textEntity.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"\r\n\r\n");
			textEntity.append(entry.getValue());
			textEntity.append("\r\n");
			DTLogger.d(entry.getKey()+"  "+ entry.getValue());
		}

		int dataLength = textEntity.toString().getBytes().length
				+ fileDataLength + endline.getBytes().length;

		URL url = new URL(path);
		int port = url.getPort() == -1 ? 80 : url.getPort();
		Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
		socket.setSoTimeout(60000);
		OutputStream outStream = socket.getOutputStream();
		String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
		outStream.write(requestmethod.getBytes());
		String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
		outStream.write(accept.getBytes());
		String language = "Accept-Language: zh-CN\r\n";
		outStream.write(language.getBytes());
		String contenttype = "Content-Type: multipart/form-data; boundary="
				+ BOUNDARY + "\r\n";
		outStream.write(contenttype.getBytes());
		String contentlength = "Content-Length: " + dataLength + "\r\n";
		outStream.write(contentlength.getBytes());
		String alive = "Connection: Keep-Alive\r\n"
				+ "Cookie:JSESSIONID=" + Constant.JSESSIONID + "\r\n";// Ìí¼Ócookie×Ö¶Î
		outStream.write(alive.getBytes());
		String host = "Host: " + url.getHost() + ":" + port + "\r\n";
		outStream.write(host.getBytes());

		outStream.write("\r\n".getBytes());

		outStream.write(textEntity.toString().getBytes());

		for (FormFile uploadFile : files) {
			StringBuilder fileEntity = new StringBuilder();
			fileEntity.append("--");
			fileEntity.append(BOUNDARY);
			fileEntity.append("\r\n");
			String fileName = uploadFile.getFilname();
			if (fileName.lastIndexOf(".jpg") == -1) {
				fileName += ".jpg";
			}
			fileEntity.append("Content-Disposition: form-data;name=\""
					+ uploadFile.getParameterName() + "\";filename=\""
					+ fileName + "\"\r\n");
			fileEntity.append("Content-Type: " + uploadFile.getContentType()
					+ "\r\n\r\n");
			outStream.write(fileEntity.toString().getBytes());
			if (uploadFile.getInStream() != null) {
				byte[] buffer = new byte[1024 * BUFFER];
				int len = 0;
				FileInputStream in = (FileInputStream) uploadFile.getInStream();
				try {
					while ((len = in.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					if (in != null) {
						in.close();
					}
					if (socket != null) {
						socket.shutdownOutput();
					}
				}
				in.close();
			} else {
				outStream.write(uploadFile.getData(), 0,
						uploadFile.getData().length);
			}
			outStream.write("\r\n".getBytes());
		}
		BufferedReader reader = null;
		try {
			outStream.write(endline.getBytes());
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String readStr = reader.readLine();
			DTLogger.NetProLog("readStr " + readStr);
			if (readStr.indexOf("200") == -1) {
				return null;
			} else {
				String str = null;
				StringBuilder builder = new StringBuilder();
				char buffer[] = new char[1024];
				for (int i = 0; i < buffer.length; i++) {
					buffer[i] = 0;
				}
				reader.read(buffer);
				str = new String(buffer);				
				if (str.indexOf("{") != -1) {
					int index = str.indexOf("{");
					String str2 = str.substring(index, str.length());
					int index2 = str2.indexOf("}");
					String str3 = str2.substring(0, index2+1);
					builder.append(str3);
					DTLogger.socketHttpRequesterLog("upload back json:"
							+ str3);
					return new JSONObject(builder.toString());
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			DTLogger.NetProLog("photoValidation E " + e.toString());
		} finally {
			if (outStream != null) {
				outStream.flush();
				outStream.close();
			}
			reader.close();
			if (socket != null) {
				socket.close();
			}
		}
		return null;
	}

	public static JSONObject post(String path, Map<String, String> params,
			FormFile file) throws Exception {
		ArrayList<FormFile> fileArray = new ArrayList<FormFile>();
		fileArray.add(file);
		return post(path, params, fileArray);
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * BUFFER];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}
}
