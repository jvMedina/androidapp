package com.hackaton.food4thought;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hackaton.food4thought.adapter.CategoryAdapter;
import com.hackaton.food4thought.constant.ConstantValue;
import com.hackaton.food4thought.constant.GlobalVariables;
import com.hackaton.food4thought.controller.AddLocationCategoryCallBack;
import com.hackaton.food4thought.database.Database;
import com.hackaton.food4thought.database.LocationTypeHandler;
import com.hackaton.food4thought.dialogs.AddCategoryDialog;
import com.hackaton.food4thought.model.MapLocationModel;
import com.nostra13.universalimageloader.core.ImageLoader;


public class MenuFragment extends Fragment{

	private View view;
	private int selection;
	private FragmentChangeActivity mainFragment;
	private final static String SELECTION = "selection";
	private final static String TAG = MenuFragment.class.getSimpleName();
	//widgets
	private TextView appNameLbl;
	private ListView locationTypeGrid;
	private MapLocationModel locationModel;
	private AddCategoryDialog addCategoryDialog;
	ImageLoader imageLoader;
	private Database db;
	//arraylist
	private ArrayList<LocationTypeHandler> locationsType = new ArrayList<LocationTypeHandler>();
	private CategoryAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new Database(getActivity());
	}
	public static MenuFragment newInstance(int selection){
		MenuFragment fragment = new MenuFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(SELECTION, selection);
		fragment.setArguments(bundle);
		return fragment ;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_navigation_slide, null);
		init();
		return view;
	}

	private void init() {
		locationTypeGrid = (ListView) view.findViewById(R.id.category_list);
		locationModel = new MapLocationModel(getActivity());
		locationsType = locationModel.getAllLocationType();
		adapter = new CategoryAdapter(getActivity(), locationsType);
		locationTypeGrid.setAdapter(adapter);

		view.findViewById(R.id.title).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mainFragment.setDefaultLocaiton();
				mainFragment.getSlidingMenu().toggle();
			}
		});

		locationTypeGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				int typeId = adapter.getItem(position).getBase_id();
				Log.i(TAG, "location type  " + typeId);
				mainFragment.setMapDisplay(typeId);
				mainFragment.getSlidingMenu().toggle();
			}
		});

		view.findViewById(R.id.add_category).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addCategoryDialog = new AddCategoryDialog(getActivity(), new AddLocationCategoryCallBack() {

					@Override
					public void addData(String categoryName, Bitmap icon, Bitmap pin) {
						if(!db.isLocationTypeNameExist(categoryName)){
							Log.i(TAG, "location list item id : " + (db.getLocationTypeCount() + 3));
							if(icon == null){
								icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
							}
							if(pin == null){
								pin = BitmapFactory.decodeResource(getResources(), R.drawable.default_pin);
							}
							db.addLocationType(new LocationTypeHandler(db.getLocationTypeCount() + 3,  GlobalVariables.instanceOf().getDate(), icon, null, categoryName, "0000FF", pin));
							adapter.setItems( locationModel.getAllLocationType());
							adapter.notifyDataSetChanged();
							addCategoryDialog.dismiss();
						}else{
							Toast.makeText(getActivity(), "Name is already exist", Toast.LENGTH_SHORT).show();
						}
					}
				}, MenuFragment.this);
			}
		});
		View footer = LayoutInflater.from(getActivity()).inflate(R.layout.footer_add_category, null);
	}
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mainFragment = (FragmentChangeActivity) activity;
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
				bitmap = scaleBitmap(bitmap, 30);
				addCategoryDialog.setLocationIconBit(bitmap);
				break;
			case ConstantValue.CATEGORY_LOCATION_PIN:
				bitmap = BitmapFactory.decodeFile(single_path);
				bitmap = scaleBitmap(bitmap, 30);
				addCategoryDialog.setLocationPinBit(bitmap);
				break;

			default:
				break;
			}
		}
	}
	public static Bitmap scaleBitmap(Bitmap bitmap, int targetW) {
		  int targetH;
		  float percentage;
		  percentage = (float) (targetW) / (float) bitmap.getWidth();
		  targetH = (int)(Math.ceil(bitmap.getHeight() * percentage));
		  return Bitmap.createScaledBitmap(bitmap, targetW, targetH, true);
		}
}
