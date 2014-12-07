package com.hackaton.food4thought.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hackaton.food4thought.R;
import com.hackaton.food4thought.database.SubLocationHandler;

@SuppressWarnings("unused")
public class SubLocationAdapter extends BaseAdapter{

	private Context activity;
	private ArrayList<SubLocationHandler> data;
	private static LayoutInflater inflater = null;
	private View vi;
	private final static String TAG = SubLocationAdapter.class.getSimpleName();
	
	public SubLocationAdapter(Context context, ArrayList<SubLocationHandler> imageArry) {
		activity = context;
		data = imageArry;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		return data.size();
	}

	public SubLocationHandler getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if(view == null){
			LayoutInflater inflater = ((Activity) activity ).getLayoutInflater();
			view = inflater.inflate(R.layout.item_establishments, parent, false);
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.name);
			view.setTag(holder);
		}else
			holder  = (ViewHolder)view.getTag();

		SubLocationHandler item = data.get(position);
		holder.title.setText(item.getName());
		
		return view;
	}
	
	static class ViewHolder{
		TextView title;
	}

}
