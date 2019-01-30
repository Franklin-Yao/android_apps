package com.pingo.net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pingo.activity.R;
import com.pingo.utils.Constant;

public class ProductListData {
	private RequestQueue mQueue;
	private Context cxt;
	public ProductListData(Context cxt,RequestQueue mQueue){
		this.mQueue=mQueue;
		this.cxt=cxt;
	}

	public void getProduct(final ListView list,String GoodId, final List<Map<String, Object>> data)
	{
		String url=Constant.URL+"/product/listEx?page=0&goods_id="+GoodId;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						Log.e("yao", "resData:"+response);
						try {
							if(response.getString("status").equals("successful"))
							{
								JSONArray jos=response.getJSONArray("data");
								for(int i=0;i<jos.length();i++)
								{
									JSONObject jo=jos.getJSONObject(i);
									Map<String, Object> map = new HashMap<String, Object>();
						        	map.put("ProductId", jo.getString("product_id"));
						        	map.put("PdtDesc", jo.getString("pdt_desc"));
						        	map.put("price", jo.getString("price"));
						        	data.add(map);
								}
						        SimpleAdapter adapter = new SimpleAdapter(cxt,data,R.layout.activity_manage_address_listitem,
						                new String[]{"ProductId","pdt_desc","price"},
						                new int[]{R.id.tv_name,R.id.tv_phone,R.id.tv_address});
						        list.setAdapter(adapter);
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