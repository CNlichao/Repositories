package com.hrcx.photovideocompare.dao.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import com.hrcx.photovideocompare.utils.Constant;
import com.hrcx.photovideocompare.utils.DTLogger;

public class HttpReq {

	private static final int REQUEST_TIMEOUT = 10 * 1000;// 设置请求超时10秒钟
	private static final int SO_TIMEOUT = 50 * 1000; // 设置等待数据超时时间10秒钟

	/**
	 * 添加请求超时时间和等待时间
	 * 
	 * @return HttpClient对象
	 */
	private static DefaultHttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		DefaultHttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

	/**
	 * 发送GET请求
	 * 
	 * @param path
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @return 请求是否成功,true为请求成功,false为请求失败
	 */
	public static InputStream get(String path, HashMap<String, String> params)
			throws Exception {
		DTLogger.d("Get method");
		StringBuilder url = new StringBuilder(path);
		if (params != null && !params.isEmpty()) {
			url.append('?');
			for (Map.Entry<String, String> entry : params.entrySet()) {
				url.append(entry.getKey()).append('=')
						.append(URLEncoder.encode(entry.getValue(), "UTF-8"))
						.append('&');
			}
			url.deleteCharAt(url.length() - 1);
			HttpURLConnection conn = (HttpURLConnection) new URL(url.toString())
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestProperty("Cookie", "XDEBUG_SESSION=ECLIPSE_DBGP");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			if (null != Constant.JSESSIONID) {
				conn.setRequestProperty("Cookie", "JSESSIONID="
						+ Constant.JSESSIONID);
			}
			/* conn.setRequestProperty("Cookie", "XDEBUG_SESSION=ECLIPSE_DBGP"); */
			conn.setRequestMethod("GET");
			conn.setUseCaches(true);
			if (conn.getResponseCode() == 200) {
				return conn.getInputStream();
			} else {
				return null;
			}
		}
		return null;
	}

	/**
	 * 发送POST请求
	 * 
	 * @param path
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @return 请求是否成功,成功则返回输入流;
	 */
	public static InputStream post(String path, HashMap<String, String> params) {
		HttpURLConnection conn = null;
		DTLogger.d("Post method");
		try {
			StringBuilder entity = new StringBuilder();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					entity.append(entry.getKey())
							.append('=')
							.append(URLEncoder.encode(entry.getValue(), "UTF-8"))
							.append('&');
				}
				entity.deleteCharAt(entity.length() - 1);
				DTLogger.d(entity.toString());
				byte[] entitydata = entity.toString().getBytes();
				conn = (HttpURLConnection) new URL(path).openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("POST");
				conn.setUseCaches(false);
				conn.setDoOutput(true);// 允许对外输出实体数据
				conn.setDoInput(true);//
				conn.setAllowUserInteraction(false);
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				conn.setRequestProperty("Content-Length",
						String.valueOf(entitydata.length));
				// conn.setRequestProperty("Accept-Encoding","gzip, deflate");
				conn.setRequestProperty("Connection", "close");// "Keep-Alive");
				if (null != Constant.JSESSIONID) {
					conn.setRequestProperty("Cookie", "JSESSIONID="
							+ Constant.JSESSIONID);
				}
				/*
				 * conn.setRequestProperty("Cookie",
				 * "XDEBUG_SESSION=ECLIPSE_DBGP");
				 */
				OutputStream outStream = conn.getOutputStream();
				try {
					outStream.write(entitydata);
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
				}
				if (conn.getResponseCode() != 200) {
					return null;
				} else {
					InputStream inputStream = conn.getInputStream();
					DTLogger.d(inputStream.toString());
					return inputStream;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			DTLogger.d("socket connet erro");
		}
		return null;
	}

