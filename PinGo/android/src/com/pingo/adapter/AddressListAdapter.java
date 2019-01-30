package com.pingo.adapter;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.pingo.activity.ConfirmOrderActivity;
import com.pingo.activity.ManageAddressActivity;
import com.pingo.activity.NewOrEditAddressActivity;
import com.pingo.activity.R;
import com.pingo.net.FragmentHomeData;
import com.pingo.utils.Constant;

public class AddressListAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, Object>> data;
	private SwipeListView mSwipeListView;
	private TextView tv_name,tv_phone,tv_address;
	private String action="";

	public AddressListAdapter(Context context,List<Map<String, Object>> data,SwipeListView swipeListView,String action) {
		this.context = context;
		this.data = data;
		this.mSwipeListView = swipeListView;
		this.action=action;
		mSwipeListView.setDividerHeight(0);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	@Override
	public View getView(final int position, View currentView, ViewGroup arg2) {
		View v= LayoutInflater.from(context).inflate(R.layout.activity_manage_address_listitem1, null);
		tv_name = (TextView) v.findViewById(R.id.tv_name);
		tv_phone = (TextView) v.findViewById(R.id.tv_phone);
		tv_address = (TextView) v.findViewById(R.id.tv_address);
		tv_name.setText(data.get(position).get("name")+"");
		tv_phone.setText(data.get(position).get("phone")+"");
		String address=""+data.get(position).get("province")+data.get(position).get("school")+
    			data.get(position).get("building")+data.get(position).get("detail");
		tv_address.setText(address);
		
		LinearLayout LiItem = (LinearLayout) v.findViewById(R.id.ll_item);
		LiItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				Log.e("action:  ", action);
				if(action.equals("chooseAddress")){
					intent.setClass(context, ConfirmOrderActivity.class);
					RequestQueue queue = Volley.newRequestQueue(context);
					new FragmentHomeData(context,queue).setDefaultAdress(data.get(position).get("addr_id")+"");
					intent.putExtra("addr_id", data.get(position).get("addr_id")+"");
					Constant.sp=context.getSharedPreferences("sp", Context.MODE_PRIVATE);
		        	Editor editor = Constant.sp.edit(); 
		        	editor.putString("addr_id", data.get(position).get("addr_id")+"");
		        	editor.putString("addr_name", data.get(position).get("name")+"");
		        	editor.putString("addr_phone",data.get(position).get("phone")+"");
		        	String address=""+data.get(position).get("province")+data.get(position).get("school")+
		        			data.get(position).get("building")+data.get(position).get("detail");
		        	editor.putString("addr_address",address);
		        	editor.commit();
		        	Log.e("defaddr:", Constant.sp.getString("addr_name", ""));
		        	ManageAddressActivity.instance.setResult(Activity.RESULT_OK, intent);
		        	ManageAddressActivity.instance.finish();
				}
				else{
					intent.setClass(context,NewOrEditAddressActivity.class);
					intent.putExtra("ClickType", NewOrEditAddressActivity.TYPE_EDIT_ADDRESS);
					intent.putExtra("addr_id", data.get(position).get("addr_id")+"");
					intent.putExtra("name", data.get(position).get("name")+"");
					intent.putExtra("phone", data.get(position).get("phone")+"");
					intent.putExtra("province", data.get(position).get("province")+"");
					intent.putExtra("school", data.get(position).get("school")+"");
					intent.putExtra("building", data.get(position).get("building")+"");
					intent.putExtra("detail", data.get(position).get("detail")+"");
					ManageAddressActivity.instance.startActivity(intent);
				}
			}
		});
		ImageButton del = (ImageButton) v.findViewById(R.id.id_remove);
		del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DelAddress(context,position);
			}
		});
		return v;
	}

	// 从服务器删除某商品
	public void DelAddress(final Context context, final int position) {
		String url = Constant.URL+"/member/addr/delete?token="+Constant.Token+ "&addr_id=" + data.get(position).get("addr_id");
		RequestQueue queue = Volley.newRequestQueue(context);
		Log.e("url:", url);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@SuppressLint("NewApi")
					public void onResponse(JSONObject response) {
						try {
							if (response.getString("status").equals("success")) {
								ManageAddressActivity.instance.recreate();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Log.e("error", "onErrorResponse");
					}
				});
		queue.add(jsObjRequest);
	}
}