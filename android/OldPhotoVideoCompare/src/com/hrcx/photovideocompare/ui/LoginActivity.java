package com.hrcx.photovideocompare.ui;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.hrcx.photovideocompare.R;
import com.hrcx.photovideocompare.checkview.CheckGetUtil;
import com.hrcx.photovideocompare.checkview.CheckView;
import com.hrcx.photovideocompare.checkview.ConmentConfig;
import com.hrcx.photovideocompare.dao.net.NetProtocol;
import com.hrcx.photovideocompare.utils.Constant;
import com.hrcx.photovideocompare.utils.DataCleanManager;
import com.hrcx.photovideocompare.utils.NetUtil;
import com.hrcx.photovideocompare.utils.UserState;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends MapActivity implements OnClickListener,
		LocationListener {

	private ImageButton btn_submit;
	private LinearLayout ll;
	private EditText usernameET;
	private EditText pswordET;
	String mobile;
	ProgressDialog pd;
	private MKSearch mMKSearch;
	private BMapManager mapManager;
	private MKLocationManager mLocationManager = null;
	private ProgressDialog dialog;
	// ������֤����Ϣ
	protected static final int UPDATA_CHECKNUM = 0x101;
	private CheckView mCheckView;
	private EditText mEditPass;
	private Button mRef;

	// ��֤�룺
	int[] checkNum = { 0, 0, 0, 0 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		findView();
	}

	@Override
	protected void onResume() {
		if (mapManager != null) {
			mapManager.start();
		}
		super.onResume();
	}

	private void findView() {
		pd = new ProgressDialog(this);
		ll = (LinearLayout) findViewById(R.id.l_ll);
		btn_submit = (ImageButton) findViewById(R.id.button_login_login);
		btn_submit.setOnClickListener(this);
		usernameET = (EditText) findViewById(R.id.editText_login_username);
		pswordET = (EditText) findViewById(R.id.editText_login_password);
		mobile = UserState.getEquipmentId(LoginActivity.this);
		// ��ʼ��MapActivity
		mapManager = new BMapManager(getApplication());
		// init�����ĵ�һ�����������������API Key
		mapManager.init("285B415EBAB2A92293E85502150ADA7F03C777C4", null);
		super.initMapActivity(mapManager);

		mLocationManager = mapManager.getLocationManager();
		// ע��λ�ø����¼�
		mLocationManager.requestLocationUpdates(this);
		// ʹ��GPS��λ
		mLocationManager
				.enableProvider((int) MKLocationManager.MK_GPS_PROVIDER);
		// �ȴ���ʾ
		dialog = new ProgressDialog(this);
		dialog.setMessage("���ڶ�λ...");
		dialog.setCancelable(true);
		dialog.show();

		mCheckView = (CheckView) findViewById(R.id.checkView);
		mEditPass = (EditText) findViewById(R.id.et_code);
		mRef = (Button) findViewById(R.id.ref);
		mRef.setOnClickListener(this);
		initCheckNum();
	}

	// ��ʼ����֤�벢��ˢ�½���
	public void initCheckNum() {
		checkNum = CheckGetUtil.getCheckNum();
		mCheckView.setCheckNum(checkNum);
		mCheckView.invaliChenkNum();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_login_login:
			String userInput = mEditPass.getText().toString();
			if (!CheckGetUtil.checkNum(userInput, checkNum)) {
				Toast.makeText(this,
						getResources().getString(R.string.code_input_error),
						Toast.LENGTH_SHORT).show();
				return;
			}

			String username,
			psword;
			username = usernameET.getText().toString();
			psword = pswordET.getText().toString();
			if (username.length() > 0 && psword.length() > 0) {
				if (NetUtil.checkNet(LoginActivity.this)) {
					pd.setMessage(this.getResources().getString(
							R.string.fyi_loading));
					pd.show();
					Constant.JSESSIONID = null;
					Constant.isSessionExpire = false;
					DataCleanManager.cleanApplicationData(this);
					new ActivityTask().execute(mobile, username, psword);
				} else {
					NetUtil.showToastNoNet(LoginActivity.this);
				}
			} else {
				ll.startAnimation(AnimationUtils.loadAnimation(
						LoginActivity.this, R.anim.shake));
				if (username.length() <= 0) {
					Toast.makeText(this,
							getResources().getString(R.string.input_username),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (psword.length() <= 0) {
					Toast.makeText(this,
							getResources().getString(R.string.input_pwd),
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
			break;
		case R.id.ref:
			initCheckNum();
			break;
		default:
			break;
		}
	}

	/** ���Լ�������߳� */
	class myThread implements Runnable {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				// ������Ϣ
				Message message = new Message();
				message.what = LoginActivity.UPDATA_CHECKNUM; // Hander�������ռ�

				LoginActivity.this.myHandler.sendMessage(message);
				try {
					// �߳�����
					Thread.sleep(ConmentConfig.PTEDE_TIME);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LoginActivity.UPDATA_CHECKNUM:
				mCheckView.invaliChenkNum();
				break;
			}
			// /////////////////////////////////////////////////
			// ���Ϊʲôд���⣿��������
			// �д��о���
			// /////////////////////////////////////////////////
			super.handleMessage(msg);
		}
	};

	class ActivityTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(LoginActivity.this).signin(params[0],
					params[1], params[2]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				Toast.makeText(LoginActivity.this, R.string.login_fail,
						Toast.LENGTH_SHORT).show();
				return;
			}
			super.onPostExecute(result);
			Toast.makeText(LoginActivity.this, R.string.login_success,
					Toast.LENGTH_SHORT).show();
			new ActivityTask2().execute();
		}
	}

	class ActivityTask2 extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(LoginActivity.this).getVideoSecond();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				pd.dismiss();
				return;
			}
			super.onPostExecute(result);
			pd.dismiss();
			Intent it = new Intent(LoginActivity.this, TypeSelectActivity.class);
			startActivity(it);
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		if (mapManager != null) {
			mapManager.destroy();
			mapManager = null;
		}
		mLocationManager = null;
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (mapManager != null) {
			mapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			Constant.gps = "longitude:" + location.getLongitude()
					+ ",latitude:" + location.getLatitude();
			mMKSearch = new MKSearch();
			mMKSearch.init(mapManager, new MySearchListener());

			mMKSearch
					.reverseGeocode(new GeoPoint((int) (1000000 * (location
							.getLatitude())), (int) (1000000 * location
							.getLongitude())));
			dialog.dismiss();
		}
	}

	/**
	 * �ڲ���ʵ��MKSearchListener�ӿ�,����ʵ���첽��������
	 * 
	 */
	public class MySearchListener implements MKSearchListener {
		/**
		 * ���ݾ�γ��������ַ��Ϣ���
		 * 
		 * @param result
		 *            �������
		 * @param iError
		 *            ����ţ�0��ʾ��ȷ���أ�
		 */
		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			if (result == null) {
				return;
			}
			StringBuffer sb = new StringBuffer();
			// ��γ������Ӧ��λ��
			sb.append(result.strAddr).append("/n");

			// �жϸõ�ַ�����Ƿ���POI��Point of Interest,����Ȥ�㣩
			if (null != result.poiList) {
				// �������е���Ȥ����Ϣ
				for (MKPoiInfo poiInfo : result.poiList) {
					// sb.append("----------------------------------------")
					// .append("/n");
					// sb.append("���ƣ�").append(poiInfo.name).append("/n");
					// sb.append("��ַ��").append(poiInfo.address).append("/n");
					// sb.append("���ȣ�")
					// .append(poiInfo.pt.getLongitudeE6() / 1000000.0f)
					// .append("/n");
					// sb.append("γ�ȣ�")
					// .append(poiInfo.pt.getLatitudeE6() / 1000000.0f)
					// .append("/n");
					// sb.append("�绰��").append(poiInfo.phoneNum).append("/n");
					// sb.append("�ʱࣺ").append(poiInfo.postCode).append("/n");
					// // poi���ͣ�0����ͨ�㣬1������վ��2��������·��3������վ��4��������·
					// sb.append("���ͣ�").append(poiInfo.ePoiType).append("/n");
					Constant.gps = poiInfo.address;
				}
			}
			// ����ַ��Ϣ����Ȥ����Ϣ��ʾ��TextView��

		}

		/**
		 * �ݳ�·���������
		 * 
		 * @param result
		 *            �������
		 * @param iError
		 *            ����ţ�0��ʾ��ȷ���أ�
		 */
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {
		}

		/**
		 * POI�����������Χ����������POI�������ܱ߼�����
		 * 
		 * @param result
		 *            �������
		 * @param type
		 *            ���ؽ�����ͣ�11,12,21:poi�б� 7:�����б�
		 * @param iError
		 *            ����ţ�0��ʾ��ȷ���أ�
		 */
		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
		}

		/**
		 * ��������·���������
		 * 
		 * @param result
		 *            �������
		 * @param iError
		 *            ����ţ�0��ʾ��ȷ���أ�
		 */
		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result,
				int iError) {
		}

		/**
		 * ����·���������
		 * 
		 * @param result
		 *            �������
		 * @param iError
		 *            ����ţ�0��ʾ��ȷ���أ�
		 */
		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result,
				int iError) {
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}
	}
}
