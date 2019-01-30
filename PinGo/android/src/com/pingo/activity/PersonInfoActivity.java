package com.pingo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pingo.utils.CloseActivityClass;
import com.pingo.utils.Constant;

public class PersonInfoActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CloseActivityClass.activityList.add(this);
		setContentView(R.layout.activity_person_info);
		ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
    	TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("个人信息");
    	
    	Constant.sp=this.getSharedPreferences("sp", Context.MODE_PRIVATE);
    	TextView profile_info_username =(TextView)findViewById(R.id.profile_info_username);
    	profile_info_username.setText(Constant.sp.getString("phone",""));
    	TextView profile_info_nickname =(TextView)findViewById(R.id.profile_info_nickname);
    	profile_info_nickname.setText(Constant.sp.getString("uname",""));
	}
	public void logout(View v) {
		CloseActivityClass.exitClient(PersonInfoActivity.this);
    	Constant.sp=getSharedPreferences("sp", Context.MODE_PRIVATE);
    	Editor editor = Constant.sp.edit();
    	editor.putString("isLogin", "no");
    	editor.commit();
		Intent intent=new Intent (PersonInfoActivity.this,LoginActivity.class);
		intent.putExtra("action", "logout");
		startActivity(intent);
		this.finish();
	 }
//	public void nicknameEdit(View v) {
//		Intent intent=new Intent (PersonInfoActivity.this,SubmitTextActivity.class);
//		intent.putExtra("from", "nickname");
//		startActivity(intent);
//	 }
	public void onTopLeft(View v) {
		this.finish();
	}
}