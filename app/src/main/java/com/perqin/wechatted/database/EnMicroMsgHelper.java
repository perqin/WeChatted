package com.perqin.wechatted.database;

import android.content.Context;
import android.util.Log;

import com.perqin.wechatted.bean.RecentConversation;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;

public class EnMicroMsgHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    private String mPassword;

    public EnMicroMsgHelper(Context context, File workingDir, String name, SQLiteDatabaseHook hook) {
        super(context, workingDir.getAbsolutePath() + "/" + name, null, VERSION, hook);
        SQLiteDatabase.loadLibs(context, workingDir);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.w("CIPHER", "onCreate is invoked");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w("CIPHER", "onUpgrade is invoked");
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public ArrayList<RecentConversation> getRecentConversations() {
        ArrayList<RecentConversation> result = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase(mPassword);
        Cursor cursor = database.query(EnMicroMsgContract.RconversationEntry.TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                result.add(RecentConversation.fromCursor(cursor));
                cursor.moveToNext();
            }
        }
        return result;
    }
}
