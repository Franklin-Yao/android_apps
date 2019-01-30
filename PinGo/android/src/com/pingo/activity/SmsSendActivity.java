package com.pingo.activity;

import com.pingo.utils.Constant;
import com.pingo.widget.MyDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class SmsSendActivity extends Activity implements OnClickListener {
	Dialog dlg;
	private Button sensmsButton;
	private EditText phonEditText;
	public String phString="",phone="",action="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//短信SDK初始化
        SMSSDK.initSDK(this, Constant.APPKEY, Constant.APPSECRET);
		setContentView(R.layout.activity_sms_send);
		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("发送验证码");
    	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
    	Intent intent=getIntent();
    	phone=intent.getStringExtra("phone");
    	action=intent.getStringExtra("action");
    	
		sensmsButton = (Button) findViewById(R.id.btn_send);
		phonEditText = (EditText) findViewById(R.id.et_phone);
		phonEditText.setText(phone);
		sensmsButton.setOnClickListener(this);
		EventHandler eh = new EventHandler() {
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

	public void onTopLeft(View v) {
		this.finish();
	}
	@Override
	public void onClick(View v) {
			if (!TextUtils.isEmpty(phonEditText.getText().toString())) {
				dlg=MyDialog.createLoadingDialog(this,"正在发送验证码");
		    	dlg.show();
				SMSSDK.getVerificationCode("86", phonEditText.getText().toString());
				phString = phonEditText.getText().toString();
			} else {
				Toast.makeText(this, "电话不能为空", 1).show();
			}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("event", "event=" + event);
			dlg.dismiss();
			if (result == SMSSDK.RESULT_COMPLETE) {
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
					Toast.makeText(getApplicationContext(), "验证成功",Toast.LENGTH_SHORT).show();
					SmsValidationActivity.instance.finish();
					Intent intent=null;
					if(action.equals("register"))
						intent = new Intent(SmsSendActivity.this,RegisterActivity.class);
					else
						intent = new Intent(SmsSendActivity.this,ResetPassActivity.class);
					SmsValidationActivity.instance.finish();
					intent.putExtra("phone", phString);
					startActivity(intent);
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					Toast.makeText(getApplicationContext(), "验证码已经发送",Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(SmsSendActivity.this,SmsValidationActivity.class);
					intent.putExtra("phone", phString);
					startActivity(intent);
					SmsSendActivity.this.finish();
				}
			} else {
				Toast.makeText(getApplicationContext(), "电话号码或验证码无效",Toast.LENGTH_SHORT).show();
			}
		}
	};
}