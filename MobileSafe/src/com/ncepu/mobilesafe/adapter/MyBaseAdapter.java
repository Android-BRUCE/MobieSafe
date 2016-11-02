package com.ncepu.mobilesafe.adapter;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;
public abstract class MyBaseAdapter<T> extends BaseAdapter {
	
	public List<T> lists;
	public Context context;
	
	protected MyBaseAdapter(List<T> lists, Context context){
		this.context = context;
		this.lists = lists;
	}
	
	protected MyBaseAdapter() {
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
