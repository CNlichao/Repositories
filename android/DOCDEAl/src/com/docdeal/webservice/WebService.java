package com.docdeal.webservice;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import android.util.Log;

import com.global.StringUtil;

public class WebService {
	// 命名空间
	public static final String NAMESPACE = "http://doc.deal.com";
	public static final String URL = "http://gooditop.wicp.net:8881/services/NodeGWWebService?wsdl";
	private String METHOD_NAME;
	private LinkedHashMap<String, String> parameters;
	private String returnProperty;
	private String reTurnXMl = "";

	public WebService(String methodName,
			LinkedHashMap<String, String> parameters, String returnProperty) {
		this.parameters = parameters;
		this.METHOD_NAME = methodName;
		this.returnProperty = returnProperty;
	}

	public WebService(String methodName,
			LinkedHashMap<String, String> parameters) {
		this.parameters = parameters;
		this.METHOD_NAME = methodName;
	}

	public WebService(String methodName, String propertyReturn) {
		this.METHOD_NAME = methodName;
		this.returnProperty = propertyReturn;
	}

	public void setMethodName(String methodName) {
		this.METHOD_NAME = methodName;
	}

	public void setParams(LinkedHashMap<String, String> params) {
		this.parameters = params;
	}

	public void setReturnProperty(String returnProperty) {
		this.returnProperty = returnProperty;
	}

	private class CallWeb implements Runnable {

		@Override
		public void run() {
			try {
				// /实例化soapObject对象
				SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
				// /设置参数in0，传入
				// rpc.addProperty("userSign", "wangxinmei");
				// 添加方法参数
				if (parameters != null && parameters.size() > 0) {
					Iterator its = parameters.keySet().iterator();
					while (its.hasNext()) {
						String key = (String) its.next();
						rpc.addProperty(key, parameters.get(key));
						// its.next();
					}

				}

				// /Android传输对象
				AndroidHttpTransport ht = new AndroidHttpTransport(URL);

				ht.debug = true;
				// /获得序列化的envelope
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);

				envelope.bodyOut = rpc;

				envelope.dotNet = true;

				envelope.setOutputSoapObject(rpc);
				// 调用WebService的方法
				ht.call(null, envelope);
				// 用SoapObject对象接受返回结果
				SoapObject result = (SoapObject) envelope.bodyIn;

				if (result != null && !StringUtil.isEmpty(returnProperty)) {
					reTurnXMl = result.getProperty(returnProperty).toString();
					// String str = result.toString();
					// System.out.println("return result--->" + str);
					// //取消进去对话框
				} else {
					System.out.println(" vote faile");
				}

			} catch (Exception e) {
				System.out.println("IO error");

				e.printStackTrace();
			}

		}
	}

	public String getData() {

		Thread th = new Thread(new CallWeb());
		th.start();
		try {
			th.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Log.i("sss",this.reTurnXMl);
		return this.reTurnXMl;
	}

}
