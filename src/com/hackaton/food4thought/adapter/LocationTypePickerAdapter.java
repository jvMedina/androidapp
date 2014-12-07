package com.hackaton.food4thought.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hackaton.food4thought.R;
import com.hackaton.food4thought.database.LocationTypeHandler;
import com.hackaton.food4thought.imageloader.ImageLoader;

public class LocationTypePickerAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<LocationTypeHandler> items;
	LayoutInflater inflater;
	ImageLoader imageLoader;

	public LocationTypePickerAdapter(Context context, ArrayList<LocationTypeHandler> items) {
		
		this.context = context;
		this.items = items;
		imageLoader = new ImageLoader(context);
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_pick_category, parent, false);
		}
		
		LocationTypeHandler item  = items.get(position);
		LinearLayout container = (LinearLayout) convertView.findViewById(R.id.container);
		ImageView icon = (ImageView) convertView.findViewById(R.id.pin_image);
		TextView description = (TextView) convertView.findViewById(R.id.name);
		//set information to widgets
//		if(item.getImage_url() != null && item.getImage_url().length() > 0){
//			imageLoader.DisplayImage(item.getImage_url(), image, R.drawable.ic_launcher);
//		}else
		 if(item.getImage_resource() != null){
			icon.setImageBitmap(item.getImage_resource());
		}else{
			icon.setImageResource(R.drawable.ic_launcher);
		}

		description.setText(item.getName());
		return convertView;
	}


	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public LocationTypeHandler getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setItems(ArrayList<LocationTypeHandler> items) {
		this.items = items;
	}
}