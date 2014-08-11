package com.hrcx.selfphotovideocompare.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hrcx.selfphotovideocompare.R;
import com.hrcx.selfphotovideocompare.dao.net.NetProtocol;
import com.hrcx.selfphotovideocompare.utils.Constant;

public class RegisterCommitPhoto extends BasicActivity implements
		OnClickListener {

	private ImageView iv_commit_photo;
	private Button btn_commit;
	private Button btn_recamera;
	private String imageFilePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register_commit_photo);
		findView();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}


	private void findView() {
		Intent intent = getIntent();
		imageFilePath = intent.getExtras().getString("curenImageUrl");
		iv_commit_photo = (ImageView) findViewById(R.id.iv_commit_photo);
		if (imageFilePath != null && !imageFilePath.equals("")) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(imageFilePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			iv_commit_photo.setImageBitmap(BitmapFactory.decodeStream(fis));
		}
		btn_commit = (Button) findViewById(R.id.btn_commit);
		btn_commit.setOnClickListener(this);
		btn_recamera = (Button) findViewById(R.id.btn_recamera);
		btn_recamera.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit:
			pd.setMessage(this.getResources().getString(
					R.string.fyi_loading));
			pd.show();
			new CommitTask().execute(Constant.registerid);
			break;
		case R.id.btn_recamera:
			finish();
			break;
/*		case R.id.btn_recamera:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
	        
			intent.putExtra("camerasensortype", 2); // 调用前置摄像头  
			intent.putExtra("autofocus", true); // 自动对焦  
			intent.putExtra("fullScreen", false); // 全屏  
			intent.putExtra("showActionIcons", false);  
			startActivityForResult(intent, 2); 
			break;*/
		default:
			break;
		}
	}

	public File getImageFile(String iconName) {
		if (iconName != null && iconName.length() == 0)
			return null;
		File file = new File(iconName);
		if (!file.exists()) {
			file = null;
		}
		return file;
	}

	class CommitTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return new NetProtocol(RegisterCommitPhoto.this).selfPhotoValidation(
					params[0], getImageFile(imageFilePath));
		}

		@Override
		protected void onPostExecute(Boolean result) {
			pd.dismiss();
			super.onPostExecute(result);
			if (!result) {
					Toast.makeText(RegisterCommitPhoto.this, "照片验证不通过请重新拍摄",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(RegisterCommitPhoto.this, RegisterTakePhoto.class);
					startActivity(intent);
					finish();
			}
			else{
				Toast.makeText(RegisterCommitPhoto.this, "照片审查中",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(RegisterCommitPhoto.this, RegisterWaitingActivity.class);
				startActivityForResult(intent, 1);
			}
			
		}
	}
}
