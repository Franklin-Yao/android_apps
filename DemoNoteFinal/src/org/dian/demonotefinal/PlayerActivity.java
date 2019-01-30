package org.dian.demonotefinal;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PlayerActivity extends Activity {
	
	private static final int STATE_STARTED = 1;
	private static final int STATE_STOPPED = 2;
	private static final int STATE_PAUSED = 3;
	
	private int mCurrentState;
	
	private Button mPlayButton;
	private Button mStopButton;
	private SeekBar mSeekBar;
	
	private ProgressReceiver mReceiver;
	public static int mId=-1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		Intent it =getIntent();
		mId=it.getIntExtra("musicId",-1);
		
		mCurrentState = STATE_STARTED;
		Intent intent = new Intent();
		intent.setClass(PlayerActivity.this, PlayerService.class);
		intent.setAction(PlayerService.ACTION_PLAY);
		intent.putExtra("musicId", mId);
		startService(intent);
		mPlayButton = (Button) findViewById(R.id.button_play);
		mPlayButton.setText("暂停");
		mPlayButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (mCurrentState) {
				case STATE_STOPPED:
				case STATE_PAUSED:
					mCurrentState = STATE_STARTED;
					mPlayButton.setText("暂停");
					Intent intent = new Intent();
					intent.setClass(PlayerActivity.this, PlayerService.class);
					intent.setAction(PlayerService.ACTION_PLAY);
					intent.putExtra("musicId", mId);
					startService(intent);
					break;
					
				case STATE_STARTED:
					mCurrentState = STATE_PAUSED;
					mPlayButton.setText("播放");
					Intent intent2 = new Intent();
					intent2.setClass(PlayerActivity.this, PlayerService.class);
					intent2.setAction(PlayerService.ACTION_PAUSE);
					intent2.putExtra("musicId",mId);
					startService(intent2);
					break;

				default:
					break;
				}
				
			}
		});
		
		mStopButton = (Button) findViewById(R.id.button_stop);
		mStopButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//改变当前记录的状态
				mCurrentState = STATE_STOPPED;
				//设置按钮显示的文字
				mPlayButton.setText("播放");
				//告诉Service去真正地改变播放状态
				Intent intent = new Intent();
				intent.setClass(PlayerActivity.this, PlayerService.class);
				intent.setAction(PlayerService.ACTION_STOP);
				intent.putExtra("musicId", mId);
				startService(intent);
			}
		});
		
		mSeekBar = (SeekBar) findViewById(R.id.seekbar);
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Intent intent = new Intent();
				intent.setClass(PlayerActivity.this, PlayerService.class);
				intent.setAction(PlayerService.ACTION_SEEKTO);
				intent.putExtra(PlayerService.CURRENT, seekBar.getProgress());
				startService(intent);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		unregisterReceiver(mReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(PlayerService.ACTION_REFRESH);
		
		if (mReceiver == null) {
			mReceiver = new ProgressReceiver();
		}
		
		registerReceiver(mReceiver, filter);
		
	}

	private class ProgressReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int progress = intent.getIntExtra(PlayerService.CURRENT, -1);
			int max = intent.getIntExtra(PlayerService.DURATION, -1);
			
			mSeekBar.setMax(max);
			mSeekBar.setProgress(progress);
		}
		
	}
	protected void onDestroy() {
		super.onDestroy();
		Intent intent = new Intent();
		intent.setClass(PlayerActivity.this, PlayerService.class);
		intent.setAction(PlayerService.ACTION_STOP);
		intent.putExtra("musicId", mId);
		startService(intent);
		this.finish();
		}		
}
