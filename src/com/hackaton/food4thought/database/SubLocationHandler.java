package com.hackaton.food4thought.database;
public class SubLocationHandler{
	private final static String TAG = "SubLocation";
	int id; 
	int base_id; 
	String date_entry; 
	String name; 
	double longitude; 
	double latitude; 
	int location_id; 

	/**======================= Default Constructor ========================**/

	public SubLocationHandler(){

	}

	public SubLocationHandler(int id, int base_id, String date_entry, String name, double longitude, double latitude, int location_id){
		this.id = id;
		this.base_id = base_id;
		this.date_entry = date_entry;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.location_id = location_id;
	}

	public SubLocationHandler( int base_id, String date_entry, String name, double longitude, double latitude, int location_id){
		this.base_id = base_id;
		this.date_entry = date_entry;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.location_id = location_id;
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

	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
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

	public int getLocation_id(){
		return location_id;
	}
	public void setLocation_id(int location_id){
		this.location_id = location_id;
	}
}