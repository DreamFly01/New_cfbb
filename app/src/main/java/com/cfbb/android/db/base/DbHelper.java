package com.cfbb.android.db.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MrCahang45 on 2016/3/23.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "yb.db";
    private static final int DATABASE_VERSION = 1;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //构建用户信息表
        //openEye 1 打开 -1关闭
        db.execSQL("CREATE TABLE IF NOT EXISTS " + UserInfoTable.TABLE_NAME
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + UserInfoTable.trueName + "  VARCHAR," + UserInfoTable.isAuth + " INTEGER," + UserInfoTable.mobile + " VARCHAR," + UserInfoTable.uid + " VARCHAR," + UserInfoTable.img + " VARCHAR," + UserInfoTable.gesturePassword + " VARCHAR," + UserInfoTable.openEye + " INTEGER," + UserInfoTable.openGesture + " INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
