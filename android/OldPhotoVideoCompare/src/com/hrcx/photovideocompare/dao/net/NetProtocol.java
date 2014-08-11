package com.hrcx.photovideocompare.dao.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import org.json.JSONObject;

import com.hrcx.photovideocompare.R;
import com.hrcx.photovideocompare.utils.Constant;
import com.hrcx.photovideocompare.utils.DTLogger;
import com.hrcx.photovideocompare.utils.FormFile;
import com.hrcx.photovideocompare.utils.SocketHttpRequester;

import android.content.Context;

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
	public boolean signin(String mobile, String userName, String userPwd) {
		String path = Constant.SERVER_PATH_ROOT + Constant.SERVER_PATH_SIGNIN;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("userName", userName);
		params.put("userPwd", userPwd);
		params.put("gps", Constant.gps);
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				JSONObject backJson = parserJson(in);
				if (backJson != null) {
					int flag = backJson.getInt("IsLogin");
					if (flag == 0) {
						String str = backJson.getString("PreList");
						Constant.buttonName = str.split(",");
						result = true;
						toast(mContext.getResources().getString(
								R.string.login_success));
					} else {
						toast(mContext.getResources().getString(
								R.string.login_fail));
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
					//先判断session是否过期
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
						Constant.verificationTotalRows = backJson.getInt("total");
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
						Constant.noVerificationTotalRows = backJson.getInt("total");
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
		/*params.put("id", "420117199111204141");*/
		params.put("id", sfzh);
		params.put("type", 0+"");
		params.put("gps", Constant.gps);
		try {
			FormFile ff = new FormFile(file, "pictrueData", "image/jpeg", 32, 1);
			JSONObject backJson = SocketHttpRequester.post(path, params, ff);
			if (backJson != null) {
				if (!backJson.isNull("IsLogin")) {
					Constant.isSessionExpire = true;
					result = false;
				} else {
					Constant.procedureNumber = backJson.getString("procedureNumber");
					Constant.waitTime = backJson.getInt("waitTime");
					result = true;
					toast(mContext.getResources()
							.getString(R.string.commit_success));
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
	 * @return
	 */
	public boolean isValidationOk() {
		String path = Constant.SERVER_PATH_ROOT
				+ Constant.SERVER_PATH_IS_VALIDATION_OK;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("procedureNumber", Constant.procedureNumber);
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				JSONObject backJson = parserJson(in);
				if (backJson != null) {
					if (!backJson.isNull("IsLogin")) {
						Constant.isSessionExpire = true;
						result = false;
					} else {
						if (!backJson.isNull("waitTime")) {
							Constant.procedureNumber = backJson.getString("procedureNumber");
							Constant.waitTime = backJson.getInt("waitTime");
						} else if (!backJson.isNull("matchRatio") && !backJson.isNull("pictureNum")) {
							Constant.procedureNumber = backJson.getString("procedureNumber");
							String matchRatio = backJson.getString("matchRatio");
							Constant.matchRatio = Float.parseFloat(matchRatio);
							Constant.pictureNum = backJson.getInt("pictureNum");
						} else if (!backJson.isNull("procedureNumber")) {
							Constant.procedureNumber = backJson.getString("procedureNumber");
							Constant.isTimeOut = true;
						} else {
							return false;
						}
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
	 * 获取要检查的图片
	 * @return
	 */
	public boolean getCheckPhotoes(int order) {
		String path = Constant.SERVER_PATH_ROOT
				+ Constant.SERVER_PATH_GET_PICTURE;

		boolean result = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("procedureNumber", Constant.procedureNumber);
		params.put("order", order+"");
		try {
			InputStream in = HttpReq.sendPOSTRequest(path, params);
			if (in != null) {
				File temFile = new File(Constant.IMAGEFILLPATH, "tempPhoto"+order+".jpg");
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
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 视频上传
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
			FormFile ff = new FormFile(file.getName(), file, "videoData", "video/mpeg");
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
		String path = Constant.SERVER_PATH_ROOT + Constant.SERVER_PATH_GET_VEDIO_SECOND;

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
}
