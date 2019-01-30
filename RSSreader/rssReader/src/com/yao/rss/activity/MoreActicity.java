package com.yao.rss.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yao.rssReader.R;

/**
 * MoreActicity--显示更多菜单界面
 * @author 姚富品
 * @date 2013-7-19
 * @version 3.0
 */
public class MoreActicity extends Activity 
{
	private TextView mRSSTitle;//标题文本
	private ImageButton mImageBtnClose;//退出按钮
	private Button mBtnAbout;//关于按钮
	private Button mBtnUserGuid;//用户引导按钮
	private String mGuidName = "count";
	

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moreactivity);
		mRSSTitle = (TextView) findViewById(R.id.tv_task_name);
		mBtnAbout = (Button) findViewById(R.id.btn_about);
		mImageBtnClose = (ImageButton) findViewById(R.id.ibtn_close);
		mBtnUserGuid = (Button) findViewById(R.id.btn_UserGuide);

		mRSSTitle.setText("");
		View.OnClickListener mOnClickListener = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_about:    //如果点击了关于，出现对话框
					AlertDialog.Builder mDialog = new AlertDialog.Builder(
							MoreActicity.this);
					mDialog.setTitle("关于");
					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.layout_more_about_altdlg,
							(ViewGroup) findViewById(R.id.about));
					mDialog.setView(view);
					mDialog.setPositiveButton("返回",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					mDialog.show();
					break;

				case R.id.btn_UserGuide:    //如果点击了用户引导按钮，则返回主界面在进入帮助界面
					
					//删除保存用户是否首次使用信息的文件，使回到主界面之后能进入帮助界面
					SharedPreferences sharedPreferences = getSharedPreferences(
							mGuidName, Context.MODE_PRIVATE);
					Editor editor = sharedPreferences.edit();
					editor.remove(mGuidName);
					editor.commit();
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), MainActivity.class);
					startActivity(intent);
					break;
					
				case R.id.ibtn_close:	//如果点击了退出
					Intent lItClose=new Intent(Intent.ACTION_MAIN);
					lItClose.addCategory(Intent.CATEGORY_HOME);
					lItClose.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					MoreActicity.this.startActivity(lItClose);
					System.exit(0);
				default:
					break;
				}
			}
		};
		mBtnAbout.setOnClickListener(mOnClickListener);
		mImageBtnClose.setOnClickListener(mOnClickListener);
		mBtnUserGuid.setOnClickListener(mOnClickListener);
	}

	public boolean onContextItemSelected(MenuItem item) {
		return super.onContextItemSelected(item);
	}

}
