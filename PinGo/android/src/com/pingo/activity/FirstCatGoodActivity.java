package com.pingo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pingo.net.FirstCatData;
import com.pingo.utils.CommonMethods;
import com.pingo.utils.Constant;
import com.pingo.widget.pullrefresh.PullToRefreshLayout;
import com.pingo.widget.pullrefresh.PullToRefreshLayout.OnRefreshListener;

public class FirstCatGoodActivity extends Activity implements OnRefreshListener{
	private RequestQueue mQueue;
	private int FirstID;
	String [][] cats={Constant.cat1,Constant.cat2,Constant.cat3,Constant.cat4,Constant.cat5};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mQueue = Volley.newRequestQueue(this);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		LayoutInflater mLi = LayoutInflater.from(this);
		View view= mLi.inflate(R.layout.activity_first_cat_good, null);
//	    setContentView(R.layout.activity_first_cat_good);
		setContentView(view);
	    Intent i=getIntent();
	    FirstID=i.getIntExtra("cat", 1);
	    initView(FirstID,view);
	}
	private void initView(int i, View view) {
		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
		LinearLayout layout = (LinearLayout) findViewById(R.id.item_second_cat);
	    LayoutInflater mLi = LayoutInflater.from(this);
    	String [] cat;
    	cat=cats[i];
    	Title.setText(Constant.cat[i]);
    	View v = new View(this);
	    for(int j=0;j<cat.length;j++){
	    	Log.e("yao", j+"");
	    	v= mLi.inflate(R.layout.common_item_cat_good, null);
	 	    layout.addView(v);
	 	    TextView TvGetMoreGoods=(TextView)v.findViewById(R.id.tv_get_more_goods);
	 	   	TvGetMoreGoods.setTag("second_cat"+j);
	 	    TextView tv=(TextView)v.findViewById(R.id.tv_goodcat_name);
	 	    tv.setText(cat[j]);
	 	    
//	 	    LinearLayout ll=(LinearLayout)v.findViewById(R.id.item_second_cat_good);
//	 	    ll.setTag("view_second_cat"+j);
//	 	    LinearLayout GoodView=(LinearLayout) v.findViewById(IdGood[j]);
	 	    new FirstCatData(this,mQueue).RequestGetGoods(v,FirstID,j);
	    }
	    
	    ArrayList<View> vs=new CommonMethods().getViewsByTag((ViewGroup) view,"tv_old_price");
		for(int j=0;j<vs.size();j++)
			((TextView) vs.get(j)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		
	    //下拉刷新监听
	    PullToRefreshLayout mrefreshLayout=((PullToRefreshLayout) findViewById(R.id.refresh_view));
	    mrefreshLayout.setOnRefreshListener(this);
//	    getAllGoods(view);
	}
	
	public void getMoreGoods(View v) {
    	Intent intent = new Intent();
    	for(int i=0;i<cats[FirstID].length;i++)
    		if(v.getTag().equals("second_cat"+i))
    			intent.putExtra("SecondID", i);
    	intent.putExtra("FirstID", FirstID);
    	intent.putExtra("from", "FirstCatGoodActivity");
		intent.setClass(this,GoodListActivity.class);
		startActivity(intent);
	}
	
//	private void getAllGoods(View v) {
//		for(int i=0;i<cats[FirstID].length;i++){
//			new ActivityFirstCatData(this,mQueue).RequestGetGoods(v,FirstID,i);
//		}
//	}
	public void onTopLeft(View v) {
		this.finish();
    }
	public void onSubmit(View v) {
		Intent intent=new Intent(FirstCatGoodActivity.this,ConfirmOrderActivity.class);
		startActivity(intent);
    }
	
	public void onTopRight(View v) {
		View view=LayoutInflater.from(this).inflate(R.layout.activity_first_cat_dialog_morecat, null);
 		final Dialog dialog = new AlertDialog.Builder(this).create();
 		dialog.show();
 		dialog.setCanceledOnTouchOutside(true);
 		dialog.getWindow().setContentView(view);
 		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
 		dialog.getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        params.width = 300;
        params.height = params.WRAP_CONTENT;
        params.x = 20;
        params.y = 90;
        dialog.getWindow().setAttributes(params);
        ListView listView = (ListView) view.findViewById(R.id.list);
        String [][] cats={Constant.cat1,Constant.cat2,Constant.cat3,Constant.cat4,Constant.cat5};
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for(int i=0;i<cats[FirstID].length;i++){
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("title", cats[FirstID][i]);
        	data.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,data,R.layout.activity_first_cat_item_cat,
                new String[]{"title"},
                new int[]{R.id.tv_cat_name});
        listView.setAdapter(adapter);
       
        listView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
        });
	}
	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		
	}
}