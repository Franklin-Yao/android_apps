package com.pingo.net;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pingo.activity.GoodInfoActivity;
import com.pingo.activity.R;
import com.pingo.adapter.ViewPagerAdapter;
import com.pingo.utils.Cache;
import com.pingo.utils.Constant;
import com.pingo.utils.ImageDownLoadUtil;
import com.pingo.utils.Md5Util;
import com.pingo.utils.ImageDownLoadUtil.ICallBack;
import com.pingo.widget.MyDialog;

public class GoodInfoData {
	private RequestQueue mQueue;
	private Context cxt;
	private Dialog dlg;
	private int oldPosition = 0;//getGoodPic 记录上一次点的位置
	public GoodInfoData(Context cxt,RequestQueue mQueue){
		this.mQueue=mQueue;
		this.cxt=cxt;
	}

	public void getGoodInfo(final View v,String GoodId)
	{
		dlg=MyDialog.createLoadingDialog(cxt,"小卓去服务器取东西了，马上回来");
    	dlg.show();
		String url=Constant.URL+"/goods/getGoodsInfo?goods_id="+GoodId;
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
								JSONObject jo=jos.getJSONObject(0);
								String mktprice=jo.getString("mktprice");
								String intro=jo.getString("goods_intro");
								String price=jo.getString("price");
								String name=jo.getString("name");
//								String limitNum=jo.getString("limit_num");
								String weight=jo.getString("weight")+jo.getString("unit");
								((TextView)v.findViewById(R.id.tv_good_name)).setText(name);
								((TextView)v.findViewById(R.id.tv_good_weight)).setText(weight);
								((TextView)v.findViewById(R.id.tv_good_price)).setText(price);
//								((TextView)v.findViewById(R.id.tv_good_limit_num)).setText("今日限购："+limitNum);
								((TextView)v.findViewById(R.id.tv_good_mktprice)).setText("校均价 ￥"+mktprice);
								((TextView)v.findViewById(R.id.tv_good_intro)).setText(intro);
								
								final ImageView GoodPic=((ImageView)v.findViewById(R.id.iv_good_pic));
								String pic=jo.getString("pic");
								
								final String picName=Md5Util.getMD5Str(pic);
								Bitmap bmp=new Cache(cxt).getBitmap(picName);
								if(bmp!=null){
									Log.e("yao", "tup图片存在");
									GoodPic.setImageBitmap(bmp);
								}
								else
								new ImageDownLoadUtil().readBitmapViaVolley1(mQueue, pic, new ICallBack(){
									@Override
									public void onSuccess(Bitmap bmp) {
										GoodPic.setImageBitmap(bmp);
									}
									public void onFailure() {
									}
								});
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
	
	public void addCart(final Context cxt,String GoodId,String name,String num,String price)
	{
		dlg=MyDialog.createLoadingDialog(cxt,"加载中，不要急哦");
    	dlg.show();
		@SuppressWarnings("deprecation")
		String url=Constant.URL+"/cart/add?token="+Constant.Token+"&goods_id="+GoodId+"&product_id=0&pdt_desc=&product_name="
		+java.net.URLEncoder.encode(name)+"&product_num="+num+"&price="+price;
		Log.e("url",url);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						dlg.dismiss();
						Log.e("yao", "resData:"+response);
						try {
							if(response.getString("status").equals("success"))
							{
								Toast.makeText(cxt, "添加成功", Toast.LENGTH_SHORT).show();
								GoodInfoActivity.instance.finish();
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