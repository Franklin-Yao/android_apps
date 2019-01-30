package com.michu.huanxin.utils;
import com.michu.pingju.activity.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;


public class BottomPopmenuUtils extends PopupWindow {


	private TextView tvTakePhoto, tvPickPhoto, tvCancel;
	private View mMenuView;

	public BottomPopmenuUtils(Activity context,OnClickListener itemsOnClick,int layoutId) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(layoutId, null);
		tvTakePhoto = (TextView) mMenuView.findViewById(R.id.bottom_pic_popmenu_tv_takephoto);
		tvPickPhoto = (TextView) mMenuView.findViewById(R.id.bottom_pic_popmenu_tv_pickphoto);
		tvCancel = (TextView) mMenuView.findViewById(R.id.bottom_pic_popmenu_tv_cancel);
		//取消按钮
		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//销毁弹出框
				dismiss();
			}
		});
		//设置按钮监听
		tvPickPhoto.setOnClickListener(itemsOnClick);
		tvTakePhoto.setOnClickListener(itemsOnClick);
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.BottomPicPopWinAnimBottom);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.bottom_pic_popmenu_ll_top).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});

	}

}
