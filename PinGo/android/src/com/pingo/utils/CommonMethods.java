package com.pingo.utils;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class CommonMethods {
	public void setImageViewParams(ImageView iv,int width,int height)
	{
		LayoutParams params = iv.getLayoutParams();
		if(width==0&&height==0){
		    params.height=params.WRAP_CONTENT;
		    params.width =params.WRAP_CONTENT;
		}
		else{
			params.height=width;
		    params.width =height;
		}
	    iv.setLayoutParams(params);
	}
	public ArrayList<View> getViewsByTag(ViewGroup root, String tag){
	    ArrayList<View> views = new ArrayList<View>();
	    final int childCount = root.getChildCount();
	    for (int i = 0; i < childCount; i++) {
	        final View child = root.getChildAt(i);
	        if (child instanceof ViewGroup) {
	            views.addAll(getViewsByTag((ViewGroup) child, tag));
	        } 

	        final Object tagObj = child.getTag();
	        if (tagObj != null && tagObj.equals(tag)) {
	            views.add(child);
	        }

	    }
	    return views;
	}
	
	public void BitmapRecycle(Bitmap bp)
	{
		if(bp != null && !bp.isRecycled()){ 
	        // 回收并且置为null
	        bp.recycle(); 
	        bp = null; 
		}
	System.gc();//调用系统垃圾回收器
	}
}
