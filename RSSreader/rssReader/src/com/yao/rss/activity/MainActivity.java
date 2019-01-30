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

//本程序代码遵循的代码规范：所有全局变量以m开头
/**
 * MainActivity--程序的入口类,显示rss源界面及菜单界面
 * @author 姚富品
 * @date 2013-7-19
 * @version 3.0
 */
public class MainActivity extends ListActivity {
	private SlidingDrawer mDrawer;
	private ArrayList<HashMap<String, Object>> mMenulistItems; // 作为菜单适配器的数据源
	private ArrayList<HashMap<String, Object>> mChannellistItems;// 作为频道列表适配器数据源
	private SimpleAdapter mMenuListAdapter;
	private Cursor mCursor = null;
	private GridView mGridViewChannelList;
	private SharedPreferences mPreferences;
	private String mPreferencesFileName = "count";// 用于保存是否首次使用软件信息的配置文件
	private ImageButton mImgBtnClose;
	private MyHandler mMyHandler;
	public List mRssList = null;// 从网址得到的列表
	private ProgressDialog mProgressDialog = null;
	private String mIsUrlRight = null;// 网址是否正确的标志量
	private SimpleCursorAdapter mChannelListAdapter;
	
	/* rss源数据库字段 */
	public final static String CHANNEL_ID = "_id";
	public final static String CHANNEL_TITLE = "title";
	public final static String CHANNEL_URL = "url";
	
	/* 文章数据库字段 */
	public final static String ARTICLE_ID = "_id";
	public final static String ARTICLE_TITLE = "article_title";
	public final static String ARTICLE_URL = "article_url";
	public final static String ARTICLE_PUBLISHEDDATE = "article_published_date";
	public final static String ARTICLE_DESCCRIPTION = "article_description";

