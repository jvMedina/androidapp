package com.hackaton.food4thought;

import java.util.Locale;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.hackaton.food4thought.controller.MapLocationController;


public class MapFragment extends Fragment implements  OnInitListener{
	//static variable
	private static final String RECOGNIZE = "RECOGNIZE";
	private static final String IGNORE = "IGNORE";
	private View view;
	private int selection;
	private static final int SPEECH_CODE = 3; 
	private final static String MAP_TYPE = "selection";
	private static final String TAG = null;
	private TextToSpeech tts;
	private  GoogleMap mMap;
	private UiSettings mapSetting;
	private  Double latitude, longitude;
	private MapLocationController mapController;
	private int locationType;
	public static final MapFragment newInstance(int type){
		MapFragment fragment = new MapFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(MAP_TYPE, type);
		fragment.setArguments(bundle);
		return fragment ;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_map, null);
		tts = new TextToSpeech(getActivity(), this);
		Bundle bundle = this.getArguments();
		locationType = bundle.getInt(MAP_TYPE, 0);
		setUpMapIfNeeded();
		return view;
	}
	/***** Sets up the map if it is possible to do so *****/
	public void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) FragmentChangeActivity.fragmentManager
					.findFragmentById(R.id.location_map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null)
				setUpMap();
		}
	}

	private  void setUpMap() {
		Log.i(TAG, "set up map ");
		mapController = new MapLocationController(view, getActivity(), mMap, this);
	}	

	public MapLocationController getMapController() {
		return mapController;
	}
	public GoogleMap getmMap() {
		return mMap;
	}

	//	@Override
	//	public void onViewCreated(View view, Bundle savedInstanceState) {
	//		// TODO Auto-generated method stub
	//		if (mMap != null)
	//			setUpMap();
	//
	//		if (mMap == null) {
	//			// Try to obtain the map from the SupportMapFragment.
	//			mMap = ((SupportMapFragment) FragmentChangeActivity.fragmentManager
	//					.findFragmentById(R.id.location_map)).getMap();
	//			// Check if we were successful in obtaining the map.
	//			if (mMap != null)
	//				setUpMap();
	//		}
	//	}

	/**** The mapfragment's id must be removed from the FragmentManager
	 **** or else if the same it is passed on the next time then 
	 **** app will crash ****/
	@Override
	public void onDestroyView() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroyView();

		//		if (mMap != null) {
			//			FragmentChangeActivity.fragmentManager.beginTransaction()
		//			.remove(FragmentChangeActivity.fragmentManager.findFragmentById(R.id.location_map)).commit();
		//			mMap = null;
		//		}
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			// tts.setPitch(5); // set pitch level

			// tts.setSpeechRate(2); // set speech speed rate

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "Language is not supported");
			}else{
				Log.i(TAG, "tts initialized");
			}

		} else {
			Log.e("TTS", "Initilization Failed");
		}

	}



	public void speakOut(String data) {

		tts.speak(data, TextToSpeech.QUEUE_FLUSH, null);
	}

}
