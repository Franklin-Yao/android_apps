package com.pingo.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public interface ListElement {

	public int getLayoutId(); 
	
	public void setLayoutId(int layoutId);
	
	public void setClickable(boolean clickable);
	
    public boolean isClickable(); 

    public View getViewForListElement(LayoutInflater layoutInflater, 
            Context context, View view); 
}
