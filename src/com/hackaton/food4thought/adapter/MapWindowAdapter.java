package com.hackaton.food4thought.adapter;

import android.app.Activity;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.hackaton.food4thought.R;
import com.hackaton.food4thought.controller.MapLocationController;
import com.hackaton.food4thought.database.Database;
import com.hackaton.food4thought.database.LocationHandler;
import com.hackaton.food4thought.database.LocationTypeHandler;
import com.hackaton.food4thought.model.GPSTracker;

public class MapWindowAdapter implements InfoWindowAdapter{
	private static final String TAG = MapWindowAdapter.class.getSimpleName();
	private MapLocationController mapLocationController;
	private  Activity mActivity;
	private View view;
	private Database db;
	private LatLng gpsData;
	public MapWindowAdapter(MapLocationController mapLocationController, Activity mActivity, LatLng userLocation) {
		this.mapLocationController = mapLocationController;
		this.mActivity = mActivity;
		db= new Database(mActivity);
		this.gpsData = userLocation;
		view = mActivity.getLayoutInflater().inflate(R.layout.item_map_info, null);
	}

	@Override
	public View getInfoContents(Marker marker) {
		try {
			LocationHandler locData = mapLocationController.getMarkers().get(marker.getId());
			if(locData != null){

				TextView title = (TextView) view.findViewById(R.id.title);
				TextView location = (TextView) view.findViewById(R.id.long_lat);
				TextView type = (TextView) view.findViewById(R.id.location_type);
				title.setText(locData.getName());
				Log.i(TAG, "loc id : " + locData.getType());

				for(LocationTypeHandler item: db.getAllLocationType()){
					Log.i(TAG, "location types : " + item.getId() + " = " + locData.getType());
				}
//				LocationTypeHandler locationType = db.getLocationType(locData.getType());
				
				type.setText(locData.getBestSeller());
				
				Location locationA = new Location("User Location");

				locationA.setLatitude(gpsData.latitude);
				locationA.setLongitude(gpsData.longitude);

				Location locationB = new Location("Interest Location");

				locationB.setLatitude(locData.getLatitude());
				locationB.setLongitude(locData.getLongitude());

				float distance = locationA.distanceTo(locationB);
				location.setText(String.valueOf(distance));
//				type.setText(locationType.getName());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		return view;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		// TODO Auto-generated method stub
		return null;
	}

}
