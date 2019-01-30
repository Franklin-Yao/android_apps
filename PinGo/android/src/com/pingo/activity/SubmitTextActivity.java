package com.pingo.activity;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
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
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pingo.adapter.GoodListAdapter;
import com.pingo.utils.Constant;
/**
 * 
 * @author max    登录
 */
public class SubmitTextActivity extends Activity {
	private Context cxt;
	private RequestQueue mQueue;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cxt=this;
		mQueue = Volley.newRequestQueue(this);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_submit_text);
    	Intent intent=getIntent();
	    String from=intent.getStringExtra("from");
	    initView(from);
	}
	
	private void initView(String from)
	{
		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
		EditText Content=(EditText)findViewById(R.id.et_content);
		Button BtnSubmit=(Button)findViewById(R.id.btn_submit);
		if(from.equals("nickname")){
			Title.setText("修改用户名");
			Content.setHint("起个给力的名字吧（不超过10个字）");
			BtnSubmit.setText("保存");
		}
		else if(from.equals("feedback")){
			Title.setText("用户反馈");
			Content.setHint("给个赞吧");
			BtnSubmit.setText("提交");
		}
    	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
	}
	
	public void onTopLeft(View v) {
		this.finish();
	}
	
	public void submit(View v) {
		TextView Content=(TextView)findViewById(R.id.et_content);
		if(Content.getText().toString().length()>=1){
				@SuppressWarnings("deprecation")
				String url=Constant.URL+"/feedback/up?token="+Constant.Token+"&content="
		+URLEncoder.encode(Content.getText().toString())+"&device_info=1&dateline=";
				Log.e("url", "url:  "+url);
				JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
						new Response.Listener<JSONObject>() {
							public void onResponse(JSONObject response) {
								try {
									if(response.getString("status").equals("success"))
									{
										Toast.makeText(cxt, "提交成功，谢谢您的建议", Toast.LENGTH_SHORT).show();
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
								Log.e("error", error.getMessage());
								Toast.makeText(cxt, "找不到服务器君啦，请检查网络", Toast.LENGTH_SHORT).show();
							}
						});
			        mQueue.add(jsObjRequest);
		}
		else
			Toast.makeText(this, "您还没有输入内容呢", Toast.LENGTH_SHORT).show();
	}
}