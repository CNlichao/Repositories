package com.docdeal.adapter;

import java.util.List;

import com.docdeal.R;
import com.docdeal.adapterview.DocView;
import com.docdeal.bean.OfficialDocument;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchDocAdapter extends BaseAdapter {
	private List<OfficialDocument> docList;
	private LayoutInflater layoutInflater;
	Context context;

	public SearchDocAdapter(Context context, LayoutInflater layoutInflater,
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
			view = layoutInflater.inflate(R.layout.searchdoclist_item, null);
			docv.docName = (TextView) view.findViewById(R.id.docname);
			// docv.sendTime = (TextView) view.findViewById(R.id.docsendtime);
			view.setTag(docv);
		} else {
			docv = (DocView) view.getTag();
		}
		docv.docName.setText(Html.fromHtml(docList.get(position).getTitle()));

		// docv.sendTime.setText(Html
		// .fromHtml(docList.get(position).getSendTime()));
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
