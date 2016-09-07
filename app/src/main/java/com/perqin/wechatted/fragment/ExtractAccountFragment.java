package com.perqin.wechatted.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.perqin.wechatted.R;
import com.perqin.wechatted.bean.WeChatAccount;

import java.util.ArrayList;
import java.util.List;

public class ExtractAccountFragment extends Fragment implements View.OnClickListener {
    private OnExtractAccountInteractionListener mListener;
    private ArrayList<WeChatAccount> mAccounts = new ArrayList<>();
    private RadioGroup mAccountRadioGroup;

    public ExtractAccountFragment() {
        // Required empty public constructor
    }

    public static ExtractAccountFragment newInstance() {
        return new ExtractAccountFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExtractAccountInteractionListener) {
            mListener = (OnExtractAccountInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mListener != null) {
            mAccounts.clear();
            mAccounts.addAll(mListener.getWeChatAccounts());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extract_reading, container, false);

        mAccountRadioGroup = (RadioGroup) view.findViewById(R.id.account_radio_group);

        prepareRadioGroups();

        view.findViewById(R.id.manage_accounts_button).setOnClickListener(this);
        view.findViewById(R.id.read_button).setOnClickListener(this);

        return view;
    }

    private void prepareRadioGroups() {
        for (int i = 0; i < mAccounts.size(); ++i) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setId(i);
            radioButton.setText(mAccounts.get(i).getTitle());
            mAccountRadioGroup.addView(radioButton);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manage_accounts_button:
                if (mListener != null) mListener.onManageAccountsButtonClick();
                break;
            case R.id.read_button:
                int id = mAccountRadioGroup.getCheckedRadioButtonId();
                String uin = "";
                if (id == R.id.detect_current_account_radio) {
                    if (mListener != null) uin = mListener.readCurrentUin();
                } else {
                    uin = mAccounts.get(id).getUin();
                }
                if (mListener != null) mListener.onReadButtonClick(uin);
                break;
            default:
                break;
        }
    }

    public interface OnExtractAccountInteractionListener {
        List<WeChatAccount> getWeChatAccounts();
        String readCurrentUin();
        void onManageAccountsButtonClick();
        void onReadButtonClick(String uin);
    }
}
