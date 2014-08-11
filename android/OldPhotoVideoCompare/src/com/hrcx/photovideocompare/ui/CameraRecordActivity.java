package com.hrcx.photovideocompare.ui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.hrcx.photovideocompare.R;
import com.hrcx.photovideocompare.dao.net.NetProtocol;
import com.hrcx.photovideocompare.utils.Constant;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(9)
public class CameraRecordActivity extends BasicActivity implements Callback, SensorEventListener {

	private ImageButton btnChange;
	private boolean bFrontCamera = false;
	private ImageButton mVideoStartBtn;
	private TextView tv_compt;
	private ImageButton verification_pass;
	private SurfaceView mSurfaceview;
	private MediaRecorder mMediaRecorder;
	private SurfaceHolder mSurfaceHolder;
	private File mRecVedioPath;
	private File mRecAudioFile;
	private TextView timer;
	private int second = 0;
	private boolean bool;
	private int parentId;
	protected Camera camera;
	protected boolean isPreview = true;
	private boolean isRecording = true; // true表示没有录像，点击开始；false表示正在录像，点击暂停
	private List<Size> mSupportedPreviewSizes;
	private Size mPreviewSize;
	private boolean bIfPreview = false;
	private boolean mPreviewRunning;
	private SensorManager mSensorManager;
	private boolean isFirst = true;

	@TargetApi(9)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		/*
		 * 全屏显示
		 */
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 getWindow().setFormat(PixelFormat.TRANSLUCENT); 
		setContentView(R.layout.activity_camera_record);

