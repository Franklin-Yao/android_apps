package com.pingo.activity;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.pingo.utils.CommonMethods;
/**
 * 
 * @author max    登录
 */
public class SettingsActivity extends Activity {
	private EditText userEdt,pswEdt;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		userEdt = (EditText)findViewById(R.id.login_user_edit);
		pswEdt = (EditText)findViewById(R.id.login_passwd_edit);
		initView();
	}
	
	private void initView() {
		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("设置");
    	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
	}
	public void onTopLeft(View v) {
		this.finish();
	 }
	
	public void aboutUs(View v) {
		Intent intent = new Intent();
        intent.setClass(SettingsActivity.this,AboutActivity.class);
        startActivity(intent);
    }
    public void onHelp(View v) {
    	Intent intent = new Intent();
        intent.setClass(SettingsActivity.this,TextDisplayActivity.class);
        intent.putExtra("from", "help");
        startActivity(intent);
    }
    
	public void onFeedBack(View v)
	{
		Intent intent = new Intent();
        intent.setClass(SettingsActivity.this,SubmitTextActivity.class);
        intent.putExtra("from", "feedback");
        startActivity(intent);
	}
}