package com.michu.huanxin.task;

import java.lang.ref.WeakReference;
import java.util.Map;

import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.michu.huanxin.interfaces.GlobalTaskNotify;
import com.michu.huanxin.utils.HttpUtils;

import android.os.AsyncTask;

public class RegisterTask extends AsyncTask<Void,Void,GlobalTaskNotify>{

	private WeakReference<GlobalTaskNotify> mContext;
	private String resultStr;
	private String url;
	private Map<String,String> data;
	
	public RegisterTask(String url,Map<String,String> map,GlobalTaskNotify context){
		this.mContext = new WeakReference<GlobalTaskNotify>(context);
		this.url = url;
		this.data = map;
	}
	
	@Override
	protected GlobalTaskNotify doInBackground(Void... params) {
		// TODO Auto-generated method stub
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
		if (isCancelled()){
			System.out.println("cancelled");
			return;
			}
		if (mContext != null && mContext.get() != null) {
			mContext.get().TaskEnd(resultStr);
		}

	}

}
