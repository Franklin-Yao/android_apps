package com.pingo.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pingo.adapter.ViewPagerAdapter;
import com.pingo.net.GoodInfoData;
import com.pingo.utils.Constant;
import com.pingo.utils.ImageDownLoadUtil;
import com.pingo.utils.ImageDownLoadUtil.ICallBack;

public class GoodInfoActivity extends Activity{
    private RequestQueue mQueue;
    /**弹出商品订单信息详情*/
	private SelectAttributePopWindow popWindow;
    public String ProductId="",PdtDesc="",GoodId="",GoodName="",Price="";
    private View view;
    public static final int REQUEST_CODE_01 = 1;
    public static GoodInfoActivity instance;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance=this;
		mQueue = Volley.newRequestQueue(this);
		view = LayoutInflater.from(this).inflate(R.layout.activity_good_info, null);
	    setContentView(view);
	    Intent intent=getIntent();
	    GoodId=intent.getStringExtra("GoodId");
	    GoodName=intent.getStringExtra("GoodName");
	    Price=intent.getStringExtra("Price");
	    TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("商品详情");
    	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
    	initView(view,GoodId);
	}
	
	private void initView(View v, String GoodId) {
		new GoodInfoData(this,mQueue).getGoodInfo(v, GoodId);
	}

	public void onTopLeft(View v) {
		this.finish();
	 }
	
	public void onMinus(View v) {
		TextView ProductNum=(TextView)findViewById(R.id.tv_product_num);
		int num=Integer.parseInt(ProductNum.getText().toString());
		if(num>1)
			ProductNum.setText((num-1)+"");
	}
	
	public void onAdd(View v) {
		TextView ProductNum=(TextView)findViewById(R.id.tv_product_num);
		int num=Integer.parseInt(ProductNum.getText().toString());
		if(num<100)
			ProductNum.setText((num+1)+"");
	}
	@SuppressLint("NewApi")
	public void onAddToCart(View v) {
		String Num=((TextView)findViewById(R.id.tv_product_num)).getText().toString();
		Constant.sp=getSharedPreferences("sp", Context.MODE_PRIVATE);
		if(!Constant.sp.getString("isLogin", "none").equals("yes")){
			Intent intent = new Intent();
			intent.setClass(this,LoginActivity.class);
			intent.putExtra("action", "");
			startActivity(intent);
		}
		else
			new GoodInfoData(this,mQueue).addCart(this,GoodId,GoodName,Num,Price);
	}
}
