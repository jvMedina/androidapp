package com.hackaton.food4thought.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hackaton.food4thought.FragmentChangeActivity;
import com.hackaton.food4thought.LocationInfoFragment;
import com.hackaton.food4thought.MapFragment;
import com.hackaton.food4thought.R;
import com.hackaton.food4thought.adapter.InstructionsAdapter;
import com.hackaton.food4thought.adapter.MapWindowAdapter;
import com.hackaton.food4thought.constant.ConstantValue;
import com.hackaton.food4thought.database.LocationHandler;
import com.hackaton.food4thought.database.MarkerData;
import com.hackaton.food4thought.database.MarkerHandler;
import com.hackaton.food4thought.model.GPSTracker;
import com.hackaton.food4thought.model.MapLocationModel;
import com.tyczj.mapnavigator.Legs;
import com.tyczj.mapnavigator.Navigator;
import com.tyczj.mapnavigator.Navigator.OnResult;
import com.tyczj.mapnavigator.Route;
import com.tyczj.mapnavigator.Steps;


public class MapLocationController{

	private static final String TAG = MapLocationController.class.getSimpleName();
	private View view;
	private Activity mActivity;
	private GoogleMap mMap, tempMap;
	private MapLocationModel mModel;
	private ArrayList<LocationHandler> locationList = new  ArrayList<LocationHandler>();
	private Map<Marker, LocationHandler> eventMarkerMap = new HashMap<Marker, LocationHandler>();
	private HashMap<String, LocationHandler> markers= new HashMap<String, LocationHandler>();
	private ArrayList<MarkerHandler> markerHandler = new ArrayList<MarkerHandler>();
	private int locationType;
	private  CameraPosition cameraPosition;
	private FragmentChangeActivity mainFragment;
	private GPSTracker gpsTracker;
	private LatLng userLocation;
	private int specificLoc = 0;
	private boolean locationIsAvailable = true;
	private MapFragment mapFragment;
	private LinearLayout travelInfo;
	private ListView travelInstructionsList;
	private TextView distance, time, viewInstruction;
	private ArrayList<Steps> instructions = new ArrayList<Steps>();
	private InstructionsAdapter instructionsAdapter;
	private boolean isViewInstruction;
	private LoadMapData loadMapData;
	public MapLocationController(View view, Activity mActivity, GoogleMap mMap, MapFragment fragment) {
		this.view = view;
		this.mapFragment = fragment;
		this.mMap = mMap;
		this.tempMap = mMap;
		mModel = new MapLocationModel(mActivity);
		mainFragment = (FragmentChangeActivity) mActivity;
		gpsTracker = new GPSTracker(mActivity);
		travelInfo = (LinearLayout) view.findViewById(R.id.location_info);
		travelInstructionsList = (ListView) view.findViewById(R.id.instruction_list);
		this.mActivity = mActivity;
		distance = (TextView) view.findViewById(R.id.distance);
		time = (TextView) view.findViewById(R.id.time);
		viewInstruction = (TextView) view.findViewById(R.id.view_intruction_btn);
		init();
	}

	public void init() {
		if(gpsTracker.canGetLocation()){
			Log.i(TAG, "can get locations");
			userLocation = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
			locationType = 0;
			setUpMapOption();
			addDatabaseLocation(locationType, 0);
			setDefaultLocation();
			travelInfo.setVisibility(View.GONE);
			//			showLocationFromUser(ConstantValue.MARIKINA_LONG_LAT);
		}else{
			Log.i(TAG, "cant get locations");
			gpsTracker.showSettingsAlert();
		}
		//		

	}

