package com.pingo.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.pingo.utils.Constant;
import com.pingo.widget.MyDialog;

public class RegisterActivity extends Activity {
	private String gender="male";
	private Button sexMale,sexFemale;
	private EditText userEdt;
	private EditText pswEdt;
	private String phone="";
	private Dialog dlg;
	private boolean passCheck=false;//false不可见
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Constant.sp=getSharedPreferences("sp", Context.MODE_PRIVATE);
        sexMale=(Button)findViewById(R.id.btn_register_sex_male);
        sexFemale=(Button)findViewById(R.id.btn_register_sex_female);
        userEdt = (EditText)findViewById(R.id.et_user);
		pswEdt = (EditText)findViewById(R.id.et_pass);
		
		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("注册");
    	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
    	Intent intent=getIntent();
		phone=intent.getStringExtra("phone");
	}
	public void onTopLeft(View v) {
		this.finish();
	}
	public void onPassSee(View v) {
		if(passCheck){
			pswEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			passCheck=false;
		}
		else{
			pswEdt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			passCheck=true;
		}
	}
	
    public void registerSexMale(View v) {
    	gender="male";
    	sexMale.setBackgroundResource(R.drawable.bg_common_round_blue_15);
    	sexFemale.setBackgroundResource(R.drawable.bg_common_round_white_nopadding);
      }
    public void registerSexFemale(View v) {
    	gender="female";
    	sexMale.setBackgroundResource(R.drawable.bg_common_round_white_nopadding);
    	sexFemale.setBackgroundResource(R.drawable.bg_common_round_blue_15);
      }
    public void onLogin(View v) {
		this.finish();
	}
    public void onRegister(View v) {
    	String user=userEdt.getText().toString();
		String pass=pswEdt.getText().toString();
    	if("".equals(user) || "".equals(pass))   //判断 帐号和密码是否为空
        {
    		Toast.makeText(getApplicationContext(), "昵称或者密码不能为空", Toast.LENGTH_SHORT).show();
         }
    	else if(user.length()<4||user.length()>15){
    		Toast.makeText(getApplicationContext(), "请输入4-15位昵称", Toast.LENGTH_SHORT).show();
    	}
    	else if(!pass.matches("[a-zA-Z0-9]{6,12}")){
    		Toast.makeText(getApplicationContext(), "密码格式不对哦，请输入6-12位数字字母", Toast.LENGTH_SHORT).show();
    	}
    	else{
    		dlg=MyDialog.createLoadingDialog(this,"注册中");
        	dlg.show();
        	postRegister(RegisterActivity.this,user,pass);
        }
      }
    public void postRegister(Context context,final String uname,final String password){
		String url = Constant.URL+"/member/reg/phone";
	    RequestQueue queue = Volley.newRequestQueue(context);
	    Map<String,String> map=new HashMap<String,String>();
        map.put("uname",uname);
        Intent intent=RegisterActivity.this.getIntent();
        map.put("phone",phone);
        map.put("password",password);
        map.put("gender",gender);
        JSONObject params=new JSONObject(map);
       JsonObjectRequest jsObjRequest = new JsonObjectRequest(
			Request.Method.POST, url, params,
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
					dlg.dismiss();
					Log.e("yao", "resData:"+response);
					try {
						Log.e("yao", "status:   "+response.getString("status"));
						if(response.getString("status").equals("success")){
							Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
							Editor editor = Constant.sp.edit();
				        	editor.putString("phone", phone);
				        	editor.putString("gender", gender);
				        	editor.putString("uname", uname);
				        	editor.commit();
							Intent intent = new Intent();
			            	intent.setClass(RegisterActivity.this,MainActivity.class);
			            	startActivity(intent);
			            	finish();
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
				}
			});
        queue.add(jsObjRequest);
	}
}
    
