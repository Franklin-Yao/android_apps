package com.michu.huanxin.task;

import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import android.os.AsyncTask;

public class QiniuUploadTask extends AsyncTask<Void,Void,Void>{

	private String token;
	private String[] data,key;
	private UpCompletionHandler upHandler;

	public QiniuUploadTask(String[] data,String[] key,String token,UpCompletionHandler upHandler){
		this.data = data;
		this.key = key;
		this.token = token;
		this.upHandler = upHandler;
	}
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		UploadManager upAvatar = new UploadManager();
		for(int i = 0; i <data.length; i++){
			upAvatar.put(data[i], key[i], token, upHandler,null);
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
