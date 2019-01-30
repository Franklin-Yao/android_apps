package com.pingo.activity;

import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pingo.adapter.GoodCatListAdapter;
import com.pingo.adapter.ViewPagerAdapter;
import com.pingo.net.FragmentHomeData;
import com.pingo.utils.CommonMethods;
import com.pingo.utils.Constant;

public class MainFragmentHome extends Fragment implements OnClickListener {
	private TextView TvSearchSubmit;
	private ImageView getCat,IvSearchCancel;
	private LinearLayout mainTopMenu,mainTopSearch;
	private EditText EdtSearch,EtSearchSubmit;
	private LinearLayout view_new_goods,view_shopping_rush;
	private RequestQueue mQueue;
	private TextView TvGoodCatName[]=new TextView[5];
	int IdInclude[]={R.id.main_part4,R.id.main_part5,R.id.main_part6,R.id.main_part7,R.id.main_part8};
	/** 用于设置背景暗淡 */
	private LinearLayout all_choice_layout = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mQueue = Volley.newRequestQueue(getActivity());
		if (getActivity() == null) {
			System.out.println("页面为空");
		}
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main_fragment_home,null);
		initView(view);
		return view;
	}
	private void initView(View view) {
		TvSearchSubmit=(TextView) view.findViewById(R.id.tv_search);
		mainTopMenu = (LinearLayout) view.findViewById(R.id.main_top_menu);
		mainTopSearch = (LinearLayout) view.findViewById(R.id.main_top_search);
		getCat = (ImageView) view.findViewById(R.id.view_getcat);
		IvSearchCancel=(ImageView) view.findViewById(R.id.iv_search_cancel);
		EdtSearch = (EditText) view.findViewById(R.id.et_search);
		EtSearchSubmit = (EditText) view.findViewById(R.id.et_search_submit);
		all_choice_layout = (LinearLayout) view.findViewById(R.id.control_background);
		view_new_goods = (LinearLayout) view.findViewById(R.id.view_new_goods);
		view_shopping_rush = (LinearLayout) view.findViewById(R.id.view_shopping_rush);
		mainTopSearch.setVisibility(View.GONE);
		view_shopping_rush.setOnClickListener(this);
		view_new_goods.setOnClickListener(this);
		EdtSearch.setOnClickListener(this);
		IvSearchCancel.setOnClickListener(this);
		TvSearchSubmit.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				Intent intent = new Intent();
	        	intent.setClass(getActivity(),GoodListActivity.class);
	        	intent.putExtra("from", "search");
	        	intent.putExtra("keyword", URLEncoder.encode(EtSearchSubmit.getText().toString()));
				startActivity(intent);
			}
		});
		getCat.setOnClickListener(this);
		ArrayList<View> vs=new CommonMethods().getViewsByTag((ViewGroup) view,"tv_old_price");
		for(int i=0;i<vs.size();i++)
			((TextView) vs.get(i)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		vs=new CommonMethods().getViewsByTag((ViewGroup) view,"iv_good");
		for(int i=0;i<vs.size();i++)
			((ImageView) vs.get(i)).setClickable(false);
//			((ImageView) vs.get(i)).setOnClickListener(this);
		for(int i=0;i<5;i++){
			TvGoodCatName[i]= (TextView) view.findViewById(IdInclude[i]).findViewById(R.id.tv_goodcat_name);
			TvGoodCatName[i].setText(Constant.cat[i]);
		}
		//获取特价商品
		new FragmentHomeData(getActivity(),mQueue).GetSpecialGoods(view);
		getAds(view);
		getAllGoods(view);
	}
	private void getAds(View v) {
		ViewPager mViewPager;
		mViewPager = (ViewPager) v.findViewById(R.id.vp);
		int[] imageIds = new int[]{R.drawable.fragment_home_ad1,R.drawable.fragment_home_ad2,R.drawable.fragment_home_ad3,
		R.drawable.fragment_home_ad4,R.drawable.fragment_home_ad5};
		 ArrayList<ImageView> images = new ArrayList<ImageView>();
		    for(int i =0; i < imageIds.length; i++){
		        ImageView imageView = new ImageView(getActivity());
		        imageView.setBackgroundResource(imageIds[i]);
		        images.add(imageView);
		    }
		ViewPagerAdapter adapter = new ViewPagerAdapter(images);
	    mViewPager.setAdapter(adapter);
	    mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}

			@Override
			public void onPageSelected(int arg0) {
			}
	    });
	}
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.view_shopping_rush:
			intent = new Intent(getActivity(),GoodShoppingRushActivity.class);
			startActivity(intent);
			break;
		case R.id.view_new_goods:
			intent = new Intent(getActivity(),GoodNewActivity.class);
			startActivity(intent);
			break;
		case R.id.view_getcat:
			getCat(v);
			break;
		case R.id.iv_good:
			intent = new Intent(getActivity(),GoodInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.et_search:
			mainTopMenu.setVisibility(View.GONE);
			mainTopSearch.setVisibility(View.VISIBLE);
			break;
		case R.id.iv_search_cancel:
			mainTopMenu.setVisibility(View.VISIBLE);
			mainTopSearch.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	
	public void getAllGoods(final View v)
	{
		String url[]={Constant.URL+"/goods/getRootDirGds?cat_id=1&condition=price&order=desc&isOnlyAvailable=0&page=1&items=3",
		Constant.URL+"/goods/getRootDirGds?cat_id=2&condition=price&order=desc&isOnlyAvailable=0&page=1&items=3",
		Constant.URL+"/goods/getRootDirGds?cat_id=3&condition=price&order=desc&isOnlyAvailable=0&page=1&items=3",
		Constant.URL+"/goods/getRootDirGds?cat_id=4&condition=price&order=desc&isOnlyAvailable=0&page=1&items=3",
		Constant.URL+"/goods/getRootDirGds?cat_id=5&condition=price&order=desc&isOnlyAvailable=0&page=1&items=3"};
		for(int i=0;i<5;i++){
			Log.e("url",url[i]);
			new FragmentHomeData(getActivity(),mQueue).RequestGetGoods(v,url[i],IdInclude[i]);
		}
	}
	
	public void getCat(View v) {
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.activity_main_home_dialog_cat, null);
 		final Dialog dialog = new AlertDialog.Builder(getActivity()).create();
 		dialog.show();
 		dialog.setCanceledOnTouchOutside(true);
 		dialog.getWindow().setContentView(view);
 		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
 		dialog.getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        params.width = 300;
        params.height = params.WRAP_CONTENT;
        params.x = 20;
        params.y = 140;
        dialog.getWindow().setAttributes(params);
        final ExpandableListAdapter adapter = new GoodCatListAdapter(getActivity(),dialog);
        final ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.list);
        expandableListView.setAdapter(adapter);
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0, count = expandableListView.getExpandableListAdapter().getGroupCount(); i < count; i++) {
                    if (groupPosition != i) {// 关闭其他分组
                    	expandableListView.collapseGroup(i);
                    }
                }
            }
        });
        //设置item点击的监听器
        expandableListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
                dialog.dismiss();
                Intent intent = new Intent();
	            intent.putExtra("from", "goodCatActivity");
	            intent.putExtra("FirstID", groupPosition);
	            intent.putExtra("SecondID", childPosition);
        		intent.setClass(getActivity(),GoodListActivity.class);
        		startActivity(intent);
                return false;
            }
        });
	}
	
	 public void getMoreGoods(View v) {
		 int[] ID={R.id.rl_get_more_goods_1,R.id.rl_get_more_goods_2,R.id.rl_get_more_goods_3,R.id.rl_get_more_goods_4,R.id.rl_get_more_goods_5};
		 Intent intent = new Intent();
		 for(int i=0;i<5;i++)
	    	if(v.getId()==ID[i])
	    		intent.putExtra("FirstID", i);
		intent.setClass(getActivity(),FirstCatGoodActivity.class);
		startActivity(intent);
	  }
}