		parentId = getIntent().getIntExtra("parentId", 0);
		timer = (TextView) findViewById(R.id.arc_hf_video_timer);
		mVideoStartBtn = (ImageButton) findViewById(R.id.arc_hf_video_start);
		tv_compt = (TextView) findViewById(R.id.tv_compt);
		verification_pass = (ImageButton) findViewById(R.id.verification_pass);
		mSurfaceview = (SurfaceView) this.findViewById(R.id.arc_hf_video_view);
		mSurfaceHolder = mSurfaceview.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		btnChange = (ImageButton) findViewById(R.id.ButtonChangeCamera);
		btnChange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeCameraFrontOrBack();
			}
		});

		// 设置计时器不可见
		timer.setVisibility(View.GONE);

		// 设置缓存路径
		mRecVedioPath = new File(Constant.IMAGEFILLPATH + "temp/");
		if (!mRecVedioPath.exists()) {
			mRecVedioPath.mkdirs();
		}

		mVideoStartBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isRecording = !isRecording;
				/*
				 * 点击开始录像
				 */
				if (isPreview) {
					camera.stopPreview();
					camera.release();
					camera = null;
				}
				second = 0;
				bool = true;
				if (mMediaRecorder == null)
					mMediaRecorder = new MediaRecorder();
				else {
					mMediaRecorder.reset();
				}

				mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
				if (bFrontCamera) {
					for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
						CameraInfo info = new CameraInfo();
						Camera.getCameraInfo(i, info);
						if (info.facing == CameraInfo.CAMERA_FACING_FRONT)// 这就是前置摄像头
						{
							if (bFrontCamera == true) {
								camera = Camera.open(i);
							}
							break;
						}
					}
				} else {
					camera = Camera.open();
				}
				setParameters(mSurfaceHolder);
				camera.unlock();
				mMediaRecorder.setCamera(camera);
				mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
				mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				mMediaRecorder
						.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

				mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
				mMediaRecorder
						.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				mMediaRecorder.setVideoSize(320, 240);
				mMediaRecorder.setVideoFrameRate(15);
				try {
					mRecAudioFile = File.createTempFile("Vedio", ".mp4",
							mRecVedioPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				mMediaRecorder.setOrientationHint(90);
				mMediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());
				try {
					mMediaRecorder.prepare();
					timer.setText("还剩" + (Constant.vedioTime - second) + "秒");
					timer.setVisibility(View.VISIBLE);
					handler.postDelayed(task, 1000);
					mMediaRecorder.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				showMsg("开始录制");
				mVideoStartBtn.setVisibility(View.INVISIBLE);
				tv_compt.setVisibility(View.INVISIBLE);
				verification_pass.setVisibility(View.INVISIBLE);
				btnChange.setVisibility(View.INVISIBLE);
			}
		});

		verification_pass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pd.setMessage(CameraRecordActivity.this.getResources()
						.getString(R.string.fyi_loading));
				pd.show();
				new VerificationPassTask().execute();
			}
		});
	}

	class VerificationPassTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(CameraRecordActivity.this).uploadVideo();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			pd.dismiss();
			if (!result) {
				if (Constant.isSessionExpire) {
					Toast.makeText(CameraRecordActivity.this,
							R.string.session_expire, Toast.LENGTH_SHORT).show();
					Intent itUpdate = new Intent(Constant.RECEIVER_SESSION_EXPIRE);
					sendBroadcast(itUpdate);
					Intent it = new Intent(CameraRecordActivity.this,
							LoginActivity.class);
					startActivity(it);
					finish();
					return;
				} else {
					Toast.makeText(CameraRecordActivity.this,
							R.string.video_save_fail, Toast.LENGTH_SHORT)
							.show();
					return;
				}
			}
			super.onPostExecute(result);
			Toast.makeText(CameraRecordActivity.this,
					R.string.video_save_success, Toast.LENGTH_SHORT).show();
			Intent itUpdate = new Intent(Constant.RECEIVER_VERIFICATION);
			sendBroadcast(itUpdate);
			finish();
		}
	}

	/*
	 * 消息提示
	 */
	private Toast toast;

	public void showMsg(String arg) {
		if (toast == null) {
			toast = Toast.makeText(this, arg, Toast.LENGTH_SHORT);
		} else {
			toast.cancel();
			toast.setText(arg);
		}
		toast.show();
	}

	/**
	 * 设置相机参数
	 */
	private void setParameters(SurfaceHolder holder) {
		// by xinger
		Camera.Parameters parameters = camera.getParameters();

		// 获得相机参数对象
		parameters.set("orientation", "landscape");

		// 设置格式
		parameters.setPictureFormat(PixelFormat.JPEG);
		String string;
		string = parameters.get("picture-size-values");
		Log.i("picture-size01", string);
		String pictureSize[] = string.split(",");
		String pictureSize01 = pictureSize[2];
		String pictureSize02[] = pictureSize01.split("x");
		String pWidth = pictureSize02[0];
		String pHeight = pictureSize02[1];

		mSupportedPreviewSizes = camera.getParameters()
				.getSupportedPreviewSizes();

		WindowManager wm = (WindowManager) CameraRecordActivity.this
				.getSystemService(Context.WINDOW_SERVICE);

		int screenWidth = wm.getDefaultDisplay().getWidth();// 屏幕宽度
		int screenHeight = wm.getDefaultDisplay().getHeight();// 屏幕高度

		if (mSupportedPreviewSizes != null) {
			/*mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes,
					screenHeight, screenWidth);*/
			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes,
					screenWidth, screenHeight);
		}
		parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		// 设置图片保存时的分辨率大小
		parameters.setPictureSize(Integer.valueOf(pWidth),
				Integer.valueOf(pHeight));
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// by xinger
		List<String> focusModes = parameters.getSupportedFocusModes();
		if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		}
		camera.setDisplayOrientation(getDefaultDegrees());
		camera.setParameters(parameters);
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;
		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;
		int targetHeight = h;
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	// 对前置或者后置相机进行互换 hupan add new function
	public void changeCameraFrontOrBack() {
		if (true == bFrontCamera) {
			bFrontCamera = false;
		} else {
			bFrontCamera = true;
		}

		surfaceDestroyed(mSurfaceHolder);
		surfaceCreated(mSurfaceHolder);
		surfaceChanged(mSurfaceHolder, 0, 0, 0);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (camera != null && bIfPreview) {
			camera.stopPreview();
			bIfPreview = false;
			camera.release();
			camera = null;
		}
	}

	// 设置相机的打开模式: 前置或者后置相机 hupan add function
	public void surfaceCreated(SurfaceHolder holder) {
		if (!bIfPreview) {
			/* 若相机非在预览模式,则打开相机 */
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
					CameraInfo info = new CameraInfo();
					Camera.getCameraInfo(i, info);
					if (info.facing == CameraInfo.CAMERA_FACING_FRONT)// 这就是前置摄像头
					{
						if (bFrontCamera == true) {
							camera = Camera.open(i);
						}
						break;
					}
				}
			}
			if (null == camera) {
				camera = Camera.open();
				bFrontCamera = false;
			}
			bIfPreview = true;
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (mPreviewRunning) {
			camera.stopPreview();
		}

		setParameters(holder);

		try {
			camera.setPreviewDisplay(holder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		camera.startPreview();
		mPreviewRunning = true;
	}

	// 获取默认的旋转角度 hupan add new function
	public int getDefaultDegrees() {
		CameraInfo info = null;

		info = new CameraInfo();
		if (bFrontCamera) {
			Camera.getCameraInfo(CameraInfo.CAMERA_FACING_FRONT, info);
		} else {
			Camera.getCameraInfo(CameraInfo.CAMERA_FACING_BACK, info);
		}

		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		int degress = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degress = 0;
			break;
		case Surface.ROTATION_90:
			degress = 90;
			break;
		case Surface.ROTATION_180:
			degress = 180;
			break;
		case Surface.ROTATION_270:
			degress = 270;
			break;
		}

		int result = 0;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degress) % 360;
			result = (360 - result) % 360;
		} else {
			// back-facing
			result = (info.orientation - degress + 360) % 360;
		}

		return result;
	}

	/*
	 * 生成video文件名字
	 */
	protected void videoRename() {
		String path = Constant.IMAGEFILLPATH + String.valueOf(parentId) + "/";
		String fileName = "video.mp4";
		Constant.videoPath = path + fileName;
		File out = new File(path);
		if (!out.exists()) {
			out.mkdirs();
		}
		out = new File(path, fileName);
		if (mRecAudioFile.exists())
			mRecAudioFile.renameTo(out);
	}

	/*
	 * 定时器设置，实现计时
	 */
	private Handler handler = new Handler();
	private Runnable task = new Runnable() {
		public void run() {
			if (bool) {
				handler.postDelayed(this, 1000);
				second++;
				if (second >= Constant.vedioTime) {
					timer.setVisibility(View.GONE);
					/*
					 * 点击停止
					 */
					try {
						bool = false;
						mMediaRecorder.stop();
						timer.setVisibility(View.GONE);
						mMediaRecorder.release();
						mMediaRecorder = null;
						videoRename();
					} catch (Exception e) {
						e.printStackTrace();
					}
					isRecording = !isRecording;
					if (isFirst) {
						isFirst = false;
					}
					mVideoStartBtn.setVisibility(View.INVISIBLE);
					tv_compt.setVisibility(View.VISIBLE);
					verification_pass.setVisibility(View.INVISIBLE);
					verification_pass.setClickable(true);
					btnChange.setVisibility(View.INVISIBLE);
					showMsg("录制完成，已保存");

					try {
						camera.setPreviewDisplay(mSurfaceHolder);
						camera.startPreview();
						isPreview = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					timer.setText("还剩" + (Constant.vedioTime - second) + "秒");
				}
			}
		}
	};

	/*
	 * 格式化时间
	 */
	public String format(int i) {
		String s = i + "";
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}

	/*
	 * 覆写返回键监听
	 */
	@Override
	public void onBackPressed() {
		if (mMediaRecorder != null) {
			mMediaRecorder.reset();
			/* mMediaRecorder.stop(); */
			mMediaRecorder.release();
			mMediaRecorder = null;
			videoRename();
		}
		mSensorManager.unregisterListener(this);
		finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(this);
		super.onPause();
		onBackPressed();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (isRecording) {
			int sensorType = event.sensor.getType();
			float[] values = event.values;

			float x = values[0];

			if (sensorType == Sensor.TYPE_ACCELEROMETER) {

				if (Math.abs(x) > 6.0) {
					mVideoStartBtn.setVisibility(View.VISIBLE);
					tv_compt.setVisibility(View.INVISIBLE);
					if (!isFirst) {
						verification_pass.setVisibility(View.VISIBLE);
					}
					btnChange.setVisibility(View.VISIBLE);
				} else {
					mVideoStartBtn.setVisibility(View.INVISIBLE);
					tv_compt.setVisibility(View.VISIBLE);
					verification_pass.setVisibility(View.INVISIBLE);
					btnChange.setVisibility(View.INVISIBLE);
				}
			}
		}		
	}
}
