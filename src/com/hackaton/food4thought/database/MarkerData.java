package com.hackaton.food4thought.database;

import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerData {
	MarkerOptions markerOptions;
	LocationHandler location;
	
	public MarkerData() {
		// TODO Auto-generated constructor stub
	}

	public MarkerData(MarkerOptions markerOptions, LocationHandler location) {
		this.markerOptions = markerOptions;
		this.location = location;
	}

	public MarkerOptions getMarkerOptions() {
		return markerOptions;
	}

	public void setMarkerOptions(MarkerOptions markerOptions) {
		this.markerOptions = markerOptions;
	}

	public LocationHandler getLocation() {
		return location;
	}

	public void setLocation(LocationHandler location) {
		this.location = location;
	}
	
}
