package com.hackaton.food4thought.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hackaton.food4thought.R;
import com.hackaton.food4thought.database.LocationHandler;
import com.hackaton.food4thought.imageloader.ImageLoader;

@SuppressWarnings("unused")
public class SearchAdapter extends BaseAdapter{

	private Context activity;
	private ArrayList<LocationHandler> data;
	private static LayoutInflater inflater = null;
	private View vi;
	private final static String TAG = SearchAdapter.class.getSimpleName();
	private ImageLoader imageLoader;

	public SearchAdapter(Context context, ArrayList<LocationHandler> imageArry) {
		activity = context;
		data = imageArry;
		Log.i(TAG , "data size " + data.size());
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(context);
	}

	public int getCount() {
		return data.size();
	}

	public LocationHandler getItem(int position) {
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
			view = inflater.inflate(R.layout.item_search_location, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.longitude = (TextView) view.findViewById(R.id.lon);
			holder.latitude = (TextView) view.findViewById(R.id.lat);
			view.setTag(holder);
		}else
			holder  = (ViewHolder)view.getTag();

		LocationHandler item = data.get(position);
		holder.name.setText(item.getName());
		holder.longitude.setText(String.valueOf(item.getLongitude()));
		holder.latitude.setText(String.valueOf(item.getLatitude()));

		return view;
	}

	static class ViewHolder{
		TextView name, longitude, latitude;
	}
	
	public void setData(ArrayList<LocationHandler> data) {
		this.data = data;
		notifyDataSetChanged();
	}

}
