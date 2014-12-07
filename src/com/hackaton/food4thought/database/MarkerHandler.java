package com.hackaton.food4thought.database;

import com.google.android.gms.maps.model.Marker;



public class MarkerHandler {
	private Marker marker;
	private int locationID;
	
	public MarkerHandler() {
		marker = null;
		locationID = 0;
	}
	
	public MarkerHandler(Marker marker , int locationID) {
		this.marker = marker;
		this.locationID = locationID;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public int getLocationID() {
		return locationID;
	}

	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}
	
	
}
