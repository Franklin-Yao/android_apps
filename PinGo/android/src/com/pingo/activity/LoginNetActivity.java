package com.pingo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import com.pingo.utils.CloseActivityClass;

public class LoginNetActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CloseActivityClass.activityList.add(this);
		setContentView(R.layout.activity_login_net);
		initView();
	}

	private void initView() {
		WebView myWebView = (WebView) findViewById(R.id.webview);
		myWebView.loadUrl("http://www.baidu.com");
	}
}
