package com.perqin.wechatted.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.perqin.wechatted.R;
import com.perqin.wechatted.adapter.RecentlyExtractedRecyclerAdapter;
import com.perqin.wechatted.adapter.bean.Extraction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final int REQUEST_EXTRACT = 0;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecentlyExtractedRecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerAdapter = new RecentlyExtractedRecyclerAdapter();
        mRecyclerAdapter.setOnItemClickListener(new OnExtractionClickListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.content_main);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mRecyclerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, ExtractActivity.class), REQUEST_EXTRACT);
            }
        });

        new ReloadExtractionsTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_EXTRACT:
                new ReloadExtractionsTask().execute();
                if (resultCode == ExtractActivity.RESULT_OPEN) {
                    openExtraction(data.getStringExtra(ExtractActivity.EXTRA_EXTRACTION_NAME));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        new ReloadExtractionsTask().execute();
    }

    private void openExtraction(String name) {
        Intent intent = new Intent(this, ExtractionEditorActivity.class);
        intent.putExtra(ExtractionEditorActivity.EXTRA_EXTRACTION_NAME, name);
        startActivity(intent);
    }

    private class ReloadExtractionsTask extends AsyncTask<Void, Void, List<Extraction>> {
        @Override
        protected void onPreExecute() {
            if (!mSwipeRefreshLayout.isRefreshing())
                mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected List<Extraction> doInBackground(Void... params) {
            ArrayList<Extraction> extractions = new ArrayList<>();
            File file = new File(Environment.getExternalStorageDirectory(), "WeChatted");
            File[] files = file.listFiles();
            for (File extractionFile : files) {
                Extraction extraction = new Extraction();
                extraction.setName(extractionFile.getName());
                extraction.setTime(extractionFile.getPath());
                extractions.add(extraction);
            }
            return extractions;
        }

        @Override
        protected void onPostExecute(List<Extraction> extractions) {
            mRecyclerAdapter.reloadExtractions(extractions);
            if (mSwipeRefreshLayout.isRefreshing())
                mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private class OnExtractionClickListener implements RecentlyExtractedRecyclerAdapter.OnItemClickListener {
        @Override
        public void onItemClick(View item, Extraction extraction) {
            openExtraction(extraction.getName());
        }
    }
}
