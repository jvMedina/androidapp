package com.hackaton.food4thought;

import com.hackaton.food4thought.controller.SearchController;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SearchFragment extends Fragment{

	private View view;
	
	public static final SearchFragment newInstance(){
		SearchFragment fragment = new SearchFragment();
		return fragment ;
	}
	
	private SearchController searchController;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_search, null);
		searchController = new SearchController(getActivity(), view);
		init();
		return view;
	}

	private void init() {
		searchController.setVariables();
		searchController.setFunctions();
	}


}
