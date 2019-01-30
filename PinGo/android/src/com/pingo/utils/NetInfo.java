package com.pingo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class NetInfo {
	private static boolean IsNetAvailable=false;
	static Intent intent=new Intent();
	public interface ICallBack {
	    public void onSuccess();
	    public void onFailure();
	}
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	public static void isNetworkNetLogin(Context context,final ICallBack call) {
        String url = "http://www.baidu.com/";
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest=new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("yao  isNetworkNetLogin", response);
                        call.onSuccess();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    	Log.e("yao  isNetworkNetLogin", error.toString());
                    	call.onFailure();
                    }
                });
        mRequestQueue.add(mStringRequest);
	}
}
