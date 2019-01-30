package com.yao.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;// �������ݿ�汾��
    private static final String DBNAME = "notes.db";// �������ݿ���

    public DBOpenHelper(Context context) {// ���幹�캯��
        super(context, DBNAME, null, VERSION);// ��д����Ĺ��캯��
    }

    @Override
    public void onCreate(SQLiteDatabase db) {// �������ݿ�
        db.execSQL("create table tb_flag (_id integer primary key,title varchar(50),flag varchar(200))");// ������ǩ��Ϣ��
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {// ��д�����onUpgrade�������Ա����ݿ�汾����

    }
}
