package com.hackaton.food4thought.controller;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.hackaton.food4thought.FragmentChangeActivity;
import com.hackaton.food4thought.MainMenu;
import com.hackaton.food4thought.R;
import com.hackaton.food4thought.adapter.SearchAdapter;
import com.hackaton.food4thought.constant.ConstantValue;
import com.hackaton.food4thought.constant.GlobalVariables;
import com.hackaton.food4thought.database.Database;
import com.hackaton.food4thought.database.LocationHandler;
import com.hackaton.food4thought.model.GPSTracker;

public class SearchController {
	protected static final String TAG = SearchController.class.getSimpleName();
	private Activity activity;
	private View view;
	private ListView list;
	private EditText seachEt;
	private SearchAdapter searchAdapter;
	private Database db;
	private FragmentChangeActivity fragmentChangeActivity;
	private GPSTracker gpsData;
	public SearchController(Activity activity, View view) {
		this.activity = activity;
		this.view = view;
		db = new Database(activity);
		fragmentChangeActivity = (FragmentChangeActivity) activity;
		gpsData = new GPSTracker(activity);
	}

	public void setVariables(){
		list  = (ListView) view.findViewById(R.id.listview);
		seachEt = (EditText) view.findViewById(R.id.search);
		searchAdapter = new SearchAdapter(activity, db.getAllLocation());
		list.setAdapter(searchAdapter);
	}
	
	public void setFunctions(){
		seachEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String searchString = seachEt.getText().toString();
//				if(searchString.contains("nearest")){
//					Log.i(TAG, "near found");
//					searchString  = searchString.replace("nearest", "");
//					searchString  = searchString.replaceAll("\\s", "");
//					searchAdapter.setData(db.findNear(searchString, gpsData.getLatitude(), gpsData.getLongitude()));
//				}
//				else if(searchString.contains("near")){
//					searchString  = searchString.replace("nearest", "");
//					searchString  = searchString.replaceAll("\\s", "");
//					searchAdapter.setData(db.findNear(searchString, gpsData.getLatitude(), gpsData.getLongitude()));
//				}
				if(searchString.contains("near")){
					searchString  = searchString.replace("near", "");
					searchString  = searchString.replaceAll("\\s", "");
					searchAdapter.setData(db.findNear(searchString, gpsData.getLatitude(), gpsData.getLongitude()));
				}
				else{
					searchAdapter.setData(db.findNear( gpsData.getLatitude(), gpsData.getLongitude()));
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				LocationHandler locData = searchAdapter.getItem(position);
				Log.i(TAG, "on click ");
				fragmentChangeActivity.displayLocation(locData.getId()); 
				fragmentChangeActivity.getSlidingMenu().toggle();
			}
		});
		
	}
}

