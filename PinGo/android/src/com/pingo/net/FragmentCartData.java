package com.pingo.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pingo.activity.R;
import com.pingo.utils.Constant;

public class FragmentCartData {
	private RequestQueue mQueue;
	private Context cxt;
	public FragmentCartData(Context cxt,RequestQueue mQueue){
		this.mQueue=mQueue;
		this.cxt=cxt;
	}

	public void getAddress(final View view)
	{
		String url=Constant.URL+"/member/addr/getDef?token="+Constant.Token;
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						Log.e("yao", "resData:"+response);
						try {
							if(response.getString("status").equals("success"))
							{
								JSONArray jos=response.getJSONArray("data");
								JSONObject jo=jos.getJSONObject(0);
								TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
					    		tv_name.setText( jo.getString("name"));
					    		TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
					    		tv_phone.setText( jo.getString("mobile"));
					    		TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
					    		String address=jo.getString("province")+jo.getString("city")+jo.getString("area")+jo.getString("addr");
					    		tv_address.setText(address);
					    		Constant.sp=cxt.getSharedPreferences("sp", Context.MODE_PRIVATE);
					        	Editor editor = Constant.sp.edit();
					        	editor.putString("addr_id",  jo.getString("addr_id"));
					        	editor.putString("addr_name",  jo.getString("name"));
					        	editor.putString("addr_phone",jo.getString("mobile"));
					        	editor.putString("addr_address",address);
					        	editor.commit();
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