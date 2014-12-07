package com.hackaton.food4thought.database;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.method.KeyListener;
import android.util.Log;

public class Database extends SQLiteOpenHelper { 
	private static final String database_MEF = "MEF"; 
	private static final String table_locationType = "locationType"; 

	private static final String key_id = "id";
	private static final String key_base_id = "base_id";
	private static final String key_date_entry = "date_entry";
	private static final String key_image_resource = "image_resource";
	private static final String key_image_url = "image_url";
	private static final String key_name = "name";
	private static final String key_color = "color";
	private static final String key_pin = "pin";


	/**
	 * ===================================================================================
	 * 							LOCATION TYPE
	 * ===================================================================================
	 */

	String CREATE_locationType_TABLE = "CREATE TABLE " + table_locationType+ " ( " + 
			key_id + " INTEGER PRIMARY KEY, " + 
			key_base_id + " INTEGER, " + 
			key_date_entry + " TEXT, " + 
			key_image_resource + " BLOB, " + 
			key_image_url + " TEXT, " + 
			key_name + " TEXT, " + 
			key_color + " TEXT, " + 
			key_pin + " BLOB);" ;
	//			key_pin + " BLOB, " + 
	//			"UNIQUE ( " + key_base_id + ") ON CONFLICT REPLACE);" ;

