package com.perqin.wechatted.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.perqin.wechatted.R;
import com.perqin.wechatted.adapter.ExtractConversationsRecyclerAdapter;
import com.perqin.wechatted.bean.RecentConversation;
import com.perqin.wechatted.database.EnMicroMsgHelper;
import com.perqin.wechatted.utils.MD5;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.chainfire.libsuperuser.Shell;

public class ExtractConversationsFragment extends Fragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ExtractConversationsRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;

    private OnExtractConversationsInteractionListener mListener;
    private String mImei;
    private String mUin;
    private ReadHistoryTask mTask;
    private EnMicroMsgHelper mEnMicroMsgHelper;
    private File mHistoryDbsDir;

    public ExtractConversationsFragment() {
        // Required empty public constructor
    }

    public static ExtractConversationsFragment newInstance() {
        return new ExtractConversationsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExtractConversationsInteractionListener) {
            mListener = (OnExtractConversationsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnExtractConversationsInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHistoryDbsDir = new File(getActivity().getFilesDir(), "copiedDbs");
        Log.i("DIR", mHistoryDbsDir.getAbsolutePath());
        if (!mHistoryDbsDir.isDirectory() && !mHistoryDbsDir.mkdirs()) {
            Toast.makeText(getActivity(), "Fail to create folder", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        mEnMicroMsgHelper = new EnMicroMsgHelper(getActivity(), mHistoryDbsDir, "EnMicroMsg.db", new SqlCipherHook());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extract_conversations, container, false);

        mRecyclerAdapter = new ExtractConversationsRecyclerAdapter();

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecyclerAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTask = new ReadHistoryTask();
        mTask.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED) mTask.cancel(true);
    }

    public void setImeiAndUin(String imei, String uin) {
        Log.i("INFO", imei + ", " + uin);
        mImei = imei;
        mUin = uin;
    }

    private class ReadHistoryTask extends AsyncTask<Void, Void, Boolean> {
        private String error;
        private ArrayList<RecentConversation> conversations;

        @Override
        protected void onPreExecute() {
            mSwipeRefreshLayout.setEnabled(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // Request root access
            if (!Shell.SU.available()) {
                error = getString(R.string.root_access_is_required_to_read_wechat_history);
                return false;
            }
            // Get current username
            Pattern idPattern = Pattern.compile("^uid=[0-9]+?\\((\\w+)\\).+");
            String id = Shell.SH.run("id").get(0);
            Matcher idMatcher = idPattern.matcher(id);
            if (!idMatcher.matches()) {
                error = getString(R.string.failed_to_get_current_uid);
                return false;
            }
            String currentUser = idMatcher.group(1);
            // Get path
            @SuppressLint("SdCardPath") String srcFile = String.format("/data/data/com.tencent.mm/MicroMsg/%s/EnMicroMsg.db", new MD5("mm" + mUin).getMD5edString());
            String destFile = mHistoryDbsDir.getAbsolutePath() + "/EnMicroMsg.db";
            // Copy database
            List<String> copyResult = Shell.run("su", new String[] { String.format("cp -f %s %s", srcFile, destFile) }, null, true);
            if (!copyResult.isEmpty()) {
                error = copyResult.get(0);
                return false;
            }
            // Chown
            List<String> chownResult = Shell.run("su", new String[]{ String.format("chown %s:%s %s", currentUser, currentUser, destFile) }, null, true);
            if (!chownResult.isEmpty()) {
                error = chownResult.get(0);
                return false;
            }
            // Set password
//            mEnMicroMsgHelper.setPassword(new MD5(mImei + mUin).getMD5edString().substring(0, 7));
            // TO//DO: Read db
            // Read recent conversations
            conversations = mEnMicroMsgHelper.getRecentConversations();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean succeed) {
            mSwipeRefreshLayout.setEnabled(false);
            if (succeed) {
                mRecyclerAdapter.setDataSet(conversations);
            } else {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SqlCipherHook implements SQLiteDatabaseHook {
        @Override
        public void preKey(SQLiteDatabase sqLiteDatabase) {}

        @Override
        public void postKey(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.rawExecSQL("PRAGMA cipher_use_hmac = off;");
            sqLiteDatabase.rawExecSQL("PRAGMA kdf_iter = 4000;");
        }
    }

    public interface OnExtractConversationsInteractionListener {
    }
}
