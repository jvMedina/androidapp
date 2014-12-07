package com.hackaton.food4thought;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hackaton.food4thought.adapter.LocationInfoAdapter;
import com.hackaton.food4thought.constant.ConstantValue;
import com.hackaton.food4thought.constant.GlobalVariables;
import com.hackaton.food4thought.controller.AddLocationInfoCallBack;
import com.hackaton.food4thought.database.Database;
import com.hackaton.food4thought.database.LocationHandler;
import com.hackaton.food4thought.database.LocationInfoHandler;
import com.hackaton.food4thought.database.SubLocationHandler;
import com.hackaton.food4thought.dialogs.AddLocationContentDialog;
import com.hackaton.food4thought.dialogs.AddSubLocationDialog;
import com.hackaton.food4thought.model.MapLocationModel;
import com.randr.utils.Dialogs;


public class LocationInfoFragment extends Fragment implements OnClickListener{

	private View view;
	private FragmentChangeActivity mainFragment;
	private int locationID, layoutType;
	private final static String LOCATION_ID = "selection";
	private static final String TAG = LocationInfoFragment.class.getSimpleName();
	private static final String LAYOUT_TYPE = "layout_type";
	private ArrayList<LocationInfoHandler> locationInfoData = new ArrayList<LocationInfoHandler>();
	private ArrayList<SubLocationHandler> subLocationData =  new ArrayList<SubLocationHandler>();
	private MapLocationModel mapLocationModel;
	private Database db;
	private ImageView showWay, addSubLocData, addLocationInfo;
	private AddLocationContentDialog locationDataDialog;
	private AddSubLocationDialog subLocationDialog;
	/** Widgets **/
	TextView title, decription, bestSeller, website;
	ListView  infoList;
	LinearLayout establishmentLayout;
	TextView subLoc[];


