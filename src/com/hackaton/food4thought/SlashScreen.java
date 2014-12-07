package com.hackaton.food4thought;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.actionbarsherlock.app.SherlockActivity;
import com.hackaton.food4thought.constant.ConstantValue;
import com.hackaton.food4thought.constant.GlobalVariables;
import com.hackaton.food4thought.controller.FontOverrideUtil;

public class SlashScreen extends SherlockActivity{
	private static final String TAG = SlashScreen.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fontOverride();
		setContentView(R.layout.activity_splash);
		getSupportActionBar().hide();
		new Handler().postDelayed(new Runnable() {

			public void run() {

				initialize();

			}

		}, 5000 );
	}

	protected void initialize() {
		SharedPreferences sp = GlobalVariables.instanceOf().getSharePreference(this);
		String userNo= sp.getString(ConstantValue.keyUserNum, "");
		Log.i(TAG, "user no " + userNo);
//		if(userNo.trim().length() > 0){
//			ConstantValue.userNumber = userNo;
//			Intent intent = new Intent(this, FragmentChangeActivity.class);
//			startActivity(intent);
//		}else{
//			Log.i(TAG, "initialize login");
//			Intent intent = new Intent(this, LoginUser.class);
//			startActivity(intent);
//		}
		Log.i(TAG, "initialize login");
		Intent intent = new Intent(this, LoginUser.class);
		startActivity(intent);
		this.finish();
	}

	protected void fontOverride() {
		FontOverrideUtil.setDefaultFont(this, "DEFAULT", "fonts/Montserrat-Regular.ttf");
		FontOverrideUtil.setDefaultFont(this, "MONOSPACE", "fonts/Montserrat-Regular.ttf");
		FontOverrideUtil.setDefaultFont(this, "SANS_SERIF", "fonts/Montserrat-Regular.ttf");
		
	}
}
