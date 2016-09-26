package com.perqin.wechatted.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.perqin.wechatted.R;
import com.perqin.wechatted.adapter.ExtractionEditorRecyclerAdapter;
import com.perqin.wechatted.adapter.bean.Conversation;
import com.perqin.wechatted.database.EnMicroMsgHelper;

import java.io.File;
import java.util.List;

public class ExtractionEditorActivity extends AppCompatActivity {
    public static final String EXTRA_EXTRACTION_NAME = "EXTRA_EXTRACTION_NAME";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String mExtractionName;
    private EnMicroMsgHelper mEnMicroMsgHelper;
    private ExtractionEditorRecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extraction_editor);

        Intent intent = getIntent();
        mExtractionName = intent.getStringExtra(EXTRA_EXTRACTION_NAME);
        File file = new File(Environment.getExternalStorageDirectory(), String.format("WeChatted/%s/EnMicroMsg.db", mExtractionName));
        if (!file.exists()) {
            Toast.makeText(this, getString(R.string.unable_to_open_file_does_not_exist, mExtractionName), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mEnMicroMsgHelper = EnMicroMsgHelper.newInstance(this, file);

        mRecyclerAdapter = new ExtractionEditorRecyclerAdapter();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(mExtractionName);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshListener());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mRecyclerAdapter);

        new ReloadConversationsTask().execute();
    }

    private class SwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            new ReloadConversationsTask().execute();
        }
    }

    private class ReloadConversationsTask extends AsyncTask<Void, Void, List<Conversation>> {
        @Override
        protected void onPreExecute() {
            if (!mSwipeRefreshLayout.isRefreshing())
                mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected List<Conversation> doInBackground(Void... params) {
            return mEnMicroMsgHelper.getAdapterConversations();
        }

        @Override
        protected void onPostExecute(List<Conversation> conversations) {
            if (mSwipeRefreshLayout.isRefreshing())
                mSwipeRefreshLayout.setRefreshing(false);
            mRecyclerAdapter.setDataSet(conversations);
        }
    }
}
