package com.hackaton.food4thought.controller;

import com.hackaton.food4thought.database.LocationHandler;


public interface AddLocationCallBack {
	public void addLocation(LocationHandler location);
	public void markerView();
}
