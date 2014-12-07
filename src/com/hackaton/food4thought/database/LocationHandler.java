package com.hackaton.food4thought.database;

import android.util.Log;

public class LocationHandler{
	private final static String TAG = "Location";
	int id; 
	int base_id; 
	String date_entry; 
	double longitude; 
	double latitude; 
	String name; 
	String type;
	String description;
	String bestSeller;
	String website;
	String searchTag;
	double amountables;

	/**======================= Default Constructor ========================**/

	public LocationHandler(){

	}

	public LocationHandler(int id, int base_id, String date_entry, double latitude,  double longitude, String name, String type,
			String searchTag, double amountables, String description, String bestSeller, String webSite){
		this.id = id;
		this.base_id = base_id;
		this.date_entry = date_entry;
		this.longitude = longitude;
		this.latitude = latitude;
		this.name = name;
		this.type = type;
		this.amountables = amountables;
		this.searchTag = searchTag;
		this.description = description;
		this.bestSeller  = bestSeller;
		this.website = webSite;
	}

	public LocationHandler( int base_id, String date_entry, double latitude,  double longitude, String name, String type, 
			String searchTag, double amountables, String description, String bestSeller, String webSite){
		this.base_id = base_id;
		this.date_entry = date_entry;
		this.longitude = longitude;
		this.latitude = latitude;
		this.name = name;
		this.type = type;
		this.amountables = amountables;
		this.searchTag = searchTag;
		this.description = description;
		this.bestSeller  = bestSeller;
		this.website = webSite;
		Log.i(TAG, "Add location data : " + base_id + " date " + date_entry + " lat lon " + latitude + " " + longitude + " name " + name + " ty[e" + type);
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

	public double getLongitude(){
		return longitude;
	}
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLatitude(){
		return latitude;
	}
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type = type;
	}

	public String getSearchTag() {
		return searchTag;
	}

	public void setSearchTag(String searchTag) {
		this.searchTag = searchTag;
	}

	public double getAmountables() {
		return amountables;
	}

	public void setAmountables(double amountables) {
		this.amountables = amountables;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBestSeller() {
		return bestSeller;
	}

	public void setBestSeller(String bestSeller) {
		this.bestSeller = bestSeller;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
	
	
	
	
}