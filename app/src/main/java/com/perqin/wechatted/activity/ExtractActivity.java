package com.perqin.wechatted.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.perqin.wechatted.R;
import com.perqin.wechatted.fragment.ExtractOptionsFragment;
import com.perqin.wechatted.fragment.ExtractReadingFragment;
import com.perqin.wechatted.fragment.ExtractWritingFragment;

public class ExtractActivity extends AppCompatActivity {
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
}
