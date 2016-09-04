package com.perqin.wechatted.database;

import android.content.Context;
import android.util.Log;

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

    public ArrayList<String> getRecentContacts() {
        // FIXME: Return real contacts
        SQLiteDatabase database = getReadableDatabase(mPassword);
        Cursor cursor = database.query("rcontact", null, null, null, null, null, null);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cursor.getColumnCount(); ++i) sb.append(cursor.getColumnName(i)).append('\t');
        Log.i("RCONTACT", sb.toString());

        if (!cursor.isAfterLast()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String stringBuilder = "Remark" + " = " + cursor.getString(2) + ", " +
                        "Nickname" + " = " + cursor.getString(4) + ", " +
                        "WeChatId" + " = " + cursor.getString(1);
                Log.i("RCONTACT", stringBuilder);
                cursor.moveToNext();
            }
        }
        return null;
    }
}
