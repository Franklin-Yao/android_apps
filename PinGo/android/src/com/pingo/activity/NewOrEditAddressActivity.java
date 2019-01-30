package com.pingo.activity;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pingo.utils.CommonMethods;
import com.pingo.utils.Constant;

public class NewOrEditAddressActivity extends Activity{
	private Context cxt;
	private EditText editTextReceiver;
	private EditText editTextPhone;
	private TextView editTextAddress;
	private EditText editTextDetail;
	private String Province="",School="",Building="";
	private long id;
	private int addressType;
	public static final int TYPE_ADD_ADDRESS = 1;
	public static final int TYPE_EDIT_ADDRESS = 2;
	public static final int REQUEST_CODE_01 = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cxt=NewOrEditAddressActivity.this;
		setContentView(R.layout.activity_new_edit_address);
		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	ImageView IvLeft=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_back);
    	IvLeft.setBackgroundResource(R.drawable.common_cancel);
    	new CommonMethods().setImageViewParams(IvLeft, 50, 50);
    	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setBackgroundResource(R.drawable.common_ok);
    	new CommonMethods().setImageViewParams(IvRight, 50, 50);
    	
		editTextReceiver = (EditText) findViewById(R.id.profile_address_receiver_editText);
		editTextPhone = (EditText) findViewById(R.id.profile_address_phone_editText);
		editTextAddress = (TextView) findViewById(R.id.profile_address_school_editText);
		editTextDetail = (EditText) findViewById(R.id.profile_address_detail_editText);
		
		Intent intent=getIntent();
		if (intent.getIntExtra("ClickType", 1) == TYPE_ADD_ADDRESS) {
			Title.setText("添加新地址");
		} else {
			Title.setText("修改地址");
			editTextReceiver.setText(intent.getStringExtra("name"));
			editTextPhone.setText(intent.getStringExtra("phone"));
			Province=intent.getStringExtra("province");
			School=intent.getStringExtra("school");
			Building=intent.getStringExtra("building");
			editTextAddress.setText(Province+School+Building);
			editTextDetail.setText(intent.getStringExtra("detail"));
		}
	}

	public void onTopLeft(View v) {
		backNotify();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			backNotify();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void backNotify() {
		
		LayoutInflater inflater = LayoutInflater.from(cxt);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_style1, null);
		final Dialog dialog = new AlertDialog.Builder(cxt).create();
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(layout);
		TextView dialog_msg = (TextView) layout.findViewById(R.id.dialog_msg);
		dialog_msg.setText("是否放弃修改？");
		Button btnOK = (Button) layout.findViewById(R.id.dialog_ok);
		btnOK.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				NewOrEditAddressActivity.this.finish();
			}
		});
		Button btnCancel = (Button) layout.findViewById(R.id.dialog_cancel);
		btnCancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});
	}

	public void onTopRight(View v) {
		String Phone=editTextPhone.getText().toString().trim();
		if ("".equals(editTextReceiver.getText().toString().trim())
				|| "".equals(Phone)
				|| "".equals(editTextAddress.getText().toString().trim())
				|| "".equals(editTextAddress.getText().toString().trim())
				|| "".equals(editTextDetail.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "您没有填写完整哦", Toast.LENGTH_SHORT).show();
		}
		else if(!Phone.matches("[0-9]{11}")){
    		Toast.makeText(getApplicationContext(), "手机号格式不对哦，请输入11位数字", Toast.LENGTH_SHORT).show();
    	}
		else {
			SubmitAddress();
		}
	}
	public void getProvinceSchoolZone(View v) {
		Intent intent = new Intent();
		intent.setClass(cxt,ProvinceSchoolZoneSelectActivity.class);
		startActivityForResult(intent, REQUEST_CODE_01);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		switch (requestCode) {
		case REQUEST_CODE_01:
		if(resultCode==Activity.RESULT_OK){
			Province=data.getStringExtra("province");
			School=data.getStringExtra("school");
			Building=data.getStringExtra("building");
			if(Building.length()>=1){
				editTextAddress.setText(Province+School+Building);
			}
		}
		break;
		}
	}
	public void SubmitAddress() {
//		http://lbang.sinaapp.com/member/addr/save?token=eyJtZW1iZXJfaWQiOiIxMyIsImxvZ2luX3RpbWUiOjE0MTk2NzIxMDZ9&name=max&province=hubei&city=wuhan&area=a&addr=a&tel=a&zip=a&phone=18202799880
		RequestQueue queue = Volley.newRequestQueue(this);
		String url="";
		if (getIntent().getIntExtra("ClickType", 1) == TYPE_ADD_ADDRESS) {
			url = Constant.URL+"/member/addr/save?token="+Constant.Token+"&name="+
					URLEncoder.encode(editTextReceiver.getText().toString())+
			"&phone="+editTextPhone.getText().toString()+
			"&province="+URLEncoder.encode(Province)+"&city="+URLEncoder.encode(School)+"&area="+URLEncoder.encode(Building)+
			"&addr="+URLEncoder.encode(editTextDetail.getText().toString())+"&tel=a&zip=a";
		}
		else{
			String addr_id=getIntent().getStringExtra("addr_id");
			url = Constant.URL+"/member/addr/update?token="+Constant.Token+"&addr_id="+addr_id+"&name="+
					URLEncoder.encode(editTextReceiver.getText().toString())+
			"&phone="+editTextPhone.getText().toString()+
			"&province="+URLEncoder.encode(Province)+"&city="+URLEncoder.encode(School)+"&area="+URLEncoder.encode(Building)+
			"&addr="+URLEncoder.encode(editTextDetail.getText().toString())+"&tel=a&zip=a";
		}
		Log.e("yao", "url:  "+url);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, url, null,
				new Response.Listener<JSONObject>() {
					@SuppressLint("NewApi")
					public void onResponse(JSONObject response) {
						Log.e("yao", "resData:"+response);
						try {
							if(response.getString("status").equals("success")){
								NewOrEditAddressActivity.this.finish();
								ManageAddressActivity.instance.recreate();
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
//		jsObjRequest.setCharacterEncoding("UTF-8");
		
	        queue.add(jsObjRequest);
	}
}
