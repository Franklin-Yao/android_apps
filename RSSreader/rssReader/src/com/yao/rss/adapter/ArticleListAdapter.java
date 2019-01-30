package com.yao.rss.adapter;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.yao.rssReader.R;

/**
 * AddChannelActivity--�����б����������
 * 
 * @author Ҧ��Ʒ
 * @date 2013-7-19
 * @version 3.0
 */
public class ArticleListAdapter extends BaseAdapter {
	public List mEntries;//�����б�
	public Context mContext;
	public int mNetState=1;//�����Ƿ�������־����RSSArticlesActivity���ж�
	//���б�position����ַ�����ļ���
	public Map<Integer,String> mMapUrl=new HashMap<Integer,String>();
	//���б�position��Title�����ļ���
	public Map<Integer,String> mMapTitle=new HashMap<Integer,String>();
	//���б�position��PublishedDate�����ļ���
	public Map<Integer,String> mMapPublishedDate=new HashMap<Integer,String>();
	//���б�position��Description�����ļ���
	public Map<Integer,String> mMapDescription=new HashMap<Integer,String>();
	
	public ArticleListAdapter(Context context,List entries)
	{
		this.mContext=context;
		this.mEntries=entries;
	}
	@Override
	public int getCount() {
		return mEntries.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mEntries.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflator=LayoutInflater.from(mContext);
		//�����б���Ĳ���
		View v=mInflator.inflate(R.layout.layout_rssarticles_item,null);
  
		TextView title;//���������ı���
		TextView publishedDate;//���·��������ı���
		TextView description_value;//���������ı���
		String url;//������ַ
		
		title=(TextView) v.findViewById(R.id.tv_item_title);
		publishedDate=(TextView) v.findViewById(R.id.tv_item_publisheddate);
		description_value=(TextView) v.findViewById(R.id.tv_item_description);
		
		SyndEntry entry =(SyndEntry)mEntries.get(position);
	    title.setText(entry.getTitle());
	    
	    //����ʱ���ʽ
	    String date=new SimpleDateFormat("yyyy��MM��dd�� HH:mm").
	    		format(entry.getPublishedDate()).toString();
	    publishedDate.setText(date);
        SyndContent description = entry.getDescription();  
		description_value.setText(description.getValue());
	    url=entry.getLink();
	    
	    mMapUrl.put(position, url);
	    mMapTitle.put(position, entry.getTitle());
	    mMapDescription.put(position, entry.getDescription().getValue());
	    mMapPublishedDate.put(position, date);
	    
		return v;
	}

}
