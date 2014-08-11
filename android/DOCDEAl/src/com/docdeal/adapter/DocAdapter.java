package com.docdeal.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import com.docdeal.R;
import com.docdeal.adapterview.DocView;
import com.docdeal.bean.OfficialDocument;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class DocAdapter extends BaseAdapter {
	private List<OfficialDocument> docList;
	private LayoutInflater layoutInflater;
	Context context;

	public DocAdapter(Context context, LayoutInflater layoutInflater,
			List<OfficialDocument> docList) {
		this.context = context;
		this.layoutInflater = layoutInflater;
		this.docList = docList;
	}

	public void addItem(OfficialDocument doc) {
		docList.add(doc);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return docList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return docList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		DocView docv = null;
		if (view == null) {
			docv = new DocView();
			view = layoutInflater.inflate(R.layout.doclist_item, null);
			docv.docName = (TextView) view.findViewById(R.id.docname);
			docv.sendTime = (TextView) view.findViewById(R.id.docsendtime);
			view.setTag(docv);
		} else {
			docv = (DocView) view.getTag();
		}
		docv.docName.setText(Html.fromHtml(docList.get(position).getTitle()));

		//页面时间格式修改 
		try {
			docv.sendTime.setText(
					Html.fromHtml(new SimpleDateFormat("yy年M月d日").format(
							new SimpleDateFormat("yyyy-MM-dd").parse(docList.get(position).getSendTime()))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// view.setOnClickListener(lineOnclick);
		return view;
	}

	private OnClickListener lineOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};

}
