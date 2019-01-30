package com.yao.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yao.model.Tb_flag;

public class FlagDAO 
{
    private DBOpenHelper helper;// ����DBOpenHelper����
    public SQLiteDatabase db;// ����SQLiteDatabase����
    Cursor cursor;

    public FlagDAO(Context context)// ���幹�캯��
    {
        helper = new DBOpenHelper(context);// ��ʼ��DBOpenHelper����
    }   
    /* ��ӱ�ǩ��Ϣ*/
    public void add(Tb_flag tb_flag) 
    {
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        db.execSQL("insert into tb_flag (_id,title,flag) values (?,?,?)", new Object[] { tb_flag.getid(),tb_flag.getTitle(), tb_flag.getFlag() });
       //close();
    }

    /* ���±�ǩ��Ϣ*/
    public void update(Tb_flag tb_flag) {
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����   
        ContentValues updateValues=new ContentValues();
        updateValues.put("flag", tb_flag.getFlag());
        updateValues.put("title", tb_flag.getTitle());
        //db.execSQL("update tb_flag set flag = ? where _id = ?", new Object[] { tb_flag.getFlag(), tb_flag.getid() });// ִ���޸ı�ǩ��Ϣ����
        db.update("tb_flag", updateValues, "_id"+"="+tb_flag.getid(), null);       
    }

    public String findTitle(int id) {
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        // ��ȡ���б�ǩ��Ϣ
        cursor = db.rawQuery("select flag from tb_flag where _id = ?", new String[] { String.valueOf(id) });// ���ݱ�Ų��ұ�ǩ��Ϣ�����洢��Cursor���� 
        if (cursor.moveToNext()) 
        {
        	return cursor.getString(cursor.getColumnIndex("flag"));
        }     
        return null;// ���ؼ���
    }
    
    /* �h����ǩ��Ϣ*/
    public void detele(int id) 
    {
        db = helper.getWritableDatabase();       
        db.execSQL("delete from tb_flag where _id = ?", new Object[] {id});
        for (int i = id+1; i <=getMaxId(); i++) 
        {// ����Ҫɾ����id����
        	db.execSQL("update tb_flag set _id = ? where _id = ?", new Object[] {i-1,i});
        }
    }

    /*��ȡ��ǩ��Ϣ */
    public List<Tb_flag> getScrollData(int start, int count) {
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        List<Tb_flag> lisTb_flags = new ArrayList<Tb_flag>();// �������϶���
        // ��ȡ���б�ǩ��Ϣ
        cursor = db.rawQuery("select * from tb_flag limit ?,?", new String[] { String.valueOf(start), String.valueOf(count) });
        while (cursor.moveToNext()) {// �������еı�ǩ��Ϣ      
            // ���������ı�ǩ��Ϣ��ӵ�������
            lisTb_flags.add(new Tb_flag(cursor.getInt(cursor.getColumnIndex("_id")),cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("flag"))));
        }     
        return lisTb_flags;// ���ؼ���
    }

    /*��ȡ�ܼ�¼��*/
    public long getCount() {
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        cursor = db.rawQuery("select count(_id) from tb_flag", null);// ��ȡ��ǩ��Ϣ�ļ�¼��
        if (cursor.moveToNext()) {// �ж�Cursor���Ƿ�������          
            int maxid=cursor.getInt(0);       	
            return maxid;// �����ܼ�¼��
        }    
        return 0;// ���û�����ݣ��򷵻�0
    }

    /*��ȡ��ǩ����� */
    public int getMaxId() {
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
        cursor = db.rawQuery("select max(_id) from tb_flag", null);// ��ȡ��ǩ��Ϣ���е������
        while (cursor.moveToLast()) {// ����Cursor�е����һ������
            int maxid=cursor.getInt(0);
            return maxid;// ��ȡ���ʵ������ݣ��������
        }
        return 0;// ���û�����ݣ��򷵻�0
    }
    public void close()
    {
        db = helper.getWritableDatabase();// ��ʼ��SQLiteDatabase����
    	if(db!=null)
    	{
    		db.close();
    		db=null;
    	} 
    	if (!cursor.isClosed()) 
    	{  
            cursor.close();  
        }
    }
}
