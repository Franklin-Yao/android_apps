package com.pingo.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.pingo.adapter.AddressListAdapter;
import com.pingo.utils.Constant;

public class ManageAddressData {
	private RequestQueue mQueue;
	private Context cxt;
	public ManageAddressData(Context cxt,RequestQueue mQueue){
		this.mQueue=mQueue;
		this.cxt=cxt;
	}

	public void getAddress(final SwipeListView list,final ArrayList<Map<String, Object>> data,final String action)
	{
		String url=Constant.URL+"/member/addr/list?token="+Constant.Token;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						Log.e("yao", "resData:"+response);
						try {
							if(response.getString("status").equals("success"))
							{
								JSONArray jos=response.getJSONArray("data");
								Log.e("length address", ""+jos.length());
								for(int i=0;i<jos.length();i++)
								{
									Log.e("address0", ""+i);
									JSONObject jo=jos.getJSONObject(i);
									String address=jo.getString("province")+jo.getString("city")+jo.getString("area")+jo.getString("addr");
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("addr_id", jo.getString("addr_id"));
						        	map.put("name", jo.getString("name"));
						        	map.put("phone", jo.getString("mobile"));
						        	map.put("province", jo.getString("province"));
						        	map.put("school", jo.getString("city"));
						        	map.put("building", jo.getString("area"));
						        	map.put("detail", jo.getString("addr"));
						        	map.put("address", address);
						        	data.add(map);
								}
//						        SimpleAdapter adapter = new SimpleAdapter(cxt,data,R.layout.activity_manage_address_listitem,
//						                new String[]{"name","phone","address"},
//						                new int[]{R.id.tv_name,R.id.tv_phone,R.id.tv_address});
								AddressListAdapter adapter = new AddressListAdapter(cxt, data,list,action);
								list.setAdapter(adapter);
								list.fixListViewHeight(list);
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
					}
				});
	        mQueue.add(jsObjRequest);
		}
}