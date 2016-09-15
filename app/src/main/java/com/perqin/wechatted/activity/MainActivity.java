package com.perqin.wechatted.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.perqin.wechatted.R;
import com.perqin.wechatted.adapter.RecentlyExtractedRecyclerAdapter;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTRACT = 0;

    private RecyclerView mRecyclerView;
    private RecentlyExtractedRecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerAdapter = new RecentlyExtractedRecyclerAdapter();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, ExtractActivity.class), REQUEST_EXTRACT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_EXTRACT:
                if (resultCode == ExtractActivity.RESULT_OPEN) {
                    Toast.makeText(this, "Will open extraction: " + data.getStringExtra(ExtractActivity.EXTRA_EXTRACTION_NAME), Toast.LENGTH_SHORT).show();
                    // TODO: Open extraction from data
                }
                break;
            default:
                break;
        }
    }
}
