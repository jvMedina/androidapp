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

public class CategoryAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<LocationTypeHandler> items;
	LayoutInflater inflater;
	ImageLoader imageLoader;

	public CategoryAdapter(Context context, ArrayList<LocationTypeHandler> items) {

		this.context = context;
		this.items = items;
		imageLoader = new ImageLoader(context);
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_location_type, parent, false);
		}

		LocationTypeHandler item  = items.get(position);
		LinearLayout container = (LinearLayout) convertView.findViewById(R.id.container);
		ImageView image = (ImageView) convertView.findViewById(R.id.image);
		TextView description = (TextView) convertView.findViewById(R.id.name);
		//set information to widgets
		//		container.setBackgroundColor(getColor(item.getBg_color()));
		if(item.getImage_url() != null && item.getImage_url().length() > 0){
			imageLoader.DisplayImage(item.getImage_url(), image, R.drawable.ic_launcher);
		}else{
			if(item.getImage_resource() != null){
				image.setImageBitmap(item.getImage_resource());
			}else{
				image.setImageResource(R.drawable.ic_launcher);
			}
		}
		description.setText(item.getName());
		return convertView;
	}



	private int getColor(String bg_color) {
		int color = 0;
		if(bg_color.length() > 0)
			color =  Integer.parseInt(bg_color, 16)+0xFF000000;
		return color;
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