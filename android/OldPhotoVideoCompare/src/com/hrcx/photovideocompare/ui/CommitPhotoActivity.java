package com.hrcx.photovideocompare.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hrcx.photovideocompare.R;
import com.hrcx.photovideocompare.dao.net.NetProtocol;
import com.hrcx.photovideocompare.utils.Constant;

public class CommitPhotoActivity extends BasicActivity implements
		OnClickListener {

	private ImageView iv_commit_photo;
	private Button btn_commit;
	private Button btn_recamera;
	private String imageFilePath;
	private String sfzh;
	private String validation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_commit_photo);
		findView();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void findView() {
		Intent intent = getIntent();
		imageFilePath = intent.getExtras().getString("curenImageUrl");
		sfzh = intent.getExtras().getString("sfzh");
		validation = intent.getExtras().getString("validation");
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
			new CommitTask().execute(sfzh, validation);
			break;
		case R.id.btn_recamera:
			Intent it = new Intent(CommitPhotoActivity.this, CameraImage.class);
			it.putExtra("sfzh", sfzh);
			it.putExtra("validation", validation);
			startActivity(it);
			finish();
			break;
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
			return new NetProtocol(CommitPhotoActivity.this).photoValidation(
					params[0], getImageFile(imageFilePath), params[1]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			pd.dismiss();
			if (!result) {
				if (Constant.isSessionExpire) {
					Toast.makeText(CommitPhotoActivity.this, R.string.session_expire,
							Toast.LENGTH_SHORT).show();
					Intent itUpdate = new Intent(Constant.RECEIVER_SESSION_EXPIRE);
					sendBroadcast(itUpdate);
					Intent it = new Intent(CommitPhotoActivity.this,
							LoginActivity.class);
					startActivity(it);
					finish();
					return;
				} else {
					Toast.makeText(CommitPhotoActivity.this, R.string.commit_fail,
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
			super.onPostExecute(result);
			Toast.makeText(CommitPhotoActivity.this, R.string.commit_success,
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(CommitPhotoActivity.this, WaitingActivity.class);
			intent.putExtra("curenImageUrl",imageFilePath);
			intent.putExtra("sfzh", sfzh);
			intent.putExtra("validation", validation);
			startActivity(intent);
			finish();
		}
	}
}
