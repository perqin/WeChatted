package com.perqin.wechatted.database;

import android.content.Context;
import android.util.Log;

import com.perqin.wechatted.WeChattedApp;
import com.perqin.wechatted.bean.RecentConversation;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;
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

    public static boolean createDecryptedDatabase(File srcFile, File destFile, String password) {
        // Create new file in advanced
        boolean deleted = destFile.delete();
        if (!deleted) Log.i("FILE", "Fail to delete " + destFile.getAbsolutePath());
        try {
            if (!destFile.createNewFile()) Log.i("FILE", destFile.getAbsolutePath() + " can not be created");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        /*
        sqlcipher EnMicroMsg.db
        PRAGMA key = "key";
        PRAGMA cipher_use_hmac = off;
        PRAGMA kdf_iter = 4000;
        ATTACH DATABASE "decrypted_database.db" AS decrypted_database KEY "";
        SELECT sqlcipher_export("decrypted_database");
        DETACH DATABASE decrypted_database;
        */
        SQLiteDatabase.loadLibs(WeChattedApp.appContext);
        SQLiteDatabase database = SQLiteDatabase.openDatabase(srcFile.getAbsolutePath(), password, null, SQLiteDatabase.OPEN_READWRITE, new EnMicroMsgHook());
        database.rawExecSQL(String.format("ATTACH DATABASE '%s' AS decrypted_database KEY ''", destFile.getAbsolutePath()));
        database.rawExecSQL("SELECT sqlcipher_export('decrypted_database')");
        database.rawExecSQL("DETACH DATABASE decrypted_database");
        database.close();
        return true;
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

    private static class EnMicroMsgHook implements SQLiteDatabaseHook {
        @Override
        public void preKey(SQLiteDatabase sqLiteDatabase) {}

        @Override
        public void postKey(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.rawExecSQL("PRAGMA cipher_use_hmac = off;");
            sqLiteDatabase.rawExecSQL("PRAGMA kdf_iter = 4000;");
        }
    }
}
