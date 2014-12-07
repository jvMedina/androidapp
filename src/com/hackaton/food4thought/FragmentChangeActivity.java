package com.hackaton.food4thought;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hackaton.food4thought.adapter.SuggestionsAdapter;
import com.hackaton.food4thought.base.BaseActivity;
import com.hackaton.food4thought.constant.ConstantValue;
import com.hackaton.food4thought.constant.GlobalVariables;
import com.hackaton.food4thought.controller.AddDragMarkerCallback;
import com.hackaton.food4thought.controller.AddDragableMarker;
import com.hackaton.food4thought.controller.AddLocationCallBack;
import com.hackaton.food4thought.database.Database;
import com.hackaton.food4thought.database.LocationHandler;
import com.hackaton.food4thought.dialogs.AddLocationDialog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class FragmentChangeActivity extends BaseActivity {

	private Fragment mContent;
	public static FragmentManager fragmentManager;
	private SuggestionsAdapter suggestion;
	private MapFragment mapFrag;
	private Database db;
	AddLocationDialog addLocation;
	
	public FragmentChangeActivity() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the Above View
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			mContent = MapFragment.newInstance(1);	
		getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		getSlidingMenu().setShadowDrawable(null);
		getSupportActionBar().setIcon(android.R.color.transparent);
		// set the Above View
		setContentView(R.layout.content_frame);

		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();

		//		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, MenuFragment.newInstance(1))
		.commit();
		db = new Database(this);
		setContent(new MenuFragment());		

		fragmentManager = getSupportFragmentManager();
		mapFrag = (MapFragment) mContent;
		// customize the SlidingMenu

	}

	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
	}

	public void setMapDisplay(int type){
		mapFrag.getMapController().addDatabaseLocation(type, 0);
	}
	public void setDefaultLocaiton(){
		mapFrag.getMapController().init();
	}


	public void setContent(Fragment fragment){
		getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame_two, fragment)
		.commit();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		case R.id.search:
			setContent(SearchFragment.newInstance());
			showSecondaryMenu();
			return true;
		case R.id.add_location:
			addLocation = new AddLocationDialog(FragmentChangeActivity.this, new AddLocationCallBack() {

				@Override
				public void addLocation(LocationHandler location) {
					db.addLocation(location);
					mapFrag.getMapController().addDatabaseLocation(0, 0);
					Toast.makeText(FragmentChangeActivity.this, "Location successfuly added", Toast.LENGTH_SHORT).show();
					addLocation.dismiss();
				}

				@Override
				public void markerView() {
					AddDragableMarker addDragMarker = new AddDragableMarker(mapFrag.getmMap(), ConstantValue.MARIKINA_LONG_LAT, new AddDragMarkerCallback() {

						@Override
						public void location(LatLng location) {
							addLocation.setLocation(location);
						}
					});
				}
			});
			addLocation.show();
			//			showSecondaryMenu();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	private static final String[] COLUMNS = {
		BaseColumns._ID,
		SearchManager.SUGGEST_COLUMN_TEXT_1,
	};
	private static final String TAG = FragmentChangeActivity.class.getSimpleName();

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		Log.i(TAG, "data");
	}

	public void showSingleCat(LocationHandler locationData) {
		MarkerOptions markerOptions  = new MarkerOptions();
		markerOptions.position(ConstantValue.MARIKINA_LONG_LAT);
		markerOptions.snippet("Drag me");
		markerOptions.title("My Location");
		Marker marker = 	mapFrag.getmMap().addMarker(markerOptions);
		mapFrag.getMapController().animateMarker(marker, ConstantValue.MARIKINA_LONG_LAT, true);
	}

	public void displayLocation(int id) {
		mapFrag.getMapController().addDatabaseLocation(0, id);
	}

	public void showWay(final LocationHandler locationData) {
		final Dialog dialog = new Dialog(mContent.getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout.activity_select_location);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		dialog.findViewById(R.id.car).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mapFrag.getMapController().showLocationFromUser(locationData, ConstantValue.DRIVING);
				dialog.dismiss();
			}
		});
		dialog.findViewById(R.id.biking).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mapFrag.getMapController().showLocationFromUser(locationData, ConstantValue.BICYCLE);
				dialog.dismiss();
			}
		});
		dialog.findViewById(R.id.walk).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mapFrag.getMapController().showLocationFromUser(locationData, ConstantValue.WALKING);
				dialog.dismiss();
			}
		});

		dialog.show();
		
	}

	public MapFragment getMapFrag() {
		return mapFrag;
	}







}