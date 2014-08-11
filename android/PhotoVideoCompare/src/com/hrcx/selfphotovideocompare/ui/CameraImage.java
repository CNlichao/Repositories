package com.hrcx.selfphotovideocompare.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.hrcx.selfphotovideocompare.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class CameraImage<_camera> extends Activity implements Callback {

	private ImageButton btnChange;
	private boolean bFrontCamera = false;

	private ImageButton btnStart;
	// 重新拍照
	private ImageButton btnCamerReset;
	// 上传
	private ImageButton btnUpload;

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private Camera mCamera;
	private String curenFilePath = "";
	private boolean run = false;
	private boolean mPreviewRunning;

	/* SurfaceView对象作为Layout上的输出接口 */
	/* 以SurfaceHolder来控制SurfaceView */

	private boolean bIfPreview = false;
	private String currentImage = "1.jpg";
	static int count;
	private File imageFile;
	private ImageView ivFocus;
	private ProgressDialog progressDialog;
	private static boolean flash = false;
	private final String IMAGEFILLPATH = Environment
			.getExternalStorageDirectory() + "/" + "selfphotovideocompare/";

	private Size mPreviewSize;
	private List<Size> mSupportedPreviewSizes;

//	private String sfzh;
//	private String validation;

	private Handler handler = new Handler(); // Handler实现异步消息处理
	private Runnable task = new Runnable() {
		public void run() { // TODO Auto-generated method stub
			if (run) {
				// handler.postDelayed(task,3000);
				// 设置闪光灯
				Camera.Parameters parameters = mCamera.getParameters();

				if (flash) {
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
				} else {
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				}

				mCamera.setParameters(parameters);

				mCamera.autoFocus(new AutoFocusCallback() {

					public void onAutoFocus(boolean success, Camera camera) {
						if (success) {
							ivFocus.setImageResource(R.drawable.focus2);
							mCamera.takePicture(shutter2, null, jpegCallback1);
						}
					}
				});
				// 拍照
				/*mCamera.takePicture(shutter2, null, jpegCallback1);*/
			}
		}
	};

	private PictureCallback jpegCallback1 = new PictureCallback() {
		public void onPictureTaken(byte[] _data, Camera _camera) {
			imageFile = new File(IMAGEFILLPATH);
			if (!imageFile.exists()) {
				imageFile.mkdirs();
			}
			final BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
			sizeOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(_data, 0, _data.length, sizeOptions);
			final float widthSampling = 2;
			sizeOptions.inJustDecodeBounds = false;
			sizeOptions.inSampleSize = (int) widthSampling;
			Bitmap bm = BitmapFactory.decodeByteArray(_data, 0, _data.length,
					sizeOptions);
			Matrix matrix = new Matrix();
			if (bFrontCamera) {
				matrix.postRotate((float) -90.0);
			} else {
				matrix.postRotate((float) 90.0);
			}
			bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
					matrix, true);
			try {
				/*SimpleDateFormat TimeFmt = new SimpleDateFormat(
						"yyyy-MM-dd-HH-mm-ss");
				TimeFmt.format(new Date());*/
				/*currentImage = TimeFmt.format(new Date()) + ".jpg";*/
				currentImage = "camera.jpg";
				curenFilePath = IMAGEFILLPATH + currentImage;
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(curenFilePath));
				/* bm = Bitmap.createScaledBitmap(bm, 600, 800, true); */
				bm = Bitmap.createScaledBitmap(bm, 300, 400, true);
				bm.compress(Bitmap.CompressFormat.JPEG, 93, bos);
				bos.flush();
				bos.close();
				/*Thread.sleep(500);*/
				/* Toast.makeText(CameraImage.this, curenFilePath, 3).show(); */
				/*btnUpload.setVisibility(View.VISIBLE);
				btnCamerReset.setVisibility(View.VISIBLE);*/
				upload();
			} catch (Exception e) {
				Log.e("camera", e.getMessage());
			}
		}
	};

	Camera.ShutterCallback shutter2 = new Camera.ShutterCallback() {
		public void onShutter() {
		}
	};

	/**
	 * Called when the activity is first created.
	 * 
	 * @para
	 */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
		setContentView(R.layout.activity_camera);
		System.out.println("cameraImage onCreate.........");

		Intent intent = this.getIntent();
	//	sfzh = intent.getExtras().getString("sfzh");
		//validation = intent.getExtras().getString("validation");

		btnUpload = (ImageButton) findViewById(R.id.ButtonUpload);
		btnCamerReset = (ImageButton) findViewById(R.id.btnCamerReset);
		btnStart = (ImageButton) findViewById(R.id.ButtonStart);
		btnChange = (ImageButton) findViewById(R.id.ButtonChangeCamera);

		mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		progressDialog = new ProgressDialog(CameraImage.this);
		// 构建进度条
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
		progressDialog.setTitle("提示");// 设置标题
		progressDialog.setIndeterminate(false);// 设置进度条是否为不明确
		progressDialog.setCancelable(true);// 设置进度条是否可以按退回键取消

		initAction();
	}

	private void initAction() {
		btnUpload.setOnClickListener(clickListener);
		btnCamerReset.setOnClickListener(clickListener);
		btnStart.setOnClickListener(clickListener);
		btnChange.setOnClickListener(clickListener);
	}

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ButtonUpload:
				upload();
				break;
			case R.id.btnCamerReset:
				resetCamer();
				break;
			case R.id.ButtonStart:
				startCamer();
				break;
			/*
			 * case R.id.btnReset: reset(); break;
			 */
			case R.id.ButtonChangeCamera:
				changeCameraFrontOrBack();
				break;
			}
		}
	};

	// 返回
	public void reset() {
		run = false;
		updateButton();
		handler.removeCallbacks(task);
		CameraImage.this.finish();
	}

	// 拍照
	public void startCamer() {
		btnStart.setVisibility(View.GONE);
		run = true;
		updateButton();
		handler.postDelayed(task, 10);
	}

	// 重新拍摄
	public void resetCamer() {
		btnUpload.setVisibility(View.GONE);
		btnStart.setVisibility(View.VISIBLE);
		btnCamerReset.setVisibility(View.GONE);
		run = false;
		updateButton();
		mCamera.startPreview();
	}

	// 上传图片
	public void upload() {
		try {
			// 上传图片
			Intent dataIntent = new Intent(CameraImage.this,
					CommitPhotoActivity.class);
			dataIntent.putExtra("curenImageUrl", IMAGEFILLPATH + currentImage);
			/* dataIntent.putExtra("curenImageUrl",IMAGEFILLPATH + "123.jpg"); */
//			dataIntent.putExtra("sfzh", sfzh);
//			dataIntent.putExtra("validation", validation);
			startActivity(dataIntent);
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 返回事件
	@Override
	public void onBackPressed() {
		reset();
	}

	private void updateButton() {
		btnStart.setEnabled(!run);
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

	protected void setDisplayOrientation(Camera camera, int angle) {
		Method downPolymorphic;
		try {
			downPolymorphic = camera.getClass().getMethod(
					"setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null)
				downPolymorphic.invoke(camera, new Object[] { angle });
		} catch (Exception e1) {
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (mPreviewRunning) {
			mCamera.stopPreview();
		}
		Camera.Parameters parameters = mCamera.getParameters();

		// 获得相机参数对象
		parameters.set("orientation", "portrait");

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

		mSupportedPreviewSizes = mCamera.getParameters()
				.getSupportedPreviewSizes();

		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);

		int screenWidth = wm.getDefaultDisplay().getWidth();// 屏幕宽度
		int screenHeight = wm.getDefaultDisplay().getHeight();// 屏幕高度

		if (mSupportedPreviewSizes != null) {
			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes,
					screenHeight, screenWidth);
		}
		parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		// 设置图片保存时的分辨率大小
		parameters.setPictureSize(Integer.valueOf(pWidth),
				Integer.valueOf(pHeight));

		try {
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			// by xinger
			List<String> focusModes = parameters.getSupportedFocusModes();
			if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			}
			mCamera.setDisplayOrientation(getDefaultDegrees());
			mCamera.setParameters(parameters);

		} catch (Exception e) {
			System.out.println("1111 " + e.getMessage());
		}

		try {
			mCamera.setPreviewDisplay(holder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ivFocus = new ImageView(CameraImage.this);
		ivFocus.setImageResource(R.drawable.focus1);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		ivFocus.setScaleType(ScaleType.CENTER);
		addContentView(ivFocus, layoutParams);
		ivFocus.setVisibility(View.VISIBLE);
		mCamera.startPreview();
		mPreviewRunning = true;
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
							mCamera = Camera.open(i);
						}
						break;
					}
				}
			}
			if (null == mCamera) {
				mCamera = Camera.open();
				bFrontCamera = false;
			}
			bIfPreview = true;
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null && bIfPreview) {
			mCamera.stopPreview();
			bIfPreview = false;
			mCamera.release();
			mCamera = null;
		}
	}

	// 删除图片
	public boolean delete() {
		try {
			if (currentImage != null && !"".equals(currentImage)) {
				boolean isdelete = new File(IMAGEFILLPATH + currentImage)
						.delete();
				currentImage = "";
				return isdelete;
			}
		} catch (Exception e) {
		}
		return false;
	}
}
