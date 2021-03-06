package com.teliapp.lovestruck;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class About extends Activity{
	
	Button aboutTheApp, howItWorks, teliApp, credits, facebook, twitter;
	Intent i = null;
	Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.about);
		
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.app_icon);

		ctx = this;
		
		aboutTheApp = 	(Button) findViewById(R.id.about_the_app);
		howItWorks 	= 	(Button) findViewById(R.id.how_it_works);
		teliApp 	= 	(Button) findViewById(R.id.teliapp);
		credits 	= 	(Button) findViewById(R.id.credits);
		facebook 	= 	(Button) findViewById(R.id.facebook);
		twitter 	= 	(Button) findViewById(R.id.twitter);
		
		
		
		aboutTheApp.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				i = new Intent(ctx, AboutTheApp.class);
				startActivity(i);
			}
			
		});
		
		howItWorks.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				i = new Intent(ctx, HowItWorks.class);
				startActivity(i);
			}
			
		});
		
		teliApp.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				i = new Intent(ctx, TeliApp.class);
				startActivity(i);
			}
			
		});
		
		credits.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				i = new Intent(ctx, Credits.class);
				startActivity(i);
			}
			
		});
		
		facebook.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Uri uri = Uri.parse("http://www.facebook.com/pages/TeliApp/382485891770070");
				Intent i = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(i);
			}

		});
		
		twitter.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Uri uri = Uri.parse("https://twitter.com/teliapp");
				Intent i = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(i);				
			}
			
		});
		
	}

}
