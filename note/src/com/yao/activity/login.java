package com.yao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.yao.R;

public class login extends Activity 
{
    private Button btnlogin, btnclose;// ��������Button����
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		init();
	}
	private void init()
	{
		 btnclose= (Button) findViewById(R.id.btn_exit);// ��ȡȡ����ť
		 btnlogin=(Button)findViewById(R.id.btn_login);
		 btnlogin.setOnClickListener(mOnClickListener);
		 btnclose.setOnClickListener(mOnClickListener);
	}
	private OnClickListener mOnClickListener=new OnClickListener(){
		public void onClick(View v)
		{			
			switch(v.getId())
			{
				case R.id.btn_login:
					Intent intent = new Intent(login.this, MainActivity.class);// ����Intent����
					startActivity(intent);// ������Activity	               
				case R.id.btn_exit:
					finish();// �˳���ǰ����
				default:
						break;
			}
		}
	};
}
