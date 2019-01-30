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
 * AddChannelActivity--��ʾ���rssԴ����
 * 
 * @author Ҧ��Ʒ
 * @date 2013-7-19
 * @version 3.0
 */
public class AddChannelActivity extends Activity {
	static final public String sTITLE = "title";
	static final public String sURL = "url";
	static final public String sID = "_id";

	private EditText mEdtTitle;// rssԴtitle�༭��
	private EditText mEdtUrl;// rssԴurl�༭��
	private Button mBtnAdd;// ��Ӱ�ť
	private TextView mTvRSSTitle;// ����
	private ImageButton mImageBtnClose;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_addchannel);
		mBtnAdd = (Button) findViewById(R.id.btn_add);
		mImageBtnClose = (ImageButton) findViewById(R.id.ibtn_close);
		mEdtTitle = (EditText) findViewById(R.id.et_name);
		mEdtUrl = (EditText) findViewById(R.id.et_url);
		mTvRSSTitle = (TextView) findViewById(R.id.tv_task_name);

		mTvRSSTitle.setText("���Ƶ��");

		Intent intent = getIntent();
		intent.getIntExtra(sID, -1);

		View.OnClickListener mOnClickListener = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_add:// �����������Ӱ�ť
					int isSave = 1;// �ж��ܷ���ӱ�־

					// �ж������Ƿ���ã�����isSaveΪ1��������Ϊ0
					if (checkNetState() == 1) {
						com.yao.rss.rome.RSSApi api = new com.yao.rss.rome.RSSApi();
						SyndFeed feed = api.getFeed(mEdtTitle.getText() + "");

						if (feed == null) {
							isSave = 0;
							Toast.makeText(AddChannelActivity.this, // ��ʾ����ɹ�
									"�ף���ַ����Ŷ", Toast.LENGTH_SHORT).show();
						}
					} else {
						isSave = 0;
						Toast.makeText(AddChannelActivity.this,
								"�ף�����������ˣ��޷��ж���ַ�Ƿ����", Toast.LENGTH_SHORT)
								.show();
					}

					// �ж��Ƿ������ӣ�Ϊ1���棬Ϊ0��ʾ��ַ�������Ƿ���Ȼ����
					if (isSave == 1) {
						if (saveChannel()) {
							Toast.makeText(AddChannelActivity.this, // ��ʾ����ɹ�
									"����ɹ�", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(AddChannelActivity.this, // ��ʾ����ʧ��
									"����ʧ��", Toast.LENGTH_SHORT).show();
						}
					} else {
						AlertDialog.Builder mDialog = new AlertDialog.Builder(
								AddChannelActivity.this);
						mDialog.setTitle("������ʾ");
						mDialog.setMessage("ȷ��������");
						mDialog.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										if (saveChannel()) {
											Toast.makeText(
													AddChannelActivity.this, // ��ʾ����ɹ�
													"����ɹ�", Toast.LENGTH_SHORT)
													.show();
										} else {
											Toast.makeText(
													AddChannelActivity.this, // ��ʾ����ʧ��
													"����ʧ��", Toast.LENGTH_SHORT)
													.show();
										}
									}
								});
						mDialog.setNegativeButton("ȡ��", null);
						mDialog.show();
					}
					break;
				// ��������˹رհ�ť
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
	 * checkNetState--��������Ƿ����
	 * 
	 * @return int 1:���� 0��������
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
	 * saveChannel--��������Ƿ����
	 * 
	 * @return boolean true:����ɹ� false������ɹ�
	 */
	private boolean saveChannel() {
		boolean isSucceed = true;
		String title = mEdtTitle.getText() + "";
		String url = mEdtUrl.getText() + "";
		// �ж�title�Ƿ�Ϊ��
		if (TextUtils.isEmpty(title)) {
			Toast.makeText(this, "��������Ϊ��", Toast.LENGTH_SHORT).show();
			return false;
		}
		DbHelper dbHelper = new DbHelper(this);
		if (dbHelper.insertChannel(title, url) == -1) {
			isSucceed = false;
		}
		return isSucceed;
	}
}
