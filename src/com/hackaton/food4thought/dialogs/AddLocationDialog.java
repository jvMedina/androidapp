package com.hackaton.food4thought.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.hackaton.food4thought.R;
import com.hackaton.food4thought.constant.GlobalVariables;
import com.hackaton.food4thought.controller.AddLocationCallBack;
import com.hackaton.food4thought.database.LocationHandler;
import com.hackaton.food4thought.database.LocationTypeHandler;
import com.hackaton.food4thought.dialogs.LocationTypePickerDialog.CategoryPickerCallBack;

public class AddLocationDialog extends Dialog{
	protected static final String TAG = null;
	private Activity activity;
	private AddLocationCallBack callback;
	private RelativeLayout selectCategory;
	private LinearLayout locationLayout, addLocation;
	private LocationTypePickerDialog locationTypePickerDialog;
	private LocationTypeHandler locationTypeHandler;
	private ImageView locationIconImage, addSubLocData, addLocationInfo;
	private TextView locationName, latTextView, longTextView;
	private EditText name;
	private LatLng location;

	public AddLocationDialog(Activity activity, AddLocationCallBack callback) {
		super(activity);
		this.activity = activity;
		this.callback = callback;
		init();
	}

	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(true);
		setContentView(R.layout.activity_add_location);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		setVariables();
		setContent();
		setFunction();

		show();
	}



	private void setVariables() {
		addSubLocData = (ImageView) findViewById(R.id.add_sub_loc);
		addLocationInfo = (ImageView) findViewById(R.id.add_loc_info);
		selectCategory = (RelativeLayout) findViewById(R.id.select_category);
		locationIconImage = (ImageView) findViewById(R.id.location_icon);
		locationName = (TextView) findViewById(R.id.location_name);
		locationLayout = (LinearLayout) findViewById(R.id.location_longlat);
		addLocation = (LinearLayout) findViewById(R.id.add_location);
		latTextView = (TextView) findViewById(R.id.location_lat);
		longTextView = (TextView) findViewById(R.id.location_lon);
		name = (EditText) findViewById(R.id.name);
	}

	private void setContent() {

	}
	private void setFunction() {
		selectCategory.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				locationTypePickerDialog = new LocationTypePickerDialog(activity, new CategoryPickerCallBack() {

					@Override
					public LocationTypeHandler setLocationType(LocationTypeHandler location) {
						locationTypeHandler = location;
						locationName.setText(location.getName());
						locationIconImage.setImageBitmap(location.getImage_resource());
						return null;
					}
				});
			}
		});

		locationLayout.setOnClickListener(new  android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				callback.markerView();
				hide();
			}
		});

		addLocation.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String latString = latTextView.getText().toString();
					String lonString = longTextView.getText().toString();
					String locationNameString = name.getText().toString();
					if(latString.length() > 0 && lonString.length() > 0){
						double latDouble = Double.parseDouble(latString);
						double lonDouble = Double.parseDouble(lonString);
						Log.i(TAG, "location  id : " + locationTypeHandler.getBase_id());
//						LocationHandler location = new LocationHandler(locationTypeHandler.getBase_id(), GlobalVariables.instanceOf().getDate(), latDouble, lonDouble, locationNameString, locationTypeHandler.getBase_id(), "", 0);
//						callback.addLocation(location);
					}
				} catch (NumberFormatException e) {
					// TODO: handle exception
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
