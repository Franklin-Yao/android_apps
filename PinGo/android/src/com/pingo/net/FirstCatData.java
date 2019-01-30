package com.pingo.net;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pingo.activity.GoodInfoActivity;
import com.pingo.activity.R;
import com.pingo.utils.Constant;
import com.pingo.utils.ImageDownLoadUtil;
import com.pingo.utils.ImageDownLoadUtil.ICallBack;

public class FirstCatData {
	private RequestQueue mQueue;
	private Context cxt;
	public FirstCatData(Context cxt,RequestQueue mQueue){
		this.mQueue=mQueue;
		this.cxt=cxt;
	}

	public void RequestGetGoods(final View view, int FirstID, int SecondID) {
		int FirstID1=FirstID+1;
		String url=Constant.URL+"/goods/getChildDirGds?condition=price&order=desc" +
				"&isOnlyAvailable=0&page=1&items=6&parent_id="+FirstID1+"&cat_id="+Constant.catId[FirstID][SecondID];
		final int IdGood[]={R.id.include_item_good0,R.id.include_item_good1,R.id.include_item_good2,
				R.id.include_item_good3,R.id.include_item_good4,R.id.include_item_good5};
		final ImageView[] GoodPicIV = new ImageView[6];
		final TextView [] GoodNameTV = new TextView[6];
		final TextView [] GoodWeightTV = new TextView[6];
		final TextView [] GoodNewPriceTV = new TextView[6];
		final TextView [] GoodOldPriceTV = new TextView[6];
		for(int j=0;j<6;j++){
//			LinearLayout GoodView=(LinearLayout) v.findViewWithTag("view_second_cat"+j).findViewById(IdGood[j]);
			LinearLayout GoodView=(LinearLayout) view.findViewById(IdGood[j]);
			GoodPicIV[j]=(ImageView)view.findViewById(R.id.iv_good);
			GoodNameTV[j]= (TextView) view.findViewById(R.id.tv_good_name);
			GoodWeightTV[j]= (TextView) view.findViewById(R.id.tv_good_weight);
			GoodNewPriceTV[j]= (TextView) view.findViewById(R.id.tv_new_price);
			GoodOldPriceTV[j]= (TextView) view.findViewById(R.id.tv_old_price);
		}
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						Log.e("yao", "resData:"+response);
						try {
							if(response.getString("status").equals("success")){
								for(int i=0;i<6;i++){
									JSONObject jo=response.getJSONArray("data").getJSONObject(i);
									String ImgUrl=jo.getString("pic");
									String name=jo.getString("name");
									String weight=jo.getString("weight");
									weight+=jo.getString("unit");
									String GoodNewPrice=jo.getString("price");
									String GoodOldPrice=jo.getString("mktprice");
									final String GoodId=jo.getString("goods_id");
									GoodNameTV[i].setText(name);
									GoodWeightTV[i].setText(weight);
									GoodNewPriceTV[i].setText(GoodNewPrice);
									GoodOldPriceTV[i].setText(GoodOldPrice);
									final int flag=i;
									new ImageDownLoadUtil().readBitmapViaVolley1(mQueue, ImgUrl, new ICallBack(){
										@Override
										public void onSuccess(Bitmap bp) {
											GoodPicIV[flag].setImageBitmap(bp);
											GoodPicIV[flag].setOnClickListener(new OnClickListener(){
												public void onClick(View arg0) {
													Intent intent = new Intent(cxt,GoodInfoActivity.class);
													intent.putExtra("GoodId",GoodId);
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
}