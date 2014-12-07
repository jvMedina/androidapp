package com.hackaton.food4thought.database;

import android.graphics.Bitmap;

public class LocationTypeHandler{
	private final static String TAG = "LocationType";
	int id; 
	int base_id; 
	String date_entry; 
	Bitmap image_resource; 
	String image_url; 
	String name; 
	String bg_color;
	Bitmap pin_resource = null;


	/**======================= Default Constructor ========================**/

	public LocationTypeHandler(){

	}

	public LocationTypeHandler(int id, int base_id, String date_entry, Bitmap image_resource, String image_url, String name, String bg_color, Bitmap pin_resource){
		this.id = id;
		this.base_id = base_id;
		this.date_entry = date_entry;
		this.image_resource = image_resource;
		this.image_url = image_url;
		this.name = name;
		this.bg_color = bg_color;
		this.pin_resource = pin_resource;
	}

	public LocationTypeHandler( int base_id, String date_entry, Bitmap image_resource, String image_url, String name, String bg_color, Bitmap pin_resource){
		this.base_id = base_id;
		this.date_entry = date_entry;
		this.image_resource = image_resource;
		this.image_url = image_url;
		this.name = name;
		this.bg_color = bg_color;
		this.pin_resource = pin_resource;
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

	public Bitmap getImage_resource(){
		return image_resource;
	}
	public void setImage_resource(Bitmap image_resource){
		this.image_resource = image_resource;
	}

	public String getImage_url(){
		return image_url;
	}
	public void setImage_url(String image_url){
		this.image_url = image_url;
	}

	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

	public String getBg_color() {
		return bg_color;
	}

	public void setBg_color(String bg_color) {
		this.bg_color = bg_color;
	}

	public Bitmap getPin_resource() {
		return pin_resource;
	}

	public void setPin_resource(Bitmap pin_resource) {
		this.pin_resource = pin_resource;
	}
	
	
}