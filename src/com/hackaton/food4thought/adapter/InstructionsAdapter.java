package com.hackaton.food4thought.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hackaton.food4thought.R;
import com.hackaton.food4thought.imageloader.ImageLoader;
import com.tyczj.mapnavigator.Steps;

@SuppressWarnings("unused")
public class InstructionsAdapter extends BaseAdapter{

	private Context activity;
	private ArrayList<Steps> data;
	private static LayoutInflater inflater = null;
	private View vi;
	private final static String TAG = InstructionsAdapter.class.getSimpleName();
	private ImageLoader imageLoader;

	public InstructionsAdapter(Context context, ArrayList<Steps> imageArry) {
		activity = context;
		data = imageArry;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(context);
	}

	public int getCount() {
		return data.size();
	}

	public Steps getItem(int position) {
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
			view = inflater.inflate(R.layout.item_instruction, parent, false);
			holder = new ViewHolder();
			holder.instrucitonLbl = (TextView) view.findViewById(R.id.instruction_lbl);
			view.setTag(holder);
		}else
			holder  = (ViewHolder)view.getTag();

		String item = data.get(position).getStepInstructions();
		holder.instrucitonLbl.setText(android.text.Html.fromHtml(item).toString());

		return view;
	}

	static class ViewHolder{
		TextView instrucitonLbl;
	}
	

}
