package com.yao.rss.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * DbHelper--���ݿ����������
 * 
 * @author Ҧ��Ʒ
 * @date 2013-7-19
 * @version 3.0
 */
public class DbHelper extends SQLiteOpenHelper {
	//���ݿ����Ͱ汾��
	private final static String DATABASE_NAME = "rss_db"; 
	private final static int DATABASE_VERSION = 1;			//�汾��			
	
	//���ݿ����
	private final static String TABLE_NAME = "channel";
	private final static String ARTICLE_TABLE_NAME = "article";
	
	//rssԴ���ֶ�
	public final static String RSS_ID = "_id";
	public final static String RSS_TITLE = "title";
	public final static String RSS_URL = "url";
	
	//���±��ֶ�
	public final static String ARTICLE_ID = "_id";
	public final static String ARTICLE_TITLE = "article_title";
	public final static String ARTICLE_URL = "article_url";
	public final static String ARTICLE_PUBLISHEDDATE = "article_published_date";
	public final static String ARTICLE_DESCCRIPTION = "article_description";
	

	
	/*���캯��*/
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/*�������ݿ�*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		// ����SQL���
		String sql = "create table "+TABLE_NAME+" ("
		+RSS_ID+" integer primary key autoincrement, "
		+RSS_TITLE+" text, "
		+RSS_URL+" text )";
		String article_sql = "create table "+ARTICLE_TABLE_NAME+" ("
				+ARTICLE_ID+" integer primary key autoincrement, "
				+ARTICLE_TITLE+" text, "+ARTICLE_URL+" text, "
				+ARTICLE_PUBLISHEDDATE +" text, "+ARTICLE_DESCCRIPTION
				+" text )";
		// ֱ��ִ�� sql ���
		db.execSQL(sql);
		db.execSQL(article_sql);
	}

	/*�������ݿ�*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// ���޲���
	}

	/**ɾ��rssԴ
	 * @param id
	 * _id�ֶ�
	 * */
	public boolean initTableChannel()
	{
		if(!(selectChannel().moveToNext()))
		{
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			String[] title=new String[]{"��������","�۰Ľ���","�ƾ�����","��Ʊ����","������Ұ","��ƽ���","���ﶯ̬","�³�����","���߷���"};
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
	
	/**��ѯƵ�����е�����	
	  */
	public Cursor selectChannel(){
		// ʵ����һ�� SQLiteDatabase ����
		SQLiteDatabase db = this.getReadableDatabase();				
		// ��ȡһ��ָ�����ݿ���α꣬������ѯ���ݿ�
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}
	
	/**��ѯ���±��е�����	
	  */
	public Cursor selectArticle()
	{
		SQLiteDatabase db = this.getReadableDatabase();				
		// ��ȡһ��ָ�����ݿ���α꣬������ѯ���ݿ�
		Cursor cursor = db.query(ARTICLE_TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}
	
	/**��ѯһƪ����	
	  */
	public Cursor selectOnewArticle(String id)
	{
		SQLiteDatabase db = this.getReadableDatabase();	
		Cursor cursor =db.rawQuery("select * from article_title where _id=?", new String[]{id});
		return cursor;
	}
	
	/**����Ƶ��
	 */
	public long insertChannel(String title, String url)
	{
		// ʵ����һ�� SQLiteDatabase ����
		SQLiteDatabase db = this.getWritableDatabase();
		/*
		 * ����Ҫ�޸ĵ����ݷ��� ContentValues ������
		 * ContentValues ���Լ�ֵ����ʽ�������ݣ����м������ݿ��������ֵ��������Ӧ������
		 * */ 
		ContentValues cv = new ContentValues();
		cv.put(RSS_TITLE, title);
		cv.put(RSS_URL, url);
		// insert()�������������ݣ��ɹ��������������򷵻�-1
		long rowid = db.insert(TABLE_NAME, null, cv);	
		db.close();
		return rowid;
	}
	
	/**��ѯ���±��е�����	
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
	
	/**ɾ��Ƶ�����е�����	
	  */
	public boolean deleteChannel(String id){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = RSS_ID+"=?";
		String[] whereValues = {id};
		// delete��������������ɾ�����ݣ�where��ʾɾ��������
		boolean is =  (db.delete(TABLE_NAME, where, whereValues) > 0);
		db.close();
		return is;
	}
	
	/**��ѯ���±��е�����	
	  */
	public boolean deleteArticle(String id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String where = ARTICLE_ID+"=?";
		String[] whereValues = {id};
		// delete��������������ɾ�����ݣ�where��ʾɾ��������
		boolean is =  (db.delete(ARTICLE_TABLE_NAME, where, whereValues) > 0);
		db.close();
		return is;
	}
	/**���¼���
	 * @param id
	 * _id�ֶ�
	 * */
	public int updateChannel(String id,String title, String url){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = RSS_ID+"=?";
		String[] whereValues = {id};
		ContentValues cv = new ContentValues();
		cv.put(RSS_TITLE, title);
		cv.put(RSS_URL, url);
		// update()���������������������ݿ⣬cv������º�����ݣ�whereΪ��������
		int numRow = db.update(TABLE_NAME, cv, where, whereValues);
		db.close();
		return numRow;
	}
	
	/**��ѯ���±��е�����	
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

