package com.hackaton.food4thought.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hackaton.food4thought.MenuFragment;
import com.hackaton.food4thought.R;
import com.hackaton.food4thought.constant.ConstantValue;
import com.hackaton.food4thought.controller.AddLocationCategoryCallBack;
import com.hackaton.food4thought.imagepicker.Action;
import com.hackaton.food4thought.imagepicker.CustomGalleryActivity;

public class AddCategoryDialog extends Dialog{
	private Activity activity;
	private AddLocationCategoryCallBack callback;
	private TextView addLocationType;
	private EditText locationName;
	private ImageView addLocationIcon, addLocationPin, iconImage, pinImage;
	private Bitmap locationIconBit, locationPinBit;
	private MenuFragment menuFragment;
	public AddCategoryDialog(Activity activity, AddLocationCategoryCallBack callback, Fragment fragment) {
		super(activity);
		this.activity = activity;
		this.callback = callback;
		menuFragment = (MenuFragment) fragment;
		init();
	}

	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(true);
		setContentView(R.layout.activity_add_category);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		setVariables();
		setFunction();

		show();
	}

	private void setFunction() {
		addLocationType.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = locationName.getText().toString();
				if(name.length() > 0){
					callback.addData(name, locationIconBit, locationPinBit);
				}
				else
					Toast.makeText(activity, "Please input name", Toast.LENGTH_SHORT).show();
			}
		});

		addLocationIcon.setOnClickListener(new android.view.View.OnClickListener()  {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Action.ACTION_PICK);
				menuFragment.startActivityForResult(i, ConstantValue.CATEGORY_LOCATION_ICON);
			}
		});

		addLocationPin.setOnClickListener(new android.view.View.OnClickListener()  {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Action.ACTION_PICK);
				menuFragment.startActivityForResult(i, ConstantValue.CATEGORY_LOCATION_PIN);
			}
		});
	}

	private void setVariables() {
		addLocationType = (TextView) findViewById(R.id.add_location);
		addLocationIcon = (ImageView) findViewById(R.id.add_location_icon);
		addLocationPin = (ImageView) findViewById(R.id.add_location_pin);
		locationName = (EditText) findViewById(R.id.location_type_name);
		iconImage = (ImageView) findViewById(R.id.icon_image);
		pinImage = (ImageView) findViewById(R.id.pin_image);
	}

	public Bitmap getLocationIconBit() {
		return locationIconBit;
	}

	public void setLocationIconBit(Bitmap locationIconBit) {
		this.locationIconBit = locationIconBit;
		iconImage.setImageBitmap(locationIconBit);

	}

	public Bitmap getLocationPinBit() {
		return locationPinBit;
	}

	public void setLocationPinBit(Bitmap locationPinBit) {
		this.locationPinBit = locationPinBit;
		pinImage.setImageBitmap(locationPinBit);
	}

}
