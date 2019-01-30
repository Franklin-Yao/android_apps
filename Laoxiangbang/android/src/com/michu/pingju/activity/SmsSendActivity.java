package com.michu.pingju.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.michu.huanxin.activity.RegisterActivity;
import com.michu.pingju.utils.Constant;
import com.michu.pingju.widget.MyDialog;

public class SmsSendActivity extends Activity{
	private Dialog dlg;
	private Context cxt;
	private Button sensmsButton;
	private EditText phonEditText;
	public String phString="",phone="",action="";
	public static SmsSendActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance=this;
		cxt=this;
		//短信SDK初始化
        SMSSDK.initSDK(this, com.michu.pingju.utils.Constant.APPKEY, Constant.APPSECRET);
		LayoutInflater mLi = LayoutInflater.from(this);
		View v= mLi.inflate(R.layout.activity_sms_send, null);
		setContentView(v);
//		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
//    	Title.setText("发送验证码");
//    	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
//    	IvRight.setVisibility(View.GONE);
    	Intent intent=getIntent();
    	phone=intent.getStringExtra("phone");
    	action=intent.getStringExtra("action");
//    	
//		sensmsButton = (Button) findViewById(R.id.btn_send);
//		phonEditText = (EditText) findViewById(R.id.et_phone);
//		phonEditText.setText(phone);
//		sensmsButton.setOnClickListener(this);
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
		
		dialogShow(v);
	}
//	@Override
//	public void onClick(View v) {
//		dialogShow(v);
//			if (!TextUtils.isEmpty(phonEditText.getText().toString())) {
//				dlg=MyDialog.createLoadingDialog(this,"正在发送验证码");
//		    	dlg.show();
//				SMSSDK.getVerificationCode("86", phonEditText.getText().toString());
//				phString = phonEditText.getText().toString();
//			} else {
//				Toast.makeText(this, "电话不能为空", 1).show();
//			}
//	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			dlg.dismiss();
			if (result == SMSSDK.RESULT_COMPLETE) {
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
					Toast.makeText(getApplicationContext(), "验证成功",Toast.LENGTH_SHORT).show();
					Intent intent=null;
					if(action.equals("register"))
						intent = new Intent(SmsSendActivity.this,RegisterActivity.class);
//					else
//						intent = new Intent(SmsSendActivity.this,ResetPassActivity.class);
					intent.putExtra("phone", phString);
					startActivity(intent);
					finish();
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					Toast.makeText(getApplicationContext(), "验证码已经发送",Toast.LENGTH_SHORT).show();
//					Intent intent = new Intent(SmsSendActivity.this,SmsValidationActivity.class);
//					intent.putExtra("phone", phString);
//					startActivity(intent);
					validationDialogShow();
				}
			} else {
				Toast.makeText(getApplicationContext(), "电话号码或验证码无效",Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@SuppressLint("NewApi")
	public void dialogShow(View v){
		final MyDialog myDialog=new MyDialog(this,R.style.dialogWindowAnim);
		View Dialogview=myDialog.createEditDialog();
		sensmsButton = (Button) Dialogview.findViewById(R.id.btn_send);
		phonEditText = (EditText) Dialogview.findViewById(R.id.et_phone);
		phonEditText.setText(phone);
		sensmsButton.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				if (!TextUtils.isEmpty(phonEditText.getText().toString())) {
//					dlg.setContentView(R.layout.common_dialog_loading);
//					dlg.setContent(R.layout.common_dialog_loading,R.style.loading_dialog);
					MyDialog.isFinishWithDialog=false;
					myDialog.dismiss();
					dlg=MyDialog.createLoadingDialog(cxt,"正在发送验证码");
			    	dlg.show();
					SMSSDK.getVerificationCode("86", phonEditText.getText().toString());
					phString = phonEditText.getText().toString();
				} else {
					Toast.makeText(cxt, "电话不能为空", 1).show();
				}
			}});
    }
	
	@SuppressLint("NewApi")
	public void validationDialogShow(){
		final MyDialog myDialog=new MyDialog(this,R.style.dialogWindowAnim);
		View Dialogview=myDialog.createEditDialog();
		sensmsButton = (Button) Dialogview.findViewById(R.id.btn_send);
		phonEditText = (EditText) Dialogview.findViewById(R.id.et_phone);
		phonEditText.setHint("请输入验证码");
		sensmsButton.setText("验证我");
		sensmsButton.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				if (!TextUtils.isEmpty(phonEditText.getText().toString())) {
					dlg=MyDialog.createLoadingDialog(cxt,"正在验证");
			    	dlg.show();
					SMSSDK.submitVerificationCode("86", phString, phonEditText.getText().toString());
				} else {
					Toast.makeText(cxt, "验证码不能为空", 1).show();
				}
			}});
    }
}