package com.yao.rss.activity;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yao.rss.database.DbHelper;
import com.yao.rssReader.R;

/**
 * FavouriteActivity--显示收藏界面
 * 
 * @author 姚富品
 * @date 2013-7-19
 * @version 3.0
 */
public class FavouriteActivity extends Activity
{
	private TextView mRSSTitle;//标题
	private ListView  mListView;//文章列表
	private SimpleCursorAdapter mArticleListAdapter;//文章列表适配器
	private ImageButton mImageBtnClose;//退出按钮
	private Cursor mCursor = null;
	
	//文章的id,title,url,publisheddate,description
	private final static String ARTICLE_ID = "article_id";
	private final static String ARTICLE_TITLE = "article_title";
	private final static String ARTICLE_URL = "article_url";
	private final static String ARTICLE_PUBLISHEDDATE = "article_published_date";
	private final static String ARTICLE_DESCCRIPTION = "article_description";

	protected void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.common_listview);
	    mRSSTitle=(TextView)findViewById(R.id.tv_task_name);
		mImageBtnClose = (ImageButton) findViewById(R.id.ibtn_close);
	    
	    final Intent intent=getIntent();
     
        mRSSTitle.setText("我的收藏"); 
		mListView=(ListView)findViewById(R.id.acticlelist);
		initArticleListView();//给文章列表设置数据源
		
		//给文章列表设置数据源
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
		
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				mCursor.moveToPosition(arg2); 
				Intent it=new Intent(FavouriteActivity.this,ArticleActivity.class);
				
				it.putExtra("article_url",mCursor.getString(2));
				it.putExtra("article_title",mCursor.getString(1));
				it.putExtra("article_published_date", mCursor.getString(3));
				it.putExtra("article_description",mCursor.getString(4));
				it.putExtra(MainActivity.CHANNEL_TITLE,intent.getStringExtra(MainActivity.CHANNEL_URL));
				it.putExtra(MainActivity.CHANNEL_URL,intent.getStringExtra(MainActivity.CHANNEL_TITLE));
				it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it);	
			}
	});
		
		this.registerForContextMenu(mListView); //给列表项注册长按监听
		
		View.OnClickListener mOnClickListener = new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				switch (v.getId()) 
				{
					//如果点击退出
					case R.id.ibtn_close:
						Intent intent=new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						FavouriteActivity.this.startActivity(intent);
						System.exit(0);
						break;

					default:
						break;
				}
			}
		};
		mImageBtnClose.setOnClickListener(mOnClickListener);
	}
	
	/**
	 * initArticleListView--给文章列表设置数据源
	 * 
	 * @return void
	 */
	private void initArticleListView() 
	{ 
		DbHelper db = new DbHelper(this);
		mCursor = db.selectArticle();//取得数据库中的文章
		if(mCursor.moveToFirst())
		{
			startManagingCursor(mCursor);//让Activity来管理cursor
			String[] from = {ARTICLE_TITLE,ARTICLE_PUBLISHEDDATE,ARTICLE_DESCCRIPTION};	//数据库中的列名
			int[] to = {R.id.tv_item_title,R.id.tv_item_publisheddate,R.id.tv_item_description};				//数据库中列的内容绑定的视图
			mArticleListAdapter=new SimpleCursorAdapter(this, 
				R.layout.layout_rssarticles_item, mCursor, from, to);
			mListView.setAdapter(mArticleListAdapter);
		}
		else
			Toast.makeText(getApplicationContext(), "亲，您还没有收藏文章呢",
					1000).show();
	}
	
	/**
	 * onCreateContextMenu--添加长按弹出菜单
	 */
	public void onCreateContextMenu(ContextMenu menu, View v,   
            ContextMenuInfo menuInfo) 
	{   
        super.onCreateContextMenu(menu, v, menuInfo);  
            menu.add(1, 0, 0, "删除").setCheckable(true);  
	}  
    
	/**
	 * onCreateContextMenu--添加点击长按弹出菜单点击监听
	 */
    public boolean onContextItemSelected(MenuItem item) 
    {  
    	AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		DbHelper dbHelper = new DbHelper(FavouriteActivity.this);
		boolean isSucceed = dbHelper.deleteArticle(String.valueOf(info.id));
		
		//判断是否删除成功，成功后刷新列表
		if(isSucceed)
		{
			Toast.makeText(FavouriteActivity.this, 		// 提示保存成功
					"删除成功", Toast.LENGTH_SHORT).show();
			initArticleListView();
		} 
		else 
		{
			Toast.makeText(FavouriteActivity.this, 		// 提示保存失败
					"删除失败", Toast.LENGTH_SHORT).show();

		}
        return super.onContextItemSelected(item);  
    }
	 
}
