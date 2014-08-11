package com.hrcx.selfphotovideocompare.dao.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hrcx.selfphotovideocompare.R;
import com.hrcx.selfphotovideocompare.R.string;
import com.hrcx.selfphotovideocompare.utils.Constant;
import com.hrcx.selfphotovideocompare.utils.DTLogger;
import com.hrcx.selfphotovideocompare.utils.FormFile;
import com.hrcx.selfphotovideocompare.utils.SocketHttpRequester;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 解析网络Json
 * 
 * @author Administrator
 * 
 */
public class NetProtocol extends BaseProtocol {

	public NetProtocol(Context con) {
		super(con);
	}

	/**
	 * 登录
	 */
	public boolean signin(String id, String userPwd) {
		String path = Constant.SERVER_PATH_ROOT + Constant.SERVER_PATH_SIGNIN;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		// params.put("mobile", mobile);
		params.put("id", id);
		params.put("password", userPwd);
		params.put("gps", Constant.gps);
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				JSONObject backJson = parserJson(in);
				if (backJson != null) {
					int loginResult = backJson.getInt("result");
					int state = backJson.getInt("state");
					Constant.loginState = state;
					if (loginResult == 0) {
						Constant.id = id;
						result = true;

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			toast(mContext.getResources().getString(R.string.login_fail));
		}
		return result;
	}

	/**
	 * 注册
	 */
	public boolean register(String id, String userPwd, String phone,
			String address) {
		String path = Constant.SERVER_PATH_ROOT + Constant.SERVER_PATH_REGISTER;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		// params.put("mobile", mobile);
		params.put("id", Constant.registerid);
		params.put("password", userPwd);
		params.put("gps", Constant.gps);
		params.put("phone", phone);
		params.put("address", address);
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				JSONObject backJson = parserJson(in);
				if (backJson != null) {
					int Result = backJson.getInt("result");
					if (Result == 0) {
						result = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			toast("注册失败");
		}
		return result;
	}

	/**
	 * 查询
	 */
	public boolean search(String sfzh) {
		String path = Constant.SERVER_PATH_ROOT + Constant.SERVER_PATH_SYNC;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", sfzh);
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				JSONObject backJson = parserJson(in);
				if (backJson != null) {
					// 先判断session是否过期
					if (!backJson.isNull("IsLogin")) {
						Constant.isSessionExpire = true;
						result = false;
					} else {
						DownLoadData downLoadData = null;
						if (downLoadData == null)
							downLoadData = new DownLoadData(mContext);
						downLoadData.downloadSearchData(backJson);
						result = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			toast(mContext.getResources().getString(R.string.search_fail));
		}
		return result;
	}

	/**
	 * 验证人员查询
	 */
	public boolean verificationSearch(String sfzh) {
		String path = Constant.SERVER_PATH_ROOT + Constant.SERVER_PATH_SYNC;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", sfzh);
		params.put("validation", "1");
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				JSONObject backJson = parserJson(in);
				if (backJson != null) {
					if (!backJson.isNull("IsLogin")) {
						Constant.isSessionExpire = true;
						result = false;
					} else {
						DownLoadData downLoadData = null;
						if (downLoadData == null)
							downLoadData = new DownLoadData(mContext);
						downLoadData.downloadSearchData(backJson);
						result = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			toast(mContext.getResources().getString(R.string.search_fail));
		}
		return result;
	}

	/**
	 * 未验证人员查询
	 */
	public boolean noVerificationSearch(String sfzh) {
		String path = Constant.SERVER_PATH_ROOT + Constant.SERVER_PATH_SYNC;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", sfzh);
		params.put("validation", "2");
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				JSONObject backJson = parserJson(in);
				if (backJson != null) {
					if (!backJson.isNull("IsLogin")) {
						Constant.isSessionExpire = true;
						result = false;
					} else {
						DownLoadData downLoadData = null;
						if (downLoadData == null)
							downLoadData = new DownLoadData(mContext);
						downLoadData.downloadSearchData(backJson);
						result = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			toast(mContext.getResources().getString(R.string.search_fail));
		}
		return result;
	}

	/**
	 * 同步接口
	 */
	public boolean syncTotalData(String showPage) {
		String path = Constant.SERVER_PATH_ROOT + Constant.SERVER_PATH_SYNC;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("rows", Constant.ROWS_PER_PAGE + "");
		params.put("page", showPage);
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				JSONObject backJson = parserJson(in);
				if (backJson != null) {
					if (!backJson.isNull("IsLogin")) {
						Constant.isSessionExpire = true;
						result = false;
					} else {
						Constant.totalRows = backJson.getInt("total");
						Constant.totalPages = Constant.totalRows
								/ Constant.ROWS_PER_PAGE + 1;
						DownLoadData downLoadData = null;
						if (downLoadData == null)
							downLoadData = new DownLoadData(mContext);
						downLoadData.downloadTotalData(backJson);
						result = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			DTLogger.NetProLog("register E " + e.toString());
			toast(mContext.getResources().getString(R.string.login_fail));
		}
		return result;
	}

	/**
	 * 同步接口
	 */
	public boolean syncVerificationData(String showPage) {
		String path = Constant.SERVER_PATH_ROOT + Constant.SERVER_PATH_SYNC;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("validation", "1");
		params.put("rows", Constant.ROWS_PER_PAGE + "");
		params.put("page", showPage);
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				JSONObject backJson = parserJson(in);
				if (backJson != null) {
					if (!backJson.isNull("IsLogin")) {
						Constant.isSessionExpire = true;
						result = false;
					} else {
						Constant.verificationTotalRows = backJson
								.getInt("total");
						Constant.verificationTotalPages = Constant.verificationTotalRows
								/ Constant.ROWS_PER_PAGE + 1;
						DownLoadData downLoadData = null;
						if (downLoadData == null)
							downLoadData = new DownLoadData(mContext);
						downLoadData.downloadVerificationData(backJson);
						result = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			DTLogger.NetProLog("register E " + e.toString());
			toast(mContext.getResources().getString(R.string.login_fail));
		}
		return result;
	}

	/**
	 * 同步接口
	 */
	public boolean syncNoVerificationData(String showPage) {
		String path = Constant.SERVER_PATH_ROOT + Constant.SERVER_PATH_SYNC;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("validation", "2");
		params.put("rows", Constant.ROWS_PER_PAGE + "");
		params.put("page", showPage);
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				JSONObject backJson = parserJson(in);
				if (backJson != null) {
					if (!backJson.isNull("IsLogin")) {
						Constant.isSessionExpire = true;
						result = false;
					} else {
						Constant.noVerificationTotalRows = backJson
								.getInt("total");
						Constant.noVerificationTotalPages = Constant.noVerificationTotalRows
								/ Constant.ROWS_PER_PAGE + 1;
						DownLoadData downLoadData = null;
						if (downLoadData == null)
							downLoadData = new DownLoadData(mContext);
						downLoadData.downloadNoVerificationData(backJson);
						result = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			DTLogger.NetProLog("register E " + e.toString());
			toast(mContext.getResources().getString(R.string.login_fail));
		}
		return result;
	}

	/**
	 * 图片验证
	 */
	public boolean photoValidation(String sfzh, File file, String type) {
		String path = Constant.SERVER_PATH_ROOT
				+ Constant.SERVER_PATH_PHOTO_VALIDATION;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		/* params.put("id", "420117199111204141"); */
		params.put("id", sfzh);
		params.put("type", 0 + "");
		params.put("gps", Constant.gps);
		try {
			FormFile ff = new FormFile(file, "pictrueData", "image/jpeg", 32, 1);
			JSONObject backJson = SocketHttpRequester.post(path, params, ff);
			if (backJson != null) {
				if (!backJson.isNull("IsLogin")) {
					Constant.isSessionExpire = true;
					result = false;
				} else {
					Constant.procedureNumber = backJson
							.getString("procedureNumber");
					Constant.waitTime = backJson.getInt("waitTime");
					result = true;
					toast(mContext.getResources().getString(
							R.string.commit_success));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			DTLogger.NetProLog("photoValidation E " + e.toString());
			toast(mContext.getResources().getString(R.string.commit_fail));
		}
		return result;
	}

	/**
	 * 自我注册照片验证
	 */
	public boolean selfPhotoValidation(String sfzh, File file) {
		String path = Constant.SERVER_PATH_ROOT
				+ Constant.SERVER_PATH_VALIDATE_PICTURE;

		boolean result = false;

		HashMap<String, String> params = new HashMap<String, String>();
		/* params.put("id", "420117199111204141"); */
		params.put("id", sfzh);
		try {
			FormFile ff = new FormFile(file, "picture", "image/jpeg", 32, 1);
			JSONObject backJson = SocketHttpRequester.post(path, params, ff);
			if (backJson != null) {
				int time = backJson.getInt("result");
				if (time == 0) {
					result = true;
				} else if (time == 1) {
					result = false;
				} else if (time >= 2) {
					Constant.waitTime = time;
					result = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			DTLogger.NetProLog("photoValidation E " + e.toString());
			toast(mContext.getResources().getString(R.string.commit_fail));
		}
		return result;
	}

	/**
	 * 注册照片验证是否完成
	 * 
	 * @return
	 */
	public boolean registerIsOk() {
		String path = Constant.SERVER_PATH_ROOT
				+ Constant.SERVER_PATH_REGISTER_IS_OK;

		boolean result = false;

		HashMap<String, String> params = new HashMap<String, String>();
		/* params.put("id", "420117199111204141"); */
		params.put("id", Constant.registerid);
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				JSONObject backJson = parserJson(in);
				if (backJson != null) {
					int time = backJson.getInt("result");
					if (time == 0) {
						result = true;
					} else if (time == 1) {
						result = false;
					} else if (time >= 2) {
						Constant.waitTime = time;
						result = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			DTLogger.NetProLog("photoValidation E " + e.toString());
			toast(mContext.getResources().getString(R.string.commit_fail));
		}
		return result;
	}

	/**
	 * 是否完成验证
	 * 
	 * @return
	 */
	// public boolean isValidationOk() {
	// String path = Constant.SERVER_PATH_ROOT
	// + Constant.SERVER_PATH_IS_VALIDATION_OK;
	//
	// boolean result = false;
	// HashMap<String, String> params = new HashMap<String, String>();
	// params.put("procedureNumber", Constant.procedureNumber);
	// try {
	// InputStream in = HttpReq.sendPOSTRequest(path, params);
	// if (in != null) {
	// JSONObject backJson = parserJson(in);
	// if (backJson != null) {
	// if (!backJson.isNull("IsLogin")) {
	// Constant.isSessionExpire = true;
	// result = false;
	// } else {
	// if (!backJson.isNull("waitTime")) {
	// Constant.procedureNumber = backJson
	// .getString("procedureNumber");
	// Constant.waitTime = backJson.getInt("waitTime");
	// } else if (!backJson.isNull("matchRatio")
	// && !backJson.isNull("pictureNum")) {
	// Constant.procedureNumber = backJson
	// .getString("procedureNumber");
	// String matchRatio = backJson
	// .getString("matchRatio");
	// Constant.matchRatio = Float.parseFloat(matchRatio);
	// Constant.pictureNum = backJson.getInt("pictureNum");
	// } else if (!backJson.isNull("procedureNumber")) {
	// Constant.procedureNumber = backJson
	// .getString("procedureNumber");
	// Constant.isTimeOut = true;
	// } else {
	// return false;
	// }
	// result = true;
	// }
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// result = true;
	// return result;
	// }

	/**
	 * 获取要检查的图片
	 * 
	 * @return
	 */
	public boolean getCheckPhotoes(int order) {
		String path = Constant.SERVER_PATH_ROOT
				+ Constant.SERVER_PATH_GET_PICTURE;

		boolean result = true;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("procedureNumber", Constant.procedureNumber);
		params.put("order", order + "");
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				File temFile = new File(Constant.IMAGEFILLPATH, "tempPhoto"
						+ order + ".jpg");
				if (!temFile.exists())
					temFile.getParentFile().mkdirs();
				FileOutputStream fileOut = new FileOutputStream(temFile);
				byte[] buffer = new byte[1024 * 8];
				for (int j = 0; (j = in.read(buffer)) != -1;) {
					fileOut.write(buffer, 0, j);
				}
				fileOut.flush();
				in.close();
				fileOut.close();
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 视频上传
	 * 
	 * @return
	 */
	public boolean uploadVideo() {
		String path = Constant.SERVER_PATH_ROOT
				+ Constant.SERVER_PATH_UPLOAD_VIDEO;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("procedureNumber", Constant.procedureNumber);
		try {
			File file = new File(Constant.videoPath);
			if (!file.exists()) {
				file = null;
			}
			FormFile ff = new FormFile(file.getName(), file, "videoData",
					"video/mpeg");
			JSONObject backJson = SocketHttpRequester.post(path, params, ff);
			if (backJson != null) {
				if (!backJson.isNull("IsLogin")) {
					Constant.isSessionExpire = true;
					result = false;
				} else {
					int isSave = backJson.getInt("isSave");
					if (isSave == 0) {
						result = true;
					} else {
						result = false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			DTLogger.NetProLog("photoValidation E " + e.toString());
		}
		return result;
	}

	/**
	 * 同步接口
	 */
	public boolean getVideoSecond() {
		String path = Constant.SERVER_PATH_ROOT
				+ Constant.SERVER_PATH_GET_VEDIO_SECOND;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("param", "param");
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				JSONObject backJson = parserJson(in);
				if (backJson != null) {
					if (!backJson.isNull("IsLogin")) {
						Constant.isSessionExpire = true;
						result = false;
					} else {
						Constant.vedioTime = backJson.getInt("time");
						result = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			DTLogger.NetProLog("register E " + e.toString());
		}
		return result;
	}

	/**
	 * 验证想要注册的身份证号码
	 */
	public boolean validateId(String id) {

		String path = Constant.SERVER_PATH_ROOT
				+ Constant.SERVER_PATH_VALIDATE_ID;
		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		InputStream in = HttpReq.sendPOSTRequest(path, params);
		if (in != null) {
			JSONObject backJson = parserJson(in);
			if (backJson != null) {
				try {
					int flag = backJson.getInt("result");
					if (flag == 3) {
						result = true;
					} else if (flag == 2 || flag == 1) {
						result = false;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 自认证获取认证历史
	 */
	public boolean getAuthenticationHistory() {
		String path = Constant.SERVER_PATH_ROOT
				+ Constant.SERVER_PATH_GET_AUS_HISTORY;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", Constant.id);
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				JSONObject backJson = parserJson(in);
				if (backJson != null) {
					Iterator iterator = backJson.keys();
					List<JSONObject> jsonList = new ArrayList<JSONObject>();
					while (iterator.hasNext()) {
						String key = (String) iterator.next();
						JSONArray tempArray = backJson.getJSONArray(key);
						for (int i = 0; i < tempArray.length(); i++) {
							jsonList.add((JSONObject) tempArray.get(i));
						}
					}
					// Constant.authenticationHistoryJson = backJson;
					Constant.ausHistoryList = jsonList;
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			DTLogger.NetProLog("register E " + e.toString());
		}
		return result;
	}

	/**
	 * 自我认真图片上传验证
	 */
	public boolean selfPictureValid(File file) {
		String path = Constant.SERVER_PATH_ROOT
				+ Constant.SERVER_PATH_SELF_PICTURE_VALID;
		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		/* params.put("id", "420117199111204141"); */
		params.put("id", Constant.id);

		try {
			FormFile ff = new FormFile(file, "picture", "image/jpeg", 32, 1);
			JSONObject backJson = SocketHttpRequester.post(path, params, ff);
			if (backJson != null) {
				if (!backJson.isNull("result")) {

					Constant.waitTime = backJson.getInt("result");
					if (Constant.waitTime == 1) {
						Toast.makeText(mContext, "照片不合格，请重新拍摄",
								Toast.LENGTH_SHORT).show();
						return result;
					}
					result = true;
					Toast.makeText(
							mContext,
							mContext.getResources().getString(
									R.string.commit_success),
							Toast.LENGTH_SHORT).show();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			DTLogger.NetProLog("photoValidation E " + e.toString());
			toast(mContext.getResources().getString(R.string.commit_fail));
		}
		return result;

	}

	/**
	 * 是否完成验证
	 * 
	 * @return
	 */
	public boolean isSelfValidationOk() {
		String path = Constant.SERVER_PATH_ROOT
				+ Constant.SERVER_PATH_SELF_VALID_OK;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", Constant.id);
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				JSONObject backJson = parserJson(in);
				if (backJson != null) {
					Constant.waitTime = backJson.getInt("result");
					// Constant.waitTime = 0;
					if (Constant.waitTime == 0) {
						result = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 视频上传
	 * 
	 * @return
	 */
	public boolean selfSaveVideo() {
		String path = Constant.SERVER_PATH_ROOT
				+ Constant.SERVER_PATH_SELF_VIDEO_SAVE;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", Constant.id);
		try {
			File file = new File(Constant.videoPath);
			if (!file.exists()) {
				file = null;
			}
			FormFile ff = new FormFile(file.getName(), file, "vedio",
					"video/mpeg");
			JSONObject backJson = SocketHttpRequester.post(path, params, ff);
			if (backJson != null) {

				int isSave = backJson.getInt("result");
				if (isSave == 0) {
					result = true;
				} else {
					result = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			DTLogger.NetProLog("photoValidation E " + e.toString());
		}
		return result;
	}

}
