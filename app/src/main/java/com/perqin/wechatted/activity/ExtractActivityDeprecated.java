package com.perqin.wechatted.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import com.perqin.wechatted.R;
import com.perqin.wechatted.bean.WeChatAccount;
import com.perqin.wechatted.fragment.ExtractAccountFragment;
import com.perqin.wechatted.fragment.ExtractConversationsFragment;
import com.perqin.wechatted.fragment.ExtractOptionsFragment;

public class ExtractActivityDeprecated extends AppCompatActivity
        implements ExtractAccountFragment.OnExtractAccountInteractionListener, ExtractConversationsFragment.OnExtractConversationsInteractionListener {
    private ExtractAccountFragment mChooseAccountFragment;
    private ExtractConversationsFragment mConversationsFragment;
    private ExtractOptionsFragment mOptionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deprecated_activity_extract);

        mChooseAccountFragment = ExtractAccountFragment.newInstance();
        mConversationsFragment = ExtractConversationsFragment.newInstance();
        mOptionsFragment = ExtractOptionsFragment.newInstance();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mChooseAccountFragment)
                    .commit();
        }
    }

    // Implements ExtractAccountFragment.OnExtractAccountInteractionListener
    @Override
    public void onManageAccountsButtonClick() {
        // TO//DO: manage accounts
    }

    // Implements ExtractAccountFragment.OnExtractAccountInteractionListener
    @Override
    public void onReadButtonClick() {
        WeChatAccount account = mChooseAccountFragment.getSelectedAccount();
        if (account == null) return;
        @SuppressLint("HardwareIds") String imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        mConversationsFragment.setImeiAndUin(imei, account.getUin());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mConversationsFragment)
                .commit();
    }
}
