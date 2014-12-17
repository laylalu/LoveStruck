package com.teliapp.lovestruck;

import com.teliapp.lovestruck.SongService.SongBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class LoveStruck extends Activity {

	Context ctx;
	SongService songService;
	Button play, about, repeat, girl, boy;
	SeekBar seekbar;
	static int pos = 0, max = 0;
	static Boolean girlPressed = false, boyPressed = false, playPressed = false, repeatPressed = false, aboutPressed = false;
	public final static String GIRL = "girl", BOY = "boy", PLAY = "play", REPEAT = "about", POS = "pos";	
	Handler handler;
	Intent serviceIntent;
	private boolean mBound = false;


	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className,
				IBinder service) {

			SongBinder binder = (SongBinder) service;
			songService = binder.getService();
			mBound = true;
		}


		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		

		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.app_icon);
		ctx = this;
		
		girl = 	(Button) findViewById(R.id.girl);
		boy = 	(Button) findViewById(R.id.boy);
		play = 	(Button) findViewById(R.id.play);
		repeat = 	(Button) findViewById(R.id.repeat);
		about = (Button) findViewById(R.id.about);
		seekbar = (SeekBar)	findViewById(R.id.seekBar);
		
		if (savedInstanceState == null)
			restore();
		
		handler = new Handler();
		handler.post(moveSeekBarThread);
		
		
		girl.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if (mBound == true){
					if(songService.song != null){
						if (boyPressed && songService.song.isPlaying())
							stopSong();
					}
				}
				if (boyPressed  || !girlPressed){
					girl.setBackgroundResource(R.drawable.girl_pressed);
					girlPressed = true;
					boyPressed = false;
					boy.setBackgroundResource(R.drawable.boy_button);
					seekbar.setProgress(0);
				}
			}

		});

		boy.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if (mBound == true){
					if(songService.song != null){
						if (girlPressed && songService.song.isPlaying())
							stopSong();
					}
				}
				if (girlPressed || !boyPressed){
					boy.setBackgroundResource(R.drawable.boy_pressed);
					boyPressed = true;
					girlPressed = false;
			
					girl.setBackgroundResource(R.drawable.girl_button);
					seekbar.setProgress(0);
				}
			}

		});

		play.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {

				if(!girlPressed && !boyPressed){
					Toast.makeText(ctx, "Before you press play, first pick if you want to attract a girl or a guy.", Toast.LENGTH_SHORT).show();
				}
				else{
					
					if(!playPressed){
			
						playPressed = true;
						play.setBackgroundResource(R.drawable.pause);

						serviceIntent = new Intent(ctx, SongService.class);
						serviceIntent.putExtra("pos", seekbar.getProgress());
						serviceIntent.putExtra("repeat", repeatPressed);
						startService(serviceIntent);

						Intent intent = new Intent(ctx, SongService.class);
						bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


					}
					else {

						if (mBound) {
							mBound = false;
							unbindService(mConnection);
						}
						serviceIntent = new Intent(ctx, SongService.class);
						playPressed = false;
						stopService(serviceIntent);
						play.setBackgroundResource(R.drawable.play);
					}				
				}
			}
		});

		repeat.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {

				if (mBound == true){
					if(songService.song != null  && !repeatPressed){
						repeat.setBackgroundResource(R.drawable.repeat_pressed);
						repeatPressed = true;
						songService.song.setLooping(true);
					}

					else if(songService.song != null && repeatPressed){
						repeatPressed = false;
						repeat.setBackgroundResource(R.drawable.repeat_button);
						songService.song.setLooping(false);
					}
				}else{
					if (repeatPressed)
						repeatPressed = false;
					else
						repeatPressed = true;
				}

			}

		});

		about.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {

				about.setBackgroundResource(R.drawable.about_button);
				Intent i = new Intent(ctx, About.class);
				startActivity(i);
			}

		});

		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(fromUser){
					if (mBound == true){
						if(songService.song!=null)	
							songService.song.seekTo(progress);
					}
					seekbar.setProgress(progress);

				}
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
		});

	}

	protected void stopSong() {
		if (mBound) {
			mBound = false;
			unbindService(mConnection);
		}
		seekbar.setProgress(0);
		serviceIntent = new Intent(ctx,SongService.class);
		stopService(serviceIntent);
		play.setBackgroundResource(R.drawable.play);
		//songService.song.release();
	}



	private Runnable moveSeekBarThread = new Runnable() {
		int mediaPos_new = 0, mediaMax_new = 0;
		public void run() {
			if (mBound == true){
				if(songService.song != null){
					max=mediaMax_new = songService.getDuration();
					pos=mediaPos_new = songService.getCurrentPostion();
					seekbar.setMax(mediaMax_new);
					seekbar.setProgress(mediaPos_new);
					if (songService.song.isPlaying()){
						playPressed = true;
						play.setBackgroundResource(R.drawable.pause);
					}else{
						playPressed = false;
						play.setBackgroundResource(R.drawable.play);
					}
				}	
				if (songService.done){
					seekbar.setProgress(0);
					playPressed = false;
					play.setBackgroundResource(R.drawable.play);
					mBound = false;
					unbindService(mConnection);
				}
			}
			
			
			handler.postDelayed(this,100); //Looping the thread after 1 second

		}

	};



	public void onPause(){
		super.onPause();
		if (mBound) {
			mBound = false;
			unbindService(mConnection);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putBoolean(GIRL, girlPressed);
		savedInstanceState.putBoolean(BOY, boyPressed);
		savedInstanceState.putBoolean(REPEAT, repeatPressed);
		savedInstanceState.putInt("pos", seekbar.getProgress());
		savedInstanceState.putInt("max",seekbar.getMax());
	}

	@Override
	public void onResume(){
			super.onResume();
			playPressed = false;
			Intent intent = new Intent(this, SongService.class);
			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState != null){
			girlPressed= savedInstanceState.getBoolean(GIRL, false);
			boyPressed = savedInstanceState.getBoolean(BOY, false);
			repeatPressed = savedInstanceState.getBoolean(REPEAT,false);
			seekbar.setMax(savedInstanceState.getInt("max",0));
			seekbar.setProgress(savedInstanceState.getInt("pos",0));
			
		}

		if(girlPressed)	girl.setBackgroundResource(R.drawable.girl_pressed);
		if(boyPressed)	boy.setBackgroundResource(R.drawable.boy_pressed);
		if (repeatPressed) repeat.setBackgroundResource(R.drawable.repeat_pressed);
	}
	
	public void restore(){
		
		if(girlPressed)	girl.setBackgroundResource(R.drawable.girl_pressed);
		if(boyPressed)	boy.setBackgroundResource(R.drawable.boy_pressed);
		if (repeatPressed) repeat.setBackgroundResource(R.drawable.repeat_pressed);
		seekbar.setMax(max);
		seekbar.setProgress(pos);
		
	}


}
