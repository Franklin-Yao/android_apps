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
 * ArticleActivity--��ʾ���½���
 * 
 * @author Ҧ��Ʒ
 * @date 2013-7-19
 * @version 3.0
 */
public class ArticleActivity extends Activity {
	WebView mWebView;
	private ImageButton mImageBtnRefresh;// ˢ�°�ť
	private ImageButton mImageBtnLightOn;// ������Ȱ�ť
	private ImageButton mImageBtnLightOff;// �������Ȱ�ť
	private ImageButton mImageBtnCollection;// �ղذ�ť
	private ImageButton mImageBtnClear;// ������水ť
	private ImageButton mImageBtnClose;// �˳���ť
	private ImageButton mImageBtnShare;// ����ť

	private int mBrightness = 1;// ��������
	private Intent intent;// ��RssArticleActivity�õ���intent

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
		
		//�ж������Ƿ����
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
		
		Toast.makeText(ArticleActivity.this, "�����У����Ժ�", Toast.LENGTH_SHORT)
				.show();
		
		View.OnClickListener mOnClickListener = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				
				//��������������Ȱ�ť
				case R.id.ibtn_light_on:
					setBrightness(30);
					break;

				//��������������水ť
				case R.id.ibtn_clear:
					clearCacheFolder(ArticleActivity.this.getCacheDir(),
							System.currentTimeMillis());
					Toast.makeText(ArticleActivity.this, // ��ʾ����ɹ�
							"�������ɹ�", Toast.LENGTH_SHORT).show();
					break;

				//��������������Ȱ�ť
				case R.id.ibtn_light_off:
					setBrightness(5);
					break;
					
				//���������ղذ�ť
				case R.id.ibtn_collection:
					if (saveArticle()) {
						Toast.makeText(ArticleActivity.this, // ��ʾ����ɹ�
								"�ղسɹ�", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(ArticleActivity.this, // ��ʾ����ʧ��
								"�ղ�ʧ��", Toast.LENGTH_SHORT).show();
					}
					break;

				//��������ˢ�°�ť
				case R.id.ibtn_refresh:
					mWebView.loadUrl(url);//����������ҳ
					Toast.makeText(ArticleActivity.this, // ��ʾ����ʧ��
							"����ˢ��", Toast.LENGTH_SHORT).show();
					break;
					
				//���������˳���ť
				case R.id.ibtn_close:
					Intent intentClose = new Intent(Intent.ACTION_MAIN);
					intentClose.addCategory(Intent.CATEGORY_HOME);
					intentClose.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					ArticleActivity.this.startActivity(intentClose);
					System.exit(0);
					break;

				//��������������Ȱ�ť
				case R.id.ibtn_share:
					Intent it4 = new Intent(Intent.ACTION_SEND);
					it4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					it4.putExtra(Intent.EXTRA_TEXT, "����΢��");
					it4.setType("image/*");
					it4.putExtra(Intent.EXTRA_SUBJECT, "����");
					startActivity(Intent.createChooser(it4, "����"));
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
	 * setBrightness--��������
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
	 * saveArticle--��������
	 * @param int level
	 * @return boolean true:����ɹ�  false������ʧ��
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
		
		//�жϲ����Ƿ�ɹ�
		if (dbHelper.insertArticle(article_title, article_url,
				article_published_date, article_description) == -1) {
			isSucceed = false;
		}
		return isSucceed;
	}

	/**
	 * clearCacheFolder--�������
	 * @param int level
	 * @return int ɾ���ļ���
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
	 * mWebViewClient--ʹ��ҳֻ��webview����ת����
	 * 
	 * @author Ҧ��Ʒ
	 * @date 2013-7-19
	 * @version 3.0
	 */
	public class mWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			Toast.makeText(ArticleActivity.this, // ��ʾ����ɹ�
					"�����У����Ժ�", Toast.LENGTH_SHORT).show();
			return true;
		}
	}
}
