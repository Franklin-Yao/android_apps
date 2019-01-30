package com.pingo.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pingo.utils.CloseActivityClass;
import com.pingo.utils.Constant;
import com.pingo.widget.MyDialog;

public class ResetPassActivity extends Activity {
	private String phone="";
	private EditText EtPass;
	Dialog dlg;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CloseActivityClass.activityList.add(this);
		Intent intent=getIntent();
    	phone=intent.getStringExtra("phone");
    	
		setContentView(R.layout.activity_reset_password);
		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("重置密码");
    	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
    	EtPass = (EditText)findViewById(R.id.et_pass);
	}
	public void onTopLeft(View v) {
		this.finish();
	}
	public void onResetPass(View v) {
		String pass=EtPass.getText().toString();
		if(!pass.matches("[a-zA-Z0-9]{6,12}")){
    		Toast.makeText(getApplicationContext(), "密码格式不对哦，请输入6-12位数字或字母", Toast.LENGTH_SHORT).show();
    		return;
    	}
		dlg=MyDialog.createLoadingDialog(this,"正在加载");
    	dlg.show();
		String url = Constant.URL+"/member/resetPass";
	    RequestQueue queue = Volley.newRequestQueue(this);
	    Map<String,String> map=new HashMap<String,String>();
        map.put("mobile",phone);
        map.put("password",pass);
        JSONObject params=new JSONObject(map);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
			Request.Method.POST, url, params,
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
					dlg.dismiss();
					Log.e("yao", "resData:"+response);
					try {
						if(response.getString("status").equals("success")){
							Toast.makeText(getApplicationContext(), "密码修改成功，请牢记",Toast.LENGTH_SHORT).show();
							ResetPassActivity.this.finish();
						}
						else{
							Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}, 
			new Response.ErrorListener() {
				public void onErrorResponse(VolleyError error) {
					dlg.dismiss();
				}
			});
        queue.add(jsObjRequest);
	 }
}