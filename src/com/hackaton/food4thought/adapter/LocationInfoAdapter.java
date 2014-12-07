package com.hackaton.food4thought.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackaton.food4thought.R;
import com.hackaton.food4thought.database.LocationInfoHandler;
import com.hackaton.food4thought.imageloader.ImageLoader;

@SuppressWarnings("unused")
public class LocationInfoAdapter extends BaseAdapter{

	private Context activity;
	private ArrayList<LocationInfoHandler> data;
	private static LayoutInflater inflater = null;
	private View vi;
	private final static String TAG = LocationInfoAdapter.class.getSimpleName();
	private ImageLoader imageLoader;

	public LocationInfoAdapter(Context context, ArrayList<LocationInfoHandler> imageArry) {
		activity = context;
		data = imageArry;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(context);
	}

	public int getCount() {
		return data.size();
	}

	public LocationInfoHandler getItem(int position) {
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
			view = inflater.inflate(R.layout.item_content, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) view.findViewById(R.id.image);
			holder.title = (TextView) view.findViewById(R.id.title);
			holder.content = (TextView) view.findViewById(R.id.content);
			view.setTag(holder);
		}else
			holder  = (ViewHolder)view.getTag();

		LocationInfoHandler item = data.get(position);
		holder.title.setText(item.getTitle());
		holder.content.setText(item.getContent());

		if(item.getImage() != null){
			holder.image.setImageBitmap(item.getImage());
		}
		else
			holder.image.setVisibility(View.GONE);


		return view;
	}

	static class ViewHolder{
		ImageView image;
		TextView title, content;
	}

}