	public void showLocationFromUser(final LocationHandler locData, int mode) {
		//		if(locationIsAvailable){

		mMap.clear();
//		drawPerimeter();
		Log.i(TAG, "drawing loctaion " + gpsTracker.getLatitude() + " al " + gpsTracker.getLongitude());
		Navigator nav = new Navigator(mMap, new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()) ,new LatLng(locData.getLatitude(), locData.getLongitude()));
		nav.setMode(mode, 0, 0);
		nav.findDirections(true, new OnResult() {

			@Override
			public ArrayList<Route> getRoute(ArrayList<Route> routes) {
				if(routes.size() > 0){
					Log.i(TAG, "item distance : " + routes.get(0).getLegs().get(0).getLegDistance());
					String distanceString, timeString;
					travelInfo.setVisibility(View.VISIBLE);
					Route routeData = routes.get(0);
					Legs legData = routeData.getLegs().get(0);
					ArrayList<Steps> stepsData = legData.getSteps();
					distanceString = legData.getLegDistance();
					timeString = legData.getLegDuration();
					for(Steps step : stepsData){
						Log.i(TAG, "steps : instruction " + step.getStepInstructions());

					}

					instructions.clear();
					instructions = stepsData;
					view.findViewById(R.id.view_intruction_btn).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							isViewInstruction = !isViewInstruction;
							viewInstruction();

						}
					});
					distance.setText(routes.get(0).getLegs().get(0).getLegDistance());
					time.setText(routes.get(0).getLegs().get(0).getLegDuration());
					mapFragment.speakOut("Your location from " + locData.getName() + " is..." + routes.get(0).getLegs().get(0).getLegDistance() + " with a travel time of : " + routes.get(0).getLegs().get(0).getLegDuration());
				}
				return null;
			}
		});
		double distance =  round(CalculationByDistance( new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), new LatLng(locData.getLatitude(), locData.getLongitude())), 2);


		Log.i(TAG, "distance between : " + distance);
		addDatabaseLocation(0, locData.getId());
		//		}else{
		//			Dialogs.showDialog(mActivity, ConstantValue.appName, "Sorry, this function is not available.", "Got it");
		//		}

	}

	public void viewInstruction(){
		if(isViewInstruction){
			viewInstruction.setText("Hide instruction");
			travelInstructionsList.setVisibility(View.VISIBLE);
			instructionsAdapter = new InstructionsAdapter(mapFragment.getActivity(), instructions);
			travelInstructionsList.setAdapter(instructionsAdapter);
		}else{
			viewInstruction.setText("View instruction");
			travelInstructionsList.setVisibility(View.GONE);
		}

	}

	private void setUpMapOption() {

		mMap.getUiSettings().setAllGesturesEnabled(true);
		mMap.setBuildingsEnabled(true);
		mMap.setMyLocationEnabled(true);
		mMap.setIndoorEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setTiltGesturesEnabled(true);
		mMap.getUiSettings().setZoomGesturesEnabled(true);

	}

	public void setDefaultLocation() {

		Circle circle = drawPerimeter();

		float[] distance = new float[2];
		Marker marikinaDefault = mMap.addMarker(new MarkerOptions()
		.position(userLocation)
		.draggable(false).visible(false));
		Log.i(TAG, "user location : " + userLocation.latitude + " " + userLocation.longitude);
		Location.distanceBetween( marikinaDefault.getPosition().latitude, marikinaDefault.getPosition().longitude,
				circle.getCenter().latitude, circle.getCenter().longitude, distance);

		//		if( distance[0] > circle.getRadius()  ){
		//			locationIsAvailable  = false;
		//			Dialogs.showDialog(mActivity, ConstantValue.appName, "Your location is not in marikina. Point to Point location will not be available", "Got it");
		//		} else {
		//						Dialogs.showDialog(mActivity, ConstantValue.appName, "You location is in marikina perimeter", "Got it");
		//			//			Toast.makeText(mActivity, "You location is in marikina perimeter", Toast.LENGTH_LONG).show();
		//		}

		CameraUpdate center=
				CameraUpdateFactory.newLatLng(userLocation);
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

		mMap.moveCamera(center);
		mMap.animateCamera(zoom);

		cameraPosition = CameraPosition.builder()
				.target( userLocation)
				.zoom(13)
				.bearing(0)
				.build();


	}


	private Circle drawPerimeter() {
		travelInfo.setVisibility(View.GONE);
		travelInstructionsList.setVisibility(View.GONE);
		Circle circle = mMap.addCircle(new CircleOptions()
		.center(ConstantValue.MARIKINA_LONG_LAT)
		.radius(3800)
		.strokeColor(Color.RED));
		return circle;
	}

	@SuppressLint("NewApi")
	public void addDatabaseLocation(int locationType, int id) {
		loadMapData = new LoadMapData(mActivity, locationType, id);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			loadMapData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			loadMapData.execute();
		//		mMap.clear();
		//		//		if(id > 0){
		//		//		LocationHandler locData = mModel.getLocationData(id);
		//		//		mMap.addMarker(new MarkerOptions().position(new LatLng(locData.getLongitude(),  locData.getLatitude())));
		//		//		Log.i(TAG, "Long lat of this id : " + locData.getLatitude() + " lat " + locData.getLatitude());
		//		//		}
		//		drawPerimeter();
		//		
		//		locationList = mModel.getLocation(locationType, id);
		//		Log.i(TAG, "locaiton list size : " + locationList.size() + " loc id " + id);
		//
		//		for(LocationHandler item : locationList){
		//			Log.i(TAG, "item types : " + item.getType());
		//			LocationTypeHandler locationTypeInfo = mModel.getTypeInfo(item.getType());
		//			MarkerOptions markerOptions  = new MarkerOptions();
		//			markerOptions.position(new LatLng(item.getLatitude(), item.getLongitude()));
		//			markerOptions.snippet(item.getName());
		//			markerOptions.title(item.getName());
		//			markerOptions.visible(true);
		//			if(locationTypeInfo != null){
		//				Log.i(TAG, "location data " + locationTypeInfo.getPin_resource());
		//				markerOptions.icon(BitmapDescriptorFactory.fromBitmap(locationTypeInfo.getPin_resource()));
		//			}
		//			Marker marker = mMap.addMarker(markerOptions);
		//			eventMarkerMap.put(marker, item);
		//			markers.put(marker.getId(), item);
		//		}
		//
		//		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
		//
		//			@Override
		//			public boolean onMarkerClick(Marker marker) {
		//				marker.showInfoWindow();
		//				return true;
		//			}
		//		});
		//
		//		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
		//
		//			@Override
		//			public void onInfoWindowClick(Marker marker) {
		//				LocationHandler locData = markers.get(marker.getId());
		//				mainFragment.setContent(LocationInfoFragment.newInstance(locData.getId(), ConstantValue.MAIN_LOC));
		//				mainFragment.showSecondaryMenu();
		//			}
		//		});
		//
		//		mMap.setInfoWindowAdapter(new MapWindowAdapter(this, mActivity));
		//		if(locationType == 0 ){
		//			try {
		//				mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
		//						2000, null);
		//			} catch (NullPointerException e) {
		//				Log.i(TAG, "location not found");
		//			}
		//
		//		}

	}

	public class LoadMapData extends AsyncTask<Void, MarkerData, ArrayList<MarkerData>>{
		private ProgressDialog pDialog = null;
		private int locationType ;
		private int id;
		private Activity activity;
		private boolean running =true;

		public LoadMapData(Activity activity, int locationType, int id){
			this.activity = activity;
			this.locationType = locationType;
			this.id = id;
		}
		@Override
		protected void onPreExecute() {
			if(locationType == 0 ){
				try {
					mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
							2000, null);
				} catch (NullPointerException e) {
					Log.i(TAG, "location not found");
				}

			}
//			drawPerimeter();
			super.onPreExecute();
		}
		@Override
		protected void onCancelled() {
			running  = false;
		}
		@Override
		protected ArrayList<MarkerData> doInBackground(Void... params) {

			ArrayList<MarkerData> markerDatas = new ArrayList<MarkerData>();
			while (running) {

				locationList = mModel.getLocation(locationType, id);
				Log.i(TAG, "locaiton list size : " + locationList.size() + " loc id " + id);

				for(LocationHandler item : locationList){
					Log.i(TAG, "item types : " + item.getType());
//					LocationTypeHandler locationTypeInfo = mModel.getTypeInfo(item.getType());
					MarkerOptions markerOptions  = new MarkerOptions();
					markerOptions.position(new LatLng(item.getLatitude(), item.getLongitude()));
					markerOptions.snippet(item.getName());
					markerOptions.title(item.getName());
					markerOptions.visible(true);
//					if(locationTypeInfo != null){
//						Log.i(TAG, "location data " + locationTypeInfo.getPin_resource());
//						markerOptions.icon(BitmapDescriptorFactory.fromBitmap(locationTypeInfo.getPin_resource()));
//					}
					markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.market_pin));
					markerDatas.add(new MarkerData(markerOptions, item));
				}// does the hard work
				break;
			}



			//				}
			//			});
			return markerDatas;
		}

		@Override
		protected void onProgressUpdate(MarkerData... values) {
		}

		@Override
		protected void onPostExecute(ArrayList<MarkerData> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mMap.clear();
			Log.i(TAG," map result size: " + result.size());
			for(MarkerData item : result){ 
				Marker marker = mMap.addMarker(item.getMarkerOptions());
				eventMarkerMap.put(marker, item.getLocation());
				markers.put(marker.getId(),  item.getLocation());
			}
			mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {
					marker.showInfoWindow();
					return true;
				}
			});

			mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker marker) {
					LocationHandler locData = markers.get(marker.getId());
					mainFragment.setContent(LocationInfoFragment.newInstance(locData.getId(), ConstantValue.MAIN_LOC));
					mainFragment.showSecondaryMenu();
				}
			});

			mMap.setInfoWindowAdapter(new MapWindowAdapter(MapLocationController.this, mActivity, userLocation));
			if(locationType == 0 ){
				try {
					mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
							2000, null);
				} catch (NullPointerException e) {
					Log.i(TAG, "location not found");
				}

			}
		}
	} 


	public void animateMarker(final Marker marker, final LatLng toPosition,
			final boolean hideMarker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = mMap.getProjection();
		Point startPoint = proj.toScreenLocation(marker.getPosition());
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 500;

		final Interpolator interpolator = new LinearInterpolator();

		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * toPosition.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * toPosition.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));

				if (t < 1.0) {
					// Post again 16ms later.
					handler.postDelayed(this, 16);
				} else {
					if (hideMarker) {
						marker.setVisible(false);
					} else {
						marker.setVisible(true);
					}
				}
			}
		});
	}

	public Map<Marker, LocationHandler> getEventMarkerMap() {
		return eventMarkerMap;
	}

	public HashMap<String, LocationHandler> getMarkers() {
		return markers;
	}

	public ArrayList<MarkerHandler> getMarkerHandler() {
		return markerHandler;
	}

	public void hideMarkers() {
		mMap.clear();
		//		setUpMapOption();
		//		setDefaultLocation();
	}
	public void displaySingleLocation(int id) {
		specificLoc = id;
	}

	public double CalculationByDistance(LatLng StartP, LatLng EndP) {
		int Radius=6371;//radius of earth in Km         
		double lat1 = StartP.latitude;
		double lat2 = EndP.latitude;
		double lon1 = StartP.longitude;
		double lon2 = EndP.longitude;
		double dLat = Math.toRadians(lat2-lat1);
		double dLon = Math.toRadians(lon2-lon1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
				Math.sin(dLon/2) * Math.sin(dLon/2);
		double c = 2 * Math.asin(Math.sqrt(a));
		double valueResult= Radius*c;
		double km=valueResult/1;
		DecimalFormat newFormat = new DecimalFormat("####");
		int kmInDec =  Integer.valueOf(newFormat.format(km));
		double meter=valueResult%1000;
		int  meterInDec= Integer.valueOf(newFormat.format(meter));
		Log.i("Radius Value",""+valueResult+"   KM  "+kmInDec+" Meter   "+meterInDec);

		return Radius * c;
	}
	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	public GPSTracker getGpsTracker() {
		return gpsTracker;
	}
}
