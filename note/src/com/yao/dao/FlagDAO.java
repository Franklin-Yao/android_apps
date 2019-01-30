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
    private DBOpenHelper helper;// 创建DBOpenHelper对象
    public SQLiteDatabase db;// 创建SQLiteDatabase对象
    Cursor cursor;

    public FlagDAO(Context context)// 定义构造函数
    {
        helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
    }   
    /* 添加便签信息*/
    public void add(Tb_flag tb_flag) 
    {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        db.execSQL("insert into tb_flag (_id,title,flag) values (?,?,?)", new Object[] { tb_flag.getid(),tb_flag.getTitle(), tb_flag.getFlag() });
       //close();
    }

    /* 更新便签信息*/
    public void update(Tb_flag tb_flag) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象   
        ContentValues updateValues=new ContentValues();
        updateValues.put("flag", tb_flag.getFlag());
        updateValues.put("title", tb_flag.getTitle());
        //db.execSQL("update tb_flag set flag = ? where _id = ?", new Object[] { tb_flag.getFlag(), tb_flag.getid() });// 执行修改便签信息操作
        db.update("tb_flag", updateValues, "_id"+"="+tb_flag.getid(), null);       
    }

    public String findTitle(int id) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        // 获取所有便签信息
        cursor = db.rawQuery("select flag from tb_flag where _id = ?", new String[] { String.valueOf(id) });// 根据编号查找便签信息，并存储到Cursor类中 
        if (cursor.moveToNext()) 
        {
        	return cursor.getString(cursor.getColumnIndex("flag"));
        }     
        return null;// 返回集合
    }
    
    /* 刪除便签信息*/
    public void detele(int id) 
    {
        db = helper.getWritableDatabase();       
        db.execSQL("delete from tb_flag where _id = ?", new Object[] {id});
        for (int i = id+1; i <=getMaxId(); i++) 
        {// 遍历要删除的id集合
        	db.execSQL("update tb_flag set _id = ? where _id = ?", new Object[] {i-1,i});
        }
    }

    /*获取便签信息 */
    public List<Tb_flag> getScrollData(int start, int count) {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        List<Tb_flag> lisTb_flags = new ArrayList<Tb_flag>();// 创建集合对象
        // 获取所有便签信息
        cursor = db.rawQuery("select * from tb_flag limit ?,?", new String[] { String.valueOf(start), String.valueOf(count) });
        while (cursor.moveToNext()) {// 遍历所有的便签信息      
            // 将遍历到的便签信息添加到集合中
            lisTb_flags.add(new Tb_flag(cursor.getInt(cursor.getColumnIndex("_id")),cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("flag"))));
        }     
        return lisTb_flags;// 返回集合
    }

    /*获取总记录数*/
    public long getCount() {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        cursor = db.rawQuery("select count(_id) from tb_flag", null);// 获取便签信息的记录数
        if (cursor.moveToNext()) {// 判断Cursor中是否有数据          
            int maxid=cursor.getInt(0);       	
            return maxid;// 返回总记录数
        }    
        return 0;// 如果没有数据，则返回0
    }

    /*获取便签最大编号 */
    public int getMaxId() {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
        cursor = db.rawQuery("select max(_id) from tb_flag", null);// 获取便签信息表中的最大编号
        while (cursor.moveToLast()) {// 访问Cursor中的最后一条数据
            int maxid=cursor.getInt(0);
            return maxid;// 获取访问到的数据，即最大编号
        }
        return 0;// 如果没有数据，则返回0
    }
    public void close()
    {
        db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
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
