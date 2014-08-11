package com.hrcx.photovideocompare.ui;
import com.hrcx.photovideocompare.R;
import com.hrcx.photovideocompare.dao.net.NetProtocol;
import com.hrcx.photovideocompare.ui.TotalShowActivity.UpdateActivity;
import com.hrcx.photovideocompare.ui.TotalShowActivity.UpdateBroadcast;
import com.hrcx.photovideocompare.utils.Constant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class TypeSelectActivity extends BasicActivity implements
		OnClickListener {

	/*private Button btn_type_1;*/
	private GridView gv_product;
	private ProductAdapter productAdapter;
	private int productImageId[] = { R.drawable.icon_a01, R.drawable.icon_a02,
			R.drawable.icon_a03, R.drawable.icon_a04 };
	
	private UpdateBroadcast updateReceiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_type_select);
		registerUpdate();
		findView();
	}
	
	private void registerUpdate() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.RECEIVER_VERIFICATION);
		filter.addAction(Constant.RECEIVER_SESSION_EXPIRE);
		updateReceiver = new UpdateBroadcast();
		registerReceiver(updateReceiver, filter);
	}
	
	class UpdateBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent == null) {
				return;
			}
			String action = intent.getAction();
			if(Constant.RECEIVER_SESSION_EXPIRE.equals(action)) {
				finish();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (updateReceiver != null) {
			unregisterReceiver(updateReceiver);
		}
	}

	private void findView() {
		/*btn_type_1 = (Button) findViewById(R.id.btn_type_1);
		btn_type_1.setOnClickListener(this);*/

		/*RelativeLayout layout = (RelativeLayout) findViewById(R.id.rl_button);
		
		Button but = new Button(this);
		but.setText(Constant.buttonName[0]);
		but.setId(0 + 1);
		but.setOnClickListener(this);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		layout.addView(but, lp);
		
		for (int i = 1; i < Constant.buttonName.length; i++) {
			but = new Button(this);
			but.setText(Constant.buttonName[i]);
			but.setId(i + 1);
			but.setOnClickListener(this);
			lp = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.BELOW, i);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
			lp.setMargins(0, UnitTransfer.px2dip(this, 10), 0, 0);
			layout.addView(but, lp);
		}*/
		
		gv_product = (GridView) findViewById(R.id.gv_product);
		productAdapter = new ProductAdapter(this);
		gv_product.setAdapter(productAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*case R.id.btn_type_1:
			pd.setMessage(this.getResources().getString(R.string.fyi_loading));
			pd.show();
			new ActivityTask2().execute();
			break;
		case 3:
			pd.setMessage(this.getResources().getString(R.string.fyi_loading));
			pd.show();
			new ActivityTask2().execute();
			break;*/
		default:
			break;
		}
	}
	
	class ProductAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public ProductAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			if (productImageId != null) {
				return productImageId.length;
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.item_product, null);
			ImageView iv_icon = (ImageView) convertView
					.findViewById(R.id.iv_icon);
			iv_icon.setImageResource(productImageId[position]);
			if (position == 2) {
				iv_icon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pd.setMessage(TypeSelectActivity.this.getResources().getString(R.string.fyi_loading));
						pd.show();
						new ActivityTask2().execute();
					}
				});
			}
			
			TextView tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			tv_name.setText(Constant.buttonName[position]);
			return convertView;
		}

	}

	class ActivityTask2 extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			/*new NetProtocol(TypeSelectActivity.this)
					.syncVerificationData(Constant.verificationCurPage + "");*/
			return new NetProtocol(TypeSelectActivity.this)
					.syncNoVerificationData(Constant.noVerificationCurPage + "");
			/*return new NetProtocol(TypeSelectActivity.this)
					.syncTotalData(Constant.totalCurPage + "");*/
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				pd.dismiss();
				return;
			}
			super.onPostExecute(result);
			pd.dismiss();
			Intent it = new Intent(TypeSelectActivity.this,
					MainTabActivity.class);
			startActivity(it);
		}
	}
}
