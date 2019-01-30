package com.yao.rss.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.yao.rss.database.DbHelper;
import com.yao.rssReader.R;

//�����������ѭ�Ĵ���淶������ȫ�ֱ�����m��ͷ
/**
 * MainActivity--����������,��ʾrssԴ���漰�˵�����
 * @author Ҧ��Ʒ
 * @date 2013-7-19
 * @version 3.0
 */
public class MainActivity extends ListActivity {
	private SlidingDrawer mDrawer;
	private ArrayList<HashMap<String, Object>> mMenulistItems; // ��Ϊ�˵�������������Դ
	private ArrayList<HashMap<String, Object>> mChannellistItems;// ��ΪƵ���б�����������Դ
	private SimpleAdapter mMenuListAdapter;
	private Cursor mCursor = null;
	private GridView mGridViewChannelList;
	private SharedPreferences mPreferences;
	private String mPreferencesFileName = "count";// ���ڱ����Ƿ��״�ʹ�������Ϣ�������ļ�
	private ImageButton mImgBtnClose;
	private MyHandler mMyHandler;
	public List mRssList = null;// ����ַ�õ����б�
	private ProgressDialog mProgressDialog = null;
	private String mIsUrlRight = null;// ��ַ�Ƿ���ȷ�ı�־��
	private SimpleCursorAdapter mChannelListAdapter;
	
	/* rssԴ���ݿ��ֶ� */
	public final static String CHANNEL_ID = "_id";
	public final static String CHANNEL_TITLE = "title";
	public final static String CHANNEL_URL = "url";
	
	/* �������ݿ��ֶ� */
	public final static String ARTICLE_ID = "_id";
	public final static String ARTICLE_TITLE = "article_title";
	public final static String ARTICLE_URL = "article_url";
	public final static String ARTICLE_PUBLISHEDDATE = "article_published_date";
	public final static String ARTICLE_DESCCRIPTION = "article_description";

