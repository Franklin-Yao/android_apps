package com.pingo.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pingo.activity.R;
import com.pingo.adapter.AddressListAdapter;
import com.pingo.adapter.GoodListAdapter;
import com.pingo.utils.Cache;
import com.pingo.utils.Constant;
import com.pingo.utils.ImageDownLoadUtil;
import com.pingo.utils.ImageDownLoadUtil.ICallBack;
import com.pingo.utils.Md5Util;
import com.pingo.widget.MyDialog;
import com.pingo.widget.pullrefresh.PullToRefreshLayout;

public class GoodListData {
	private RequestQueue mQueue;
	private Context cxt;
	private Dialog dlg;
	private static ArrayList<Map<String, Object>> data =null,dataSearch=null;;
	private static int page=0,pageSearch=0;
	public GoodListData(Context cxt,RequestQueue mQueue){
		this.mQueue=mQueue;
		this.cxt=cxt;
	}

	public void RequestGetGoods(View view,final GridView mGridView,int FirstID,int SecondID,final String from, String type, String SearchKeyword, final PullToRefreshLayout pullToRefreshLayout)
	{
		dlg=MyDialog.createLoadingDialog(cxt,"加载中，不要急哦");
    	dlg.show();
		int FirstID1=FirstID+1;
		String url="";
		//搜索
		if(from.equals("search")){
			if(type.equals("more")){
				pageSearch++;
				url=Constant.URL+"/goods/searchGdsInfo?name=" +SearchKeyword+"&page="+pageSearch+"&items=10";
			}
			else{
				dataSearch=new ArrayList<Map<String, Object>>();
				pageSearch=0;
				url=Constant.URL+"/goods/searchGdsInfo?name=" +SearchKeyword+"&page=0&items=10";
			}
			Log.e("url", "url:  "+url);
		}
		//二级目录商品
		else{
			if(type.equals("more")){
				page++;
				url=Constant.URL+"/goods/getChildDirGds?condition=price&order=desc" +
						"&isOnlyAvailable="+page+"&page=1&items=6&parent_id="+FirstID1+"&cat_id="+Constant.catId[FirstID][SecondID];
			}
			else{
				data=new ArrayList<Map<String, Object>>();
				page=0;
				url=Constant.URL+"/goods/getChildDirGds?condition=price&order=desc" +
						"&isOnlyAvailable=0&page=0&items=6&parent_id="+FirstID1+"&cat_id="+Constant.catId[FirstID][SecondID];
			}
		}
		Log.e("url:",url);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						dlg.dismiss();
						Log.e("yao", "resData:"+response);
						try {
							if(response.getString("status").equals("success"))
							{
								JSONArray jos=response.getJSONArray("data");
								Log.e("length address", ""+jos.length());
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
						        	if(from.equals("search"))
						        		dataSearch.add(map);
						        	else
						        		data.add(map);
								}
								GoodListAdapter adapter=null;
								if(from.equals("search"))
									adapter = new GoodListAdapter(cxt, dataSearch);
					        	else
					        		adapter = new GoodListAdapter(cxt, data);
								mGridView.setAdapter(adapter);
								if(pullToRefreshLayout!=null)
									pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
							}
							else if(response.getString("status").equals("empty")){
								Toast.makeText(cxt, "没有啦", Toast.LENGTH_SHORT).show();
							}
							else{
								Toast.makeText(cxt, response.getString("msg"), Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if(pullToRefreshLayout!=null)
								pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
						}
					}
				}, 
				new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
					}
				});
	        mQueue.add(jsObjRequest);
		}
}