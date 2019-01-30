package com.pingo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
    	Title.setText("关于我们");
    	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
	}
	public void onIntroduction(View v) {
		Intent intent = new Intent();
        intent.setClass(AboutActivity.this,TextDisplayActivity.class);
        intent.putExtra("from", "introduction");
        startActivity(intent);
	 }
	public void onAgreement(View v) {
		Intent intent = new Intent();
        intent.setClass(AboutActivity.this,TextDisplayActivity.class);
        intent.putExtra("from", "agreement");
        startActivity(intent);
	 }
	public void onTopLeft(View v) {
		this.finish();
	}
}
