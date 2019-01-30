package com.pingo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.pingo.adapter.GoodListAdapter;
import com.pingo.net.FragmentHomeData;
import com.pingo.net.ManageAddressData;
import com.pingo.utils.Constant;

public class OrderActivity extends Activity {
	private ListView AdressList;
	private RequestQueue mQueue;
	private Context cxt;
	// 购物车的listview
	private SwipeListView swipeListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cxt=this;
		mQueue = Volley.newRequestQueue(this);
		LayoutInflater mLi = LayoutInflater.from(this);
		View v= mLi.inflate(R.layout.activity_good_new, null);
		setContentView(v);
        TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("我的订单");
    	ImageView IvLeft=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvLeft.setVisibility(View.GONE);
		swipeListView = (SwipeListView)findViewById(R.id.listAddress);
		swipeListView.setOffsetLeft(this.getResources().getDisplayMetrics().widthPixels * 2 / 3);
		final ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		getOrder(v);
	}
	public void getOrder(final View view)
	{
		String url=Constant.URL+"/member/order/list?page=0&token"+Constant.Token;
		Log.e("url", "url:  "+url);
		final ArrayList<Map<String, Object>> data =new ArrayList<Map<String, Object>>();
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if(response.getString("status").equals("success"))
							{
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
								GoodListAdapter adapter=new GoodListAdapter(cxt, data);
								GridView mGridView = (GridView) findViewById(R.id.gv_channellist);
								mGridView.setAdapter(adapter);
							}
							else if(response.getString("status").equals("empty")){
								Toast.makeText(cxt, "没有啦", Toast.LENGTH_SHORT).show();
							}
							else{
								Toast.makeText(cxt, response.getString("msg"), Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, 
				new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Log.e("error", error.getMessage());
						Toast.makeText(cxt, "找不到服务器君啦，请检查网络", Toast.LENGTH_SHORT).show();
					}
				});
	        mQueue.add(jsObjRequest);
	}
	public void onTopLeft(View v) {
    	finish();
	}
}