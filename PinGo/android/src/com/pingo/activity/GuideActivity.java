package com.pingo.activity;

import java.util.ArrayList;

import com.pingo.utils.Constant;

import cn.smssdk.SMSSDK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
/**
 * 
 * @author max    初始化界面
 */
public class GuideActivity extends Activity {
    /** Called when the activity is first created. */
	private ViewPager mViewPager;
	private int currIndex = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        /*用户登录成功后，SharedPreferences保存信息，此处启动时读取信息，如果已经登录则直接进入主界面 */
        Context ctx = GuideActivity.this;
    	SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
    	Log.e("yao", "sp1:  "+sp.getString("isLogin", "none"));
    	Intent intent = new Intent();
    	if(sp.getString("isLogin", "none").equals("yes")){
    		intent.setClass(GuideActivity.this,MainActivity.class);
    		startActivity(intent);
    		this.finish();
    	}
    	SharedPreferences.Editor editor = sp.edit();
    	editor.putString("isLogin", "yes");
        editor.commit();
        
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.guide_page1, null);
        View view2 = mLi.inflate(R.layout.guide_page2, null);
        View view3 = mLi.inflate(R.layout.guide_page3, null);
        View view4 = mLi.inflate(R.layout.guide_page4, null);
        	    
        //每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        
        
        //填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		mViewPager.setAdapter(mPagerAdapter);
    }
    
    public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			 /*以下是三个引导页切换动画，主要通过换图片实现 */
			switch (arg0) {
			case 0:	
				if (currIndex == arg0+1) {
					animation = new TranslateAnimation(20*(arg0+1), 20*arg0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == arg0-1) {
					animation = new TranslateAnimation(20*(arg0-1), 20*arg0, 0, 0);
				} else if (currIndex == arg0+1) {
					animation = new TranslateAnimation(20*(arg0+1), 20*arg0, 0, 0);
				}
				break;
			case 2:
				if (currIndex == arg0-1) {
					animation = new TranslateAnimation(20*(arg0-1), 20*arg0, 0, 0);
				} else if (currIndex == arg0+1) {
					animation = new TranslateAnimation(20*(arg0+1), 20*arg0, 0, 0);
				}
				break;
			case 3:
				if (currIndex == arg0-1) {
					animation = new TranslateAnimation(20*(arg0-1), 20*arg0, 0, 0);
				} else if (currIndex == arg0+1) {
					animation = new TranslateAnimation(20*(arg0+1), 20*arg0, 0, 0);
				}
				break;
			}
			currIndex = arg0;
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageScrollStateChanged(int arg0) {
		}
	}
    public void onEnter(View v) {
	      	Intent intent = new Intent();
			intent.setClass(GuideActivity.this,MainActivity.class);
			startActivity(intent);
      }
}