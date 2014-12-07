package com.hackaton.food4thought.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hackaton.food4thought.R;
import com.hackaton.food4thought.base.FileUtils;
import com.hackaton.food4thought.constant.ConstantValue;
import com.hackaton.food4thought.constant.GlobalVariables;
import com.hackaton.food4thought.database.Database;
import com.hackaton.food4thought.database.LocationHandler;
import com.hackaton.food4thought.database.LocationInfoHandler;
import com.hackaton.food4thought.database.LocationTypeHandler;
import com.hackaton.food4thought.database.SubLocationHandler;

public class MapLocationModel {

	private static final String TAG = MapLocationModel.class.getSimpleName();
	private Activity mActivity;
	private Database db;

	public MapLocationModel(Activity mActivity) {
		this.mActivity = mActivity;
		db = new Database(mActivity);
		if(db.getLocationCount() <= 0)
			addLocation();
		if(db.getLocationTypeCount() <= 0)
			addLocationType();
		db.getDatabase(mActivity);
	}

	public void addLocation(){
		
	}

	public void addLocationType(){
		
	}


	public ArrayList<LocationTypeHandler> getAllLocationType(){
		return db.getAllLocationType();
	}
	public ArrayList<LocationHandler> getLocation(int locationType, int specificLoc) {
		ArrayList<LocationHandler> locations = new ArrayList<LocationHandler>();
		if(specificLoc > 0){
			locations.add(db.getLocation(specificLoc));
		}else{
			if(locationType == 0)
				locations  = db.getAllLocation();
			else{
				locations = db.getAllLocationByType(locationType);
			}
		}
		Log.i(TAG, "locations isze " + locations.size());
		return locations;
	}

	public LocationTypeHandler getTypeInfo(int type) {
		return db.getLocationType(type);
	}

	public LocationHandler getLocationData(int locationID) {
		return db.getLocation(locationID);
	}

	public ArrayList<LocationInfoHandler> getLocationInfoData(int locationID) {
		return db.getAllLocationInfoByLocationID(locationID);
	}

	public SubLocationHandler getSubLocationData(int locationID) {
		return db.getSubLocation(locationID);
	}
	public ArrayList<SubLocationHandler> getAllSubLocationData(int locationID) {
		return db.getAllSubLocationByLocationID(locationID);
	}

	public static Bitmap getBitmap(Context context, int image){
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(),image);
		Log.i(TAG, "converting to bitmap : " + image  + " bitmap data " + bm);
		return bm;
	}



}
