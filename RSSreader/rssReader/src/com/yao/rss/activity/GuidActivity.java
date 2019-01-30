package com.yao.rss.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ViewFlipper;

import com.yao.rssReader.R;

/**
 * GuidActivity--显示用户引导界面
 * @author 姚富品
 * @date 2013-7-19
 * @version 3.0
 */
public class GuidActivity extends Activity implements OnGestureListener , OnTouchListener {   
    private ViewFlipper mViewFlipper; 
    private GestureDetector mGestureDetector;
    @Override  
    public void onCreate(Bundle savedInstanceState) 
    {   
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);      
        mViewFlipper = (ViewFlipper) findViewById(R.id.flipper);  
        mViewFlipper.setOnTouchListener(this);
        LayoutInflater factory = LayoutInflater.from(GuidActivity.this);
        //渲染用户引导布局文件
        View page1 = factory.inflate(R.layout.layout_guid_page1, null);
        View page2 = factory.inflate(R.layout.layout_guid_page2, null);
        View page3 = factory.inflate(R.layout.layout_guid_page3, null);
        View page4 = factory.inflate(R.layout.layout_guid_page4, null);
        View page5 = factory.inflate(R.layout.layout_guid_page5, null);
        View page6 = factory.inflate(R.layout.layout_guid_page6, null);
        View page7 = factory.inflate(R.layout.layout_guid_page7, null);
        View page8 = factory.inflate(R.layout.layout_guid_page8, null);
        View page9 = factory.inflate(R.layout.layout_guid_page9, null);
        
        //将帮助页面加入主布局文件
        mViewFlipper.addView(page1);
        mViewFlipper.addView(page2);
        mViewFlipper.addView(page3);
        mViewFlipper.addView(page4);
        mViewFlipper.addView(page5);
        mViewFlipper.addView(page6);
        mViewFlipper.addView(page7);
        mViewFlipper.addView(page8);
        mViewFlipper.addView(page9);
        
        mViewFlipper.setLongClickable(true);
        mGestureDetector = new GestureDetector(this);
        mViewFlipper.setOnTouchListener(this);
    }
    
	/**
	 * onTouch--点击事件监听
	 * @return boolean
	 */
	public boolean onTouch(View v, MotionEvent event) {
	    return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * onTouch--滑动事件监听
	 * @return boolean
	 */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
        // 参数e1是按下事件，e2是放开事件，剩下两个是滑动的速度分量，这里用不到 
        // 按下时的横坐标大于放开时的横坐标，从右向左滑动 
        if (e1.getX() > e2.getX()) 
        {
            mViewFlipper.showNext();
        }
        // 按下时的横坐标小于放开时的横坐标，从左向右滑动 
        else if (e1.getX() < e2.getX()) {
            mViewFlipper.showPrevious();
        }
        return false;
    }



	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}




        