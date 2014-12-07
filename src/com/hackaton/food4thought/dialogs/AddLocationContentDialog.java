package com.hackaton.food4thought.dialogs;

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

import com.hackaton.food4thought.LocationInfoFragment;
import com.hackaton.food4thought.R;
import com.hackaton.food4thought.constant.ConstantValue;
import com.hackaton.food4thought.constant.GlobalVariables;
import com.hackaton.food4thought.controller.AddLocationInfoCallBack;
import com.hackaton.food4thought.database.LocationInfoHandler;
import com.hackaton.food4thought.imagepicker.Action;

public class AddLocationContentDialog extends Dialog{
	private LocationInfoFragment fragment;
	private AddLocationInfoCallBack callback;
	private TextView addLocationType;
	private ImageView image, addImage;
	private EditText name, content;
	private Bitmap imageBit;
	private int locationId;
	public AddLocationContentDialog(Fragment fragment, int locationId, AddLocationInfoCallBack callback) {
		super(fragment.getActivity());
		this.fragment = (LocationInfoFragment) fragment;
		this.locationId = locationId;
		this.callback = callback;
		init();
	}

	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(true);
		setContentView(R.layout.activity_add_location_content);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		setVariables();
		setFunction();
		show();
	}
	private void setVariables() {
		image = (ImageView) findViewById(R.id.image);
		addImage = (ImageView) findViewById(R.id.add_image);
		name = (EditText) findViewById(R.id.name);
		content = (EditText) findViewById(R.id.description);
		addLocationType = (TextView) findViewById(R.id.location_name);
	}


	private void setFunction() {
		addLocationType.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Action.ACTION_PICK);
				fragment.startActivityForResult(i, ConstantValue.CATEGORY_LOCATION_ICON);
			}
		});
		findViewById(R.id.add).setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LocationInfoHandler locationInfoHandler = new LocationInfoHandler();
				locationInfoHandler = new LocationInfoHandler(0, GlobalVariables.instanceOf().getDate(), content.getText().toString(), name.getText().toString(), locationId, imageBit);
				callback.addData(locationInfoHandler);
			}
		});
	}

	public Bitmap getImageBit() {
		return imageBit;
	}

	public void setImageBit(Bitmap imageBit) {
		this.imageBit = imageBit;
		image.setImageBitmap(imageBit);
	}



}
