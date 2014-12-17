package com.teliapp.lovestruck;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class TeliApp extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.teliapp);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.app_icon);
	}
}
