package com.michu.huanxin.task;

import java.util.Map;

import com.michu.huanxin.interfaces.GlobalTaskNotify;
import com.michu.huanxin.utils.HttpUtils;

import android.os.AsyncTask;

public class GlobalHttpPostTask extends AsyncTask<Void, Void, String>{
	
	private String urlStr;
	private Map<String,String> dataMap;
	private GlobalTaskNotify taskNotify;
	
	public GlobalHttpPostTask(String url,Map<String,String> data,GlobalTaskNotify taskNotify){
		this.urlStr = url;
		this.dataMap = data;
		this.taskNotify = taskNotify;
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String resultStr = HttpUtils.sendPost(urlStr, dataMap);
		return resultStr;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		taskNotify.TaskEnd(result);
	}

	
	
}
