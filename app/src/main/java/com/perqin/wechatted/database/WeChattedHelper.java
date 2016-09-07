package com.perqin.wechatted.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.perqin.wechatted.bean.WeChatAccount;

import java.util.ArrayList;


public class WeChattedHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "WeChatted.db";
    private static WeChattedHelper mInstance;

    public static WeChattedHelper getInstance(Context context) {
        if (mInstance == null)
            mInstance = new WeChattedHelper(context, DB_NAME, null, VERSION);
        return mInstance;
    }

    private WeChattedHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(WeChattedContract.WeChatAccountEntry.QUERY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public ArrayList<WeChatAccount> getWeChatAccounts() {
        ArrayList<WeChatAccount> accounts = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(WeChattedContract.WeChatAccountEntry.TABLE, new String[] {
                WeChattedContract.WeChatAccountEntry.COLUMN_TITLE, WeChattedContract.WeChatAccountEntry.COLUMN_UIN
        }, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            accounts.add(WeChatAccount.fromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return accounts;
    }
}
