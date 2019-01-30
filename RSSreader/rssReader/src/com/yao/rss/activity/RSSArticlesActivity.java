package com.yao.rss.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yao.rss.activity.MainActivity.MyHandler;
import com.yao.rss.adapter.ArticleListAdapter;
import com.yao.rssReader.R;

/**
 * RSSArticlesActivity--��ʾ�����б����
 * 
 * @author Ҧ��Ʒ
 * @date 2013-7-19
 * @version 3.0
 */
public class RSSArticlesActivity extends Activity
{
	ArticleListAdapter mArticleListAdapter;//�����б�������
	String mFeedUrl=null;	//rssԴ��ַ	
	String mTitle=null;		//rssԴ����
	protected void onCreate(Bundle savedInstanceState) 
	{

		TextView mRSSTitle;//����
		ListView  mListViewEntries;//�����б�
		ImageButton mImageBtnClose;//�˳���ť
		Intent mIntent;//��MainActivity�õ�����ͼ
		List mEntries;//��MainActivity�õ��������б�
		
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.common_listview);
	    mRSSTitle=(TextView)findViewById(R.id.tv_task_name);
		mImageBtnClose = (ImageButton) findViewById(R.id.ibtn_close);
		mIntent=getIntent();
		mFeedUrl=mIntent.getStringExtra(MainActivity.CHANNEL_URL);				
        mTitle = mIntent.getStringExtra(MainActivity.CHANNEL_TITLE);
        
        //�ȵõ�ArrayList���ٵõ������б�
        ArrayList list =mIntent.getExtras().getParcelableArrayList("list");
        mEntries= (List<Object>) list.get(0);
        if (mTitle!= null)
        { 
            mRSSTitle.setText(mTitle); 
        } 
        
		mListViewEntries=(ListView)findViewById(R.id.acticlelist);
		mArticleListAdapter=new ArticleListAdapter(this,mEntries);
		
		//�ж������Ƿ�����
		if(mArticleListAdapter.mNetState==0)
		{
			Intent it=new Intent(RSSArticlesActivity.this,MainActivity.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Toast.makeText(this,"�ף���������������", Toast.LENGTH_SHORT).show();
			startActivity(it); 
		}
		else
		{
			mListViewEntries.setAdapter(mArticleListAdapter);
		}
		
		//Ϊ�����б������ü���������������½���
		mListViewEntries.setOnItemClickListener(new OnItemClickListener()
		{
		
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				Intent it=new Intent(RSSArticlesActivity.this,ArticleActivity.class);
				it.putExtra("article_url",mArticleListAdapter.mMapUrl.get(arg2));
				it.putExtra("article_title",mArticleListAdapter.mMapTitle.get(arg2));
				it.putExtra("article_published_date", mArticleListAdapter.mMapPublishedDate.get(arg2));
				it.putExtra("article_description",mArticleListAdapter.mMapDescription.get(arg2));
				it.putExtra(MainActivity.CHANNEL_TITLE,mTitle);
				it.putExtra(MainActivity.CHANNEL_URL,mFeedUrl);
				startActivity(it);	

				
			}
	});
		
		//���˳������ü���
		View.OnClickListener mOnClickListener = new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				switch (v.getId()) 
				{
					case R.id.ibtn_close:
						Intent intent=new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						RSSArticlesActivity.this.startActivity(intent);
						System.exit(0);
					default:
						break;
				}
			}
		};
		mImageBtnClose.setOnClickListener(mOnClickListener);
	}
	

}
