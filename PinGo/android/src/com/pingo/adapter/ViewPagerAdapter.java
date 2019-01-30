package com.pingo.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPagerAdapter extends PagerAdapter {
	private int imageIds[];
    private String[] titles;
    private ArrayList<ImageView> images;
    private ArrayList<View> dots; 
    private TextView title;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private int oldPosition = 0;//记录上一次点的位置
    private int currentItem; //当前页面
	public ViewPagerAdapter(ArrayList<ImageView> images){
		this.images=images;
	}
    public int getCount() {
        // TODO Auto-generated method stub
        return images.size();
    }

    //是否是同一张图片
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView(images.get(position));
        
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        view.addView(images.get(position));
        return images.get(position);
    }
}
