package com.hackaton.food4thought;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.hackaton.food4thought.database.Database;
import com.hackaton.food4thought.database.LocationTypeHandler;

public class MainMenu extends SherlockActivity{

	private static final String TAG = MainMenu.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Database db = new Database(MainMenu.this);
		setContentView(R.layout.activity_main_menu);
		getSupportActionBar().hide();
		findViewById(R.id.start).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainMenu.this, FragmentChangeActivity.class);
				startActivity(intent);
			}
		});
		findViewById(R.id.help).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainMenu.this, HelpUp.class);
				startActivity(intent);
			}
		});
		//		db.addLocationType(new LocationTypeHandler(ConstantValue.MALL, GlobalVariables.instanceOf().getDate(), R.drawable.mall_icon, null, "Malls", "0000FF", MapLocationModel.getBitmap(MainMenu.this,R.drawable.mall_pin)));
		Log.i(TAG, "location data === ");
		for(LocationTypeHandler item : db.getAllLocationType()){
			Log.i(TAG, "location item ids : " + item.getId());
		}
	}
}
