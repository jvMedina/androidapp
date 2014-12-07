package com.hackaton.food4thought.controller;

import android.graphics.Bitmap;

public interface AddLocationCategoryCallBack {
	public void addData(String categoryName, Bitmap icon, Bitmap pin);
}
