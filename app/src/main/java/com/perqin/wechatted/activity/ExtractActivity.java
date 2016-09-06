package com.perqin.wechatted.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.perqin.wechatted.R;
import com.perqin.wechatted.bean.WeChatAccount;
import com.perqin.wechatted.database.EnMicroMsgHelper;
import com.perqin.wechatted.fragment.ExtractOptionsFragment;
import com.perqin.wechatted.fragment.ExtractReadingFragment;
import com.perqin.wechatted.fragment.ExtractWritingFragment;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExtractActivity extends AppCompatActivity implements ExtractReadingFragment.OnExtractReadingInteractionListener {
    private ExtractReadingFragment mReadingFragment;
    private ExtractOptionsFragment mOptionsFragment;
    private ExtractWritingFragment mWritingFragment;

    private int currentStep = 0;

    private EnMicroMsgHelper mEnMicroMsgHelper;
    private File mDatabasesDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);

        mDatabasesDir = new File(getFilesDir(), "copiedDbs");
        Log.i("DIR", mDatabasesDir.getAbsolutePath());
        if (!mDatabasesDir.isDirectory() && !mDatabasesDir.mkdirs()) {
            Toast.makeText(this, "Fail to create folder", Toast.LENGTH_SHORT).show();
            finish();
        }

        mEnMicroMsgHelper = new EnMicroMsgHelper(this, mDatabasesDir, "EnMicroMsg.db", new SqlCipherHook());

        mReadingFragment = ExtractReadingFragment.newInstance();
        mOptionsFragment = ExtractOptionsFragment.newInstance("", "");
        mWritingFragment = ExtractWritingFragment.newInstance("", "");

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mReadingFragment)
                    .commit();
        }
    }

    @Override
    public List<WeChatAccount> getWeChatAccounts() {
        ArrayList<WeChatAccount> accounts = new ArrayList<>();
        WeChatAccount account = new WeChatAccount();
        account.setTitle("Title");
        accounts.add(account);
        return accounts;
    }

//    @Override
//    public void onCanStart() {
//        if (!taskRun) {
//        new ExtractReadingSequenceTask(ExtractReadingSequenceTask.TASK_0_REQUEST_ROOT).execute();
//            taskRun = true;
//        }
//    }
//
//    @Override
//    public void onRetryClick() {
//        mReadingFragment.hideErrorMessage();
//        new ExtractReadingSequenceTask(currentStep).execute();
//    }

