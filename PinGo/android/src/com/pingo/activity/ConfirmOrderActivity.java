package com.pingo.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pingo.adapter.OrderListViewAdapter;
import com.pingo.model.CartGoodsInfo;
import com.pingo.net.FragmentCartData;
import com.pingo.utils.Constant;
import com.pingo.widget.MyDialog;
import com.pingo.widget.TimePickerPopWindow;

/**
 * 确认订单界面
 * 
 * @author http://yecaoly.taobao.com
 * 
 */
public class ConfirmOrderActivity extends Activity{

	private ImageView bt_back;
	private TextView bt_ok,tv_goodsprice,tv_orderprice;
	private ListView listView_Order;
	private OrderListViewAdapter adapter;
	private TimePickerPopWindow m_PickerPopWindow;
	private ArrayList<CartGoodsInfo> cartGoodList;
	private RequestQueue mQueue;
	private View view;
	private String priceAll="0";
	Dialog dlg;
	public static final int REQUEST_CODE_01 = 1;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mQueue = Volley.newRequestQueue(this);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		try{
			cartGoodList = (ArrayList<CartGoodsInfo>)getIntent().getSerializableExtra("goodslist");
		}catch(Exception e){
		}
		priceAll=getIntent().getStringExtra("PriceAll");
		view = LayoutInflater.from(this).inflate(R.layout.order_pay_main, null);
		setContentView(view);
		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
		Title.setText("下单");
		ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
		initView(view);
	}
	
	public void onTopLeft(View v) {
		this.finish();
	}
	
	private void initView(View view) {
		tv_goodsprice=(TextView)view.findViewById(R.id.tv_goodsprice);
		tv_goodsprice.setText("商品价格\n"+priceAll);
		tv_orderprice=(TextView)view.findViewById(R.id.tv_orderprice);
		tv_orderprice.setText("订单金额\n"+priceAll);
		listView_Order = (ListView) findViewById(R.id.listView_order);

		if (cartGoodList != null && cartGoodList.size() != 0) {
			adapter = new OrderListViewAdapter(this,cartGoodList);
			listView_Order.setAdapter(adapter);
		}

		// 给地址栏填充数据
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		Constant.sp=this.getSharedPreferences("sp", Context.MODE_PRIVATE);
    	if((""+Constant.sp.getString("addr_id","")).equals("")){
    		new FragmentCartData(this,mQueue).getAddress(view);
    	}
    	else{
    		tv_name.setText(Constant.sp.getString("addr_name",""));
    		Log.e("new_addr_name",Constant.sp.getString("addr_name",""));
    		TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
    		tv_phone.setText(Constant.sp.getString("addr_phone",""));
    		TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
    		tv_address.setText(Constant.sp.getString("addr_address",""));
    	}
    	
    	if(tv_name.getText().toString().equals("")){
    		tv_name.setText("您还没有添加收货地址，点击我添加吧");
    	}
		bt_ok = (TextView) findViewById(R.id.tv_buy_ok);
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CreateOrder();
			}
		});
		m_PickerPopWindow = new TimePickerPopWindow(this);
	}
	
	//下单
	public void CreateOrder()
	{
		Constant.sp=this.getSharedPreferences("sp", Context.MODE_PRIVATE);
		String addrId=Constant.sp.getString("addr_id", "");
		String url = Constant.URL+"/order/cart/purchase?&addr_id="+addrId+"&token="+Constant.Token;
		for(int i=0;i<cartGoodList.size();i++){
			url+="&cart_id[]="+cartGoodList.get(i).getId();
		}
		Log.e("url",url);
		dlg=MyDialog.createLoadingDialog(this,"请稍等，俺正在为你下单");
    	dlg.show();
		RequestQueue queue = Volley.newRequestQueue(this);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						dlg.dismiss();
						try {
							if (response.getString("status").equals("success")) {
								 Toast.makeText(ConfirmOrderActivity.this,"下单成功" ,Toast.LENGTH_SHORT).show();
								 ConfirmOrderActivity.this.finish();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						Log.e("error", error.toString());
					}
				});
		queue.add(jsObjRequest);
	}
	public void chooseAddress(View v)
	{
		Intent it = new Intent(this, ManageAddressActivity.class);
		it.putExtra("action", "chooseAddress");
		startActivityForResult(it,0);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_name.setText(Constant.sp.getString("addr_name",""));
		TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
		tv_phone.setText(Constant.sp.getString("addr_phone",""));
		TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
		tv_address.setText(Constant.sp.getString("addr_address",""));
	}
}
