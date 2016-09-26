package com.perqin.wechatted.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.perqin.wechatted.R;
import com.perqin.wechatted.adapter.RecentlyExtractedRecyclerAdapter;
import com.perqin.wechatted.adapter.bean.Extraction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final int REQUEST_EXTRACT = 0;
    private static final int PERMISSIONS_REQUEST_FOR_RELOADING = 0;

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

        runReloadExtractionsTask();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_FOR_RELOADING:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new ReloadExtractionsTask().execute();
                } else {
                    Toast.makeText(this, R.string.request_permissions_not_granted, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        runReloadExtractionsTask();
    }

    private void openExtraction(String name) {
        Intent intent = new Intent(this, ExtractionEditorActivity.class);
        intent.putExtra(ExtractionEditorActivity.EXTRA_EXTRACTION_NAME, name);
        startActivity(intent);
    }

    private void runReloadExtractionsTask() {
        // Runtime permissions request
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user asynchronously
                new AlertDialog.Builder(this)
                        .setTitle(R.string.request_permissions)
                        .setMessage(R.string.request_extraction_permissions_explanation)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, PERMISSIONS_REQUEST_FOR_RELOADING);
                            }
                        })
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, PERMISSIONS_REQUEST_FOR_RELOADING);
            }
        } else {
            new ReloadExtractionsTask().execute();
        }
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
            if (!file.exists() && !file.mkdirs()) return extractions;
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
