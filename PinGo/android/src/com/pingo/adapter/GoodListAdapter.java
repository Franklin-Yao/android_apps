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
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.pingo.activity.GoodInfoActivity;
import com.pingo.activity.ManageAddressActivity;
import com.pingo.activity.NewOrEditAddressActivity;
import com.pingo.activity.R;
import com.pingo.net.FragmentHomeData;
import com.pingo.utils.Cache;
import com.pingo.utils.Constant;
import com.pingo.utils.ImageDownLoadUtil;
import com.pingo.utils.Md5Util;
import com.pingo.utils.ImageDownLoadUtil.ICallBack;
import com.pingo.widget.pullrefresh.PullToRefreshLayout;

public class GoodListAdapter extends BaseAdapter {
	private Context cxt;
	private List<Map<String, Object>> data;
	private ImageView iv_good;
	private TextView tv_good_name,tv_good_weight,tv_new_price,tv_old_price;
	private String action="";
	private static int enterNum=0;

	public GoodListAdapter(Context cxt,List<Map<String, Object>> data) {
		this.cxt = cxt;
		this.data = data;
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
		View v= LayoutInflater.from(cxt).inflate(R.layout.common_item_good_one, null);
		iv_good=(ImageView)v.findViewById(R.id.iv_good);
		tv_good_name = (TextView) v.findViewById(R.id.tv_good_name);
		tv_good_weight = (TextView) v.findViewById(R.id.tv_good_weight);
		tv_new_price = (TextView) v.findViewById(R.id.tv_new_price);
		tv_old_price= (TextView) v.findViewById(R.id.tv_old_price);
		
    	tv_good_name.setText(data.get(position).get("name")+"");
    	tv_good_weight.setText(data.get(position).get("weight")+"");
    	tv_new_price.setText(data.get(position).get("price")+"");
    	tv_old_price.setText(data.get(position).get("mktprice")+"");
		try{
			
			String ImgUrl=data.get(position).get("pic")+"";
			final String picName=Md5Util.getMD5Str(ImgUrl);
			Bitmap bmp=new Cache(cxt).getBitmap(picName);
			RequestQueue mQueue = Volley.newRequestQueue(cxt);
			if(bmp!=null){
				Log.e("yao", "tup图片存在");
				iv_good.setImageBitmap(bmp);
				iv_good.setOnClickListener(new OnClickListener(){
					public void onClick(View arg0) {
						Intent intent = new Intent(cxt,GoodInfoActivity.class);
						intent.putExtra("GoodId",data.get(position).get("good_id")+"");
						intent.putExtra("GoodName",data.get(position).get("name")+"");
						intent.putExtra("Price",data.get(position).get("price")+"");
						cxt.startActivity(intent);
					}
				});
			}
			else
			new ImageDownLoadUtil().readBitmapViaVolley1(mQueue, ImgUrl, new ICallBack(){
				@Override
				public void onSuccess(Bitmap bp) {
					new Cache(cxt).saveBitmap(picName, bp);
					iv_good.setImageBitmap(bp);
//					iv_good.setTag(data.get(position).get("good_id")+"");
					iv_good.setOnClickListener(new OnClickListener(){
						public void onClick(View arg0) {
							Intent intent = new Intent(cxt,GoodInfoActivity.class);
							intent.putExtra("GoodId",data.get(position).get("good_id")+"");
							intent.putExtra("GoodName",data.get(position).get("name")+"");
							intent.putExtra("Price",data.get(position).get("price")+"");
							cxt.startActivity(intent);
						}
					});
				}
				
				@Override
				public void onFailure() {
				}
			});
		}catch(Exception e){
		}
		return v;
	}
}