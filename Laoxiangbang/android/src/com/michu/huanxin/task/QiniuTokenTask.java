package com.michu.huanxin.task;

import com.michu.huanxin.Constant;
import com.michu.huanxin.interfaces.GlobalTaskNotify;
import com.michu.huanxin.utils.HttpUtils;

import android.os.AsyncTask;

public class QiniuTokenTask extends AsyncTask<Void, Void, Void>{

	private GlobalTaskNotify taskNotify;
	private String resultStr = "";
	
	public QiniuTokenTask(GlobalTaskNotify taskNotify){
		this.taskNotify = taskNotify;
	}
	
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String[] strName = {"pic"};
		String[] strValue = {""};
		resultStr = HttpUtils.sendGet(Constant.QINIUTOKEN + "?", strName, strValue);
		return null;
	}


	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		taskNotify.TaskEnd(resultStr);
	}

	
	
}
