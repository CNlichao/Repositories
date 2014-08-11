package com.docdeal.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.docdeal.R;
import com.docdeal.adapterview.DealPeopleView;
import com.docdeal.bean.User;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DealPeopleAdapter extends BaseAdapter {
	private List<User> dealPeopleList;
	private LayoutInflater inflater;
	private Context context;
	private List<Boolean> listselected;// ��¼ѡ��״̬
	private List<Integer> chooseList = new LinkedList<Integer>();

	public DealPeopleAdapter(Context context, LayoutInflater inflater,
			List<User> dealPeopleList) {
		this.context = context;
		this.inflater = inflater;
		this.dealPeopleList = dealPeopleList;
		this.setListselected(new ArrayList<Boolean>(getCount()));
		for (int i = 0; i < getCount(); i++)
			getListselected().add(false);// ��ʼΪfalse�����Ⱥ�listviewһ��
	}

	public List<Boolean> getListselected() {
		return listselected;
	}

	public void setListselected(List<Boolean> listselected) {
		this.listselected = listselected;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dealPeopleList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dealPeopleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub

		DealPeopleView dealV = null;
		if (dealV == null) {
			dealV = new DealPeopleView();
			view = inflater.inflate(R.layout.people_choose_item, null);
			dealV.name = (TextView) view.findViewById(R.id.name);
			view.setTag(dealV);
			// view.setBackgroundResource(R.drawable.list_item_bg);
		} else {
			dealV = (DealPeopleView) view.getTag();
		}
		dealV.name.setText(dealPeopleList.get(position).getName());
		if (getListselected().size() > 0
				&& getListselected().get(position) == false) {// ���δ��ѡ�У�����Ϊ��ɫ
			// item.selection.setImageResource(R.drawable.bus_icon_red);
			dealV.name.setTextColor(Color.RED);

		} else {// �����ѡ�У�����Ϊ��ɫ
				// item.selection.setImageResource(R.drawable.bus_icon_green);
			dealV.name.setTextColor(Color.GREEN);
		}
		ListView list = new ListView(context);
		return view;
	}

	public void modifyStates(int position) {
		if (getListselected().get(position) == false) {
			getListselected().set(position, true);// �����Ӧposition�ļ�¼��δ��ѡ��������Ϊѡ�У�true��
			chooseList.add(Integer.valueOf(position));
			notifyDataSetChanged();
		} else {
			getListselected().set(position, false);// ������Ӧposition�ļ�¼�Ǳ�ѡ��������Ϊδѡ�У�false��
			chooseList.remove(Integer.valueOf(position));
			notifyDataSetChanged();
		}
	}

	public void addItem(User dealItem) {
		dealPeopleList.add(dealItem);
		listselected.add(false);
	}

	public void removeItem(int positon) {
		dealPeopleList.remove(positon);
		listselected.remove(positon);
		chooseList.remove(Integer.valueOf(positon));
	}

	public void removeItem(User dealItem) {
		dealPeopleList.remove(dealItem);
	}

	public List<Integer> getChooseList() {
		return chooseList;
	}

	public void clear() {
		dealPeopleList.clear();
		chooseList.clear();
		listselected.clear();
		notifyDataSetChanged();
	}

	public void clearChoosed() {

		for (int i = 0; i < listselected.size(); i++) {
			listselected.set(i, false);
		}
		chooseList.clear();
		notifyDataSetChanged();
	}

	public void addAllItem(List<User> items) {
		for (User u : items) {
			dealPeopleList.add(u);
			listselected.add(false);
		}
	}

}
