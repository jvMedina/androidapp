package com.hackaton.food4thought;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;

public class HelpUp extends SherlockActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_us);
		getSupportActionBar().hide();
	}
}