	public Database(Context context) { 
		super( context, database_MEF, null, 2); 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_locationType_TABLE);
		db.execSQL(CREATE_location_TABLE);
		db.execSQL(CREATE_locationInfo_TABLE);
		db.execSQL(CREATE_subLocation_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + table_locationType);
		db.execSQL("DROP TABLE IF EXISTS " + table_location);
		db.execSQL("DROP TABLE IF EXISTS " + table_locationInfo);
		db.execSQL("DROP TABLE IF EXISTS " + table_subLocation);
		onCreate(db);
	}
	/** ===================== CRUD ================ **/
	public void addLocationType(LocationTypeHandler locationTypeHandlerArray) { 
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put ( key_base_id, locationTypeHandlerArray.base_id);
		values.put ( key_date_entry, locationTypeHandlerArray.date_entry);
		values.put ( key_image_resource, convertToByte(locationTypeHandlerArray.image_resource));
		values.put ( key_image_url, locationTypeHandlerArray.image_url);
		values.put ( key_name, locationTypeHandlerArray.name);
		values.put ( key_color, locationTypeHandlerArray.bg_color);
		values.put ( key_pin, convertToByte(locationTypeHandlerArray.pin_resource));

		long data = db.insert( table_locationType, null, values);
		Log.i(TAG, "adding location type : "  + locationTypeHandlerArray.getBase_id() + " addded "+ data);
		db.close();
	}
	/** ===================== GET SINGLE DATA ================ **/
	public LocationTypeHandler getLocationType (long id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query( table_locationType, new String[] { 
				key_id, key_base_id, key_date_entry, key_image_resource, key_image_url, key_name, key_color, key_pin}, key_base_id + "=?", 
				new String[]{ String.valueOf(id) } , null, null, null, null);
		LocationTypeHandler locationTypeHandlerArray = null;
		if(cursor != null){
			if(cursor.moveToFirst()){
				locationTypeHandlerArray = new LocationTypeHandler(
						cursor.getInt(0), 
						cursor.getInt(1), 
						cursor.getString(2), 
						convertToBitmap(cursor.getBlob(3)),
						cursor.getString(4), 
						cursor.getString(5),
						cursor.getString(6),
						convertToBitmap(cursor.getBlob(7))
						);
				Log.i(TAG, "get locaitont ype : " + cursor.getBlob(7));
			}
		}
		db.close();
		return locationTypeHandlerArray; 
	}

	public boolean isLocationTypeNameExist(String name){
		boolean isNameExist  = false;
		String selectQuery = "SELECT * FROM " + table_locationType + " WHERE " + key_name + " = " + "'" + name + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor != null){
			if(cursor.getCount() > 0){
				isNameExist =  true;
			}
		}
		return isNameExist;
	}
	/** ===================== GET LATEST DATA ================ **/
	public LocationTypeHandler getLatestLocationType (){
		String selectQuery = "SELECT * FROM " + table_locationType + " ORDER BY " 
				+ key_date_entry + " DESC " + " LIMIT 1 " ;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor.getCount() == 0)
			return null;
		cursor.moveToFirst();
		LocationTypeHandler locationTypeHandlerArray = new LocationTypeHandler(
				cursor.getInt(0), 
				cursor.getInt(1), 
				cursor.getString(2), 
				convertToBitmap(cursor.getBlob(3)),
				cursor.getString(4), 
				cursor.getString(5),
				cursor.getString(6),
				convertToBitmap(cursor.getBlob(7))
				);
		db.close();
		return locationTypeHandlerArray;
	}

	/** ===================== GET ALL DATA ================ **/
	public ArrayList< LocationTypeHandler >getAllLocationType (){
		ArrayList< LocationTypeHandler> locationTypeList = new ArrayList< LocationTypeHandler>();
		String selectQuery = "SELECT * FROM " + table_locationType + " ORDER BY " + key_date_entry; 
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				LocationTypeHandler locationTypeHandlerArray = new LocationTypeHandler();
				locationTypeHandlerArray.setId(cursor.getInt(0)); 
				locationTypeHandlerArray.setBase_id(cursor.getInt(1)); 
				locationTypeHandlerArray.setDate_entry(cursor.getString(2)); 
				locationTypeHandlerArray.setImage_resource(convertToBitmap(cursor.getBlob(3))); 
				locationTypeHandlerArray.setImage_url(cursor.getString(4)); 
				locationTypeHandlerArray.setName(cursor.getString(5)); 
				locationTypeHandlerArray.setBg_color(cursor.getString(6)); 
				locationTypeHandlerArray.setPin_resource(convertToBitmap(cursor.getBlob(7))); 
				locationTypeList.add(locationTypeHandlerArray);
			} while (cursor.moveToNext());
		}

		db.close();
		return locationTypeList;
	}
	/** ===================== UPDATE DATA ================ **/
	public int updateLocationType(LocationTypeHandler locationTypeHandlerArray) {
		SQLiteDatabase db = this.getWritableDatabase(); 
		ContentValues values = new ContentValues(); 
		values.put ( key_id, locationTypeHandlerArray.id);
		values.put ( key_base_id, locationTypeHandlerArray.base_id);
		values.put ( key_date_entry, locationTypeHandlerArray.date_entry);
		values.put ( key_image_resource, convertToByte(locationTypeHandlerArray.image_resource));
		values.put ( key_image_url, locationTypeHandlerArray.image_url);
		values.put ( key_name, locationTypeHandlerArray.name);
		values.put ( key_color, locationTypeHandlerArray.bg_color);
		values.put ( key_pin, convertToByte(locationTypeHandlerArray.pin_resource));
		return db.update( table_locationType, values, key_base_id + " =? " ,
				new String []{ String.valueOf(locationTypeHandlerArray.getId())});
	}
	/** ===================== DELETE DATA ================ **/
	public void deleteLocationType(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(table_locationType, key_id + " = ? ", new String [] { String.valueOf(id) });
		db.close();
	}
	public void deleteLocationType(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(table_locationType, null, null);
		db.close();
	}
	/** ===================== GET COUNT ================ **/
	public int getLocationTypeCount(){
		String countQuery = "SELECT * FROM " + table_locationType;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		db.close();
		return count;
	}


	/**
	 * ===================================================================================
	 * 							LOCATION 
	 * ===================================================================================
	 */
	private static final String table_location = "location"; 

	private static final String key_longitude = "longitude";
	private static final String key_latitude = "latitude";
	private static final String key_type = "type";
	private static final String key_amountables = "amount";
	private static final String key_search_tag = "search_tag";
	private static final String key_description = "description";
	private static final String key_best_seller = "best_seller";
	private static final String key_web_site = "website";


	String CREATE_location_TABLE = "CREATE TABLE " + table_location+ " ( " + 
			key_id + " INTEGER PRIMARY KEY, " + 
			key_base_id + " INTEGER, " + 
			key_date_entry + " TEXT, " + 
			key_latitude + " REAL, " + 
			key_longitude + " REAL, " + 
			key_name + " TEXT, " + 
			key_type + " INTEGER, " + 
			key_search_tag + " TEXT, " +
			key_amountables + " INTEGER, " + 
			key_description + " TEXT, " + 
			key_best_seller + " TEXT, " +
			key_web_site + " TEXT);" ;
	//			key_type + " INTEGER, " + 
	//			"UNIQUE ( " + key_base_id + ") ON CONFLICT REPLACE);" ;

	/** ===================== CRUD ================ **/
	public void addLocation(LocationHandler locationHandlerArray) { 
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put ( key_base_id, locationHandlerArray.base_id);
		values.put ( key_date_entry, locationHandlerArray.date_entry);
		values.put ( key_latitude, locationHandlerArray.longitude);
		values.put ( key_longitude, locationHandlerArray.latitude);
		values.put ( key_name, locationHandlerArray.name);
		values.put ( key_type, locationHandlerArray.type);
		values.put ( key_search_tag, locationHandlerArray.getSearchTag());
		values.put ( key_amountables, locationHandlerArray.getAmountables());
		values.put ( key_description, locationHandlerArray.getDescription());
		values.put ( key_best_seller, locationHandlerArray.getBestSeller());
		values.put ( key_web_site, locationHandlerArray.getWebsite());
		long addLog = db.insert( table_location, null, values);
		Log.i(TAG, "add data : " + addLog);
		db.close();
	}
	/** ===================== GET SINGLE DATA ================ **/
	public LocationHandler getLocation (long id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query( table_location, new String[] { 
				key_id, key_base_id, key_date_entry, key_latitude, key_longitude, key_name, key_type, key_search_tag, key_amountables, key_description,
				key_best_seller, key_web_site}, key_id + "=?", 
				new String[]{ String.valueOf(id) } , null, null, null, null);
		LocationHandler locationHandlerArray  = null;
		Log.i(TAG, "cussor size : " + cursor.getCount() + " with id : " + id);
		if(cursor != null)
			if(cursor.moveToFirst()){
				locationHandlerArray = new LocationHandler(
						cursor.getInt(cursor.getColumnIndex(key_id)), 
						cursor.getInt(cursor.getColumnIndex(key_base_id)), 
						cursor.getString(cursor.getColumnIndex(key_date_entry)), 
						cursor.getDouble(cursor.getColumnIndex(key_longitude)), 
						cursor.getDouble(cursor.getColumnIndex(key_latitude)), 
						cursor.getString(cursor.getColumnIndex(key_name)), 
						cursor.getString(cursor.getColumnIndex(key_type)),
						cursor.getString(cursor.getColumnIndex(key_search_tag)),
						cursor.getDouble(cursor.getColumnIndex(key_amountables)),
						cursor.getString(cursor.getColumnIndex(key_description)),
						cursor.getString(cursor.getColumnIndex(key_best_seller)),
						cursor.getString(cursor.getColumnIndex(key_web_site))
						);
			}
		db.close();
		return locationHandlerArray; 
	}

	/** ===================== GET LATEST DATA ================ **/
	public LocationHandler getLatestLocation (){
		String selectQuery = "SELECT * FROM " + table_location + " ORDER BY " 
				+ key_date_entry + " DESC " + " LIMIT 1 " ;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor.getCount() == 0)
			return null;
		cursor.moveToFirst();
		LocationHandler locationHandlerArray = new LocationHandler(
				cursor.getInt(cursor.getColumnIndex(key_id)), 
				cursor.getInt(cursor.getColumnIndex(key_base_id)), 
				cursor.getString(cursor.getColumnIndex(key_date_entry)), 
				cursor.getDouble(cursor.getColumnIndex(key_latitude)), 
				cursor.getDouble(cursor.getColumnIndex(key_longitude)), 
				cursor.getString(cursor.getColumnIndex(key_name)), 
				cursor.getString(cursor.getColumnIndex(key_type)),
				cursor.getString(cursor.getColumnIndex(key_search_tag)),
				cursor.getDouble(cursor.getColumnIndex(key_amountables)),
				cursor.getString(cursor.getColumnIndex(key_description)),
				cursor.getString(cursor.getColumnIndex(key_best_seller)),
				cursor.getString(cursor.getColumnIndex(key_web_site))
				);
		db.close();
		return locationHandlerArray;
	}

	/** ===================== GET ALL DATA ================ **/
	public ArrayList< LocationHandler >getAllLocation (){
		ArrayList< LocationHandler> locationList = new ArrayList< LocationHandler>();
		String selectQuery = "SELECT * FROM " + table_location + " ORDER BY " + key_date_entry; 
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				LocationHandler locationHandlerArray = new LocationHandler();
				locationHandlerArray.setId(cursor.getInt(0)); 
				locationHandlerArray.setBase_id(cursor.getInt(1)); 
				locationHandlerArray.setDate_entry(cursor.getString(2)); 
				locationHandlerArray.setLongitude(cursor.getDouble(3)); 
				locationHandlerArray.setLatitude(cursor.getDouble(4)); 
				locationHandlerArray.setName(cursor.getString(5)); 
				locationHandlerArray.setType(cursor.getString(6)); 
				locationHandlerArray.setSearchTag(cursor.getString(7));
				locationHandlerArray.setAmountables(cursor.getDouble(8));
				locationHandlerArray.setDescription(cursor.getString(9));
				locationHandlerArray.setBestSeller(cursor.getString(10));
				locationHandlerArray.setWebsite(cursor.getString(11));
				locationList.add(locationHandlerArray);
			} while (cursor.moveToNext());
		}

		db.close();
		return locationList;
	}

	/** ===================== GET ALL DATA ================ **/
	public ArrayList< LocationHandler >getAllLocationFilter (String filter){
		ArrayList< LocationHandler> locationList = new ArrayList< LocationHandler>();
		String selectQuery = "SELECT * FROM " + table_location + " WHERE " + key_name + " LIKE + " + " '%" + filter + "%'" + " ORDER BY " + key_date_entry; 
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				LocationHandler locationHandlerArray = new LocationHandler();
				locationHandlerArray.setId(cursor.getInt(0)); 
				locationHandlerArray.setBase_id(cursor.getInt(1)); 
				locationHandlerArray.setDate_entry(cursor.getString(2)); 
				locationHandlerArray.setLongitude(cursor.getDouble(3)); 
				locationHandlerArray.setLatitude(cursor.getDouble(4)); 
				locationHandlerArray.setName(cursor.getString(5)); 
				locationHandlerArray.setType(cursor.getString(6));
				locationHandlerArray.setSearchTag(cursor.getString(7));
				locationHandlerArray.setSearchTag(cursor.getString(7));
				locationHandlerArray.setAmountables(cursor.getDouble(8));
				locationHandlerArray.setDescription(cursor.getString(9));
				locationHandlerArray.setBestSeller(cursor.getString(10));
				locationHandlerArray.setWebsite(cursor.getString(11));
				locationList.add(locationHandlerArray);
			} while (cursor.moveToNext());
		}
		db.close();
		return locationList;
	}

	/** ===================== GET ALL DATA ================ **/
	public ArrayList< LocationHandler >findNear (String tag, double lat, double lon){
		ArrayList< LocationHandler> locationList = new ArrayList< LocationHandler>();
		String selectQuery = "SELECT "+ table_location +".*, " +
				"(( " + lat +" - " + table_location+"."+ key_latitude +") * (" + lat +" - " + table_location+"."+ key_latitude +") + " +
				"(" + lon +" - " + table_location+"."+ key_longitude +") * (" + lon +" - " + table_location+"."+ key_longitude +"))" +
				" AS distance "+ 
				" FROM " + table_location + " LEFT JOIN " + table_locationType + " ON " +
				table_location +"."+key_type + " = " + table_locationType+ "." +key_base_id
				+ " WHERE " + table_locationType +"."+key_name + " LIKE " + "'%" + tag + "%'" +
				" ORDER BY distance";
		//					" ORDER BY " + "ABS( " + table_location +"." +key_latitude + " - " + lon + ") " + " AND " 
		//					 + "ABS( "  + table_location +"."+ key_longitude + " - " + lat + ") " + " DESC";
		Log.i(TAG, "near " + selectQuery);
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				LocationHandler locationHandlerArray = new LocationHandler();
				locationHandlerArray.setId(cursor.getInt(0)); 
				locationHandlerArray.setBase_id(cursor.getInt(1)); 
				locationHandlerArray.setDate_entry(cursor.getString(2)); 
				locationHandlerArray.setLongitude(cursor.getDouble(3)); 
				locationHandlerArray.setLatitude(cursor.getDouble(4)); 
				locationHandlerArray.setName(cursor.getString(5)); 
				locationHandlerArray.setType(cursor.getString(6));
				locationHandlerArray.setSearchTag(cursor.getString(7));
				locationHandlerArray.setAmountables(cursor.getDouble(8));
				locationHandlerArray.setDescription(cursor.getString(9));
				locationHandlerArray.setBestSeller(cursor.getString(10));
				locationHandlerArray.setWebsite(cursor.getString(11));
				locationList.add(locationHandlerArray);
			} while (cursor.moveToNext());
		}
		db.close();
		return locationList;
	}

	
	/** ===================== GET ALL DATA ================ **/
	public ArrayList< LocationHandler >findNear ( double lat, double lon){
		ArrayList< LocationHandler> locationList = new ArrayList< LocationHandler>();
		String selectQuery = "SELECT "+ table_location +".*, " +
				"(( " + lat +" - " + table_location+"."+ key_latitude +") * (" + lat +" - " + table_location+"."+ key_latitude +") + " +
				"(" + lon +" - " + table_location+"."+ key_longitude +") * (" + lon +" - " + table_location+"."+ key_longitude +"))" +
				" AS distance "+ 
				" FROM " + table_location + " LEFT JOIN " + table_locationType + " ON " +
				table_location +"."+key_type + " = " + table_locationType+ "." +key_base_id +
				" ORDER BY distance";
		//					" ORDER BY " + "ABS( " + table_location +"." +key_latitude + " - " + lon + ") " + " AND " 
		//					 + "ABS( "  + table_location +"."+ key_longitude + " - " + lat + ") " + " DESC";
		Log.i(TAG, "near " + selectQuery);
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				LocationHandler locationHandlerArray = new LocationHandler();
				locationHandlerArray.setId(cursor.getInt(0)); 
				locationHandlerArray.setBase_id(cursor.getInt(1)); 
				locationHandlerArray.setDate_entry(cursor.getString(2)); 
				locationHandlerArray.setLongitude(cursor.getDouble(3)); 
				locationHandlerArray.setLatitude(cursor.getDouble(4)); 
				locationHandlerArray.setName(cursor.getString(5)); 
				locationHandlerArray.setType(cursor.getString(6));
				locationHandlerArray.setSearchTag(cursor.getString(7));
				locationHandlerArray.setAmountables(cursor.getDouble(8));
				locationHandlerArray.setDescription(cursor.getString(9));
				locationHandlerArray.setBestSeller(cursor.getString(10));
				locationHandlerArray.setWebsite(cursor.getString(11));
				locationList.add(locationHandlerArray);
			} while (cursor.moveToNext());
		}
		db.close();
		return locationList;
	}



	/** ===================== GET ALL DATA ================ **/
	public ArrayList< LocationHandler >getAllLocationByType (int type){
		ArrayList< LocationHandler> locationList = new ArrayList< LocationHandler>();
		String selectQuery = "SELECT * FROM " + table_location + " WHERE " + key_type + " LIKE " + "'"+ type +","+"%'" + " ORDER BY " + key_date_entry ; 
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				LocationHandler locationHandlerArray = new LocationHandler();
				locationHandlerArray.setId(cursor.getInt(0)); 
				locationHandlerArray.setBase_id(cursor.getInt(1)); 
				locationHandlerArray.setDate_entry(cursor.getString(2)); 
				locationHandlerArray.setLongitude(cursor.getDouble(3)); 
				locationHandlerArray.setLatitude(cursor.getDouble(4)); 
				locationHandlerArray.setName(cursor.getString(5)); 
				locationHandlerArray.setType(cursor.getString(6)); 
				locationHandlerArray.setSearchTag(cursor.getString(7));
				locationHandlerArray.setAmountables(cursor.getDouble(8));
				locationHandlerArray.setDescription(cursor.getString(9));
				locationHandlerArray.setBestSeller(cursor.getString(10));
				locationHandlerArray.setWebsite(cursor.getString(11));
				locationList.add(locationHandlerArray);
			} while (cursor.moveToNext());
		}

		db.close();
		return locationList;
	}
	/** ===================== UPDATE DATA ================ **/
	public int updateLocation(LocationHandler locationHandlerArray) {
		SQLiteDatabase db = this.getWritableDatabase(); 
		ContentValues values = new ContentValues(); 
		values.put ( key_id, locationHandlerArray.id);
		values.put ( key_base_id, locationHandlerArray.base_id);
		values.put ( key_date_entry, locationHandlerArray.date_entry);
		values.put ( key_longitude, locationHandlerArray.longitude);
		values.put ( key_latitude, locationHandlerArray.latitude);
		values.put ( key_name, locationHandlerArray.name);
		values.put ( key_type, locationHandlerArray.type);
		return db.update( table_location, values, key_base_id + " =? " ,
				new String []{ String.valueOf(locationHandlerArray.getId())});
	}
	/** ===================== DELETE DATA ================ **/
	public void deleteLocation(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(table_location, key_id + " = ? ", new String [] { String.valueOf(id) });
		db.close();
	}
	public void deleteLocation(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(table_location, null, null);
		db.close();
	}
	/** ===================== GET COUNT ================ **/
	public int getLocationCount(){
		String countQuery = "SELECT * FROM " + table_location;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		db.close();
		return count;
	}


	/**
	 * ===================================================================================
	 * 							LOCATION INFO
	 * ===================================================================================
	 */
	private static final String table_locationInfo = "locationInfo"; 

	private static final String key_content = "content";
	private static final String key_title = "title";
	private static final String key_location_id = "location_id";
	private static final String key_image = "image";


	String CREATE_locationInfo_TABLE = "CREATE TABLE " + table_locationInfo+ " ( " + 
			key_id + " INTEGER PRIMARY KEY, " + 
			key_base_id + " INTEGER, " + 
			key_date_entry + " TEXT, " + 
			key_content + " TEXT, " + 
			key_title + " TEXT, " + 
			key_location_id + " INTEGER, " + 
			key_image + " BLOB);" ;
	//			key_image + " TEXT, " + 
	//			"UNIQUE ( " + key_base_id + ") ON CONFLICT REPLACE);" ;


	/** ===================== CRUD ================ **/
	public void addLocationInfo(LocationInfoHandler locationInfoHandlerArray) { 
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put ( key_base_id, locationInfoHandlerArray.base_id);
		values.put ( key_date_entry, locationInfoHandlerArray.date_entry);
		values.put ( key_content, locationInfoHandlerArray.content);
		values.put ( key_title, locationInfoHandlerArray.title);
		values.put ( key_location_id, locationInfoHandlerArray.location_id);
		values.put ( key_image, convertToByte(locationInfoHandlerArray.image));
		db.insert( table_locationInfo, null, values);
		db.close();
	}
	/** ===================== GET SINGLE DATA ================ **/
	public LocationInfoHandler getLocationInfo (long id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query( table_locationInfo, new String[] { 
				key_id, key_base_id, key_date_entry, key_content, key_title, key_location_id, key_image}, key_id + "=?", 
				new String[]{ String.valueOf(id) } , null, null, null, null);
		if(cursor != null)
			cursor.moveToFirst();
		LocationInfoHandler locationInfoHandlerArray = new LocationInfoHandler(
				cursor.getInt(0), 
				cursor.getInt(1), 
				cursor.getString(2), 
				cursor.getString(3), 
				cursor.getString(4), 
				cursor.getInt(5), 
				convertToBitmap(cursor.getBlob(6))
				);
		db.close();
		return locationInfoHandlerArray; 
	}

	/** ===================== GET LATEST DATA ================ **/
	public LocationInfoHandler getLatestLocationInfo (){
		String selectQuery = "SELECT * FROM " + table_locationInfo + " ORDER BY " 
				+ key_date_entry + " DESC " + " LIMIT 1 " ;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor.getCount() == 0)
			return null;
		cursor.moveToFirst();
		LocationInfoHandler locationInfoHandlerArray = new LocationInfoHandler(
				cursor.getInt(0), 
				cursor.getInt(1), 
				cursor.getString(2), 
				cursor.getString(3), 
				cursor.getString(4), 
				cursor.getInt(5), 
				convertToBitmap(cursor.getBlob(6))
				);
		db.close();
		return locationInfoHandlerArray;
	}

	/** ===================== GET ALL DATA ================ **/
	public ArrayList< LocationInfoHandler >getAllLocationInfo (){
		ArrayList< LocationInfoHandler> locationInfoList = new ArrayList< LocationInfoHandler>();
		String selectQuery = "SELECT * FROM " + table_locationInfo + " ORDER BY " + key_date_entry; 
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				LocationInfoHandler locationInfoHandlerArray = new LocationInfoHandler();
				locationInfoHandlerArray.setId(cursor.getInt(0)); 
				locationInfoHandlerArray.setBase_id(cursor.getInt(1)); 
				locationInfoHandlerArray.setDate_entry(cursor.getString(2)); 
				locationInfoHandlerArray.setContent(cursor.getString(3)); 
				locationInfoHandlerArray.setTitle(cursor.getString(4)); 
				locationInfoHandlerArray.setLocation_id(cursor.getInt(5)); 
				locationInfoHandlerArray.setImage(convertToBitmap(cursor.getBlob(6))); 
				locationInfoList.add(locationInfoHandlerArray);
			} while (cursor.moveToNext());
		}

		db.close();
		return locationInfoList;
	}
	/** ===================== GET ALL DATA ================ **/
	public ArrayList< LocationInfoHandler >getAllLocationInfoByLocationID (int locationID){
		ArrayList< LocationInfoHandler> locationInfoList = new ArrayList< LocationInfoHandler>();
		String selectQuery = "SELECT * FROM " + table_locationInfo + " WHERE " + key_location_id + " = " + locationID
				+ " ORDER BY " + key_date_entry; 
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				LocationInfoHandler locationInfoHandlerArray = new LocationInfoHandler();
				locationInfoHandlerArray.setId(cursor.getInt(0)); 
				locationInfoHandlerArray.setBase_id(cursor.getInt(1)); 
				locationInfoHandlerArray.setDate_entry(cursor.getString(2)); 
				locationInfoHandlerArray.setContent(cursor.getString(3)); 
				locationInfoHandlerArray.setTitle(cursor.getString(4)); 
				locationInfoHandlerArray.setLocation_id(cursor.getInt(5)); 
				locationInfoHandlerArray.setImage(convertToBitmap(cursor.getBlob(6))); 
				locationInfoList.add(locationInfoHandlerArray);
			} while (cursor.moveToNext());
		}

		db.close();
		return locationInfoList;
	}
	/** ===================== UPDATE DATA ================ **/
	public int updateLocationInfo(LocationInfoHandler locationInfoHandlerArray) {
		SQLiteDatabase db = this.getWritableDatabase(); 
		ContentValues values = new ContentValues(); 
		values.put ( key_id, locationInfoHandlerArray.id);
		values.put ( key_base_id, locationInfoHandlerArray.base_id);
		values.put ( key_date_entry, locationInfoHandlerArray.date_entry);
		values.put ( key_content, locationInfoHandlerArray.content);
		values.put ( key_title, locationInfoHandlerArray.title);
		values.put ( key_location_id, locationInfoHandlerArray.location_id);
		values.put ( key_image, convertToByte(locationInfoHandlerArray.image));
		return db.update( table_locationInfo, values, key_base_id + " =? " ,
				new String []{ String.valueOf(locationInfoHandlerArray.getId())});
	}
	/** ===================== DELETE DATA ================ **/
	public void deleteLocationInfo(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(table_locationInfo, key_id + " = ? ", new String [] { String.valueOf(id) });
		db.close();
	}
	/** ===================== GET COUNT ================ **/
	public int getLocationInfoCount(){
		String countQuery = "SELECT * FROM " + table_locationInfo;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		db.close();
		return count;
	}

	/**
	 * ===================================================================================
	 * 								SUB LOCATION 
	 * ===================================================================================
	 */

	private static final String table_subLocation = "subLocation";
	private static final String TAG = Database.class.getSimpleName(); 


	String CREATE_subLocation_TABLE = "CREATE TABLE " + table_subLocation+ " ( " + 
			key_id + " INTEGER PRIMARY KEY, " + 
			key_base_id + " INTEGER, " + 
			key_date_entry + " TEXT, " + 
			key_name + " TEXT, " + 
			key_longitude + " REAL, " + 
			key_latitude + " REAL, " + 
			key_location_id + " INTEGER);" ;
	//			key_location_id + " INTEGER, " + 
	//			"UNIQUE ( " + key_base_id + ") ON CONFLICT REPLACE);" ;

	/** ===================== CRUD ================ **/
	public void addSubLocation(SubLocationHandler subLocationHandlerArray) { 
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put ( key_base_id, subLocationHandlerArray.base_id);
		values.put ( key_date_entry, subLocationHandlerArray.date_entry);
		values.put ( key_name, subLocationHandlerArray.name);
		values.put ( key_longitude, subLocationHandlerArray.longitude);
		values.put ( key_latitude, subLocationHandlerArray.latitude);
		values.put ( key_location_id, subLocationHandlerArray.location_id);
		db.insert( table_subLocation, null, values);
		db.close();
	}
	/** ===================== GET SINGLE DATA ================ **/
	public SubLocationHandler getSubLocation (long id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query( table_subLocation, new String[] { 
				key_id, key_base_id, key_date_entry, key_name, key_longitude, key_latitude, key_location_id}, key_id + "=?", 
				new String[]{ String.valueOf(id) } , null, null, null, null);
		SubLocationHandler subLocationHandlerArray =null;
		if(cursor != null)
			if(cursor.moveToFirst()){
				subLocationHandlerArray = new SubLocationHandler(
						cursor.getInt(0), 
						cursor.getInt(1), 
						cursor.getString(2), 
						cursor.getString(3), 
						cursor.getDouble(4), 
						cursor.getDouble(5), 
						cursor.getInt(6)
						);
			}
		db.close();
		return subLocationHandlerArray; 
	}

	/** ===================== GET LATEST DATA ================ **/
	public SubLocationHandler getLatestSubLocation (){
		String selectQuery = "SELECT * FROM " + table_subLocation + " ORDER BY " 
				+ key_date_entry + " DESC " + " LIMIT 1 " ;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor.getCount() == 0)
			return null;
		cursor.moveToFirst();
		SubLocationHandler subLocationHandlerArray = new SubLocationHandler(
				cursor.getInt(0), 
				cursor.getInt(1), 
				cursor.getString(2), 
				cursor.getString(3), 
				cursor.getDouble(4), 
				cursor.getDouble(5), 
				cursor.getInt(6)
				);
		db.close();
		return subLocationHandlerArray;
	}

	/** ===================== GET ALL DATA ================ **/
	public ArrayList< SubLocationHandler >getAllSubLocation (){
		ArrayList< SubLocationHandler> subLocationList = new ArrayList< SubLocationHandler>();
		String selectQuery = "SELECT * FROM " + table_subLocation + " ORDER BY " + key_date_entry; 
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				SubLocationHandler subLocationHandlerArray = new SubLocationHandler();
				subLocationHandlerArray.setId(cursor.getInt(0)); 
				subLocationHandlerArray.setBase_id(cursor.getInt(1)); 
				subLocationHandlerArray.setDate_entry(cursor.getString(2)); 
				subLocationHandlerArray.setName(cursor.getString(3)); 
				subLocationHandlerArray.setLongitude(cursor.getDouble(4)); 
				subLocationHandlerArray.setLatitude(cursor.getDouble(5)); 
				subLocationHandlerArray.setLocation_id(cursor.getInt(6)); 
				subLocationList.add(subLocationHandlerArray);
			} while (cursor.moveToNext());
		}

		db.close();
		return subLocationList;
	}
	/** ===================== GET ALL DATA ================ **/
	public ArrayList< SubLocationHandler >getAllSubLocationByLocationID (int locationID){
		ArrayList< SubLocationHandler> subLocationList = new ArrayList< SubLocationHandler>();
		String selectQuery = "SELECT * FROM " + table_subLocation + " WHERE " + key_location_id + " = " + locationID + " ORDER BY " + key_date_entry; 
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				SubLocationHandler subLocationHandlerArray = new SubLocationHandler();
				subLocationHandlerArray.setId(cursor.getInt(0)); 
				subLocationHandlerArray.setBase_id(cursor.getInt(1)); 
				subLocationHandlerArray.setDate_entry(cursor.getString(2)); 
				subLocationHandlerArray.setName(cursor.getString(3)); 
				subLocationHandlerArray.setLongitude(cursor.getDouble(4)); 
				subLocationHandlerArray.setLatitude(cursor.getDouble(5)); 
				subLocationHandlerArray.setLocation_id(cursor.getInt(6)); 
				subLocationList.add(subLocationHandlerArray);
			} while (cursor.moveToNext());
		}

		db.close();
		return subLocationList;
	}
	/** ===================== UPDATE DATA ================ **/
	public int updateSubLocation(SubLocationHandler subLocationHandlerArray) {
		SQLiteDatabase db = this.getWritableDatabase(); 
		ContentValues values = new ContentValues(); 
		values.put ( key_id, subLocationHandlerArray.id);
		values.put ( key_base_id, subLocationHandlerArray.base_id);
		values.put ( key_date_entry, subLocationHandlerArray.date_entry);
		values.put ( key_name, subLocationHandlerArray.name);
		values.put ( key_longitude, subLocationHandlerArray.longitude);
		values.put ( key_latitude, subLocationHandlerArray.latitude);
		values.put ( key_location_id, subLocationHandlerArray.location_id);
		return db.update( table_subLocation, values, key_base_id + " =? " ,
				new String []{ String.valueOf(subLocationHandlerArray.getId())});
	}
	/** ===================== DELETE DATA ================ **/
	public void deleteSubLocation(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(table_subLocation, key_id + " = ? ", new String [] { String.valueOf(id) });
		db.close();
	}
	/** ===================== GET COUNT ================ **/
	public int getSubLocationCount(){
		String countQuery = "SELECT * FROM " + table_subLocation;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		db.close();
		return count;
	}



	public static byte[] convertToByte(Bitmap bmp){
		byte[] imageByte  = null;
		if(bmp != null){
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
			imageByte = stream.toByteArray();
		}

		return imageByte;
	}

	public static Bitmap convertToBitmap(byte[] imageData){

		Bitmap bm = null;
		if(imageData != null){
			bm = BitmapFactory.decodeByteArray(imageData, 0 ,imageData.length);
		}else{
			Log.i(TAG,"image is null");
		}
		return bm;
	}


	public static void backupDatabase() throws IOException {
		//Open your local db as the input stream
		String inFileName = "/data/data/com.tip.mefgps/databases/MEF";
		File dbFile = new File(inFileName);
		FileInputStream fis = new FileInputStream(dbFile);

		String outFileName = Environment.getExternalStorageDirectory()+"/MEF.sqlite3";
		//Open the empty db as the output stream
		OutputStream output = new FileOutputStream(outFileName);
		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = fis.read(buffer))>0){
			output.write(buffer, 0, length);
		}
		//Close the streams
		output.flush();
		output.close();
		fis.close();
	}

	public void getDatabase(Activity activity){
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "/data/" + activity.getPackageName() + "/databases/yourdatabasename";
				String backupDBPath = "backupname.db";
				File currentDB = new File(currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			}
		} catch (Exception e) {

		}
	}
}