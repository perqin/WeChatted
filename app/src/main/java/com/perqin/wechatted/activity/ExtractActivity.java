package com.perqin.wechatted.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.perqin.wechatted.R;
import com.perqin.wechatted.bean.TaskInfo;
import com.perqin.wechatted.fragment.ExtractOptionsFragment;
import com.perqin.wechatted.fragment.ExtractReadingFragment;
import com.perqin.wechatted.fragment.ExtractWritingFragment;

public class ExtractActivity extends AppCompatActivity implements ExtractReadingFragment.OnExtractReadingInteractionListener {
    private ExtractReadingFragment mReadingFragment;
    private ExtractOptionsFragment mOptionsFragment;
    private ExtractWritingFragment mWritingFragment;

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
        new ExtractReadingSequenceTask(ExtractReadingSequenceTask.TASK_0_REQUEST_ROOT).execute();
    }

    private class ExtractReadingSequenceTask extends AsyncTask<Void, Integer, Boolean> {
        public static final int TASK_0_REQUEST_ROOT = 0;
        public static final int TASK_1_READ_IMEI = 1;
        public static final int TASK_2_READ_UIN = 2;
        public static final int TASK_3_COPY_HISTORY = 3;
        public static final int TASK_4_READ_HISTORY = 4;

        private Process mSuProcess;
        private SparseArray<SequenceTask> mTasks = new SparseArray<>();
        private int startFrom;

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
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }

        private class RequestRootTask implements SequenceTask {
            @Override
            public boolean execute() {
                return true;
            }
        }

        private class ReadImeiTask implements SequenceTask {
            @Override
            public boolean execute() {
                return true;
            }
        }

        private class ReadUinTask implements SequenceTask {
            @Override
            public boolean execute() {
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
