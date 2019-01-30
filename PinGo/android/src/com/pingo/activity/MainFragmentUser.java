package com.pingo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pingo.net.FragmentUserData;
import com.pingo.utils.CommonMethods;
import com.pingo.utils.ImageProcessUtil;

public class MainFragmentUser extends Fragment implements OnClickListener{
	private LinearLayout profile,address,purse;
	private View view;
	private RequestQueue mQueue;
	static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	public static final int PHOTO_REQUEST_CUT = 3;// 结果
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main_fragment_user, null);
		initView(view);
		return view;
	}
	private void initView(View v) {
		mQueue = Volley.newRequestQueue(getActivity());
		profile=(LinearLayout)v.findViewById(R.id.view_profile);
		address=(LinearLayout)v.findViewById(R.id.view_address);
		profile.setOnClickListener(this);
		address.setOnClickListener(this);
		TextView Title=(TextView)v.findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("个人中心");
    	ImageView IvLeft=(ImageView)v.findViewById(R.id.common_top).findViewById(R.id.iv_back);
    	IvLeft.setVisibility(View.GONE);
    	ImageView IvRight=(ImageView)v.findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setBackgroundResource(R.drawable.activity_main_user_settings);
    	IvRight.setOnClickListener(this);
    	new CommonMethods().setImageViewParams(IvRight, 60, 60);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_profile:
			profilePersonInfo(v);
			break;
		case R.id.iv_cat:
			setting(v);
			break;
		case R.id.view_address:
			myAddress(v);
			break;
		}
	}
	private void myAddress(View v) {
		Intent intent = new Intent();
		intent.putExtra("action", "");
		intent.setClass(getActivity(),ManageAddressActivity.class);
		startActivity(intent);
	}
	public void MyStore(View v)
	{
		Intent intent = new Intent();
		intent.putExtra("from", "MyStore");
		intent.putExtra("FirstID", 1);
		intent.putExtra("SecondID", 1);
		intent.setClass(getActivity(),GoodListActivity.class);
		startActivity(intent);
	}
	public void profilePersonInfo(View v)
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(),PersonInfoActivity.class);
		startActivity(intent);
	}
	public void setting(View v)
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(),SettingsActivity.class);
		startActivity(intent);
	}
	public void exit_settings(View v) {                       //退出  伪“对话框”，其实是一个activity
		/*Intent intent = new Intent (mainActivity.this,ExitFromSettings.class);			
		startActivity(intent);*/
	 }
}
