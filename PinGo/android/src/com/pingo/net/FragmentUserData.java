package com.pingo.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pingo.utils.Constant;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

public class FragmentUserData {
	private RequestQueue mQueue;
	private Context cxt;
	public FragmentUserData(Context cxt,RequestQueue mQueue){
		this.mQueue=mQueue;
		this.cxt=cxt;
	}
	public void getQiniuKey(final Bitmap photo) {
		String url = Constant.URL+"/pic/auth";
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						Log.e("yao", "resData:"+response);
						try {
								UploadGetKey(photo,response.getString("qiniu_token"),response.getString("qiniu_key"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, 
				new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
					}
				});
	        mQueue.add(jsObjRequest);
	}
	private void UploadGetKey(Bitmap photo, String token, String key) {
		RequestQueue queue = Volley.newRequestQueue(cxt);
		String url = "http://lbang.qiniudn.com/";
//		String token="TUI9cntm9BlpA6gDzr3lO5h-rixSlIq00iAXnzS8:kOgNkJWjjw0-gfub5O8tdW4I_fI=:eyJzY29wZSI6ImxiYW5nIiwiZGVhZGxpbmUiOjE0MjI3OTkzOTJ9";
//		String key="ZgK854ce2410190ed";
	    Map<String,String> map=new HashMap<String,String>();
        map.put("key",key);
        map.put("token",token);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        Map<String, String> params = new HashMap<String, String>();
        params.put("x:foo", "fooval");
        final UploadOptions opt = new UploadOptions(params, null, true, null, null);
        new UploadManager().put(baos.toByteArray(), key, token, new UpCompletionHandler() {
            public void complete(String k, ResponseInfo rinfo, JSONObject response) {
            	try {
						String PhotoName=response.getString("key");
						upload(PhotoName);
				} catch (JSONException e) {
					e.printStackTrace();
				}
            }
        }, opt);
	}
	private void upload(String photoName) {
		RequestQueue queue = Volley.newRequestQueue(cxt);
		String url = Constant.URL+"/member/pic/upload?token="+Constant.Token+"&pic="+photoName;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						Log.e("yao", "resData:"+response);
					}
				}, 
				new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
					}
				});
	        queue.add(jsObjRequest);
	}
}
