package com.yao.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;// 定义数据库版本号
    private static final String DBNAME = "notes.db";// 定义数据库名

    public DBOpenHelper(Context context) {// 定义构造函数
        super(context, DBNAME, null, VERSION);// 重写基类的构造函数
    }

    @Override
    public void onCreate(SQLiteDatabase db) {// 创建数据库
        db.execSQL("create table tb_flag (_id integer primary key,title varchar(50),flag varchar(200))");// 创建便签信息表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {// 覆写基类的onUpgrade方法，以便数据库版本更新

    }
}
