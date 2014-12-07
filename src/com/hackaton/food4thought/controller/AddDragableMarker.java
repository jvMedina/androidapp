package com.hackaton.food4thought.controller;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hackaton.food4thought.constant.ConstantValue;

public class AddDragableMarker {
	protected static final String TAG = AddDragableMarker.class.getSimpleName();
	private GoogleMap map;
	private LatLng initialLocation;
	MarkerOptions markerOptions;
	private AddDragMarkerCallback callback;
	private CameraPosition cameraPosition;
	public AddDragableMarker(GoogleMap map,  LatLng initialLocation, AddDragMarkerCallback callback ) {
		this.map = map;
		this.initialLocation = initialLocation;
		this.callback = callback;
		init();
	}

	private void init() {
		setUpMap();
		setUpFunctions();
	}

	private void setUpMap() {
		map.clear();
		markerOptions  = new MarkerOptions();
		markerOptions.position(initialLocation);
		markerOptions.snippet("Tap to add");
		markerOptions.title("My Location");
		markerOptions.draggable(true);
		
		Marker marker = map.addMarker(markerOptions);
		cameraPosition = CameraPosition.builder()
                .target(initialLocation)
                .zoom(13)
                .bearing(90)
                .build();
		
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
				2000, null);
		map.setInfoWindowAdapter(null);

	}
	private void setUpFunctions() {
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				Log.i(TAG, "add ");
				LatLng lastlocationLatLng = marker.getPosition();
				Log.i(TAG, "lat long : " + lastlocationLatLng.latitude + " long " + lastlocationLatLng.longitude);
				callback.location(lastlocationLatLng);
			}
		});
		map.setOnMarkerDragListener(new OnMarkerDragListener() {

			@Override
			public void onMarkerDragStart(Marker marker) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				Log.i(TAG, "drag end");
			}

			@Override
			public void onMarkerDrag(Marker marker) {
				// TODO Auto-generated method stub

			}
		});
	}


}
