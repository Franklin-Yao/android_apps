package com.pingo.activity;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pingo.utils.Constant;
import com.pingo.widget.MyDialog;
/**
 * 
 * @author max    登录
 */
public class LoginActivity extends Activity {
	private EditText userEdt;
	private EditText pswEdt;
	private String pass="";
	private String from="";
	Dialog dlg;
	private String phone="";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_login);
		Constant.sp=getSharedPreferences("sp", Context.MODE_PRIVATE);
		userEdt = (EditText)findViewById(R.id.et_user);
		pswEdt = (EditText)findViewById(R.id.et_pass);
		userEdt.setText(Constant.sp.getString("phone",""));
		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("登录");
    	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
    	ImageView IvLeft=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_back);
    	IvLeft.setVisibility(View.GONE);
    	
    	Intent intent=getIntent();
    	from=intent.getStringExtra("action");
    	Log.e("yao", "from:  "+from);
	}
	public void register(View v) {
		  	Intent intent = new Intent();
		  	intent.putExtra("phone", phone);
		  	intent.putExtra("action", "register");
			intent.setClass(LoginActivity.this,SmsSendActivity.class);//真实环境使用
//		  	intent.setClass(LoginActivity.this,RegisterActivity.class);
			startActivity(intent);
	  }
	public void onResetPass(View v) {
		Intent intent = new Intent();
		intent.putExtra("phone", phone);
	  	intent.putExtra("action", "resetPass");
		intent.setClass(LoginActivity.this,SmsSendActivity.class);//真实环境使用
		startActivity(intent);
  }
	public void login(View v) {
		String phone=userEdt.getText().toString();
		String pass=pswEdt.getText().toString();
    	if("".equals(phone) || "".equals(pass))   //判断 帐号和密码是否为空
        {
    		Toast.makeText(getApplicationContext(), "帐号或者密码不能为空，\n请输入后再登录！", Toast.LENGTH_SHORT).show();
    		return;
         }
    	else if(!phone.matches("[0-9]{11}")){
    		Toast.makeText(getApplicationContext(), "手机号格式不对哦，请输入11位数字", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	else if(!pass.matches("[a-zA-Z0-9]{6,12}")){
    		Toast.makeText(getApplicationContext(), "密码格式不对哦，请输入6-12位数字或字母", Toast.LENGTH_SHORT).show();
    		return;
    	}
        else{
        	 /*此处实现了联网操作，测试方便注释掉了 */
        	postLogin(LoginActivity.this,phone,pass);
        }
	}
	
	@SuppressLint("NewApi")
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (from.equals("logout")){
			LoginActivity.this.finish();
			MainActivity.instance.finish();
			startActivity(new Intent(this,MainActivity.class));
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_BACK ) {
			Intent intent=new Intent();
    		setResult(Activity.RESULT_OK, intent);
        	LoginActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	 /*登录提交数据到服务器验证，网址和参数参考梁誉给的文档 ，此处使用volley的post方法，务必导入volley包。
	  如果自己写网络通信程序，只需要把URL和参数改一下就可以了*/
	public void postLogin(Context context,final String phone,final String pass){
		dlg=MyDialog.createLoadingDialog(this,"登录中");
    	dlg.show();
		String url = Constant.URL+"/member/attempt";
	    RequestQueue queue = Volley.newRequestQueue(context);
	    Map<String,String> map=new HashMap<String,String>();
        map.put("login_name",phone);
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
							JSONObject jo=response.getJSONArray("member").getJSONObject(0);
				        	 /*这里和guideActivity文件里面读取登录信息对应，此处登录成功就记录“用户已经登录”的状态，下次可以免登录 */
				        	Constant.sp=getSharedPreferences("sp", Context.MODE_PRIVATE);
				        	Editor editor = Constant.sp.edit();
				        	editor.putString("isLogin", "yes");
				        	editor.putString("phone", phone);
				        	editor.putString("uname", jo.getString("uname"));
				        	Log.e("yao", "token:  "+jo.getString("token"));
				        	editor.putString("token",jo.getString("token"));
				        	Constant.Token=jo.getString("token");
				        	editor.commit();
				        	Intent intent=new Intent();
				    		setResult(Activity.RESULT_OK, intent);
			            	LoginActivity.this.finish();
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