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
 * MoreActicity--��ʾ����˵�����
 * @author Ҧ��Ʒ
 * @date 2013-7-19
 * @version 3.0
 */
public class MoreActicity extends Activity 
{
	private TextView mRSSTitle;//�����ı�
	private ImageButton mImageBtnClose;//�˳���ť
	private Button mBtnAbout;//���ڰ�ť
	private Button mBtnUserGuid;//�û�������ť
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
				case R.id.btn_about:    //�������˹��ڣ����ֶԻ���
					AlertDialog.Builder mDialog = new AlertDialog.Builder(
							MoreActicity.this);
					mDialog.setTitle("����");
					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.layout_more_about_altdlg,
							(ViewGroup) findViewById(R.id.about));
					mDialog.setView(view);
					mDialog.setPositiveButton("����",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					mDialog.show();
					break;

				case R.id.btn_UserGuide:    //���������û�������ť���򷵻��������ڽ����������
					
					//ɾ�������û��Ƿ��״�ʹ����Ϣ���ļ���ʹ�ص�������֮���ܽ����������
					SharedPreferences sharedPreferences = getSharedPreferences(
							mGuidName, Context.MODE_PRIVATE);
					Editor editor = sharedPreferences.edit();
					editor.remove(mGuidName);
					editor.commit();
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), MainActivity.class);
					startActivity(intent);
					break;
					
				case R.id.ibtn_close:	//���������˳�
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
