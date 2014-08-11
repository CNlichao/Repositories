package com.docdeal.activity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.kobjects.base64.Base64;

import com.docdeal.R;
import com.docdeal.webservice.WebService;
import com.global.Base64Util;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class FileJpgView extends Activity implements OnTouchListener {

	private String methodName = "getFJFileBase64Content";
	private String returnProperty = "getFJFileBase64ContentReturn";
	private LinkedHashMap<String, String> paraMap = new LinkedHashMap<String, String>();
	private WebService webService;
	private float winWidth;
	private float winHeight;
	private ImageView imageview;
	private List<Bitmap> fileList = new LinkedList<Bitmap>();
	private String docId;
	private String title;
	private int pageNum = 1;
	private String totalPage;
	private String content;
	private Map<String, String> map = new HashMap<String, String>();
	private ProgressDialog progress;
	// 原始图片和缩放位置
	private Bitmap originalMap;
	private Matrix originalMatrix;

	// 放大缩小
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist;

	// 模式
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		setContentView(R.layout.filejpgview);
		progress = ProgressDialog.show(this, "请稍等", "数据访问中", false, false);
		initView();
		// guestureDetector = new GestureDetector(this, gestureListener);
		initData();
		loadDate(docId, title, pageNum);

	}

	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	private void initData() {

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        winWidth = metric.widthPixels;     // 屏幕宽度（像素）
        winHeight = metric.heightPixels;   // 屏幕高度（像素）

        
		docId = this.getIntent().getStringExtra("docId");
		title = this.getIntent().getStringExtra("title");
		pageNum = this.getIntent().getIntExtra("pageNum", pageNum);

		paraMap.put("docId", docId);
		paraMap.put("title", title);
		webService = new WebService(methodName, returnProperty);
	}

	private boolean nextPage() {
		if (pageNum == Integer.valueOf(totalPage)) {
			Toast.makeText(this, "没有更多内容显示", Toast.LENGTH_SHORT).show();
			return false;
		}
		Intent intent = new Intent(this, FileJpgView.class);
		intent.putExtra("docId", docId);
		intent.putExtra("title", title);
		intent.putExtra("pageNum", ++pageNum);
		startActivity(intent);
		finish();
		// loadDate(docId, title, ++pageNum);
		return true;
	}

	private boolean proPage() {
		if (pageNum == 1) {
			Toast.makeText(this, "没有更多内容显示", Toast.LENGTH_SHORT).show();
			return false;
		}
		Intent intent = new Intent(this, FileJpgView.class);
		intent.putExtra("docId", docId);
		intent.putExtra("title", title);
		intent.putExtra("pageNum", --pageNum);
		startActivity(intent);
		finish();
		// loadDate(docId, title, --pageNum);
		return true;
	}

	/**
	 * 加载图片数据
	 * 
	 * @param docId
	 * @param fileName
	 * @param i
	 */
	private void loadDate(String docId, String fileName, int i) {
		paraMap.put("pageNum", Integer.valueOf(i).toString());
		webService.setParams(paraMap);
		String data = webService.getData();
		paserXml(data);
		Toast.makeText(this, pageNum+"/"+totalPage+"页", Toast.LENGTH_SHORT).show();
		Base64.decode(content);
		String file = Base64Util.decoderBase64FileWithFileName(content,
				fileName.substring(0, fileName.lastIndexOf(".")) + ".jpg");

		originalMap = BitmapFactory.decodeFile(file);
		if (originalMap == null) {
			return;
		}
		int w = originalMap.getWidth();
		int h = originalMap.getHeight();
		float scaleWidth = winWidth / w;
		float scaleHeight = winHeight / h;
		
		originalMatrix = new Matrix();
		imageview.setImageMatrix(new Matrix());
		originalMatrix.postScale(scaleWidth, scaleHeight);
		// Bitmap newBitmap = Bitmap.createBitmap(originalMap, 0, 0,
		// originalMap.getWidth(), originalMap.getHeight(),
		// originalMatrix, true);
		if (progress != null) {
			progress.dismiss();
		}
		imageview.setImageBitmap(originalMap);
		//imageview.setScaleType(ScaleType.FIT_XY);
		imageview.setImageMatrix(originalMatrix);
		fileList.add(originalMap);

	}

	private void paserXml(String data) {
		try {
			Document doc = DocumentHelper.parseText(data);
			totalPage = doc.selectSingleNode("/root/总页数").getText();
			// totalPage = doc.selectSingleNode("/root/当前页数").getText();
			content = doc.selectSingleNode("/root/内容").getText();
			// Element el = doc.getRootElement();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initView() {
		imageview = (ImageView) findViewById(R.id.filePage);
		imageview.setOnTouchListener(this);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView myImageView = (ImageView) v;
		//Log.e("View",v.getClass().getSimpleName());
		//Log.e("Action",Integer.toString(event.getAction()));
		//Log.e("trueAction",Integer.toString(event.getAction() & MotionEvent.ACTION_MASK));
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 设置拖拉模式
		case MotionEvent.ACTION_DOWN:
			matrix.set(myImageView.getImageMatrix());
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_UP:
			if (mode == DRAG) {
				float xMove = event.getX() - start.x;
				float eventTime = event.getEventTime() - event.getDownTime();
				boolean result=true;
				//获取imageview中图片的真实尺寸
				float imageHeight,imageWidth;
				float[] values=new float[10];
				imageview.getImageMatrix().getValues(values);
				imageHeight=imageview.getDrawable().getBounds().height();
				imageWidth=imageview.getDrawable().getBounds().width();
				float realImageHeight,realImageWidth;
				realImageHeight=imageHeight*values[0];
				realImageWidth=imageWidth*values[4];
				//Log.e("imageview", imageview.getLeft()+":"+imageview.getRight()+":"+realImageHeight+":"+realImageWidth);
				//如果图片放大倍数小于1.3则认定为翻页动作，否则认定为图片拖动操作
				if(realImageWidth<=1.3*winWidth){
				if (xMove / eventTime * 1000 < -winWidth / 2) {
					result=nextPage();		
					if(!result){
						matrix.postTranslate(start.x-event.getX() , start.y-event.getY());
					}
				}
				else if (xMove / eventTime * 1000 > winWidth / 2) {
					result=proPage();
					if(!result){
						matrix.postTranslate(start.x-event.getX() , start.y-event.getY());
					}
				}
			}
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		// 设置多点触摸模式
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		// 若为DRAG模式，则点击移动图片
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y);
			}
			// 若为ZOOM模式，则点击触摸缩放
			else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);

					float scale = newDist / oldDist;
					// 设置硕放比例和图片的中点位置
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}
		myImageView.setImageMatrix(matrix);
		return true;
	}

	// 计算移动距离
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	// 计算中点位置
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

}
