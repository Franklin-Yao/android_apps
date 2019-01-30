package com.pingo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
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

public class GoodNewActivity extends Activity {
	private  ArrayList<Map<String, Object>> data =new ArrayList<Map<String, Object>>();;
	private RequestQueue mQueue;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mQueue = Volley.newRequestQueue(this);
		LayoutInflater mLi = LayoutInflater.from(this);
		View v= mLi.inflate(R.layout.activity_good_new, null);
		setContentView(v);
		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("新品上架");
    	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
		
    	GetGoods(v);
	}
	public void GetGoods(final View view)
	{
		String url=Constant.URL+"/goods/getNew";
		Log.e("url", "url:  "+url);
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
								GoodListAdapter adapter=new GoodListAdapter(GoodNewActivity.this, data);
								GridView mGridView = (GridView) findViewById(R.id.gv_channellist);
								mGridView.setAdapter(adapter);
							}
							else if(response.getString("status").equals("empty")){
								Toast.makeText(GoodNewActivity.this, "没有啦", Toast.LENGTH_SHORT).show();
							}
							else{
								Toast.makeText(GoodNewActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, 
				new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Log.e("error", error.getMessage());
						Toast.makeText(GoodNewActivity.this, "找不到服务器君啦，请检查网络", Toast.LENGTH_SHORT).show();
					}
				});
	        mQueue.add(jsObjRequest);
	}
	public void onTopLeft(View v) {
		this.finish();
	}
}