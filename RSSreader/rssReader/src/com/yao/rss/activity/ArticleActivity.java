package com.yao.rss.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yao.rss.database.DbHelper;
import com.yao.rssReader.R;

/**
 * ArticleActivity--显示文章界面
 * 
 * @author 姚富品
 * @date 2013-7-19
 * @version 3.0
 */
public class ArticleActivity extends Activity {
	WebView mWebView;
	private ImageButton mImageBtnRefresh;// 刷新按钮
	private ImageButton mImageBtnLightOn;// 提高亮度按钮
	private ImageButton mImageBtnLightOff;// 降低亮度按钮
	private ImageButton mImageBtnCollection;// 收藏按钮
	private ImageButton mImageBtnClear;// 清除缓存按钮
	private ImageButton mImageBtnClose;// 退出按钮
	private ImageButton mImageBtnShare;// 分享按钮

	private int mBrightness = 1;// 调整亮度
	private Intent intent;// 从RssArticleActivity得到的intent

	@SuppressLint("SetJavaScriptEnabled")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);

		mImageBtnClear = (ImageButton) findViewById(R.id.ibtn_clear);
		mImageBtnShare = (ImageButton) findViewById(R.id.ibtn_share);
		mImageBtnRefresh = (ImageButton) findViewById(R.id.ibtn_refresh);
		mImageBtnLightOn = (ImageButton) findViewById(R.id.ibtn_light_on);
		mImageBtnClose = (ImageButton) findViewById(R.id.ibtn_close);
		mImageBtnLightOff = (ImageButton) findViewById(R.id.ibtn_light_off);
		mImageBtnCollection = (ImageButton) findViewById(R.id.ibtn_collection);

		mWebView = (WebView) findViewById(R.id.wv_webview);
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setDefaultTextEncodingName("UTF-8");
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		mWebView.requestFocusFromTouch();
		mWebView.setWebViewClient(new mWebViewClient());

		intent = getIntent();
		final String url = intent.getStringExtra("article_url");
		String title = intent.getStringExtra("article_title");

		if (title != null) {
			setTitle(title);
		}
		
		//判断网络是否可用
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		
		if (info != null && info.isAvailable()) {
			settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		} else {
			settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}
		
		if (url != null) {
			mWebView.loadUrl(url);

		} else {
			mWebView.loadUrl("http://www.biadu.com");
		}
		
		Toast.makeText(ArticleActivity.this, "加载中，请稍后", Toast.LENGTH_SHORT)
				.show();
		
		View.OnClickListener mOnClickListener = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				
				//如果点击了提高亮度按钮
				case R.id.ibtn_light_on:
					setBrightness(30);
					break;

				//如果点击了清除缓存按钮
				case R.id.ibtn_clear:
					clearCacheFolder(ArticleActivity.this.getCacheDir(),
							System.currentTimeMillis());
					Toast.makeText(ArticleActivity.this, // 提示保存成功
							"清除缓存成功", Toast.LENGTH_SHORT).show();
					break;

				//如果点击了提高亮度按钮
				case R.id.ibtn_light_off:
					setBrightness(5);
					break;
					
				//如果点击了收藏按钮
				case R.id.ibtn_collection:
					if (saveArticle()) {
						Toast.makeText(ArticleActivity.this, // 提示保存成功
								"收藏成功", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(ArticleActivity.this, // 提示保存失败
								"收藏失败", Toast.LENGTH_SHORT).show();
					}
					break;

				//如果点击了刷新按钮
				case R.id.ibtn_refresh:
					mWebView.loadUrl(url);//重新载入网页
					Toast.makeText(ArticleActivity.this, // 提示保存失败
							"正在刷新", Toast.LENGTH_SHORT).show();
					break;
					
				//如果点击了退出按钮
				case R.id.ibtn_close:
					Intent intentClose = new Intent(Intent.ACTION_MAIN);
					intentClose.addCategory(Intent.CATEGORY_HOME);
					intentClose.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					ArticleActivity.this.startActivity(intentClose);
					System.exit(0);
					break;

				//如果点击了提高亮度按钮
				case R.id.ibtn_share:
					Intent it4 = new Intent(Intent.ACTION_SEND);
					it4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					it4.putExtra(Intent.EXTRA_TEXT, "分享微博");
					it4.setType("image/*");
					it4.putExtra(Intent.EXTRA_SUBJECT, "标题");
					startActivity(Intent.createChooser(it4, "分享"));
					break;

				default:
					break;
				}
			}
		};

		mImageBtnRefresh.setOnClickListener(mOnClickListener);
		mImageBtnShare.setOnClickListener(mOnClickListener);
		mImageBtnCollection.setOnClickListener(mOnClickListener);
		mImageBtnClear.setOnClickListener(mOnClickListener);
		mImageBtnLightOn.setOnClickListener(mOnClickListener);
		mImageBtnLightOff.setOnClickListener(mOnClickListener);
		mImageBtnClose.setOnClickListener(mOnClickListener);
	}

	/**
	 * setBrightness--设置亮度
	 * @param int level
	 * @return void
	 */
	public void setBrightness(int level) {
		ContentResolver cr = getContentResolver();
		Settings.System.putInt(cr, "screen_brightness", level);
		Window window = getWindow();
		LayoutParams attributes = window.getAttributes();
		float flevel = level;
		attributes.screenBrightness = flevel / 255;
		getWindow().setAttributes(attributes);
	}

	/**
	 * saveArticle--设置亮度
	 * @param int level
	 * @return boolean true:保存成功  false：保存失败
	 */
	private boolean saveArticle() {
		boolean isSucceed = true;
		String article_title = intent.getStringExtra("article_title");
		String article_url = intent.getStringExtra("article_url");
		String article_published_date = intent
				.getStringExtra("article_published_date");
		String article_description = intent
				.getStringExtra("article_description");
		DbHelper dbHelper = new DbHelper(this);
		
		//判断插入是否成功
		if (dbHelper.insertArticle(article_title, article_url,
				article_published_date, article_description) == -1) {
			isSucceed = false;
		}
		return isSucceed;
	}

	/**
	 * clearCacheFolder--清除缓存
	 * @param int level
	 * @return int 删除文件数
	 */
	private int clearCacheFolder(File dir, long numDays) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, numDays);
					}
					if (child.lastModified() < numDays) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	/**
	 * mWebViewClient--使网页只在webview里跳转的类
	 * 
	 * @author 姚富品
	 * @date 2013-7-19
	 * @version 3.0
	 */
	public class mWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			Toast.makeText(ArticleActivity.this, // 提示保存成功
					"加载中，请稍后", Toast.LENGTH_SHORT).show();
			return true;
		}
	}
}
