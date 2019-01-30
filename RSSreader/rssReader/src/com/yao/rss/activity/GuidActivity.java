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
 * GuidActivity--��ʾ�û���������
 * @author Ҧ��Ʒ
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
        //��Ⱦ�û����������ļ�
        View page1 = factory.inflate(R.layout.layout_guid_page1, null);
        View page2 = factory.inflate(R.layout.layout_guid_page2, null);
        View page3 = factory.inflate(R.layout.layout_guid_page3, null);
        View page4 = factory.inflate(R.layout.layout_guid_page4, null);
        View page5 = factory.inflate(R.layout.layout_guid_page5, null);
        View page6 = factory.inflate(R.layout.layout_guid_page6, null);
        View page7 = factory.inflate(R.layout.layout_guid_page7, null);
        View page8 = factory.inflate(R.layout.layout_guid_page8, null);
        View page9 = factory.inflate(R.layout.layout_guid_page9, null);
        
        //������ҳ������������ļ�
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
	 * onTouch--����¼�����
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
	 * onTouch--�����¼�����
	 * @return boolean
	 */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
        // ����e1�ǰ����¼���e2�Ƿſ��¼���ʣ�������ǻ������ٶȷ����������ò��� 
        // ����ʱ�ĺ�������ڷſ�ʱ�ĺ����꣬�������󻬶� 
        if (e1.getX() > e2.getX()) 
        {
            mViewFlipper.showNext();
        }
        // ����ʱ�ĺ�����С�ڷſ�ʱ�ĺ����꣬�������һ��� 
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




        