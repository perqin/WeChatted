package com.perqin.wechatted.database;


import android.provider.BaseColumns;

public final class WeChattedContract {
    public static abstract class WeChatAccountEntry implements BaseColumns {
        public static final String TABLE = "wechat_account";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_UIN = "uin";

        public static final String QUERY_CREATE = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT NOT NULL)",
                TABLE, _ID, COLUMN_TITLE, COLUMN_UIN
        );
    }
}
