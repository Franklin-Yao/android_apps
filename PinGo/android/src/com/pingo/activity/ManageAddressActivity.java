package com.pingo.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.pingo.net.ManageAddressData;
public class ManageAddressActivity extends Activity {
	private ListView AdressList;
	private RequestQueue mQueue;
	public static ManageAddressActivity instance;
	private String action="";
	// 购物车的listview
	private SwipeListView swipeListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance=this;
		mQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_manage_address);
        TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("地址管理");
    	ImageView IvLeft=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvLeft.setVisibility(View.GONE);
    	Intent intent=getIntent();
    	action=intent.getStringExtra("action");
    	Log.e("yao", "ManageAddress action:  "+action);
    	initView();
	}
	private void initView() {
		swipeListView = (SwipeListView)findViewById(R.id.listAddress);
		swipeListView.setOffsetLeft(this.getResources().getDisplayMetrics().widthPixels * 2 / 3);
		final ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		new ManageAddressData(this,mQueue).getAddress(swipeListView,data,action);
//		swipeListView.setOnItemClickListener(new OnItemClickListener(){
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//					long arg3) {
//				Intent intent=new Intent();
//				if(action.equals("chooseAddress")){
//					intent.setClass(ManageAddressActivity.this, ConfirmOrderActivity.class);
//					new FragmentHomeData(ManageAddressActivity.this,mQueue).setDefaultAdress(data.get(position).get("addr_id")+"");
//					intent.putExtra("addr_id", data.get(position).get("addr_id")+"");
//					Constant.sp=ManageAddressActivity.this.getSharedPreferences("sp", Context.MODE_PRIVATE);
//		        	Editor editor = Constant.sp.edit(); 
//		        	editor.putString("addr_id", data.get(position).get("addr_id")+"");
//		        	editor.putString("addr_name", data.get(position).get("name")+"");
//		        	editor.putString("addr_phone",data.get(position).get("phone")+"");
//		        	String address=""+data.get(position).get("province")+data.get(position).get("school")+
//		        			data.get(position).get("building")+data.get(position).get("detail");
//		        	editor.putString("addr_address",address);
//		        	editor.commit();
//		        	Log.e("defaddr:", Constant.sp.getString("addr_name", ""));
//		        	setResult(Activity.RESULT_OK, intent);
//		        	ManageAddressActivity.this.finish();
//				}
//				else{
//					intent.setClass(ManageAddressActivity.this,NewOrEditAddressActivity.class);
//					intent.putExtra("ClickType", NewOrEditAddressActivity.TYPE_EDIT_ADDRESS);
//					intent.putExtra("name", data.get(position).get("name")+"");
//					intent.putExtra("phone", data.get(position).get("phone")+"");
//					intent.putExtra("province", data.get(position).get("province")+"");
//					intent.putExtra("school", data.get(position).get("school")+"");
//					intent.putExtra("building", data.get(position).get("building")+"");
//					intent.putExtra("detail", data.get(position).get("detail")+"");
//					startActivity(intent);
//				}
//			}
//        });
	}
	public void onTopLeft(View v) {
    	finish();
	}
	public void addAddress(View v) {
		Intent intent=new Intent();
		intent.setClass(this,NewOrEditAddressActivity.class);
		intent.putExtra("ClickType", NewOrEditAddressActivity.TYPE_ADD_ADDRESS);
		startActivity(intent);
	}
}