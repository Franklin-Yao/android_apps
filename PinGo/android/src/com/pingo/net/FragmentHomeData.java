package com.pingo.net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pingo.activity.GoodInfoActivity;
import com.pingo.activity.R;
import com.pingo.utils.Cache;
import com.pingo.utils.Constant;
import com.pingo.utils.ImageDownLoadUtil;
import com.pingo.utils.ImageDownLoadUtil.ICallBack;
import com.pingo.utils.Md5Util;
import com.pingo.widget.MyDialog;

public class FragmentHomeData {
	private RequestQueue mQueue;
	private Context cxt;
	private Dialog dlg;
	public FragmentHomeData(Context cxt,RequestQueue mQueue){
		this.mQueue=mQueue;
		this.cxt=cxt;
	}

	public void RequestGetGoods(final View v,String url,final int IdInclude) {
		dlg=MyDialog.createLoadingDialog(cxt,"小卓去服务器取东西了，马上回来");
    	dlg.show();
		final ImageView[] GoodPicIV = new ImageView[3];
		final int IdGood[]={R.id.include_main_good_cat_1,R.id.include_main_good_cat_2,R.id.include_main_good_cat_3};
		final TextView [] GoodNameTV = new TextView[3];
		final TextView [] GoodWeightTV = new TextView[3];
		final TextView [] GoodNewPriceTV = new TextView[3];
		final TextView [] GoodOldPriceTV = new TextView[3];
		for(int j=0;j<3;j++){
			LinearLayout GoodView=(LinearLayout) v.findViewById(IdInclude).findViewById(IdGood[j]);
			GoodPicIV[j]=(ImageView)GoodView.findViewById(R.id.iv_good);
//			PicId[j]=GoodView.findViewById(R.id.iv_good).getId();
			GoodNameTV[j]= (TextView) GoodView.findViewById(R.id.tv_good_name);
			GoodWeightTV[j]= (TextView) GoodView.findViewById(R.id.tv_good_weight);
			GoodNewPriceTV[j]= (TextView) GoodView.findViewById(R.id.tv_new_price);
			GoodOldPriceTV[j]= (TextView) GoodView.findViewById(R.id.tv_old_price);
		}
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						dlg.dismiss();
						try {
							if(response.getString("status").equals("success")){
								for(int i=0;i<3;i++){
									final int flag=i;
									JSONObject jo=response.getJSONArray("data").getJSONObject(i);
									final String ImgUrl=jo.getString("pic");
									final String name=jo.getString("name");
									String weight=jo.getString("weight");
									weight+=jo.getString("unit");
									final String GoodNewPrice=jo.getString("price");
									String GoodOldPrice=jo.getString("mktprice");
									final String GoodId=jo.getString("goods_id");
									GoodNameTV[i].setText(name);
									GoodWeightTV[i].setText(weight);
									GoodNewPriceTV[i].setText(GoodNewPrice);
									GoodOldPriceTV[i].setText(GoodOldPrice);
									final String picName=Md5Util.getMD5Str(ImgUrl);
									Bitmap bmp=new Cache(cxt).getBitmap(picName);
									if(bmp!=null){
										Log.e("yao", "tup图片存在");
										GoodPicIV[flag].setImageBitmap(bmp);
										GoodPicIV[flag].setOnClickListener(new OnClickListener(){
											public void onClick(View arg0) {
												Intent intent = new Intent(cxt,GoodInfoActivity.class);
												intent.putExtra("GoodId",GoodId);
												intent.putExtra("GoodName",name);
												intent.putExtra("Price",GoodNewPrice);
												cxt.startActivity(intent);
											}
										});
									}
									else
									new ImageDownLoadUtil().readBitmapViaVolley1(mQueue, ImgUrl, new ICallBack(){
										@Override
										public void onSuccess(Bitmap bmp) {
											GoodPicIV[flag].setImageBitmap(bmp);
											new Cache(cxt).saveBitmap(picName, bmp);
											GoodPicIV[flag].setOnClickListener(new OnClickListener(){
												public void onClick(View arg0) {
													Intent intent = new Intent(cxt,GoodInfoActivity.class);
													intent.putExtra("GoodId",GoodId);
													intent.putExtra("GoodName",name);
													intent.putExtra("Price",GoodNewPrice);
													cxt.startActivity(intent);
												}
											});
										}
										public void onFailure() {
										}});
									}
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

	public void getAddress(final ListView list,final List<Map<String, Object>> data)
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
						        	map.put("name", jo.getString("name"));
						        	map.put("phone", jo.getString("mobile"));
						        	map.put("province", jo.getString("province"));
						        	map.put("school", jo.getString("city"));
						        	map.put("building", jo.getString("area"));
						        	map.put("detail", jo.getString("addr"));
						        	map.put("address", address);
						        	map.put("addId", jo.getString("addr_id"));
						        	data.add(map);
								}
						        SimpleAdapter adapter = new SimpleAdapter(cxt,data,R.layout.activity_main_home_address_listitem,
						                new String[]{"name","phone","address"},
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
	
	public void setDefaultAdress(String AddId) {
		String url = Constant.URL+"/member/addr/setDef?token="+Constant.Token+"&addr_id="+AddId;
		Log.e("yao", "addurl:"+url);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						Log.e("yao", "resData:"+response);
						try {
							if(response.getString("status").equals("success")){
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
	
	public void delAdress(String AddId) {
		String url = Constant.URL+"/member/addr/del?token="+Constant.Token+"&addr_id="+AddId;
		Log.e("yao", "addurl:"+url);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						Log.e("yao", "resData:"+response);
						try {
							if(response.getString("status").equals("success")){
								Toast.makeText(cxt, "删除成功", Toast.LENGTH_SHORT).show();
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
	public void GetSpecialGoods(final View view)
	{
		final int [] ID={R.id.include_main_good_1,R.id.include_main_good_2,R.id.include_main_good_3,R.id.include_main_good_4,
				R.id.include_main_good_5,R.id.include_main_good_6,R.id.include_main_good_7};
		String url=Constant.URL+"/goods/getSale";
		Log.e("url", "url:  "+url);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							if(response.getString("status").equals("success"))
							{
								JSONArray jos=response.getJSONArray("data");
								for(int i=0;i<jos.length();i++)
								{
									if(i>6)
										break;
									final ImageView iv_good=(ImageView)view.findViewById(ID[i]).findViewById(R.id.iv_good);
									TextView tv_good_name=(TextView)view.findViewById(ID[i]).findViewById(R.id.tv_good_name);
									TextView tv_good_weight=(TextView)view.findViewById(ID[i]).findViewById(R.id.tv_good_weight);
									TextView tv_new_price=(TextView)view.findViewById(ID[i]).findViewById(R.id.tv_new_price);
									TextView tv_old_price=(TextView)view.findViewById(ID[i]).findViewById(R.id.tv_old_price);
									final int flag=i;
									JSONObject jo=response.getJSONArray("data").getJSONObject(i);
									final String ImgUrl=jo.getString("pic");
									final String name=jo.getString("name");
									String weight=jo.getString("weight");
									weight+=jo.getString("unit");
									final String GoodNewPrice=jo.getString("price");
									String GoodOldPrice=jo.getString("mktprice");
									final String GoodId=jo.getString("goods_id");
									tv_good_name.setText(name);
									tv_good_weight.setText(weight);
									tv_new_price.setText(GoodNewPrice);
									tv_old_price.setText(GoodOldPrice);
									final String picName=Md5Util.getMD5Str(ImgUrl);
									Bitmap bmp=new Cache(cxt).getBitmap(picName);
									if(bmp!=null){
										Log.e("yao", "tup图片存在");
										iv_good.setImageBitmap(bmp);
										iv_good.setOnClickListener(new OnClickListener(){
											public void onClick(View arg0) {
												Intent intent = new Intent(cxt,GoodInfoActivity.class);
												intent.putExtra("GoodId",GoodId);
												intent.putExtra("GoodName",name);
												intent.putExtra("Price",GoodNewPrice);
												cxt.startActivity(intent);
											}
										});
									}
									else
									new ImageDownLoadUtil().readBitmapViaVolley1(mQueue, ImgUrl, new ICallBack(){
										@Override
										public void onSuccess(Bitmap bmp) {
											iv_good.setImageBitmap(bmp);
											new Cache(cxt).saveBitmap(picName, bmp);
											iv_good.setOnClickListener(new OnClickListener(){
												public void onClick(View arg0) {
													Intent intent = new Intent(cxt,GoodInfoActivity.class);
													intent.putExtra("GoodId",GoodId);
													intent.putExtra("GoodName",name);
													intent.putExtra("Price",GoodNewPrice);
													cxt.startActivity(intent);
												}
											});
										}
										public void onFailure() {
										}});
								}
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
						Toast.makeText(cxt, "找不到服务器君啦，请检查网络", Toast.LENGTH_SHORT).show();
					}
				});
	        mQueue.add(jsObjRequest);
		}
}
