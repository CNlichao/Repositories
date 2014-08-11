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
	// �����ռ�
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
				// /ʵ����soapObject����
				SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
				// /���ò���in0������
				// rpc.addProperty("userSign", "wangxinmei");
				// ��ӷ�������
				if (parameters != null && parameters.size() > 0) {
					Iterator its = parameters.keySet().iterator();
					while (its.hasNext()) {
						String key = (String) its.next();
						rpc.addProperty(key, parameters.get(key));
						// its.next();
					}

				}

				// /Android�������
				AndroidHttpTransport ht = new AndroidHttpTransport(URL);

				ht.debug = true;
				// /������л���envelope
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);

				envelope.bodyOut = rpc;

				envelope.dotNet = true;

				envelope.setOutputSoapObject(rpc);
				// ����WebService�ķ���
				ht.call(null, envelope);
				// ��SoapObject������ܷ��ؽ��
				SoapObject result = (SoapObject) envelope.bodyIn;

				if (result != null && !StringUtil.isEmpty(returnProperty)) {
					reTurnXMl = result.getProperty(returnProperty).toString();
					// String str = result.toString();
					// System.out.println("return result--->" + str);
					// //ȡ����ȥ�Ի���
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