//    private class ExtractReadingSequenceTask extends AsyncTask<Void, Integer, Boolean> {
//        public static final int TASK_0_REQUEST_ROOT = 0;
//        public static final int TASK_1_READ_IMEI = 1;
//        public static final int TASK_2_READ_UIN = 2;
//        public static final int TASK_3_COPY_HISTORY = 3;
//        public static final int TASK_4_READ_HISTORY = 4;
//
//        private SparseArray<SequenceTask> mTasks = new SparseArray<>();
//        private int startFrom;
//        private String lastError;
//        private String currentUser;
//        private String imei;
//        private String uin;
//
//        ExtractReadingSequenceTask(int startFrom) {
//            this.startFrom = startFrom;
//            if (startFrom < TASK_0_REQUEST_ROOT || startFrom > TASK_4_READ_HISTORY) return;
//            if (startFrom <= TASK_0_REQUEST_ROOT)
//                mTasks.append(TASK_0_REQUEST_ROOT, new RequestRootTask());
//            if (startFrom <= TASK_1_READ_IMEI)
//                mTasks.append(TASK_1_READ_IMEI, new ReadImeiTask());
//            if (startFrom <= TASK_2_READ_UIN)
//                mTasks.append(TASK_2_READ_UIN, new ReadUinTask());
//            if (startFrom <= TASK_3_COPY_HISTORY)
//                mTasks.append(TASK_3_COPY_HISTORY, new CopyHistoryTask());
//            if (startFrom <= TASK_4_READ_HISTORY)
//                mTasks.append(TASK_4_READ_HISTORY, new ReadHistoryTask());
////            try {
////                mSuProcess = Runtime.getRuntime().exec("ls", null, new File("/"));
////                inputStream = new DataInputStream(mSuProcess.getInputStream());
////                outputStream = new DataOutputStream(mSuProcess.getOutputStream());
////                String line = inputStream.readLine();
////                Log.d("SHELL", line);
////                outputStream.writeBytes("id\n");
////                outputStream.flush();
////                shellReader = new BufferedReader(new InputStreamReader(mSuProcess.getInputStream()));
////                shellWriter = new BufferedWriter(new OutputStreamWriter(mSuProcess.getOutputStream()));
////                String line = shellReader.readLine();
////                Log.d("SHELL", line);
////                shellWriter.write("id\n");
////                shellWriter.flush();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            for (int step = startFrom; step <= TASK_4_READ_HISTORY; ++step) {
//                currentStep = step;
//                publishProgress(step, TaskInfo.RUNNING);
//                if (mTasks.get(step).execute()) {
//                    publishProgress(step, TaskInfo.SUCCEED);
//                } else {
//                    publishProgress(step, TaskInfo.FAIL);
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        @Override
//        protected final void onProgressUpdate(Integer... values) {
////            mReadingFragment.setState(values[0], values[1]);
//        }
//
//        @Override
//        protected void onPostExecute(Boolean succeed) {
//            if (succeed) {
////                mReadingFragment.hideErrorMessage();
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.container, mOptionsFragment)
//                        .commit();
//            } else {
////                mReadingFragment.showErrorMessage(lastError);
//            }
//        }
//
//        private class RequestRootTask implements SequenceTask {
//            @Override
//            public boolean execute() {
////                try {
////                    shellWriter.write("id\n");
////                    shellWriter.flush();
////                    String idInfo = shellReader.readLine();
////                    String uid = idInfo.split(" ")[0];
////                    String username = uid.substring(uid.indexOf('('));
////                    username = username.substring(1, username.length() - 1);
////                    Log.d("SHELL", username);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                    return false;
////                }
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
//
//        private class ReadImeiTask implements SequenceTask {
//            @SuppressLint("HardwareIds")
//            @Override
//            public boolean execute() {
//                imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//                Log.i("IMEI", imei);
//                boolean succeed = imei != null;
//                if (!succeed) {
//                    lastError = "Cannot read IMEI";
//                    return false;
//                }
//                return true;
//            }
//        }
//
//        private class ReadUinTask implements SequenceTask {
//            @Override
//            public boolean execute() {
//                List<String> contentInUin = Shell.SU.run("cat /data/data/com.tencent.mm/shared_prefs/system_config_prefs.xml");
//                for (String line : contentInUin) {
//                    if (line.contains("default_uin")) {
//                        String tmp = line.substring(line.indexOf("value") + 7);
//                        uin = tmp.substring(0, tmp.indexOf('"'));
//                        Log.i("UIN", uin);
//                        break;
//                    }
//                }
//                boolean succeed = uin != null;
//                if (!succeed) {
//                    lastError = "Cannot read UIN. You have to login WeChat";
//                    return false;
//                }
//                return true;
//            }
//        }
//
//        private class CopyHistoryTask implements SequenceTask {
//            @Override
//            public boolean execute() {
//                @SuppressLint("SdCardPath")
//                String src = "/data/data/com.tencent.mm/MicroMsg/" + new MD5("mm" + uin).getMD5edString() + "/EnMicroMsg.db";
//                String dest = mDatabasesDir.getAbsolutePath() + "/EnMicroMsg.db";
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
//
//        private class ReadHistoryTask implements SequenceTask {
//            @Override
//            public boolean execute() {
//                return true;
//            }
//        }
//    }

    private class SqlCipherHook implements SQLiteDatabaseHook {
        @Override
        public void preKey(SQLiteDatabase sqLiteDatabase) {}

        @Override
        public void postKey(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.rawExecSQL("PRAGMA cipher_use_hmac = off;");
            sqLiteDatabase.rawExecSQL("PRAGMA kdf_iter = 4000;");
        }
    }

    private interface SequenceTask {
        boolean execute();
    }
}
