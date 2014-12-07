package com.hackaton.food4thought.constant;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

public class GlobalVariables {
	private Activity mActivity;

	public static GlobalVariables instanceOf() {
		GlobalVariables constantValue = new GlobalVariables();
		return constantValue;
	}

	public SharedPreferences getSharePreference(Activity mActivity){
		SharedPreferences settings = mActivity.getSharedPreferences(ConstantValue.SHARE_PREFERENCE_NAME, 0);
		return settings;
	}
	
	public String getDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new java.util.Date());
	}
	
	public static Bitmap scaleBitmap(Bitmap bitmap, int targetW) {
		  int targetH;
		  float percentage;
		  percentage = (float) (targetW) / (float) bitmap.getWidth();
		  targetH = (int)(Math.ceil(bitmap.getHeight() * percentage));
		  return Bitmap.createScaledBitmap(bitmap, targetW, targetH, true);
		}

	
}
