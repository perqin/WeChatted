package com.perqin.wechatted.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class WeChatAccountHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "WeChatAccount.db";
    private static WeChatAccountHelper mInstance;

    public static WeChatAccountHelper getInstance(Context context) {
        if (mInstance == null)
            mInstance = new WeChatAccountHelper(context, DB_NAME, null, VERSION);
        return mInstance;
    }

    private WeChatAccountHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(WeChatAccountContract.WeChatAccountEntry.QUERY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
