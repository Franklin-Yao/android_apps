package com.pingo.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author max    登录
 */
public class TextDisplayActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_display);
		Intent intent=getIntent();
	    String from=intent.getStringExtra("from");
		initView(from);
	}
	
	private void initView(String from) 
	{
		TextView Title=(TextView)findViewById(R.id.common_top).findViewById(R.id.tv_title);
		WebView Content=(WebView)findViewById(R.id.wv_content);
		if(from.equals("introduction")){
			Title.setText("软件介绍");
			Content.loadUrl("file:///android_asset/introduction.html");
		}
		else if(from.equals("agreement")){
			Title.setText("用户协议");
			Content.loadUrl("file:///android_asset/agreement.html");
		}
		else if(from.equals("help")){
			Title.setText("帮助");
			Content.loadUrl("file:///android_asset/help.html");
		}
    	ImageView IvRight=(ImageView)findViewById(R.id.common_top).findViewById(R.id.iv_cat);
    	IvRight.setVisibility(View.GONE);
	}
	public void onTopLeft(View v) {
		this.finish();
	 }
}