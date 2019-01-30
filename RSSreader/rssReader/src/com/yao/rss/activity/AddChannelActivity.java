package com.yao.rss.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.yao.rss.database.DbHelper;
import com.yao.rssReader.R;

/**
 * AddChannelActivity--显示添加rss源界面
 * 
 * @author 姚富品
 * @date 2013-7-19
 * @version 3.0
 */
public class AddChannelActivity extends Activity {
	static final public String sTITLE = "title";
	static final public String sURL = "url";
	static final public String sID = "_id";

	private EditText mEdtTitle;// rss源title编辑框
	private EditText mEdtUrl;// rss源url编辑框
	private Button mBtnAdd;// 添加按钮
	private TextView mTvRSSTitle;// 标题
	private ImageButton mImageBtnClose;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_addchannel);
		mBtnAdd = (Button) findViewById(R.id.btn_add);
		mImageBtnClose = (ImageButton) findViewById(R.id.ibtn_close);
		mEdtTitle = (EditText) findViewById(R.id.et_name);
		mEdtUrl = (EditText) findViewById(R.id.et_url);
		mTvRSSTitle = (TextView) findViewById(R.id.tv_task_name);

		mTvRSSTitle.setText("添加频道");

		Intent intent = getIntent();
		intent.getIntExtra(sID, -1);

		View.OnClickListener mOnClickListener = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_add:// 如果按下了添加按钮
					int isSave = 1;// 判断能否添加标志

					// 判断网络是否可用，可用isSave为1，不可用为0
					if (checkNetState() == 1) {
						com.yao.rss.rome.RSSApi api = new com.yao.rss.rome.RSSApi();
						SyndFeed feed = api.getFeed(mEdtTitle.getText() + "");

						if (feed == null) {
							isSave = 0;
							Toast.makeText(AddChannelActivity.this, // 提示保存成功
									"亲，网址不对哦", Toast.LENGTH_SHORT).show();
						}
					} else {
						isSave = 0;
						Toast.makeText(AddChannelActivity.this,
								"亲，网络出问题了，无法判断网址是否可用", Toast.LENGTH_SHORT)
								.show();
					}

					// 判断是否可以添加，为1保存，为0提示网址不可用是否仍然保存
					if (isSave == 1) {
						if (saveChannel()) {
							Toast.makeText(AddChannelActivity.this, // 提示保存成功
									"保存成功", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(AddChannelActivity.this, // 提示保存失败
									"保存失败", Toast.LENGTH_SHORT).show();
						}
					} else {
						AlertDialog.Builder mDialog = new AlertDialog.Builder(
								AddChannelActivity.this);
						mDialog.setTitle("操作提示");
						mDialog.setMessage("确定保存吗？");
						mDialog.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										if (saveChannel()) {
											Toast.makeText(
													AddChannelActivity.this, // 提示保存成功
													"保存成功", Toast.LENGTH_SHORT)
													.show();
										} else {
											Toast.makeText(
													AddChannelActivity.this, // 提示保存失败
													"保存失败", Toast.LENGTH_SHORT)
													.show();
										}
									}
								});
						mDialog.setNegativeButton("取消", null);
						mDialog.show();
					}
					break;
				// 如果按下了关闭按钮
				case R.id.ibtn_close:
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					AddChannelActivity.this.startActivity(intent);
					System.exit(0);
					break;

				default:
					break;
				}
			}
		};
		mBtnAdd.setOnClickListener(mOnClickListener);
		mImageBtnClose.setOnClickListener(mOnClickListener);
	}

	/**
	 * checkNetState--检查网络是否可用
	 * 
	 * @return int 1:可用 0：不可用
	 */
	public int checkNetState() {
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * saveChannel--检查网络是否可用
	 * 
	 * @return boolean true:保存成功 false：保存成功
	 */
	private boolean saveChannel() {
		boolean isSucceed = true;
		String title = mEdtTitle.getText() + "";
		String url = mEdtUrl.getText() + "";
		// 判断title是否为空
		if (TextUtils.isEmpty(title)) {
			Toast.makeText(this, "保存内容为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		DbHelper dbHelper = new DbHelper(this);
		if (dbHelper.insertChannel(title, url) == -1) {
			isSucceed = false;
		}
		return isSucceed;
	}
}