	/*退出按钮监听器*/
	View.OnClickListener mBtnOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MainActivity.this.startActivity(intent);
			System.exit(0);
		}
	};

	/*菜单列表监听器*/
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		switch (position) {
		
		/*跳转到收藏界面*/
		case 0:
			Intent itFavourite = new Intent(MainActivity.this,
					FavouriteActivity.class);
			//将从数据库得到的rss源名字传到FavouriteActivity
			itFavourite
					.putExtra(MainActivity.CHANNEL_TITLE,mCursor.getString(mCursor
					.getColumnIndexOrThrow(MainActivity.CHANNEL_TITLE)));
			//将从数据库得到的rss源网址传到FavouriteActivity
			itFavourite.putExtra(MainActivity.CHANNEL_URL, mCursor
					.getString(mCursor
					.getColumnIndexOrThrow(MainActivity.CHANNEL_URL)));
			startActivity(itFavourite);
			break;
			
		/*跳转到添加rss源界面*/
		case 1:
			Intent itAddChannel = new Intent(MainActivity.this,
					AddChannelActivity.class);
			itAddChannel.putExtra("_id", position);
			startActivity(itAddChannel);
			break;

		/*跳转到添加反馈界面*/
		case 2:
			//调用友盟平台包中的方法
			FeedbackAgent agent = new FeedbackAgent(MainActivity.this);
			agent.startFeedbackActivity();
			break;
			
		/*跳转到设置网络界面*/
		case 3:
			//设置网络
			startActivityForResult(new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
			break;

		/*跳转到更多选项界面*/
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
		MobclickAgent.onError(this);// 调用友盟平台方法自动捕获异常退出

		initMenuListView();
		this.setListAdapter(mMenuListAdapter);
		
		mGridViewChannelList = (GridView) findViewById(R.id.gv_channellist);
		this.registerForContextMenu(mGridViewChannelList);

		//rss源列表监听器
		mGridViewChannelList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//判断网络
				if (checkNetState() == 1) {
					mProgressDialog = ProgressDialog.show(MainActivity.this,
							"", "加载中，请稍后", true);
					mMyHandler = new MyHandler();
					MyThread myThread = new MyThread(position);
					new Thread(myThread).start();//开启线程执行获取联网耗时操作
				} 
				else {
					//调用友盟平台方法发送捕获的异常
					MobclickAgent.reportError(MainActivity.this, "net error");
					Toast.makeText(MainActivity.this, "亲，网络出问题了，去设置网络吧",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		//读取用户是否首次使用的信息
		mPreferences = getSharedPreferences(mPreferencesFileName,
				MODE_WORLD_READABLE);
		int count = mPreferences.getInt(mPreferencesFileName, 0);
		//判断是否是首次使用，如果是进入用户引导界面，否则不进入
		if (count == 0) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), GuidActivity.class);
			startActivity(intent);
		}
		Editor editor = mPreferences.edit();
		//改变信息（使用次数）
		editor.putInt(mPreferencesFileName, ++count);
		editor.commit();
	}

	// 退出时提示
	public boolean onKeyDown(int lKeyCode, KeyEvent lEvent) {
		//判断是否按下返回键，如果是则提示是否退出
		if (lKeyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MainActivity.this);
			dialog.setTitle("操作提示");
			dialog.setMessage("确定退出吗？");
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							System.exit(0);
						}
					});
			dialog.setNegativeButton("取消", null);
			dialog.show();
		}
		return super.onKeyDown(lKeyCode, lEvent);
	}
	

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);// 统计使用次数
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
		menu.add(1, 0, 0, "删除").setCheckable(true);
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo lInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		DbHelper dbHelper = new DbHelper(MainActivity.this);
		boolean isSucceed = dbHelper.deleteChannel(String.valueOf(lInfo.id));
		
		if (isSucceed) {
			Toast.makeText(MainActivity.this, // 提示保存成功
					"删除成功", Toast.LENGTH_SHORT).show();
			initChannelListView();
		} else {
			Toast.makeText(MainActivity.this, // 提示保存失败
					"删除失败", Toast.LENGTH_SHORT).show();
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * initMenuListView--设置菜单列表数据源
	 * @return void
	 */
	private void initMenuListView() {
		mMenulistItems = new ArrayList<HashMap<String, Object>>();
		String[] menu = new String[] { "我的收藏", "添加rss频道", "反馈意见", "网络", "更多" };
		int[] picture = new int[] { R.drawable.store, R.drawable.add,
				R.drawable.feedback, R.drawable.net, R.drawable.more };
		
		//将图片和文字添加到map
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
	 * initChannelListView--设置rss源列表数据源
	 * @return void
	 */

	private void initChannelListView() {
		DbHelper dbHelper = new DbHelper(this);
		//对列表初始化，添加默认rss源
		dbHelper.initTableChannel();
		mCursor = dbHelper.selectChannel(); // 取得数据库中的记事
		if (mCursor.moveToFirst()) {
			startManagingCursor(mCursor);// 让Activity来管理cursor
			String[] from = { MainActivity.CHANNEL_TITLE }; // 数据库中的列名
			int[] to = { R.id.tv_item_channellist_channelName }; // 数据库中列的内容绑定的视图
			mChannelListAdapter = new SimpleCursorAdapter(this,
					R.layout.layout_channellist_item, mCursor, from, to);
			mGridViewChannelList.setAdapter(mChannelListAdapter); // listview
																	// 绑定适配器
		}
	}
	
	/**
	 * checkNetState--检查网络是否可用
	 * @return int    1:可用         0：不可用
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

	 /** MyHandler--接收子线程数据并更新ui
	 * 
	 * @author 姚富品
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
			
			//判断网址是否可用，如果是进入RSSArticlesActivity，否则提示错误
			if (b.get(mIsUrlRight) == "1") {
				mProgressDialog.dismiss();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, RSSArticlesActivity.class);
				//将从数据库得到的rss源名称传到RSSArticlesActivity
				intent.putExtra(
						MainActivity.CHANNEL_TITLE,
						mCursor.getString(mCursor
								.getColumnIndexOrThrow(MainActivity.CHANNEL_TITLE)));
				//将从数据库得到的rss源网址传到RSSArticlesActivity
				intent.putExtra(
						MainActivity.CHANNEL_URL,
						mCursor.getString(mCursor
								.getColumnIndexOrThrow(MainActivity.CHANNEL_URL)));
				intent.putExtras(b);
				startActivity(intent);
			} else {
				mProgressDialog.dismiss();
				Toast.makeText(MainActivity.this, "网址错误", Toast.LENGTH_LONG)
						.show();
			}
		}
	}
	 /** MyThread--用处创建子线程的类
		 * 
		 * @author 姚富品
		 * @date 2013-7-19
		 * @version 3.0
		 */
	private class MyThread implements Runnable {
		int position;//点击源列表的位置

		public MyThread(int position) {
			this.position = position;
		}

		public void run() {
			if (mRssList == null) {
				mCursor.moveToPosition(position); // 将cursor指向position
				String feedUrl = mCursor.getString(mCursor
						.getColumnIndexOrThrow(MainActivity.CHANNEL_URL));
				com.yao.rss.rome.RSSApi lApi = new com.yao.rss.rome.RSSApi();
				SyndFeed feed = lApi.getFeed(feedUrl);

				//通过捕获异常判断网址是否可用
				try {
					mRssList = feed.getEntries();
					Message msg = new Message();
					Bundle b = new Bundle();
					ArrayList list = new ArrayList();
					list.add(mRssList);
					
					//未发生异常则网址正确
					b.putString(mIsUrlRight, "1");
					b.putParcelableArrayList("list", list);
					msg.setData(b);
					MainActivity.this.mMyHandler.sendMessage(msg);
				} catch (NullPointerException e) {
					Message msg = new Message();
					Bundle b = new Bundle();
					//发生异常则网址正确
					b.putString(mIsUrlRight, "0");
					msg.setData(b);
					MainActivity.this.mMyHandler.sendMessage(msg);
					return;
				}
			}

		}
	}
}
