package com.hrcx.photovideocompare.utils;

import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.hrcx.photovideocompare.entity.UserInfo;

public class Constant {

	public static final String SERVER_ROOT = "http://gooditop.wicp.net:8888";
	/** ����������·�� */
	// public static final String SERVER_PATH_ROOT =
	// "http://gooditop.wicp.net:8888/SHBZ";

	public static final String SERVER_PATH_ROOT = "http://192.168.3.116:8080/SHBZ";

	/************************************************* Protocol Path Start ********************************************/

	/** ��½ */
	public static final String SERVER_PATH_SIGNIN = "/mlogcheck";

	/** ͬ���ӿ� (�µ��� ������ ������) */
	public static final String SERVER_PATH_SYNC = "/do/control/InsurerDo/mobileList";

	// ͼƬ��֤
	public static final String SERVER_PATH_PHOTO_VALIDATION = "/validAction";

	// �Ƿ������֤
	public static final String SERVER_PATH_IS_VALIDATION_OK = "/validIsOK";

	// ���ͼƬ
	public static final String SERVER_PATH_GET_PICTURE = "/getPicture";

	// �ϴ���Ƶ
	public static final String SERVER_PATH_UPLOAD_VIDEO = "/vidioVertify";

	// ��ȡ��Ƶ¼������
	public static final String SERVER_PATH_GET_VEDIO_SECOND = "/getVideoTime";

	/************************************************* Protocol Path End ********************************************/

	/** ��Toast�㲥 */
	public static final String RECEVIER_TOAST = "com.hrcx.photovideocompare.toast";
	public static final String RECEIVER_VERIFICATION = "com.hrcx.photovideocompare.verification";
	public static final String RECEIVER_SESSION_EXPIRE = "com.hrcx.photovideocompare.sessionexpire";

	// ȫ���û�����
	public static List<UserInfo> totalUserInfoList = new ArrayList<UserInfo>();
	// ��֤�û�����
	public static List<UserInfo> verificationUserInfoList = new ArrayList<UserInfo>();
	// δ��֤�û�����
	public static List<UserInfo> noVerificationUserInfoList = new ArrayList<UserInfo>();
	// ��ѯ����
	public static List<UserInfo> searchUserInfoList = new ArrayList<UserInfo>();
	// sessionid
	public static String JSESSIONID = null;

	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;

	// ÿҳ��ʾ������
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
	/** �ٶȵ�ͼAPI KEY */
	public static final String BAIDU_MAP_API_KEY = "6saMpXqvvtsypsGq8ZcBjv2x";
	public static String gps = "δ��ȡgps��Ϣ";
}
