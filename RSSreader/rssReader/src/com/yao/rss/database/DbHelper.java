package com.yao.rss.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * DbHelper--数据库操作辅助类
 * 
 * @author 姚富品
 * @date 2013-7-19
 * @version 3.0
 */
public class DbHelper extends SQLiteOpenHelper {
	//数据库名和版本号
	private final static String DATABASE_NAME = "rss_db"; 
	private final static int DATABASE_VERSION = 1;			//版本号			
	
	//数据库表名
	private final static String TABLE_NAME = "channel";
	private final static String ARTICLE_TABLE_NAME = "article";
	
	//rss源表字段
	public final static String RSS_ID = "_id";
	public final static String RSS_TITLE = "title";
	public final static String RSS_URL = "url";
	
	//文章表字段
	public final static String ARTICLE_ID = "_id";
	public final static String ARTICLE_TITLE = "article_title";
	public final static String ARTICLE_URL = "article_url";
	public final static String ARTICLE_PUBLISHEDDATE = "article_published_date";
	public final static String ARTICLE_DESCCRIPTION = "article_description";
	

	
	/*构造函数*/
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/*创建数据库*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 定义SQL语句
		String sql = "create table "+TABLE_NAME+" ("
		+RSS_ID+" integer primary key autoincrement, "
		+RSS_TITLE+" text, "
		+RSS_URL+" text )";
		String article_sql = "create table "+ARTICLE_TABLE_NAME+" ("
				+ARTICLE_ID+" integer primary key autoincrement, "
				+ARTICLE_TITLE+" text, "+ARTICLE_URL+" text, "
				+ARTICLE_PUBLISHEDDATE +" text, "+ARTICLE_DESCCRIPTION
				+" text )";
		// 直接执行 sql 语句
		db.execSQL(sql);
		db.execSQL(article_sql);
	}

	/*更新数据库*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 暂无操作
	}

	/**删除rss源
	 * @param id
	 * _id字段
	 * */
	public boolean initTableChannel()
	{
		if(!(selectChannel().moveToNext()))
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			String[] title=new String[]{"房产焦点","港澳焦点","财经焦点","股票焦点","环球视野","理财焦点","人物动态","新车焦点","政策风向"};
			String[] url=new String[]{"http://news.baidu.com/n?cmd=1&class=housenews&tn=rss&sub=0",
					"http://news.baidu.com/n?cmd=1&class=gangaotai&tn=rss&sub=0",
					"http://news.baidu.com/n?cmd=1&class=finannews&tn=rss&sub=0",
					"http://news.baidu.com/n?cmd=1&class=stock&tn=rss&sub=0",
					"http://news.baidu.com/n?cmd=1&class=hqsy&tn=rss&sub=0",
					"http://news.baidu.com/n?cmd=1&class=money&tn=rss&sub=0",
					"http://news.baidu.com/n?cmd=1&class=rwdt&tn=rss&sub=0",
					"http://news.baidu.com/n?cmd=1&class=autobuy&tn=rss&sub=0",
					"http://news.baidu.com/n?cmd=4&class=zcfx&tn=rss"
					
					};
			for(int i=0;i<9;i++)
			{
				cv.put(RSS_TITLE,title[i]);
				cv.put(RSS_URL, url[i]);
			long rowid = db.insert(TABLE_NAME, null, cv);	
			}
			db.close();
			if((selectChannel().moveToNext()))
				return true;
		}
		return false;
	}
	
	/**查询频道表中的内容	
	  */
	public Cursor selectChannel(){
		// 实例化一个 SQLiteDatabase 对象
		SQLiteDatabase db = this.getReadableDatabase();				
		// 获取一个指向数据库的游标，用来查询数据库
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}
	
	/**查询文章表中的内容	
	  */
	public Cursor selectArticle()
	{
		SQLiteDatabase db = this.getReadableDatabase();				
		// 获取一个指向数据库的游标，用来查询数据库
		Cursor cursor = db.query(ARTICLE_TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}
	
	/**查询一篇文章	
	  */
	public Cursor selectOnewArticle(String id)
	{
		SQLiteDatabase db = this.getReadableDatabase();	
		Cursor cursor =db.rawQuery("select * from article_title where _id=?", new String[]{id});
		return cursor;
	}
	
	/**插入频道
	 */
	public long insertChannel(String title, String url)
	{
		// 实例化一个 SQLiteDatabase 对象
		SQLiteDatabase db = this.getWritableDatabase();
		/*
		 * 将需要修改的数据放在 ContentValues 对象中
		 * ContentValues 是以键值对形式储存数据，其中键是数据库的列名，值是列名对应的数据
		 * */ 
		ContentValues cv = new ContentValues();
		cv.put(RSS_TITLE, title);
		cv.put(RSS_URL, url);
		// insert()方法：插入数据，成功返回行数，否则返回-1
		long rowid = db.insert(TABLE_NAME, null, cv);	
		db.close();
		return rowid;
	}
	
	/**查询文章表中的内容	
	  */
	public long insertArticle(String article_title,String article_url,
			String article_published_date,String article_description)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(ARTICLE_TITLE, article_title);
		cv.put(ARTICLE_URL, article_url);
		cv.put(ARTICLE_PUBLISHEDDATE, article_published_date);
		cv.put(ARTICLE_DESCCRIPTION, article_description);
		long rowid = db.insert(ARTICLE_TABLE_NAME, null, cv);	
		db.close();
		return rowid;
	}
	
	/**删除频道表中的内容	
	  */
	public boolean deleteChannel(String id){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = RSS_ID+"=?";
		String[] whereValues = {id};
		// delete方法：根据条件删除数据，where表示删除的条件
		boolean is =  (db.delete(TABLE_NAME, where, whereValues) > 0);
		db.close();
		return is;
	}
	
	/**查询文章表中的内容	
	  */
	public boolean deleteArticle(String id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String where = ARTICLE_ID+"=?";
		String[] whereValues = {id};
		// delete方法：根据条件删除数据，where表示删除的条件
		boolean is =  (db.delete(ARTICLE_TABLE_NAME, where, whereValues) > 0);
		db.close();
		return is;
	}
	/**更新记事
	 * @param id
	 * _id字段
	 * */
	public int updateChannel(String id,String title, String url){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = RSS_ID+"=?";
		String[] whereValues = {id};
		ContentValues cv = new ContentValues();
		cv.put(RSS_TITLE, title);
		cv.put(RSS_URL, url);
		// update()方法：根据条件更新数据库，cv保存更新后的数据，where为更新条件
		int numRow = db.update(TABLE_NAME, cv, where, whereValues);
		db.close();
		return numRow;
	}
	
	/**查询文章表中的内容	
	  */
	public int updateArticle(String article_id,String article_title,String article_url, String article_published_date
			){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = ARTICLE_ID+"=?";
		String[] whereValues = {article_id};
		ContentValues cv = new ContentValues();
		cv.put(ARTICLE_TITLE, article_title);
		cv.put(ARTICLE_URL, article_url);
		int numRow = db.update(ARTICLE_TABLE_NAME, cv, where, whereValues);
		db.close();
		return numRow;
	}
}