	/*�˳���ť������*/
	View.OnClickListener mBtnOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MainActivity.this.startActivity(intent);
			System.exit(0);
		}
	};

	/*�˵��б������*/
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		switch (position) {
		
		/*��ת���ղؽ���*/
		case 0:
			Intent itFavourite = new Intent(MainActivity.this,
					FavouriteActivity.class);
			//�������ݿ�õ���rssԴ���ִ���FavouriteActivity
			itFavourite
					.putExtra(MainActivity.CHANNEL_TITLE,mCursor.getString(mCursor
					.getColumnIndexOrThrow(MainActivity.CHANNEL_TITLE)));
			//�������ݿ�õ���rssԴ��ַ����FavouriteActivity
			itFavourite.putExtra(MainActivity.CHANNEL_URL, mCursor
					.getString(mCursor
					.getColumnIndexOrThrow(MainActivity.CHANNEL_URL)));
			startActivity(itFavourite);
			break;
			
		/*��ת�����rssԴ����*/
		case 1:
			Intent itAddChannel = new Intent(MainActivity.this,
					AddChannelActivity.class);
			itAddChannel.putExtra("_id", position);
			startActivity(itAddChannel);
			break;

		/*��ת����ӷ�������*/
		case 2:
			//��������ƽ̨���еķ���
			FeedbackAgent agent = new FeedbackAgent(MainActivity.this);
			agent.startFeedbackActivity();
			break;
			
		/*��ת�������������*/
		case 3:
			//��������
			startActivityForResult(new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
			break;

		/*��ת������ѡ�����*/
		case 4:
			Intent itMore = new Intent(MainActivity.this, MoreActicity.class);
			startActivity(itMore);
			break;
		default:
			break;
		}
	}

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_main_slidingdrawer);
		mImgBtnClose = (ImageButton) findViewById(R.id.ibtn_close);

		mImgBtnClose.setOnClickListener(mBtnOnClickListener);
		MobclickAgent.onError(this);// ��������ƽ̨�����Զ������쳣�˳�

		initMenuListView();
		this.setListAdapter(mMenuListAdapter);
		
		mGridViewChannelList = (GridView) findViewById(R.id.gv_channellist);
		this.registerForContextMenu(mGridViewChannelList);

		//rssԴ�б������
		mGridViewChannelList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//�ж�����
				if (checkNetState() == 1) {
					mProgressDialog = ProgressDialog.show(MainActivity.this,
							"", "�����У����Ժ�", true);
					mMyHandler = new MyHandler();
					MyThread myThread = new MyThread(position);
					new Thread(myThread).start();//�����߳�ִ�л�ȡ������ʱ����
				} 
				else {
					//��������ƽ̨�������Ͳ�����쳣
					MobclickAgent.reportError(MainActivity.this, "net error");
					Toast.makeText(MainActivity.this, "�ף�����������ˣ�ȥ���������",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		//��ȡ�û��Ƿ��״�ʹ�õ���Ϣ
		mPreferences = getSharedPreferences(mPreferencesFileName,
				MODE_WORLD_READABLE);
		int count = mPreferences.getInt(mPreferencesFileName, 0);
		//�ж��Ƿ����״�ʹ�ã�����ǽ����û��������棬���򲻽���
		if (count == 0) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), GuidActivity.class);
			startActivity(intent);
		}
		Editor editor = mPreferences.edit();
		//�ı���Ϣ��ʹ�ô�����
		editor.putInt(mPreferencesFileName, ++count);
		editor.commit();
	}

	// �˳�ʱ��ʾ
	public boolean onKeyDown(int lKeyCode, KeyEvent lEvent) {
		//�ж��Ƿ��·��ؼ������������ʾ�Ƿ��˳�
		if (lKeyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MainActivity.this);
			dialog.setTitle("������ʾ");
			dialog.setMessage("ȷ���˳���");
			dialog.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							System.exit(0);
						}
					});
			dialog.setNegativeButton("ȡ��", null);
			dialog.show();
		}
		return super.onKeyDown(lKeyCode, lEvent);
	}
	

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);// ͳ��ʹ�ô���
		MobclickAgent.onEventBegin(this, "start");
		initChannelListView();
	}
	

	public void onPause() {
		super.onPause();
		MobclickAgent.onEventEnd(this, "finish");
		MobclickAgent.onPause(this);
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(1, 0, 0, "ɾ��").setCheckable(true);
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo lInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		DbHelper dbHelper = new DbHelper(MainActivity.this);
		boolean isSucceed = dbHelper.deleteChannel(String.valueOf(lInfo.id));
		
		if (isSucceed) {
			Toast.makeText(MainActivity.this, // ��ʾ����ɹ�
					"ɾ���ɹ�", Toast.LENGTH_SHORT).show();
			initChannelListView();
		} else {
			Toast.makeText(MainActivity.this, // ��ʾ����ʧ��
					"ɾ��ʧ��", Toast.LENGTH_SHORT).show();
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * initMenuListView--���ò˵��б�����Դ
	 * @return void
	 */
	private void initMenuListView() {
		mMenulistItems = new ArrayList<HashMap<String, Object>>();
		String[] menu = new String[] { "�ҵ��ղ�", "���rssƵ��", "�������", "����", "����" };
		int[] picture = new int[] { R.drawable.store, R.drawable.add,
				R.drawable.feedback, R.drawable.net, R.drawable.more };
		
		//��ͼƬ��������ӵ�map
		for (int i = 0; i < 5; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemTitle", menu[i]);
			map.put("ItemImage", picture[i]);
			mMenulistItems.add(map);
		}
		
		mMenuListAdapter = new SimpleAdapter(this, mMenulistItems,
				R.layout.layout_menulist_item, new String[] { "ItemTitle",
						"ItemImage" }, new int[] { R.id.iv_item_choice,
						R.id.iv_item_icon });
	}
	
	/**
	 * initChannelListView--����rssԴ�б�����Դ
	 * @return void
	 */

	private void initChannelListView() {
		DbHelper dbHelper = new DbHelper(this);
		//���б��ʼ�������Ĭ��rssԴ
		dbHelper.initTableChannel();
		mCursor = dbHelper.selectChannel(); // ȡ�����ݿ��еļ���
		if (mCursor.moveToFirst()) {
			startManagingCursor(mCursor);// ��Activity������cursor
			String[] from = { MainActivity.CHANNEL_TITLE }; // ���ݿ��е�����
			int[] to = { R.id.tv_item_channellist_channelName }; // ���ݿ����е����ݰ󶨵���ͼ
			mChannelListAdapter = new SimpleCursorAdapter(this,
					R.layout.layout_channellist_item, mCursor, from, to);
			mGridViewChannelList.setAdapter(mChannelListAdapter); // listview
																	// ��������
		}
	}
	
	/**
	 * checkNetState--��������Ƿ����
	 * @return int    1:����         0��������
	 */

	private int checkNetState() {
		ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo lInfo = cManager.getActiveNetworkInfo();
		if (lInfo != null && lInfo.isAvailable()) {
			return 1;
		} else {
			return 0;
		}
	}

	 /** MyHandler--�������߳����ݲ�����ui
	 * 
	 * @author Ҧ��Ʒ
	 * @date 2013-7-19
	 * @version 3.0
	 */
	public class MyHandler extends Handler {
		public MyHandler() {
		}

		private MyHandler(Looper l) {
			super(l);
		}

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle b = msg.getData();
			
			//�ж���ַ�Ƿ���ã�����ǽ���RSSArticlesActivity��������ʾ����
			if (b.get(mIsUrlRight) == "1") {
				mProgressDialog.dismiss();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, RSSArticlesActivity.class);
				//�������ݿ�õ���rssԴ���ƴ���RSSArticlesActivity
				intent.putExtra(
						MainActivity.CHANNEL_TITLE,
						mCursor.getString(mCursor
								.getColumnIndexOrThrow(MainActivity.CHANNEL_TITLE)));
				//�������ݿ�õ���rssԴ��ַ����RSSArticlesActivity
				intent.putExtra(
						MainActivity.CHANNEL_URL,
						mCursor.getString(mCursor
								.getColumnIndexOrThrow(MainActivity.CHANNEL_URL)));
				intent.putExtras(b);
				startActivity(intent);
			} else {
				mProgressDialog.dismiss();
				Toast.makeText(MainActivity.this, "��ַ����", Toast.LENGTH_LONG)
						.show();
			}
		}
	}
	 /** MyThread--�ô��������̵߳���
		 * 
		 * @author Ҧ��Ʒ
		 * @date 2013-7-19
		 * @version 3.0
		 */
	private class MyThread implements Runnable {
		int position;//���Դ�б��λ��

		public MyThread(int position) {
			this.position = position;
		}

		public void run() {
			if (mRssList == null) {
				mCursor.moveToPosition(position); // ��cursorָ��position
				String feedUrl = mCursor.getString(mCursor
						.getColumnIndexOrThrow(MainActivity.CHANNEL_URL));
				com.yao.rss.rome.RSSApi lApi = new com.yao.rss.rome.RSSApi();
				SyndFeed feed = lApi.getFeed(feedUrl);

				//ͨ�������쳣�ж���ַ�Ƿ����
				try {
					mRssList = feed.getEntries();
					Message msg = new Message();
					Bundle b = new Bundle();
					ArrayList list = new ArrayList();
					list.add(mRssList);
					
					//δ�����쳣����ַ��ȷ
					b.putString(mIsUrlRight, "1");
					b.putParcelableArrayList("list", list);
					msg.setData(b);
					MainActivity.this.mMyHandler.sendMessage(msg);
				} catch (NullPointerException e) {
					Message msg = new Message();
					Bundle b = new Bundle();
					//�����쳣����ַ��ȷ
					b.putString(mIsUrlRight, "0");
					msg.setData(b);
					MainActivity.this.mMyHandler.sendMessage(msg);
					return;
				}
			}

		}
	}
}
