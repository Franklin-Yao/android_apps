package com.michu.huanxin.task;

import java.lang.ref.WeakReference;










import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.michu.huanxin.interfaces.GlobalTaskNotify;
import com.michu.huanxin.utils.HttpUtils;

import android.os.AsyncTask;

public class LoginTask extends AsyncTask<Void,Void,GlobalTaskNotify>{

	private WeakReference<GlobalTaskNotify> mContext;
	private String resultStr;
	private String url;
	private Map<String,String> data;
	

	public LoginTask(String url,Map<String,String> map,GlobalTaskNotify context){
		this.mContext = new WeakReference<GlobalTaskNotify>(context);
		System.out.println("login task constru");
		this.url = url;
		this.data = map;
		
	}


	@Override
	protected GlobalTaskNotify doInBackground(Void... params) {
		// TODO Auto-generated method stub
		System.out.println("login task background");
		try{
			resultStr = HttpUtils.sendPost(url, data);
		}catch(Exception e){
			resultStr = "{\"status\":\"network_fail\"}";
		}
		return null;
	}

	@Override
	protected void onPostExecute(GlobalTaskNotify result) {
		// TODO Auto-generated method stub

		System.out.println("login task postexecute");
		if (isCancelled())
			return;
		if (mContext != null && mContext.get() != null) {
			mContext.get().TaskEnd(resultStr);
		}
	}	

}
