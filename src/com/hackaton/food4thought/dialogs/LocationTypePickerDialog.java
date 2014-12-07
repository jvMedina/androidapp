package com.hackaton.food4thought.dialogs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hackaton.food4thought.R;
import com.hackaton.food4thought.adapter.LocationTypePickerAdapter;
import com.hackaton.food4thought.controller.AddLocationCategoryCallBack;
import com.hackaton.food4thought.database.Database;
import com.hackaton.food4thought.database.LocationTypeHandler;

public class LocationTypePickerDialog extends Dialog{
	private Activity activity;
	private AddLocationCategoryCallBack callback;
	private ListView categoryList;
	private LocationTypePickerAdapter adapter;
	private Database db;
	private ArrayList<LocationTypeHandler> locationTypes = new ArrayList<LocationTypeHandler>();
	private CategoryPickerCallBack categoryPickerCallBack;
	public LocationTypePickerDialog(Activity activity, CategoryPickerCallBack categoryPickerCallBack) {
		super(activity);
		this.activity = activity;
		db = new Database(activity);
		this.categoryPickerCallBack = categoryPickerCallBack;
		init();
	}

	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		setContentView(R.layout.activity_pick_category);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		show();
		
		setFunction();
		setVariables();
	}

	private void setFunction() {
		categoryList 		= (ListView) findViewById(R.id.list_category);
		locationTypes		= db.getAllLocationType();
		adapter				= new LocationTypePickerAdapter(activity, locationTypes);
		categoryList.setAdapter(adapter);
	}

	private void setVariables() {
		categoryList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				categoryPickerCallBack.setLocationType(adapter.getItem(position));
				dismiss();
			}
		});
	}
	
	public interface CategoryPickerCallBack{
		public LocationTypeHandler setLocationType(LocationTypeHandler location);
	}



}
