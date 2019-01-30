package org.dian.demonotefinal;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class PlayerService extends Service {
	
	private static final String TAG = "PlayerService";
	
	public static final String ACTION_PLAY = "play";
	public static final String ACTION_STOP = "stop";
	public static final String ACTION_PAUSE = "pause";
	public static final String ACTION_SEEKTO = "seekto";
	
	public static final String ACTION_REFRESH = "org.dian.refresh";
	
	public static final String CURRENT = "current";
	public static final String DURATION = "duration";
	
	private PlayerEngine mPlayerEngine;
	
	private Timer mTimer;
	public static int musicId;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		mPlayerEngine = new PlayerEngine(PlayerService.this,PlayerActivity.mId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v(TAG, "onStartCommand()");
		String action = intent.getAction();
		intent.getExtras().getInt("musicId",PlayerActivity.mId);
		
		if(action.equals(ACTION_PLAY)) {
			Log.v(TAG, "play()");
			mPlayerEngine.play();
			
			if(mTimer == null) {
				TimerTask task = new TimerTask() {
					
					@Override
					public void run() {
						Intent intent = new Intent();
						intent.setAction(ACTION_REFRESH);
						intent.putExtra(CURRENT, mPlayerEngine.getCurrentPosition());
						intent.putExtra(DURATION, mPlayerEngine.getDuration());
						sendBroadcast(intent);
					}
				};
				mTimer = new Timer();
				mTimer.schedule(task, 0, 1000);
			}
		}
		
		if (action.equals(ACTION_STOP)) {
			mPlayerEngine.stop();
			mPlayerEngine.release();
			
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}
		}
		
		if (action.equals(ACTION_PAUSE)) {
			mPlayerEngine.pause();
			
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}
		}
		
		if (action.equals(ACTION_SEEKTO)) {
			int msec = intent.getIntExtra(CURRENT, -1);
			mPlayerEngine.seekTo(msec);
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	

}