	public static final LocationInfoFragment newInstance(int selection, int type){
		LocationInfoFragment fragment = new LocationInfoFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(LOCATION_ID, selection);
		bundle.putInt(LAYOUT_TYPE, type);
		fragment.setArguments(bundle);
		return fragment ;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_content, null);
		Bundle bundle = this.getArguments();
		locationID = bundle.getInt(LOCATION_ID, 0);
		layoutType = bundle.getInt(LAYOUT_TYPE, 0);
		db = new Database(getActivity());
		init();
		return view;
	}

	private void init() {
		title = (TextView) view.findViewById(R.id.title);
		mapLocationModel = new MapLocationModel(getActivity());
		showWay = (ImageView) view.findViewById(R.id.show_way);
		addLocationInfo =  (ImageView) view.findViewById(R.id.add_loc_info);
		addSubLocData = (ImageView) view.findViewById(R.id.add_sub_loc);
		bestSeller = (TextView) view.findViewById(R.id.best_seller);
		decription = (TextView) view.findViewById(R.id.description);
		website = (TextView) view.findViewById(R.id.web_site);
		//		if(layoutType == ConstantValue.MAIN_LOC)
		setUpContent();
		//		else
		//			setUpEstablismentContent();
	}

	private void setUpEstablismentContent() {
		final SubLocationHandler subLocationHandler = mapLocationModel.getSubLocationData(locationID);
		ArrayList<LocationInfoHandler> locationInfoData = mapLocationModel.getLocationInfoData(locationID);

		title.setText(subLocationHandler.getName());
		addLocationInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				locationDataDialog = new AddLocationContentDialog(LocationInfoFragment.this,subLocationHandler.getId(), new AddLocationInfoCallBack() {

					@Override
					public void addData(LocationInfoHandler info) {
						db.addLocationInfo(info);
						//						infoList.removeFooterView(footer);

						locationDataDialog.dismiss();
						init();
					}
				});				
			}
		});

		//		addSubLocData.setOnClickListener(new OnClickListener() {
		//			
		//			@Override
		//			public void onClick(View v) {
		//
		//				mainFragment.toggle();
		//				subLocationDialog = new AddSubLocationDialog(getActivity(), new AddSubLocationCallBack() {
		//					
		//					@Override
		//					public void markerView() {
		//						
		//						new AddDragableMarker(mainFragment.getMapFrag().getmMap(), ConstantValue.MARIKINA_LONG_LAT, new AddDragMarkerCallback() {
		//							
		//							@Override
		//							public void location(LatLng location) {
		//								subLocationDialog.setLocation(location);
		//								mainFragment.showSecondaryMenu();
		//							}
		//						});
		//					}
		//					
		//					@Override
		//					public void addLocation(SubLocationHandler subLocation) {
		//						db.addSubLocation(subLocation);
		////						infoList.removeFooterView(footer);
		//						subLocationDialog.dismiss();
		//						mainFragment.setMapDisplay(0);
		//						init();
		//						
		//					}
		//				}, subLocationHandler);
		//			}
		//		});
		infoList.setAdapter(new LocationInfoAdapter(getActivity(), locationInfoData));
	}

	private void setUpContent() {

		final LocationHandler locationData = mapLocationModel.getLocationData(locationID);
		locationInfoData = mapLocationModel.getLocationInfoData(locationID);
		subLocationData = mapLocationModel.getAllSubLocationData(locationID);

		title.setText(locationData.getName());
		decription.setText(locationData.getDescription());
		website.setText(locationData.getWebsite());
		bestSeller.setText(locationData.getBestSeller());

		website.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String websiteString = website.getText().toString();
				if(URLUtil.isValidUrl(websiteString)){
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(websiteString));
					getActivity().startActivity(i);
				}else{
					Dialogs.showDialog(getActivity(), "Error!", "Invalid URL", "Ok");
				}
			}
		});
		//		/**Set visibitliy of establishments Layout **/
		//		View header = LayoutInflater.from(getActivity()).inflate(R.layout.view_paralax_header, null);
		//		final View footer = LayoutInflater.from(getActivity()).inflate(R.layout.footer_add_info, null);
		//		establishmentLayout = (LinearLayout) header.findViewById(R.id.establishments_layout);
		//		establishmentLayout.setVisibility(View.GONE);
		//		if(subLocationData.size() > 0){
		//			subLoc = new TextView[subLocationData.size()];
		//			establishmentLayout.setVisibility(View.VISIBLE);
		//			int itemCount = 0;
		//			for(SubLocationHandler item : subLocationData ){
		//				subLoc[itemCount] = new TextView(getActivity());
		//				subLoc[itemCount].setText(item.getName());
		//				subLoc[itemCount].setId(itemCount);
		//				subLoc[itemCount].setPadding(5, 0, 0, 0);
		//				//				subLoc[itemCount].setTextColor(getActivity().getResources().getColor(R.color.));
		//				subLoc[itemCount].setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_small));
		//				establishmentLayout.addView(subLoc[itemCount]);
		//				subLoc[itemCount].setOnClickListener(this);
		//				itemCount++;
		//			}
		//
		//		}
		//		infoList.addHeaderView(header);
		////		infoList.addFooterView(footer);
		//
		////		footer.findViewById(R.id.add_location_info).setOnClickListener(new OnClickListener() {
		////
		////			@Override
		////			public void onClick(View v) {
		////				
		////			}
		////		});
		//		addLocationInfo.setOnClickListener(new OnClickListener() {
		//			
		//			@Override
		//			public void onClick(View v) {
		//				locationDataDialog = new AddLocationContentDialog(LocationInfoFragment.this,locationData.getId(), new AddLocationInfoCallBack() {
		//
		//					@Override
		//					public void addData(LocationInfoHandler info) {
		//						db.addLocationInfo(info);
		////						infoList.removeFooterView(footer);
		//
		//						locationDataDialog.dismiss();
		//						init();
		//					}
		//				});				
		//			}
		//		});
		//		
		//		addSubLocData.setOnClickListener(new OnClickListener() {
		//			
		//			@Override
		//			public void onClick(View v) {
		//
		//				mainFragment.toggle();
		//				subLocationDialog = new AddSubLocationDialog(getActivity(), new AddSubLocationCallBack() {
		//					
		//					@Override
		//					public void markerView() {
		//						
		//						new AddDragableMarker(mainFragment.getMapFrag().getmMap(), ConstantValue.MARIKINA_LONG_LAT, new AddDragMarkerCallback() {
		//							
		//							@Override
		//							public void location(LatLng location) {
		//								subLocationDialog.setLocation(location);
		//								mainFragment.showSecondaryMenu();
		//							}
		//						});
		//					}
		//					
		//					@Override
		//					public void addLocation(SubLocationHandler subLocation) {
		//						db.addSubLocation(subLocation);
		////						infoList.removeFooterView(footer);
		//						subLocationDialog.dismiss();
		//						mainFragment.setMapDisplay(0);
		//						init();
		//						
		//					}
		//				}, locationData);
		//			}
		//		});
		////		footer.findViewById(R.id.add_sub_loc).setOnClickListener(new OnClickListener() {
		////
		////			@Override
		////			public void onClick(View v) {
		////				mainFragment.toggle();
		////				subLocationDialog = new AddSubLocationDialog(getActivity(), new AddSubLocationCallBack() {
		////					
		////					@Override
		////					public void markerView() {
		////						
		////						new AddDragableMarker(mainFragment.getMapFrag().getmMap(), ConstantValue.MARIKINA_LONG_LAT, new AddDragMarkerCallback() {
		////							
		////							@Override
		////							public void location(LatLng location) {
		////								subLocationDialog.setLocation(location);
		////								mainFragment.showSecondaryMenu();
		////							}
		////						});
		////					}
		////					
		////					@Override
		////					public void addLocation(SubLocationHandler subLocation) {
		////						db.addSubLocation(subLocation);
		////						infoList.removeFooterView(footer);
		////						subLocationDialog.dismiss();
		////						mainFragment.setMapDisplay(0);
		////						init();
		////						
		////					}
		////				}, locationData);
		////			}
		////		});
		//
		//		infoList.setAdapter(new LocationInfoAdapter(getActivity(), locationInfoData));
		//
		//		view.findViewById(R.id.show_way).setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) {
		//				mainFragment.showWay(locationData);
		//				mainFragment.getSlidingMenu().toggle();
		//			}
		//		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mainFragment = (FragmentChangeActivity) activity;
	}

	@Override
	public void onClick(View v) {
		for (int i = 0; i < subLocationData.size(); i++) {
			if(v.getId() == i){
				int id = subLocationData.get(i).getId();
				mainFragment.setContent(LocationInfoFragment.newInstance(id, ConstantValue.SUB_LOC));
				mainFragment.showSecondaryMenu();
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "on result : " + requestCode);
		if (resultCode == Activity.RESULT_OK) {
			String single_path = data.getStringExtra("single_path");
			Bitmap bitmap = null;
			switch (requestCode) {
			case ConstantValue.CATEGORY_LOCATION_ICON:
				bitmap = BitmapFactory.decodeFile(single_path);
				bitmap = GlobalVariables.scaleBitmap(bitmap, 50);
				locationDataDialog.setImageBit(bitmap);
				break;

			default:
				break;
			}
		}
	}

}
