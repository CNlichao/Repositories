package com.hrcx.selfphotovideocompare.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.hrcx.selfphotovideocompare.R;
import com.hrcx.selfphotovideocompare.utils.Constant;
import com.hrcx.selfphotovideocompare.utils.UnitTransfer;

public class RegisterShowPhotoActivity extends BasicActivity implements
		OnClickListener {

	private ImageView iv_cur_picture;
	private Gallery mGallery;
	private Button btn_recamera;
	private Button btn_next;
	private String imageFilePath;
	private BabyAdapter bAdapter;
	private List<Bitmap> bitList = new ArrayList<Bitmap>();
	private Bitmap bitmap = null;
	private int selectItem = 0;
	private ImageView ph_left_dir;
	private ImageView ph_right_dir;
	private TextView tv_cur_photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register_show_photo);
		findView();
	}



	private void findView() {
		Intent intent = getIntent();
		imageFilePath = intent.getExtras().getString("curenImageUrl");
		tv_cur_photo = (TextView) findViewById(R.id.tv_cur_photo);
		BigDecimal b = new BigDecimal(Constant.matchRatio * 100); 
		float f = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		tv_cur_photo.setText("本次上传的照片匹配率：" + f 
				+ "%");
		iv_cur_picture = (ImageView) findViewById(R.id.iv_cur_picture);
		if (imageFilePath != null && !imageFilePath.equals("")) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(imageFilePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			iv_cur_picture.setImageBitmap(BitmapFactory.decodeStream(fis));
		}
		FileInputStream fis = null;

		if (Constant.matchRatio >= (float)0.9) {
			switch (Constant.pictureNum) {
			case 0:
				try {
					fis = new FileInputStream(Constant.IMAGEFILLPATH
							+ "tempPhoto0.jpg");
					bitmap = BitmapFactory.decodeStream(fis);
					bitList.add(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case 1:
				try {
					fis = new FileInputStream(Constant.IMAGEFILLPATH
							+ "tempPhoto1.jpg");
					bitmap = BitmapFactory.decodeStream(fis);
					bitList.add(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try {
					fis = new FileInputStream(Constant.IMAGEFILLPATH
							+ "tempPhoto2.jpg");
					bitmap = BitmapFactory.decodeStream(fis);
					bitList.add(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				try {
					fis = new FileInputStream(Constant.IMAGEFILLPATH
							+ "tempPhoto3.jpg");
					bitmap = BitmapFactory.decodeStream(fis);
					bitList.add(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;
			}
		} else {
			switch (Constant.pictureNum) {
			case 2:
				try {
					fis = new FileInputStream(Constant.IMAGEFILLPATH
							+ "tempPhoto2.jpg");
					bitmap = BitmapFactory.decodeStream(fis);
					bitList.add(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			case 1:
				try {
					fis = new FileInputStream(Constant.IMAGEFILLPATH
							+ "tempPhoto1.jpg");
					bitmap = BitmapFactory.decodeStream(fis);
					bitList.add(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			case 0:
				try {
					fis = new FileInputStream(Constant.IMAGEFILLPATH
							+ "tempPhoto0.jpg");
					bitmap = BitmapFactory.decodeStream(fis);
					bitList.add(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				try {
					fis = new FileInputStream(Constant.IMAGEFILLPATH
							+ "tempPhoto3.jpg");
					bitmap = BitmapFactory.decodeStream(fis);
					bitList.add(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					fis = new FileInputStream(Constant.IMAGEFILLPATH
							+ "tempPhoto2.jpg");
					bitmap = BitmapFactory.decodeStream(fis);
					bitList.add(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					fis = new FileInputStream(Constant.IMAGEFILLPATH
							+ "tempPhoto1.jpg");
					bitmap = BitmapFactory.decodeStream(fis);
					bitList.add(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;
			}
		}

		btn_recamera = (Button) findViewById(R.id.btn_recamera);
		btn_recamera.setOnClickListener(this);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		mGallery = (Gallery) findViewById(R.id.ph_gallery);
		bAdapter = new BabyAdapter(this);
		mGallery.setAdapter(bAdapter);
		mGallery.setSelection(0);
		bAdapter.setSelectItem(0);

		mGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mGallery.setSelection(position);
				bAdapter.setSelectItem(position);
			}
		});

		mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				mGallery.setSelection(position);
				bAdapter.setSelectItem(position);
				if (RegisterShowPhotoActivity.this.selectItem <= 0) {
					ph_left_dir.setBackgroundResource(R.drawable.btn_left_over);
				} else {
					ph_left_dir
							.setBackgroundResource(R.drawable.btn_left_normal);
				}
				if (RegisterShowPhotoActivity.this.selectItem >= bitList.size() - 1) {
					ph_right_dir
							.setBackgroundResource(R.drawable.btn_right_over);
				} else {
					ph_right_dir
							.setBackgroundResource(R.drawable.btn_right_normal);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		ph_left_dir = (ImageView) findViewById(R.id.ph_left_dir);
		if (RegisterShowPhotoActivity.this.selectItem <= 0) {
			ph_left_dir.setBackgroundResource(R.drawable.btn_left_over);
		} else {
			ph_left_dir.setBackgroundResource(R.drawable.btn_left_normal);
		}
		ph_left_dir.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (RegisterShowPhotoActivity.this.selectItem > 0) {
					RegisterShowPhotoActivity.this.selectItem--;
				} else {
					RegisterShowPhotoActivity.this.selectItem = 0;
				}
				mGallery.setSelection(RegisterShowPhotoActivity.this.selectItem);
				bAdapter.setSelectItem(RegisterShowPhotoActivity.this.selectItem);
				bAdapter.notifyDataSetChanged();
				if (RegisterShowPhotoActivity.this.selectItem <= 0) {
					ph_left_dir.setBackgroundResource(R.drawable.btn_left_over);
				} else {
					ph_left_dir
							.setBackgroundResource(R.drawable.btn_left_normal);
				}
				if (RegisterShowPhotoActivity.this.selectItem >= bitList.size() - 1) {
					ph_right_dir
							.setBackgroundResource(R.drawable.btn_right_over);
				} else {
					ph_right_dir
							.setBackgroundResource(R.drawable.btn_right_normal);
				}
			}
		});
		ph_right_dir = (ImageView) findViewById(R.id.ph_right_dir);
		if (RegisterShowPhotoActivity.this.selectItem >= bitList.size() - 1) {
			ph_right_dir.setBackgroundResource(R.drawable.btn_right_over);
		} else {
			ph_right_dir.setBackgroundResource(R.drawable.btn_right_normal);
		}
		ph_right_dir.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (RegisterShowPhotoActivity.this.selectItem < bitList.size() - 1) {
					RegisterShowPhotoActivity.this.selectItem++;
				} else {
					RegisterShowPhotoActivity.this.selectItem = bitList.size() - 1;
				}
				mGallery.setSelection(RegisterShowPhotoActivity.this.selectItem);
				bAdapter.setSelectItem(RegisterShowPhotoActivity.this.selectItem);
				bAdapter.notifyDataSetChanged();
				if (RegisterShowPhotoActivity.this.selectItem <= 0) {
					ph_left_dir.setBackgroundResource(R.drawable.btn_left_over);
				} else {
					ph_left_dir
							.setBackgroundResource(R.drawable.btn_left_normal);
				}
				if (RegisterShowPhotoActivity.this.selectItem >= bitList.size() - 1) {
					ph_right_dir
							.setBackgroundResource(R.drawable.btn_right_over);
				} else {
					ph_right_dir
							.setBackgroundResource(R.drawable.btn_right_normal);
				}
			}
		});
	}

	class BabyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public BabyAdapter(Context con) {
			mInflater = LayoutInflater.from(con);
		}

		@Override
		public int getCount() {
			return bitList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void setSelectItem(int selectItem) {
			if (RegisterShowPhotoActivity.this.selectItem != selectItem) {
				RegisterShowPhotoActivity.this.selectItem = selectItem;
				notifyDataSetChanged();
			}
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (view == null) {
				view = mInflater.inflate(R.layout.item_photo, null);
			}
			ImageView imgHead = (ImageView) view.findViewById(R.id.iv_photo);
			imgHead.setImageBitmap(bitList.get(position));

			if (selectItem == position) {
				int w = UnitTransfer.dip2px(RegisterShowPhotoActivity.this, 300);
				view.setLayoutParams(new Gallery.LayoutParams(w, w));
			} else {
				int w = UnitTransfer.dip2px(RegisterShowPhotoActivity.this, 250);
				view.setLayoutParams(new Gallery.LayoutParams(w, w));
			}
			return view;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_recamera:
			intent = new Intent(RegisterShowPhotoActivity.this, RegisterTakePhoto.class);
			startActivity(intent);
			finish();
			break;
		case R.id.btn_next:
			if (Constant.matchRatio >= (float)0.9) {
				intent = new Intent(RegisterShowPhotoActivity.this,
						RegisterInfo.class);
				startActivity(intent);
				finish();
			} else {
				// 弹出对话框
				Dialog dialog = new AlertDialog.Builder(
						RegisterShowPhotoActivity.this)
						.setMessage("本次拍照匹配率低于90%，是否确认通过并上传？")
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										Intent intent = new Intent(
												RegisterShowPhotoActivity.this,
												RegisterInfo.class);
										startActivity(intent);
										finish();
									}
								})
						.setNegativeButton("否",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								})
						.setNeutralButton("重拍",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										Intent intent = new Intent(
												RegisterShowPhotoActivity.this,
												RegisterTakePhoto.class);
										startActivity(intent);
										finish();
									}
								}).create();
				dialog.show();
			}
			break;
		default:
			break;
		}
	}
}
