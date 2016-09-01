package com.perqin.wechatted.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;

import com.perqin.wechatted.R;
import com.perqin.wechatted.bean.TaskInfo;
import com.perqin.wechatted.fragment.ExtractOptionsFragment;
import com.perqin.wechatted.fragment.ExtractReadingFragment;
import com.perqin.wechatted.fragment.ExtractWritingFragment;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class ExtractActivity extends AppCompatActivity implements ExtractReadingFragment.OnExtractReadingInteractionListener {
    private ExtractReadingFragment mReadingFragment;
    private ExtractOptionsFragment mOptionsFragment;
    private ExtractWritingFragment mWritingFragment;

    private boolean taskRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);

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
    public void onCanStart() {
        if (!taskRun) {
            new ExtractReadingSequenceTask(ExtractReadingSequenceTask.TASK_0_REQUEST_ROOT).execute();
            taskRun = true;
        }
    }

    private class ExtractReadingSequenceTask extends AsyncTask<Void, Integer, Boolean> {
        public static final int TASK_0_REQUEST_ROOT = 0;
        public static final int TASK_1_READ_IMEI = 1;
        public static final int TASK_2_READ_UIN = 2;
        public static final int TASK_3_COPY_HISTORY = 3;
        public static final int TASK_4_READ_HISTORY = 4;

//        private Process mSuProcess;
//        DataInputStream inputStream;
//        DataOutputStream outputStream;
        private SparseArray<SequenceTask> mTasks = new SparseArray<>();
        private int startFrom;
        private String lastError;
        private String currentUser;
        private String imei;
        private String uin;

        public ExtractReadingSequenceTask(int startFrom) {
            this.startFrom = startFrom;
            if (startFrom < TASK_0_REQUEST_ROOT || startFrom > TASK_4_READ_HISTORY) return;
            if (startFrom <= TASK_0_REQUEST_ROOT)
                mTasks.append(TASK_0_REQUEST_ROOT, new RequestRootTask());
            if (startFrom <= TASK_1_READ_IMEI)
                mTasks.append(TASK_1_READ_IMEI, new ReadImeiTask());
            if (startFrom <= TASK_2_READ_UIN)
                mTasks.append(TASK_2_READ_UIN, new ReadUinTask());
            if (startFrom <= TASK_3_COPY_HISTORY)
                mTasks.append(TASK_3_COPY_HISTORY, new CopyHistoryTask());
            if (startFrom <= TASK_4_READ_HISTORY)
                mTasks.append(TASK_4_READ_HISTORY, new ReadHistoryTask());
//            try {
//                mSuProcess = Runtime.getRuntime().exec("ls", null, new File("/"));
//                inputStream = new DataInputStream(mSuProcess.getInputStream());
//                outputStream = new DataOutputStream(mSuProcess.getOutputStream());
//                String line = inputStream.readLine();
//                Log.d("SHELL", line);
//                outputStream.writeBytes("id\n");
//                outputStream.flush();
//                shellReader = new BufferedReader(new InputStreamReader(mSuProcess.getInputStream()));
//                shellWriter = new BufferedWriter(new OutputStreamWriter(mSuProcess.getOutputStream()));
//                String line = shellReader.readLine();
//                Log.d("SHELL", line);
//                shellWriter.write("id\n");
//                shellWriter.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            for (int step = startFrom; step <= TASK_4_READ_HISTORY; ++step) {
                publishProgress(step, TaskInfo.RUNNING);
                if (mTasks.get(step).execute()) {
                    publishProgress(step, TaskInfo.SUCCEED);
                } else {
                    publishProgress(step, TaskInfo.FAIL);
                    return false;
                }
            }
            return true;
        }

        @Override
        protected final void onProgressUpdate(Integer... values) {
            mReadingFragment.setState(values[0], values[1]);
        }

        @Override
        protected void onPostExecute(Boolean succeed) {
            if (succeed) {
                mReadingFragment.hideErrorMessage();
            } else {
                mReadingFragment.showErrorMessage(lastError);
            }
        }

        private class RequestRootTask implements SequenceTask {
            @Override
            public boolean execute() {
//                try {
//                    shellWriter.write("id\n");
//                    shellWriter.flush();
//                    String idInfo = shellReader.readLine();
//                    String uid = idInfo.split(" ")[0];
//                    String username = uid.substring(uid.indexOf('('));
//                    username = username.substring(1, username.length() - 1);
//                    Log.d("SHELL", username);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return false;
//                }
                String idInfo = Shell.SH.run("id").get(0);
                String uid = idInfo.split(" ")[0];
                String username = uid.substring(uid.indexOf('('));
                username = username.substring(1, username.length() - 1);
                Log.i("SHELL", username);
                currentUser = username;
                boolean succeed = Shell.SU.available();
                if (!succeed) {
                    lastError = "You must root your device";
                    return false;
                }
                return true;
            }
        }

        private class ReadImeiTask implements SequenceTask {
            @SuppressLint("HardwareIds")
            @Override
            public boolean execute() {
                imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                Log.i("IMEI", imei);
                boolean succeed = imei != null;
                if (!succeed) {
                    lastError = "Cannot read IMEI";
                    return false;
                }
                return true;
            }
        }

        private class ReadUinTask implements SequenceTask {
            @Override
            public boolean execute() {
                List<String> contentInUin = Shell.SU.run("cat /data/data/com.tencent.mm/shared_prefs/system_config_prefs.xml");
                for (String line : contentInUin) {
                    if (line.contains("default_uin")) {
                        String tmp = line.substring(line.indexOf("value") + 7);
                        uin = tmp.substring(0, tmp.indexOf('"'));
                        Log.i("UIN", uin);
                        break;
                    }
                }
                boolean succeed = uin != null;
                if (!succeed) {
                    lastError = "Cannot read UIN. You have to login WeChat";
                    return false;
                }
                return true;
            }
        }

        private class CopyHistoryTask implements SequenceTask {
            @Override
            public boolean execute() {
                return false;
            }
        }

        private class ReadHistoryTask implements SequenceTask {
            @Override
            public boolean execute() {
                return false;
            }
        }
    }

    private interface SequenceTask {
        boolean execute();
    }
}
