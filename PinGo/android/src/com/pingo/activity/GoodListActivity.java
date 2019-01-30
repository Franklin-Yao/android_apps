package com.pingo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pingo.net.GoodListData;
import com.pingo.utils.Constant;
import com.pingo.widget.pullrefresh.PullToRefreshLayout;
import com.pingo.widget.pullrefresh.PullToRefreshLayout.OnRefreshListener;
import com.pingo.widget.pullrefresh.PullableScrollView;

public class GoodListActivity extends Activity implements OnRefreshListener{
        private GridView mygridview;
        private View v;
        private int FirstID,SecondID;
        private RequestQueue mQueue;
        private List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
        String from="";
        
        PullableScrollView mPullRefreshScrollView;
    	ScrollView mScrollView;
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            mQueue = Volley.newRequestQueue(this);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            LayoutInflater mLi = LayoutInflater.from(this);
            v= mLi.inflate(R.layout.activity_good_list, null);
    		setContentView(v);
            Intent intent=getIntent();
    	    from=intent.getStringExtra("from");
    	    FirstID=intent.getIntExtra("FirstID", 1);
    	    SecondID=intent.getIntExtra("SecondID", 1);
    	    initView(v);
    	    if(from.equals("MyStore")){
    	    	TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	    	Title.setText("我收藏的宝贝");
    	    }
    	    else if(from.equals("search")){
    	    	TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	    	Title.setText("搜索结果");
    	    }
        }
        private void initView(View view) {
        	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
        	IvRight.setVisibility(View.GONE);
        	String [][] cats={Constant.cat1,Constant.cat2,Constant.cat3,Constant.cat4,Constant.cat5};
        	String CatName=cats[FirstID][SecondID];
        	TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
        	Title.setText(CatName);
        	
        	PullToRefreshLayout mrefreshLayout=((PullToRefreshLayout) findViewById(R.id.refresh_view));
    	    mrefreshLayout.setOnRefreshListener(this);
    	    mygridview = (GridView) findViewById(R.id.gv_channellist);
    	    Log.e("", "keyword:  "+getIntent().getStringExtra("keyword"));
    	    new GoodListData(this,mQueue).RequestGetGoods(view,mygridview,FirstID,SecondID,getIntent().getStringExtra("from"), "",getIntent().getStringExtra("keyword"), null);
		}
		
		public void onTopLeft(View v) {
			this.finish();
	    }
		@Override
		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
			new GoodListData(this,mQueue).RequestGetGoods(v,mygridview,FirstID,SecondID,getIntent().getStringExtra("from"),"refresh","",pullToRefreshLayout);
			new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					// 千万别忘了告诉控件加载完毕了哦！
					pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
				}
			}.sendEmptyMessageDelayed(0, 4000);
		}
		@Override
		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
			new GoodListData(this,mQueue).RequestGetGoods(v,mygridview,FirstID,SecondID,getIntent().getStringExtra("from"),"more","",pullToRefreshLayout);
			new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					// 千万别忘了告诉控件加载完毕了哦！
					pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
				}
			}.sendEmptyMessageDelayed(0, 4000);
		}
}