package com.perqin.wechatted.database;

import android.content.Context;
import android.util.Log;

import com.perqin.wechatted.WeChattedApp;
import com.perqin.wechatted.adapter.bean.Conversation;
import com.perqin.wechatted.bean.RecentConversation;
import com.perqin.wechatted.util.LogTag;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EnMicroMsgHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    public EnMicroMsgHelper(Context context, File workingDir, String name, SQLiteDatabaseHook hook) {
        super(context, workingDir.getAbsolutePath() + "/" + name, null, VERSION, hook);
        SQLiteDatabase.loadLibs(context, workingDir);
    }

    public static EnMicroMsgHelper newInstance(Context context, File databaseFile) {
        return new EnMicroMsgHelper(context, databaseFile.getParentFile(), databaseFile.getName(), new EnMicroMsgHook());
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.w("CIPHER", "onCreate is invoked");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w("CIPHER", "onUpgrade is invoked");
    }

    /**
     * The following operations are executed:
     * <code>
     *     sqlcipher EnMicroMsg.db
     *     PRAGMA key = "key";
     *     PRAGMA cipher_use_hmac = off;
     *     PRAGMA kdf_iter = 4000;
     *     ATTACH DATABASE "decrypted_database.db" AS decrypted_database KEY "";
     *     SELECT sqlcipher_export("decrypted_database");
     *     DETACH DATABASE decrypted_database;
     * </code>
     */
    public static boolean createDecryptedDatabase(File srcFile, File destFile, String password) {
        // Ensure that dest directory is creatable
        if (destFile.getParentFile().exists() && destFile.getParentFile().isFile()) {
            Log.e(LogTag.TAG_FILE, "destFile parent should be a directory: " + destFile.getParent());
            return false;
        }
        // Create parent dest directory if not exists
        if (!destFile.getParentFile().exists() && !destFile.getParentFile().mkdirs()) {
            Log.e(LogTag.TAG_FILE, "Failed to recursively create destFile parent directory: " + destFile.getParent());
            return false;
        }
        // Remove destFile is exists
        if (destFile.exists() && !destFile.delete()) {
            Log.e(LogTag.TAG_FILE, "Failed to remove destFile: " + destFile.getAbsolutePath());
            return false;
        }
        // Create new file in advanced
        try {
            if (!destFile.createNewFile()) {
                Log.e(LogTag.TAG_FILE, "Failed to create empty destFile: " + destFile.getAbsolutePath());
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        SQLiteDatabase.loadLibs(WeChattedApp.appContext);
        EnMicroMsgHook hook = new EnMicroMsgHook();
        SQLiteDatabase database;
        // Export decrypted database
        database = SQLiteDatabase.openDatabase(srcFile.getAbsolutePath(), password, null, SQLiteDatabase.CREATE_IF_NECESSARY, hook);
        database.rawExecSQL(String.format("ATTACH DATABASE '%s' AS decrypted_database KEY '';", destFile.getAbsolutePath()));
        database.rawExecSQL("SELECT sqlcipher_export('decrypted_database');");
        database.rawExecSQL("DETACH DATABASE decrypted_database;");
        database.close();
        // Set version to 1
        database = SQLiteDatabase.openDatabase(destFile.getAbsolutePath(), "", null, SQLiteDatabase.OPEN_READWRITE, hook);
        database.setVersion(VERSION);
        database.close();

        return true;
    }

    public ArrayList<RecentConversation> getRecentConversations() {
        ArrayList<RecentConversation> result = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase("");
        Cursor cursor = database.query(EnMicroMsgContract.RconversationEntry.TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                result.add(RecentConversation.fromCursor(cursor));
                cursor.moveToNext();
            }
        }
        return result;
    }

    // Get list for conversation list adapter
    public ArrayList<Conversation> getAdapterConversations() {
        ArrayList<Conversation> conversations = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase("");
        Cursor cursor = database.rawQuery("SELECT * FROM rconversation", new String[] {});
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                conversations.add(Conversation.fromCursor(cursor));
                cursor.moveToNext();
            }
        }
        return conversations;
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
