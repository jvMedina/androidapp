package com.hackaton.food4thought.database;

import android.graphics.Bitmap;

public class LocationInfoHandler{
	private final static String TAG = "LocationInfo";
	int id; 
	int base_id; 
	String date_entry; 
	String content; 
	String title; 
	int location_id; 
	Bitmap image; 

	/**======================= Default Constructor ========================**/

	public LocationInfoHandler(){

	}

	public LocationInfoHandler(int id, int base_id, String date_entry, String content, String title, int location_id, Bitmap image){
		this.id = id;
		this.base_id = base_id;
		this.date_entry = date_entry;
		this.content = content;
		this.title = title;
		this.location_id = location_id;
		this.image = image;
	}

	public LocationInfoHandler( int base_id, String date_entry, String content, String title, int location_id, Bitmap image){
		this.base_id = base_id;
		this.date_entry = date_entry;
		this.content = content;
		this.title = title;
		this.location_id = location_id;
		this.image = image;
	}

	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}

	public int getBase_id(){
		return base_id;
	}
	public void setBase_id(int base_id){
		this.base_id = base_id;
	}

	public String getDate_entry(){
		return date_entry;
	}
	public void setDate_entry(String date_entry){
		this.date_entry = date_entry;
	}

	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content = content;
	}

	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}

	public int getLocation_id(){
		return location_id;
	}
	public void setLocation_id(int location_id){
		this.location_id = location_id;
	}

	public Bitmap getImage(){
		return image;
	}
	public void setImage(Bitmap image){
		this.image = image;
	}
}