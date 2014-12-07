package com.hackaton.food4thought;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;
import com.hackaton.food4thought.constant.ConstantValue;
import com.hackaton.food4thought.constant.GlobalVariables;
import com.hackaton.food4thought.database.Database;
import com.hackaton.food4thought.database.LocationHandler;
import com.hackaton.food4thought.database.LocationTypeHandler;
import com.randr.utils.Dialogs;
import com.randr.webservice.OnTaskCompleted;
import com.randr.webservice.ResultHandler;
import com.randr.webservice.SendDataHandler;
import com.randr.webservice.SendResultModel;

public class LoginUser extends SherlockActivity{

	private EditText phoneNumber;
	private ImageView sendBtn;
	private final String TAG = LoginUser.class.getSimpleName();

	private Database db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_login_number);
		db = new Database(this);
		init();
	}
	private void init() {
		setWidgets();
		setContents();
		setFunctions();
	}

	private void setWidgets() {
		phoneNumber = (EditText) findViewById(R.id.number);
		sendBtn = (ImageView) findViewById(R.id.send_btn);
	}
	private void setContents() {

	}
	private void setFunctions() {
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SendResultModel sendNumber = new SendResultModel(LoginUser.this, null, new OnTaskCompleted() {

					@Override
					public void setResult(ResultHandler result) {
						Log.i(TAG, "result : " + result.getResult());
						try {
							parseLoginResult(result.getResultJSON());
							getCategories();
							getAllData();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}


						//						try {
						//							JSONObject resultJson = new JSONObject(result.getResult());
						//							parseLoginResult(resultJson);
						//						} catch (JSONException e) {
						//							// TODO Auto-generated catch block
						//							e.printStackTrace();
						//						}

						//						Intent intent = new Intent(LoginUser.this, FragmentChangeActivity.class);
						//						startActivity(intent);

					}



					@Override
					public void onNetworkError(ResultHandler obj) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
					}
				});
				sendNumber.execute(new SendDataHandler(generateParams(), ConstantValue.LOGIN_URL, "POST"));
			}
		});
	}
	//get id
	private void parseLoginResult(JSONObject resultJson) throws JSONException {
		//		{"status":true,"data":4,"message":"Successfully Saved"}
		if(resultJson.getBoolean("status")){
			String number = phoneNumber.getText().toString();
			Editor editor = GlobalVariables.instanceOf().getSharePreference(this ).edit();
			editor.putInt(ConstantValue.USER_ID, resultJson.getInt("data"));
			editor.putString(ConstantValue.keyUserNum, number);
			editor.commit();
		}else{
			Log.i(TAG, "no more ballers");
		}
	}

	//get categores
	private void getCategories(){
		SendResultModel result = new SendResultModel(LoginUser.this, null, new OnTaskCompleted() {

			@Override
			public void setResult(ResultHandler obj) {
				try {
					Log.i(TAG, "categories " + obj.getResult());
					parseCategories(obj.getResultJSON());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onNetworkError(ResultHandler obj) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCancel() {

			}
		});
		result.execute(new SendDataHandler(generateCategoryList(), ConstantValue.CATEGORY_URL, "POST"));
	}
	//parse data of categories
	protected void parseCategories(JSONObject resultJSON) throws JSONException {
		if(resultJSON.getBoolean("status")){
			db.deleteLocationType();
			String dataNow = GlobalVariables.instanceOf().getDate();
			JSONArray cast = resultJSON.getJSONArray("data");
			for (int i=0; i<cast.length(); i++) {
				JSONObject actor = cast.getJSONObject(i);
				db.addLocationType(new LocationTypeHandler(actor.getInt("id"), dataNow, null, actor.getString("icon"), actor.getString("name"), null, null));
			}
		}
	}
	//get all date
	private void getAllData(){
		SendResultModel result = new SendResultModel(LoginUser.this, null, new OnTaskCompleted() {

			@Override
			public void setResult(ResultHandler obj) {
				Log.i(TAG,"result of type all : " + obj.getResult());
				try {
					processAllLocation(obj.getResultJSON());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onNetworkError(ResultHandler obj) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCancel() {

			}
		});
		result.execute(new SendDataHandler(generateAllLocation(), ConstantValue.GET_ALL, "POST"));
	}

	/*process json object of get all*/
	protected void processAllLocation(JSONObject resultJSON) throws JSONException {
		if(resultJSON.getBoolean("status")){
			Log.i(TAG, "count : " + db.getLocationCount());
			db.deleteLocation();
			Log.i(TAG, "count : " + db.getLocationCount());
			JSONArray cast = resultJSON.getJSONArray("data");
			String dataNow = GlobalVariables.instanceOf().getDate();
			for (int i=0; i<cast.length(); i++) {
				JSONObject actor = cast.getJSONObject(i);
				//			    public LocationHandler( int base_id, String date_entry, double latitude,  double longitude, String name, String type, 
				//						String searchTag, double amountables, String description, String bestSeller, String webSite){
				String categoryList = "";
				String categories = actor.getJSONArray("category").toString().replaceAll("[\"\\[\\]]", "")+",";
				Log.i(TAG, "locations categories : " + categories);
				db.addLocation(new LocationHandler(0,
						dataNow,
						actor.getDouble("lat"),
						actor.getDouble("lng"),
						actor.getString("client"),
						categoryList,
						"",
						0,
						actor.getString("desc"),
						actor.getString("best_seller"),
						actor.getString("website_url")));
			}

			if(db.getLocationCount() > 0){
				Intent intent = new Intent(LoginUser.this, FragmentChangeActivity.class);
				startActivity(intent);
				this.finish();

			}else{
				Dialogs.showDialog(LoginUser.this, "Error!", "Donwload incomplete. Please check your internet connection", "Ok");
			}
		}
	}
	private List<NameValuePair> generateCategoryList() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", "category"));
		//		params.add(new BasicNameValuePair("deviceKey", deviceKey));
		//		params.add(new BasicNameValuePair("deviceType", deviceType));
		//		params.add(new BasicNameValuePair("subscribe", String.valueOf(subcribe)));
		return params;
	}

	private List<NameValuePair> generateAllLocation() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("type", "all"));
		//		params.add(new BasicNameValuePair("deviceKey", deviceKey));
		//		params.add(new BasicNameValuePair("deviceType", deviceType));
		//		params.add(new BasicNameValuePair("subscribe", String.valueOf(subcribe)));
		return params;
	}

	protected List<NameValuePair> generateParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String number = phoneNumber.getText().toString();
		int subcribe = 0;
		if(number.trim().length() > 0){
			subcribe = 1;
		}
		String deviceType = "Android";
		String deviceKey = "KEY";
		params.add(new BasicNameValuePair("number", number));
		params.add(new BasicNameValuePair("deviceKey", deviceKey));
		params.add(new BasicNameValuePair("deviceType", deviceType));
		params.add(new BasicNameValuePair("subscribe", String.valueOf(subcribe)));
		params.add(new BasicNameValuePair("type", "reg"));
		return params;
	}
}
