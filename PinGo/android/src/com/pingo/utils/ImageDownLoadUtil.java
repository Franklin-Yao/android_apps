package com.pingo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class ImageDownLoadUtil {
	public interface ICallBack {
	    public void onSuccess(Bitmap bp);
	    public void onFailure();
	}
	public void readBitmapViaVolley1(RequestQueue mQueue,String imgUrl,final ICallBack call) {
		ImageRequest imgRequest = new ImageRequest(imgUrl,
				new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap bp) {
				call.onSuccess(bp);
			}
		}, 90, 90, Config.ALPHA_8, new ErrorListener() {
            @Override  
            public void onErrorResponse(VolleyError arg0) {
            	call.onFailure();
            }
        });
	    mQueue.add(imgRequest);
	}
	public void readBitmapViaVolley(RequestQueue mQueue, String imgUrl, final ImageView imageView) {
		ImageRequest imgRequest = new ImageRequest(imgUrl,
				new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap bp) {
				 imageView.setImageBitmap(bp);
			}
		}, 300, 200, Config.ARGB_8888, new ErrorListener() {
            @Override  
            public void onErrorResponse(VolleyError arg0) {
            }
        });
	    mQueue.add(imgRequest);
	}
}
