package com.perqin.wechatted.bean;


import android.database.Cursor;

import com.perqin.wechatted.database.WeChattedContract;

public class WeChatAccount {
    private String title;
    private String uin;

    public static WeChatAccount fromCursor(Cursor cursor) {
        WeChatAccount account = new WeChatAccount();
        account.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(WeChattedContract.WeChatAccountEntry.COLUMN_TITLE)));
        account.setUin(cursor.getString(cursor.getColumnIndexOrThrow(WeChattedContract.WeChatAccountEntry.COLUMN_UIN)));
        return account;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUin() {
        return uin;
    }

    public void setUin(String uin) {
        this.uin = uin;
    }
}
