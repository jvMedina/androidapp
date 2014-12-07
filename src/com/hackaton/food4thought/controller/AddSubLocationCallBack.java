package com.hackaton.food4thought.controller;

import com.hackaton.food4thought.database.SubLocationHandler;


public interface AddSubLocationCallBack {
	public void addLocation(SubLocationHandler location);
	public void markerView();
}
