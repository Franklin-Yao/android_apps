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
 * FavouriteActivity--��ʾ�ղؽ���
 * 
 * @author Ҧ��Ʒ
 * @date 2013-7-19
 * @version 3.0
 */
public class FavouriteActivity extends Activity
{
	private TextView mRSSTitle;//����
	private ListView  mListView;//�����б�
	private SimpleCursorAdapter mArticleListAdapter;//�����б�������
	private ImageButton mImageBtnClose;//�˳���ť
	private Cursor mCursor = null;
	
	//���µ�id,title,url,publisheddate,description
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
     
        mRSSTitle.setText("�ҵ��ղ�"); 
		mListView=(ListView)findViewById(R.id.acticlelist);
		initArticleListView();//�������б���������Դ
		
		//�������б���������Դ
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
		
		this.registerForContextMenu(mListView); //���б���ע�᳤������
		
		View.OnClickListener mOnClickListener = new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				switch (v.getId()) 
				{
					//�������˳�
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
	 * initArticleListView--�������б���������Դ
	 * 
	 * @return void
	 */
	private void initArticleListView() 
	{ 
		DbHelper db = new DbHelper(this);
		mCursor = db.selectArticle();//ȡ�����ݿ��е�����
		if(mCursor.moveToFirst())
		{
			startManagingCursor(mCursor);//��Activity������cursor
			String[] from = {ARTICLE_TITLE,ARTICLE_PUBLISHEDDATE,ARTICLE_DESCCRIPTION};	//���ݿ��е�����
			int[] to = {R.id.tv_item_title,R.id.tv_item_publisheddate,R.id.tv_item_description};				//���ݿ����е����ݰ󶨵���ͼ
			mArticleListAdapter=new SimpleCursorAdapter(this, 
				R.layout.layout_rssarticles_item, mCursor, from, to);
			mListView.setAdapter(mArticleListAdapter);
		}
		else
			Toast.makeText(getApplicationContext(), "�ף�����û���ղ�������",
					1000).show();
	}
	
	/**
	 * onCreateContextMenu--��ӳ��������˵�
	 */
	public void onCreateContextMenu(ContextMenu menu, View v,   
            ContextMenuInfo menuInfo) 
	{   
        super.onCreateContextMenu(menu, v, menuInfo);  
            menu.add(1, 0, 0, "ɾ��").setCheckable(true);  
	}  
    
	/**
	 * onCreateContextMenu--��ӵ�����������˵��������
	 */
    public boolean onContextItemSelected(MenuItem item) 
    {  
    	AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		DbHelper dbHelper = new DbHelper(FavouriteActivity.this);
		boolean isSucceed = dbHelper.deleteArticle(String.valueOf(info.id));
		
		//�ж��Ƿ�ɾ���ɹ����ɹ���ˢ���б�
		if(isSucceed)
		{
			Toast.makeText(FavouriteActivity.this, 		// ��ʾ����ɹ�
					"ɾ���ɹ�", Toast.LENGTH_SHORT).show();
			initArticleListView();
		} 
		else 
		{
			Toast.makeText(FavouriteActivity.this, 		// ��ʾ����ʧ��
					"ɾ��ʧ��", Toast.LENGTH_SHORT).show();

		}
        return super.onContextItemSelected(item);  
    }
	 
}
