package com.hackaton.food4thought.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class SuggestionsAdapter extends CursorAdapter {

	private static final String TAG = SuggestionsAdapter.class.getSimpleName();

	public SuggestionsAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		return v;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView tv = (TextView) view;
		tv.setText(cursor.getString(6));
		Log.i(TAG, "cursor dta " + cursor.getString(1));
	}
}