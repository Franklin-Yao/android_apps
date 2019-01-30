package com.michu.huanxin.task;

import java.lang.ref.WeakReference;

import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.michu.huanxin.interfaces.NotifyRegisterTask;

import android.os.AsyncTask;

public class RegisterNewAccount extends AsyncTask<Void, Void, NotifyRegisterTask>{

	private WeakReference<NotifyRegisterTask> mContext;

	public RegisterNewAccount(NotifyRegisterTask context){
		this.mContext = new WeakReference<NotifyRegisterTask>(context);
	}

	@Override
	protected NotifyRegisterTask doInBackground(Void... params) {
		// TODO Auto-generated method stub

		try {
			EMChatManager.getInstance().createAccountOnServer("xcs", "123");
		} catch (EaseMobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("register fail");
		}
		return null;
	}

	@Override
	protected void onPostExecute(NotifyRegisterTask result) {
		// TODO Auto-generated method stub
		if (isCancelled())
			return;
		if (mContext != null && mContext.get() != null) {
			mContext.get().notifyDataget();;
		}
	}

	
	
}
