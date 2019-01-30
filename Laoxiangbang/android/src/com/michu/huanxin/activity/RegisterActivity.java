/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.michu.huanxin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import static cn.smssdk.framework.utils.R.getStringRes;

import com.michu.pingju.activity.R;

/**
 * 注册页
 * 
 */
public class RegisterActivity extends BaseActivity {
	private EditText userNameEditText;
	private EditText passwordEditText;
	private ImageView pswEyeImg,codeImg;
	private CheckBox agreeinfoCkb;
	
	private LinearLayout codeLin;
	private ProgressBar codePb;
	private EditText codeEt;
	
	private boolean phoneChecked = false;
	
	private String usernameChecked = "";

	private final String APP_KEY = "703b87f91606";
	private final String APP_SECRET = "941efba712ad3c9195e4331d4ad3994a";

	private String[] phoneBegin = {"134","135","136","137","138","139","147","150"
			,"151","152","157","158","159","182","187","188","130","131","132","155"
			,"156","185","186133","153","180","189"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		userNameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		pswEyeImg = (ImageView) findViewById(R.id.activity_register_img_psweye);
		codeImg = (ImageView) findViewById(R.id.activity_register_img_code);
		agreeinfoCkb = (CheckBox) findViewById(R.id.activity_register_ckb_agreeinfo);
		
		codeLin = (LinearLayout) findViewById(R.id.activity_register_ll_code);
		codePb = (ProgressBar) findViewById(R.id.activity_register_pb_code);
		codeEt = (EditText) findViewById(R.id.activity_register_et_code);
		
		pswEyeImg.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					System.out.println("action down");
					passwordEditText.setInputType(InputType.TYPE_NULL);
				}else{
					System.out.println("action other");
					passwordEditText.setInputType(0x81);
				}

				return true;
			}
		});
//
//		phoneChecked = true;
//		codeImg.setImageResource(R.drawable.actionbar_camera_icon);
		
		userNameEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(phoneChecked){
					phoneChecked = false;
					codeImg.setImageResource(R.drawable.ic_launcher);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		codeEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s != null && s.length() == 4){
					final String username = userNameEditText.getText().toString().trim();
					String codeStr = String.valueOf(s);
					System.out.println(s);
					SMSSDK.submitVerificationCode("86", username, codeStr);
					codePb.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		SMSSDK.initSDK(this, APP_KEY, APP_SECRET);
		EventHandler eh=new EventHandler(){

			@Override
			public void afterEvent(int event, int result, Object data) {

				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}

		};
		SMSSDK.registerEventHandler(eh);
	}

	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
		final String username = userNameEditText.getText().toString().trim();
		final String pwd = passwordEditText.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
			userNameEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			passwordEditText.requestFocus();
			return;
		}else if(!agreeinfoCkb.isChecked()){
			Toast.makeText(this, "请阅读并同意用户须知", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!phoneChecked){
			Toast.makeText(this, "手机号码尚未验证", Toast.LENGTH_SHORT).show();
			userNameEditText.requestFocus();
			return;
		}

		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
			codePb.setVisibility(View.GONE);
			codeLin.setVisibility(View.GONE);
			Intent intent = new Intent();
			intent.setClass(RegisterActivity.this, PerfectInfomationActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("username", username);
			bundle.putString("password", pwd);
			intent.putExtras(bundle);
			startActivity(intent);			
		}
	}

	public void check(View view){
//		if(phoneChecked){
//			return;
//		}
		final String username = userNameEditText.getText().toString().trim();
		if(checkPhone(username)){
			SMSSDK.getVerificationCode("86",username);
			codeLin.setVisibility(View.VISIBLE);
		}else{
			Toast.makeText(this, "请输入正确手机号码", Toast.LENGTH_SHORT).show();
			userNameEditText.requestFocus();
			return;
		}
	}

	private boolean checkPhone(String username) {
		// TODO Auto-generated method stub
		boolean resultBool = false;
		if(username != null && username.length() == 11){
			int phoneBeginNumInt = phoneBegin.length;
			for(int i = 0;i < phoneBeginNumInt && resultBool == false;i++){
				if(username.startsWith(phoneBegin[i])){
					resultBool = true;
				}			
			}
		}
		return resultBool;
	}

	public void back(View view) {
		finish();
	}

	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("event", "event="+event);
			if (result == SMSSDK.RESULT_COMPLETE) {
				//短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
					Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
					codePb.setVisibility(View.GONE);
					codeLin.setVisibility(View.GONE);
					phoneChecked = true;
					codeEt.setText("");
					codeImg.setImageResource(R.drawable.actionbar_camera_icon);

				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
					Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();

				}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
					Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();

				}
			} else {
				((Throwable) data).printStackTrace();
				int resId = getStringRes(RegisterActivity.this, "smssdk_network_error");
				Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
				codePb.setVisibility(View.GONE);
				if (resId > 0) {
					Toast.makeText(RegisterActivity.this, resId, Toast.LENGTH_SHORT).show();
				}
			}

		}

	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}


}
