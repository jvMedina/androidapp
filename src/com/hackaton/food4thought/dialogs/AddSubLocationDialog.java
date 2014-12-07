package com.hackaton.food4thought.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.hackaton.food4thought.R;
import com.hackaton.food4thought.constant.GlobalVariables;
import com.hackaton.food4thought.controller.AddSubLocationCallBack;
import com.hackaton.food4thought.database.LocationHandler;
import com.hackaton.food4thought.database.SubLocationHandler;

public class AddSubLocationDialog extends Dialog{
	private Activity activity;
	private AddSubLocationCallBack callback;
	private LinearLayout latLongLayout;
	private TextView latTextView, longTextView, locationName;
	private EditText name,content;
	private LatLng location;
	private LinearLayout addLocation;
	private LocationHandler locationData;
	public AddSubLocationDialog(Activity activity, AddSubLocationCallBack callback, LocationHandler locationData) {
		super(activity);
		this.locationData  = locationData;
		this.activity = activity;
		this.callback = callback;
		init();
	}

	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(true);
		setContentView(R.layout.activity_add_sublocation_location);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		setVariables();
		setContent();
		setFunction();
		show();
	}



	private void setVariables() {
		locationName = (TextView) findViewById(R.id.location_name);
		latTextView = (TextView) findViewById(R.id.location_lat);
		longTextView = (TextView) findViewById(R.id.location_lon);
		name = (EditText) findViewById(R.id.name);
		content = (EditText) findViewById(R.id.description);
		latLongLayout = (LinearLayout) findViewById(R.id.location_longlat);
		addLocation = (LinearLayout) findViewById(R.id.add_location);
	}

	private void setContent() {
		locationName.setText(locationData.getName());
	}
	
	private void setFunction() {
		latLongLayout.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				callback.markerView();
				hide();
			}
		});
		addLocation.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SubLocationHandler subLocationHandler = new SubLocationHandler();
				String nameString = name.getText().toString();
				String contentString = content.getText().toString();
				if(nameString.length() > 0 && contentString.length() >0){
					if(location != null){
						subLocationHandler = new SubLocationHandler(0, GlobalVariables.instanceOf().getDate(), nameString, location.latitude, location.longitude, locationData.getId()); 
						callback.addLocation(subLocationHandler);
					}else{
						Toast.makeText(activity, "Please input your location", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(activity, "Please input location name", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	public void setLocation(LatLng lastlocationLatLng) {
		location = lastlocationLatLng;
		latTextView.setText(String.valueOf(lastlocationLatLng.latitude));
		longTextView.setText(String.valueOf(lastlocationLatLng.longitude));
		show();
	}



}
