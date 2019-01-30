package com.michu.pingju.activity;

import com.michu.pingju.widget.MyDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.SMSSDK;

public class SmsValidationActivity extends Activity implements OnClickListener {
	public static Dialog dlg;
	private Button  verificationButton;
	private EditText verEditText;
	public String phString;
	public static Activity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance=this;
		setContentView(R.layout.activity_sms_validation);
		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("验证");
    	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
		Intent intent=getIntent();
		phString=intent.getStringExtra("phone");
		verificationButton = (Button) findViewById(R.id.btn_send);
		verEditText = (EditText) findViewById(R.id.et_ver);
		verificationButton.setOnClickListener(this);
	}

	public void onTopLeft(View v) {
		this.finish();
	}
	@Override
	public void onClick(View v) {
			if (!TextUtils.isEmpty(verEditText.getText().toString())) {
				dlg=MyDialog.createLoadingDialog(this,"正在验证");
		    	dlg.show();
				SMSSDK.submitVerificationCode("86", phString, verEditText
						.getText().toString());
			} else {
				Toast.makeText(this, "验证码不能为空", 1).show();
			}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		dlg.dismiss();
		SMSSDK.unregisterAllEventHandler();
	}
}