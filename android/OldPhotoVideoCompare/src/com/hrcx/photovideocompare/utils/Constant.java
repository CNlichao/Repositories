package com.hrcx.photovideocompare.utils;

import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.hrcx.photovideocompare.entity.UserInfo;

public class Constant {

	public static final String SERVER_ROOT = "http://gooditop.wicp.net:8888";
	/** 服务器基础路径 */
	// public static final String SERVER_PATH_ROOT =
	// "http://gooditop.wicp.net:8888/SHBZ";

	public static final String SERVER_PATH_ROOT = "http://192.168.3.116:8080/SHBZ";

	/************************************************* Protocol Path Start ********************************************/

	/** 登陆 */
	public static final String SERVER_PATH_SIGNIN = "/mlogcheck";

	/** 同步接口 (新的题 或者是 其他的) */
	public static final String SERVER_PATH_SYNC = "/do/control/InsurerDo/mobileList";

	// 图片验证
	public static final String SERVER_PATH_PHOTO_VALIDATION = "/validAction";

	// 是否完成验证
	public static final String SERVER_PATH_IS_VALIDATION_OK = "/validIsOK";

	// 获得图片
	public static final String SERVER_PATH_GET_PICTURE = "/getPicture";

	// 上传视频
	public static final String SERVER_PATH_UPLOAD_VIDEO = "/vidioVertify";

	// 获取视频录制秒数
	public static final String SERVER_PATH_GET_VEDIO_SECOND = "/getVideoTime";

	/************************************************* Protocol Path End ********************************************/

	/** 弹Toast广播 */
	public static final String RECEVIER_TOAST = "com.hrcx.photovideocompare.toast";
	public static final String RECEIVER_VERIFICATION = "com.hrcx.photovideocompare.verification";
	public static final String RECEIVER_SESSION_EXPIRE = "com.hrcx.photovideocompare.sessionexpire";

	// 全部用户数据
	public static List<UserInfo> totalUserInfoList = new ArrayList<UserInfo>();
	// 验证用户数据
	public static List<UserInfo> verificationUserInfoList = new ArrayList<UserInfo>();
	// 未验证用户数据
	public static List<UserInfo> noVerificationUserInfoList = new ArrayList<UserInfo>();
	// 查询数据
	public static List<UserInfo> searchUserInfoList = new ArrayList<UserInfo>();
	// sessionid
	public static String JSESSIONID = null;

	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;

	// 每页显示的行数
	public static final int ROWS_PER_PAGE = 10;
	public static int totalRows = 0;
	public static int verificationTotalRows = 0;
	public static int noVerificationTotalRows = 0;
	public static int totalPages = 0;
	public static int verificationTotalPages = 0;
	public static int noVerificationTotalPages = 0;
	public static int totalCurPage = 1;
	public static int verificationCurPage = 1;
	public static int noVerificationCurPage = 1;
	public static String procedureNumber;
	public static int waitTime = -1;
	public static float matchRatio = -1;
	public static int pictureNum = -1;
	public static boolean isTimeOut = false;
	public static String videoPath = null;
	public static int vedioTime = 0;
	public static String buttonName[] = null;
	public static boolean isSessionExpire = false;

	public static final String IMAGEFILLPATH = Environment
			.getExternalStorageDirectory() + "/" + "photovideocompare/";

	public static final String LOCATION = "location";
	public static final String LOCATION_ACTION = "locationAction";
	/** 百度地图API KEY */
	public static final String BAIDU_MAP_API_KEY = "6saMpXqvvtsypsGq8ZcBjv2x";
	public static String gps = "未获取gps信息";
}
