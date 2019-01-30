package com.pingo.receiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
/**
 * 
 * @author 郭宏志
 *
 */
public class getMessegeReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("com.max.autoStart")) {
			// 获得提示信息
			String msg = intent.getStringExtra("msg");
			//Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }  
	}
}