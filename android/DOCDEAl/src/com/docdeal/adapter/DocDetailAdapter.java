package com.docdeal.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.textmining.text.extraction.WordExtractor;

import com.docdeal.R;
import com.docdeal.activity.DocText;
import com.docdeal.activity.FileJpgView;
import com.docdeal.adapterview.DocDetailView;
import com.docdeal.bean.Attachment;
import com.docdeal.bean.Attachments;
import com.docdeal.bean.Body;
import com.docdeal.bean.NodeInfo;
import com.docdeal.webservice.WebService;
import com.global.Base64Util;
import com.global.FileUtil;
import com.global.OpenFileUtil;
import com.global.StringUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class DocDetailAdapter {
	private List<Object> nodeList;
	private LayoutInflater inflater;
	private Context content;
	private int width;
	private Map<String, String> map;
	private String filePath;
	private Attachments attachments;
	private List<String> attachGroups;
	private PopupWindow popupWindow;
	private PopupWindow popupWindowForWord;
	private View view;
	private ListView lv_group;
	// private String txtPath;
	private LinearLayout btnLine;
	private List<Attachment> attList;
	private boolean isSearch = false;
	private String docId;
	private Body body;

	@SuppressWarnings("deprecation")
	public DocDetailAdapter(Context content, LayoutInflater inflater,
			List<Object> nodeList, WindowManager dm, Map<String, String> map,
			String docId) {
		this.content = content;
		this.inflater = inflater;
		this.nodeList = nodeList;
		this.width = dm.getDefaultDisplay().getWidth();
		this.map = map;
		this.docId = docId;
	}

	public DocDetailAdapter(Context content, LayoutInflater inflater,
			List<Object> nodeList, WindowManager dm, Map<String, String> map,
			boolean isSearch, String docId) {
		this.content = content;
		this.inflater = inflater;
		this.nodeList = nodeList;
		this.width = dm.getDefaultDisplay().getWidth();
		this.map = map;
		this.isSearch = isSearch;
		this.docId = docId;
	}

	public void addAll(List<Object> list) {
		nodeList.addAll(list);
	}

	public View getView(LinearLayout view) {
		btnLine = (LinearLayout) inflater.inflate(R.layout.detail_btn, null);
		Resources resource = (Resources) content.getResources();
		ColorStateList csl = (ColorStateList) resource
				.getColorStateList(R.drawable.detailLable);
		for (int i = 0; i < nodeList.size(); i++) {
			Object ob = nodeList.get(i);
			if (ob instanceof NodeInfo) {
				final NodeInfo node = (NodeInfo) ob;
				LinearLayout line = new LinearLayout(content);
				line.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				line.setOrientation(LinearLayout.HORIZONTAL);
				view.addView(line);
				TextView text = new TextView(content);
				text.setLayoutParams(new LayoutParams(width / 3,
						LayoutParams.MATCH_PARENT));
				text.setText(node.getTagName());

				text.setBackgroundResource(R.drawable.view_yuan_morelist);
				text.setTextColor(csl);
				text.setGravity(Gravity.CENTER);

				line.addView(text);
				if (node.getCanwrite().equals("false") || this.isSearch) {

					TextView etext = new TextView(content);
					etext.setLayoutParams(new LayoutParams(width * 2 / 3,
							LayoutParams.MATCH_PARENT));
					etext.setText(node.getText());
					etext.setBackgroundResource(R.drawable.view_yuan_morelist);
					etext.setGravity(Gravity.CENTER);
					line.addView(etext);

				} else {
					final EditText etext = new EditText(content);
					etext.setLayoutParams(new LayoutParams(width * 2 / 3,
							LayoutParams.MATCH_PARENT));
					etext.setText(node.getText());
					etext.setBackgroundResource(R.drawable.tablestyle);
					etext.setGravity(Gravity.CENTER);
					line.addView(etext);
					etext.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							map.put(node.getName(), etext.getText().toString());
						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
						}

						@Override
						public void afterTextChanged(Editable s) {
						}
					});

				}

				// line.addView(etext);

			} else if (ob instanceof Body) {

				setBodyButton(view, (Body) ob);
			} else if (ob instanceof Attachments) {
				setAttaButton(view, (Attachments) ob);
			}
		}
		view.addView(btnLine);
		return view;
	}

	/**
	 * 正文按钮
	 * 
	 * @param view
	 * @param body
	 */
	private void setBodyButton(LinearLayout view, final Body body) {
		// view.removeAllViews();
		view.setGravity(Gravity.CENTER);
		Button btn = (Button) btnLine.findViewById(R.id.body);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (body.getText() != null&&!body.getText().equals("")) {
					showWindowForWord(v, body);
				} else {
					Toast.makeText(content, "没有正文可显示", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		// view.addView(btn);
	}

	/**
	 * 附件按钮
	 * 
	 * @param view
	 * @param atta
	 */
	private void setAttaButton(LinearLayout view, final Attachments atta) {
		attachments = atta;
		Button btn = (Button) btnLine.findViewById(R.id.attachment);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (atta.getList().size() > 0) {
					showWindow(v);
				} else {
					Toast.makeText(content, "没有附件可显示", Toast.LENGTH_LONG)
							.show();
				}

			}
		});
		// view.addView(btn);
	}

	/**
	 * 附件弹窗
	 * 
	 * @param parent
	 */
	private void showWindow(View parent) {
		this.attList = attachments.getList();// 附件列表
		if (popupWindow == null) {
			view = inflater.inflate(R.layout.group_list, null);

			lv_group = (ListView) view.findViewById(R.id.lvGroup);
			// 加载数据
			attachGroups = new ArrayList<String>();
			for (int i = 0; i < attList.size(); i++) {
				attachGroups.add(i + 1 + ":" + attList.get(i).getTitle());
			}

			GroupAdapter groupAdapter = new GroupAdapter(content, attachGroups);
			lv_group.setAdapter(groupAdapter);
			// 创建一个PopuWidow对象
			popupWindow = new PopupWindow(view, width * 9 / 10,
					LayoutParams.WRAP_CONTENT);
		}

		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) content
				.getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		int xPos = -width * 2 / 5;

		// popupWindow.showAsDropDown((View) parent, xPos, 300);
		popupWindow.showAtLocation((View) parent, Gravity.CENTER, 0, 0);

		lv_group.setOnItemClickListener(popItemClick);
	}

	/**
	 * word弹窗
	 * 
	 * @param body
	 **/
	private void showWindowForWord(View parent, Body body) {
		this.body = body;
		this.attList = attachments.getList();// 附件列表
		if (popupWindowForWord == null) {
			view = inflater.inflate(R.layout.group_list, null);

			lv_group = (ListView) view.findViewById(R.id.lvGroup);
			// 加载数据
			attachGroups = new ArrayList<String>();
			attachGroups.add(1 + ":" + "选择word打开");
			attachGroups.add(2 + ":" + "文本形式打开");

			GroupAdapter groupAdapter = new GroupAdapter(content, attachGroups);
			lv_group.setAdapter(groupAdapter);
			// 创建一个PopuWidow对象
			popupWindowForWord = new PopupWindow(view, width * 9 / 10,
					LayoutParams.WRAP_CONTENT);
		}

		// 使其聚集
		popupWindowForWord.setFocusable(true);
		// 设置允许在外点击消失
		popupWindowForWord.setOutsideTouchable(true);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindowForWord.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) content
				.getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		int xPos = -width * 2 / 5;

		// popupWindow.showAsDropDown((View) parent, xPos, 300);
		popupWindowForWord.showAtLocation((View) parent, Gravity.CENTER, 0, 0);
		lv_group.setOnItemClickListener(popItemClickForWord);
	}

	/**
	 * 正文弹窗点击事件
	 */
	private OnItemClickListener popItemClickForWord = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			String code = body.getText();
			filePath = Base64Util.decoderBase64File(code, "doc");
			if (position == 0) {
				if (StringUtil.isEmpty(filePath)) {
					filePath = Base64Util.decoderBase64File(code, "doc");
					// String wordContent = FileUtil.readword(filePath);
					// txtPath = FileUtil.writeStringToTxt(filePath,
					// wordContent);
				}
				if (filePath.equals("unavailable")) {
					Toast.makeText(content, "存储卡不可用", Toast.LENGTH_LONG).show();
					return;
				} else {
					OpenFileUtil.openFile(content, filePath);
					// content.startActivity(intent);
				}
			} else {
				WordExtractor w = new WordExtractor();
				InputStream is = null;
				String str = null;
				try {
					is = new FileInputStream(new File(filePath));
					str = w.extractText(is).replaceAll("-\\s\\d+\\s-", "").replaceAll("", "");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent = new Intent(content, DocText.class);
				intent.putExtra("text", str);
				content.startActivity(intent);
			}
		}
	};
	/**
	 * 弹窗附件点击事件
	 */
	private OnItemClickListener popItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {

			// Toast.makeText(content, attachGroups.get(position), 1000)
			// .show();
			if (position + 1 > attList.size()) {
				return;
			}
			Attachment at = attList.get(position);

			// String code = at.getBody();
			if (at.getTitle().endsWith(".pdf")
					|| at.getTitle().endsWith(".tif")) {
				// start a new intent for file's pages image view
				Intent intent = new Intent(content, FileJpgView.class);

				intent.putExtra("docId", docId);
				intent.putExtra("title", at.getTitle());
				content.startActivity(intent);
			} else {
				// get file encoding Base64
				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
				// doc's id
				map.put("docId", docId);
				// attachment's title with attachment suffix
				map.put("title", at.getTitle());
				WebService web = new WebService("getFJFileBase64Content", map,
						"getFJFileBase64ContentReturn");
				String code = web.getData();// get file code
				code=paserXml(code);

				String filePath = Base64Util.decoderBase64FileWithFileName(
						code, at.getTitle());
				OpenFileUtil.openFile(content, filePath);
				// Intent intent = OpenFileUtil.openFile(filePath);
				// content.startActivity(intent);
			}
			if (popupWindow != null) {
				popupWindow.dismiss();
			}
		}
	};
	
	private String paserXml(String data) {
		try {
			Document doc = DocumentHelper.parseText(data);
			String content1 = doc.selectSingleNode("/root/内容").getText();
			return content1;
			// Element el = doc.getRootElement();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private OnClickListener lineOnclick = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			// Toast.makeText(content, ((TextView) v).getText().toString(),
			// Toast.LENGTH_SHORT).show();
			LinearLayout line = (LinearLayout) v.getParent();
			TextView lable = (TextView) line.findViewById(R.id.detailLable);
			final View layout = inflater.inflate(R.layout.docdetail_deal, null);

			new AlertDialog.Builder(content)
					.setTitle("设置" + lable.getText().toString() + ":")
					.setView(layout)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// layout.findViewById(R.id.etname);
									((TextView) v).setText(((EditText) layout
											.findViewById(R.id.etname))
											.getText());
								}
							}).setNegativeButton("取消", null).show();
		}
	};
}
