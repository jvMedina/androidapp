package com.hackaton.food4thought.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.hackaton.food4thought.R;

public class NowLayout extends LinearLayout implements OnGlobalLayoutListener { 
	public NowLayout(Context context, AttributeSet attrs)
	{ 
		super(context, attrs);
		initLayoutObserver(); 
	} 
	public NowLayout(Context context) 
	{ 
		super(context);
		initLayoutObserver();
	}
	private void initLayoutObserver() 
	{ 
		// force vertical orientation and add observer 
		setOrientation(LinearLayout.VERTICAL);
		getViewTreeObserver().addOnGlobalLayoutListener(this); 
	} 

	@Override 
	public void onGlobalLayout() 
	{
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
		final int heightPx = getContext().getResources(). getDisplayMetrics().heightPixels;
		boolean inversed = false; 
		final int childCount = getChildCount(); 
		for (int i = 0; i < childCount; i++) { 
			View child = getChildAt(i); 
			int[] location = new int[2]; 
			child.getLocationOnScreen(location);
			if (location[1] > heightPx) 
			{ break; 
			} 
			if(!inversed) {
				child.startAnimation( AnimationUtils.loadAnimation(getContext(), R.drawable.slide_up_left)); 
			} else {
				child.startAnimation( AnimationUtils.loadAnimation(getContext(), R.drawable.slide_up_right)); 
			} 
			inversed = !inversed; } 
	} 
//- See more at: http://shardulprabhu.blogspot.in/2012/09/google-now-cards-layout.html#sthash.Atri1ZHq.dpuf
}
