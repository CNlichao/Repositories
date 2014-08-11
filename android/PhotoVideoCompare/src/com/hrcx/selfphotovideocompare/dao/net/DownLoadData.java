package com.hrcx.selfphotovideocompare.dao.net;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hrcx.selfphotovideocompare.entity.UserInfo;
import com.hrcx.selfphotovideocompare.utils.Constant;

import android.content.Context;

public class DownLoadData {

	public DownLoadData(Context context) {
	}

	// ���ز�ѯ����
	public void downloadSearchData(JSONObject allObject) {
		if (!allObject.isNull("rows")) {
			try {
				JSONArray pathArray = allObject.getJSONArray("rows");
				JSONObject contentObject = null;
				Constant.searchUserInfoList.clear();
				for (int i = 0; !pathArray.isNull(i); i++) {
					try {
						UserInfo userInfo = new UserInfo();
						contentObject = pathArray.getJSONObject(i);
						userInfo.setName(contentObject.getString("name"));
						userInfo.setSfzh(contentObject.getString("id"));
						userInfo.setArea(contentObject.getString("areaName"));
						userInfo.setValidation(contentObject.getString("validation"));
						userInfo.setCheckYear(contentObject.getString("checkYear"));
						Constant.searchUserInfoList.add(userInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		return;
	}

	// ����ȫ������
	public void downloadTotalData(JSONObject allObject) {
		if (!allObject.isNull("rows")) {
			try {
				JSONArray pathArray = allObject.getJSONArray("rows");
				JSONObject contentObject = null;
				Constant.totalUserInfoList.clear();
				for (int i = 0; !pathArray.isNull(i); i++) {
					try {
						UserInfo userInfo = new UserInfo();
						contentObject = pathArray.getJSONObject(i);
						userInfo.setName(contentObject.getString("name"));
						userInfo.setSfzh(contentObject.getString("id"));
						userInfo.setArea(contentObject.getString("areaName"));
						userInfo.setValidation(contentObject.getString("validation"));
						userInfo.setCheckYear(contentObject.getString("checkYear"));
						Constant.totalUserInfoList.add(userInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		return;
	}

	// ������֤����
	public void downloadVerificationData(JSONObject allObject) {
		if (!allObject.isNull("rows")) {
			try {
				JSONArray pathArray = allObject.getJSONArray("rows");
				JSONObject contentObject = null;
				Constant.verificationUserInfoList.clear();
				for (int i = 0; !pathArray.isNull(i); i++) {
					try {
						UserInfo userInfo = new UserInfo();
						contentObject = pathArray.getJSONObject(i);
						userInfo.setName(contentObject.getString("name"));
						userInfo.setSfzh(contentObject.getString("id"));
						userInfo.setArea(contentObject.getString("areaName"));
						userInfo.setValidation(contentObject.getString("validation"));
						userInfo.setCheckYear(contentObject.getString("checkYear"));
						Constant.verificationUserInfoList.add(userInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		return;
	}

	// ����δ��֤����
	public void downloadNoVerificationData(JSONObject allObject) {
		if (!allObject.isNull("rows")) {
			try {
				JSONArray pathArray = allObject.getJSONArray("rows");
				JSONObject contentObject = null;
				Constant.noVerificationUserInfoList.clear();
				for (int i = 0; !pathArray.isNull(i); i++) {
					try {
						UserInfo userInfo = new UserInfo();
						contentObject = pathArray.getJSONObject(i);
						userInfo.setName(contentObject.getString("name"));
						userInfo.setSfzh(contentObject.getString("id"));
						userInfo.setArea(contentObject.getString("areaName"));
						userInfo.setValidation(contentObject.getString("validation"));
						userInfo.setCheckYear(contentObject.getString("checkYear"));
						Constant.noVerificationUserInfoList.add(userInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		return;
	}

}
