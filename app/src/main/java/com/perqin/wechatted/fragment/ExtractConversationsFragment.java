package com.perqin.wechatted.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perqin.wechatted.R;

public class ExtractConversationsFragment extends Fragment {
    private OnExtractConversationsInteractionListener mListener;
    private String mImei;
    private String mUin;

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
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_extract_options, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (mListener != null) mListener.startReading(mImei, mUin);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setImeiAndUin(String imei, String uin) {
        Log.i("INFO", imei + ", " + uin);
        mImei = imei;
        mUin = uin;
    }

    public interface OnExtractConversationsInteractionListener {
        void startReading(String imei, String uin);
    }
}
