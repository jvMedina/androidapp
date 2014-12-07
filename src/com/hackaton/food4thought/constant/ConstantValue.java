package com.hackaton.food4thought.constant;

import com.google.android.gms.maps.model.LatLng;




public class ConstantValue {
	
	
	public static final String appName = "MET:GPS";
	
	public static final String SHARE_PREFERENCE_NAME = "SharedPreference";
	
	//default longlat 14.799653, 121.065035
	public static final LatLng MARIKINA_LONG_LAT = new LatLng(14.650730000000000000, 121.102854600000000000) ;
//	public static final LatLng MARIKINA_LONG_LAT = new LatLng(14.799653, 121.065035) ;
	//Location types
	public final static int MALL 										= 1;
	public final static int ESTABLISHMENT 								= 2;
	public final static int MARKET 										= 3;
	public final static int BANK 										= 4;
	public final static int AMUSEMENT 									= 5;
	public static final int MAIN_LOC 									= 1;
	public static final int SUB_LOC 									= 2;
	
	//location mode
	public final static int DRIVING										= 0;
	public final static int TRANSIT										= 1;
	public final static int BICYCLE										= 2;
	public final static int WALKING										= 3;
	
	public final static int TOLLS										= 0;
	public final static int HIGHWAYS									= 1;
	//callbacks
	public final static int CATEGORY_LOCATION_ICON						= 1;
	public final static int CATEGORY_LOCATION_PIN						= 2;
	public static String userNumber;

	public final static String keyUserNum								= "user_number";

	public static final String LOGIN_URL =	 "http://devapi2.clinimedicus.com/food4thought/index.php/json/index/";

	public static final String CATEGORY_URL = "http://devapi2.clinimedicus.com/food4thought/index.php/json/index/";

	public static final String GET_ALL 									= "http://devapi2.clinimedicus.com/food4thought/index.php/json/index/";

	public static final String USER_ID 									= "user_id";
	
}
