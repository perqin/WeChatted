package com.perqin.wechatted.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.perqin.wechatted.R;
import com.perqin.wechatted.bean.WeChatAccount;
import com.perqin.wechatted.database.EnMicroMsgHelper;
import com.perqin.wechatted.database.WeChattedHelper;
import com.perqin.wechatted.fragment.ExtractAccountFragment;
import com.perqin.wechatted.fragment.ExtractConversationsFragment;
import com.perqin.wechatted.fragment.ExtractOptionsFragment;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import eu.chainfire.libsuperuser.Shell;

public class ExtractActivity
        extends AppCompatActivity
        implements ExtractAccountFragment.OnExtractAccountInteractionListener, ExtractConversationsFragment.OnExtractConversationsInteractionListener {
    private ExtractAccountFragment mChooseAccountFragment;
    private ExtractConversationsFragment mConversationsFragment;
    private ExtractOptionsFragment mOptionsFragment;

    private ReadHistoryTask mTask;

    private EnMicroMsgHelper mEnMicroMsgHelper;
    private File mHistoryDbsDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);

        mHistoryDbsDir = new File(getFilesDir(), "copiedDbs");
        Log.i("DIR", mHistoryDbsDir.getAbsolutePath());
        if (!mHistoryDbsDir.isDirectory() && !mHistoryDbsDir.mkdirs()) {
            Toast.makeText(this, "Fail to create folder", Toast.LENGTH_SHORT).show();
            finish();
        }

        mEnMicroMsgHelper = new EnMicroMsgHelper(this, mHistoryDbsDir, "EnMicroMsg.db", new SqlCipherHook());

        mChooseAccountFragment = ExtractAccountFragment.newInstance();
        mConversationsFragment = ExtractConversationsFragment.newInstance();
        mOptionsFragment = ExtractOptionsFragment.newInstance("", "");

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mChooseAccountFragment)
                    .commit();
        }
    }

    // Implements ExtractAccountFragment.OnExtractAccountInteractionListener
    @Override
    public List<WeChatAccount> getWeChatAccounts() {
        return WeChattedHelper.getInstance(this).getWeChatAccounts();
    }

    // Implements ExtractAccountFragment.OnExtractAccountInteractionListener
    @Override
    public String readCurrentUin() {
        if (Shell.SU.available()) {
            Pattern pattern = Pattern.compile("default_uin.+?value=\"([-0-9]+)");
            List<String> lines = Shell.SU.run("cat /data/data/com.tencent.mm/shared_prefs/system_config_prefs.xml");
            for (String line : lines) {
                if (pattern.matcher(line).matches()) {
                    return pattern.matcher(line).group();
                }
            }
            Toast.makeText(this, getString(R.string.failed_to_read_uin), Toast.LENGTH_SHORT).show();
            return "";
        } else {
            Toast.makeText(this, getString(R.string.root_access_is_required_to_read_wechat_uin), Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    // Implements ExtractAccountFragment.OnExtractAccountInteractionListener
    @Override
    public void onManageAccountsButtonClick() {
        // TODO: manage accounts
    }

    // Implements ExtractAccountFragment.OnExtractAccountInteractionListener
    @Override
    public void onReadButtonClick(String uin) {
        @SuppressLint("HardwareIds") String imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        mConversationsFragment.setImeiAndUin(imei, uin);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mConversationsFragment)
                .addToBackStack(null)
                .commit();
    }

    // Implements ExtractConversationsFragment.OnExtractConversationsInteractionListener
    @Override
    public void startReading(String imei, String uin) {
        mTask = new ReadHistoryTask();
        mTask.execute(imei, uin);
    }

    private class ReadHistoryTask extends AsyncTask<String, Void, Boolean> {
        private String error;
        @Override
        protected Boolean doInBackground(String... params) {
            if (!Shell.SU.available()) {
                error = getString(R.string.root_access_is_required_to_read_wechat_history);
                return false;
            }
            // TODO
            return true;
        }

        @Override
        protected void onPostExecute(Boolean succeed) {
            if (succeed) {
                // TODO: Load list
            } else {
                Toast.makeText(ExtractActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        }
    }

//        private class RequestRootTask implements SequenceTask {
//            @Override
//            public boolean execute() {
//                String idInfo = Shell.SH.run("id").get(0);
//                String uid = idInfo.split(" ")[0];
//                String username = uid.substring(uid.indexOf('('));
//                username = username.substring(1, username.length() - 1);
//                Log.i("SHELL", username);
//                currentUser = username;
//                boolean succeed = Shell.SU.available();
//                if (!succeed) {
//                    lastError = "You must root your device";
//                    return false;
//                }
//                return true;
//            }
//        }
//        private class CopyHistoryTask implements SequenceTask {
//            @Override
//            public boolean execute() {
//                @SuppressLint("SdCardPath")
//                String src = "/data/data/com.tencent.mm/MicroMsg/" + new MD5("mm" + uin).getMD5edString() + "/EnMicroMsg.db";
//                String dest = mHistoryDbsDir.getAbsolutePath() + "/EnMicroMsg.db";
//                String cpCmd = "cp -f " + src + " " + dest;
//                String chownCmd = "chown " + currentUser + ":" + currentUser + " " + dest;
//                Log.i("CMD", cpCmd);
//                Log.i("CMD", chownCmd);
//                List<String> output = Shell.run("su", new String[] { cpCmd, chownCmd }, null, true);
//                boolean succeed = output.size() == 0;
//                if (!succeed) lastError = "Fail to copy the history database: " + output.get(0);
//                return succeed;
//            }
//        }

    private class SqlCipherHook implements SQLiteDatabaseHook {
        @Override
        public void preKey(SQLiteDatabase sqLiteDatabase) {}

        @Override
        public void postKey(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.rawExecSQL("PRAGMA cipher_use_hmac = off;");
            sqLiteDatabase.rawExecSQL("PRAGMA kdf_iter = 4000;");
        }
    }
}
