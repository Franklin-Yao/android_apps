package com.pingo.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pingo.adapter.GoodListAdapter;
import com.pingo.utils.Constant;

public class GoodShoppingRushActivity extends Activity {
	private  ArrayList<Map<String, Object>> data =new ArrayList<Map<String, Object>>();;
	private RequestQueue mQueue;
	private TextView tv_rush_days,tv_rush_hours,tv_rush_minutes,tv_rush_seconds;
	private long days,hours,minutes,seconds;
	private String time="";
	private Handler handler=null;
	private Thread mThread=null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mQueue = Volley.newRequestQueue(this);
		LayoutInflater mLi = LayoutInflater.from(this);
		View v= mLi.inflate(R.layout.activity_shopping_rush, null);
		setContentView(v);
		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("限时抢购");
    	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
    	
    	tv_rush_days=(TextView)findViewById(R.id.tv_rush_days);
    	tv_rush_hours=(TextView)findViewById(R.id.tv_rush_hours);
    	tv_rush_minutes=(TextView)findViewById(R.id.tv_rush_minutes);
    	tv_rush_seconds=(TextView)findViewById(R.id.tv_rush_seconds);
		
    	GetRushGoods(v);
    	handler =new Handler(){
    		public void handleMessage(Message msg){
    			super.handleMessage(msg);
    			getTime();
    			}
    		};
	}
	public void GetRushGoods(final View view)
	{
		String url=Constant.URL+"/goods/getRush";
		Log.e("url", "url:  "+url);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if(response.getString("status").equals("success"))
							{
								time=response.getString("time");
								mThread= new Thread(){
					    			@Override
					    			public void run(){
					    				Message message=new Message();
					    				message.what=1;
					    				handler.postDelayed(this, 1000);
					    				handler.sendMessage(message);
					    				}
					    			};
						    	mThread.start();
						    			
								JSONArray jos=response.getJSONArray("data");
								for(int i=0;i<jos.length();i++)
								{
									final Map<String, Object> map = new HashMap<String, Object>();
									JSONObject jo=jos.getJSONObject(i);
									map.put("good_id",jo.getString("goods_id"));
						        	map.put("name", jo.getString("name"));
						        	String weight=jo.getString("weight");
									weight+=jo.getString("unit");
						        	map.put("weight", weight);
						        	map.put("price",jo.getString("price"));
						        	map.put("mktprice",jo.getString("mktprice"));
						        	map.put("pic",jo.getString("pic"));
						        	data.add(map);
								}
								GoodListAdapter adapter=new GoodListAdapter(GoodShoppingRushActivity.this, data);
								GridView mGridView = (GridView) findViewById(R.id.gv_channellist);
								mGridView.setAdapter(adapter);
							}
							else if(response.getString("status").equals("empty")){
								Toast.makeText(GoodShoppingRushActivity.this, "没有啦", Toast.LENGTH_SHORT).show();
							}
							else{
								Toast.makeText(GoodShoppingRushActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, 
				new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Log.e("error", error.getMessage());
						Toast.makeText(GoodShoppingRushActivity.this, "找不到服务器君啦，请检查网络", Toast.LENGTH_SHORT).show();
					}
				});
	        mQueue.add(jsObjRequest);
	}
	@SuppressLint("SimpleDateFormat")
	public void getTime(){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
		  Date d1 = df.parse(time);
		  Date curDate=new Date(System.currentTimeMillis());//获取当前时间
		  Date d2=df.parse(df.format(curDate));

		  long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
		  days = diff / (1000 * 60 * 60 * 24);
		  hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
		  minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
		  seconds=(diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60)-minutes*1000*60)/1000;
		  if(days<0){
			  return;
		  }
		  tv_rush_days.setText(days+"");
		  tv_rush_hours.setText(hours+"");
		  tv_rush_minutes.setText(minutes+"");
		  tv_rush_seconds.setText(seconds+"");
		}
		catch (Exception e){
			 e.printStackTrace();
		}
	}
	public void onTopLeft(View v) {
		this.finish();
	}
}