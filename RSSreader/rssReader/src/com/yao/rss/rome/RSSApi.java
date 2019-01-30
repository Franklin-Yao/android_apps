package com.yao.rss.rome;

import java.net.URL;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedOutput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

/**
 * DbHelper--rss源解析辅助类
 * 
 * @author 姚富品
 * @date 2013-7-19
 * @version 3.0
 */
public class RSSApi 
{
	public SyndFeed getFeed(String str)
	{
		SyndFeed feed=null;
		SyndFeedInput input=new SyndFeedInput();
		try
		{
			URL feedUrl=new URL(str);
			feed=input.build(new XmlReader(feedUrl));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return feed;
	}
	public void saveFeed(SyndFeed feed)
	{
			SyndFeedOutput output=new SyndFeedOutput();
			try
			{
				String feedStr=output.outputString(feed);
			}
			catch(FeedException e)
			{
				e.printStackTrace();
			}
			
	}

}