	/**
	 * post 方法
	 * 
	 * @param path
	 * @param params
	 * @return
	 */
	public static InputStream sendPOSTRequest(String path,
			HashMap<String, String> params) {
		UrlEncodedFormEntity data = null;
		try {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					pairs.add(new BasicNameValuePair(entry.getKey(), entry
							.getValue()));
					DTLogger.d(entry.getKey() + "  " + entry.getValue());
				}
				data = new UrlEncodedFormEntity(pairs, "utf-8");
				DefaultHttpClient http = getHttpClient();
				HttpPost hp = new HttpPost(path);
				hp.addHeader("Accept-Encoding", "gzip");
				if (null != Constant.JSESSIONID) {
					hp.addHeader("Cookie", "JSESSIONID=" + Constant.JSESSIONID);
				}
				hp.setEntity(data);
				try {
					HttpResponse response = http.execute(hp);
					DTLogger.d("post code: "
							+ response.getStatusLine().getStatusCode() + "");
					if (response.getStatusLine().getStatusCode() == 200) {
						HttpEntity httpEntity = response.getEntity();
						Header firstHeader = response
								.getFirstHeader("Content-Encoding"); // 判断是否压缩了返回值
						InputStream inputStream = null;
						if (firstHeader != null) {
							DTLogger.d("firstHeader = " + firstHeader.getName()
									+ " ; " + firstHeader.getValue());
							if (firstHeader.getValue().equals("gzip")) {
								inputStream = new GZIPInputStream(
										httpEntity.getContent());
							}
							if (Constant.JSESSIONID == null) {
								CookieStore mCookieStore = http
										.getCookieStore();
								List<Cookie> cookies = mCookieStore
										.getCookies();
								for (int i = 0; i < cookies.size(); i++) {
									if ("JSESSIONID".equals(cookies.get(i)
											.getName())) {
										Constant.JSESSIONID = cookies.get(i)
												.getValue();
										DTLogger.d("Constant.JSESSIONID = " + Constant.JSESSIONID);
										break;
									}
								}
							}
						} else {
							if (Constant.JSESSIONID == null) {
								CookieStore mCookieStore = http
										.getCookieStore();
								List<Cookie> cookies = mCookieStore
										.getCookies();
								for (int i = 0; i < cookies.size(); i++) {
									if ("JSESSIONID".equals(cookies.get(i)
											.getName())) {
										Constant.JSESSIONID = cookies.get(i)
												.getValue();
										DTLogger.d("Constant.JSESSIONID = " + Constant.JSESSIONID);
										break;
									}
								}
							}
							inputStream = httpEntity.getContent();
						}
						return inputStream;
					}
					return null;
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static InputStream sendGETRequest(String path,
			HashMap<String, String> params) throws Exception {
		DTLogger.d("Get method : client");
		StringBuilder url = new StringBuilder(path);
		if (params != null && !params.isEmpty()) {
			url.append('?');
			for (Map.Entry<String, String> entry : params.entrySet()) {
				url.append(entry.getKey()).append('=')
						.append(URLEncoder.encode(entry.getValue(), "UTF-8"))
						.append('&');
			}
			StringBuilder deleteCharAt = url.deleteCharAt(url.length() - 1);
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet hg = new HttpGet(deleteCharAt.toString());
			hg.addHeader("Accept-Encoding", "gzip");
			if (null != Constant.JSESSIONID) {
				hg.addHeader("Cookie", "JSESSIONID=" + Constant.JSESSIONID);
			}
			/* hg.addHeader("Cookie","XDEBUG_SESSION=ECLIPSE_DBGP"); */
			HttpResponse response = httpClient.execute(hg);
			DTLogger.d("get code: " + response.getStatusLine().getStatusCode()
					+ "");
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = response.getEntity();
				Header firstHeader = response
						.getFirstHeader("Content-Encoding"); // 判断是否压缩了返回值
				InputStream inputStream = null;
				if (firstHeader != null) {
					DTLogger.d("firstHeader = " + firstHeader.getName() + " ; "
							+ firstHeader.getValue());
					if (firstHeader.getValue().equals("gzip")) {
						inputStream = new GZIPInputStream(
								httpEntity.getContent());// httpEntity.getContent();
					}
				} else {
					inputStream = httpEntity.getContent();
				}
				// InputStream inputStream = new
				// GZIPInputStream(response.getEntity().getContent());//
				// response.getEntity().getContent();
				return inputStream;
			}
		}
		return null;
	}

	public static InputStream sendGETRequest(String path, String uid,
			String authkey) throws Exception {
		StringBuilder url = new StringBuilder(path);
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet hg = new HttpGet(url.toString());
		hg.addHeader("Accept-Encoding", "gzip");
		if (uid != null)
			hg.addHeader("Cookie", "u=" + uid + "; s=" + authkey);
		HttpResponse response = httpClient.execute(hg);
		DTLogger.d("get path: " + path + " !!");
		DTLogger.d("get code: " + response.getStatusLine().getStatusCode() + "");
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream inputStream = new GZIPInputStream(response.getEntity()
					.getContent()); // response.getEntity().getContent();
			return inputStream;
		}
		return null;
	}
}
