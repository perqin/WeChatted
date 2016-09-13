package com.perqin.wechatted.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.perqin.wechatted.R;

public class ExtractOptionsFragment extends Fragment {
    private TextInputEditText mExtractionNameEdit;
    private Button mSelectConversationsButton;
    private Button mSaveButton;

    private OnFragmentInteractionListener mListener;

    public ExtractOptionsFragment() {
        // Required empty public constructor
    }

    public static ExtractOptionsFragment newInstance() {
        return new ExtractOptionsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extract_options, container, false);

        mExtractionNameEdit = (TextInputEditText) view.findViewById(R.id.extraction_name_edit);
        mSelectConversationsButton = (Button) view.findViewById(R.id.select_conversations_button);
        mSaveButton = (Button) view.findViewById(R.id.save_button);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onSelectConversationsButtonClick();
        void onSaveExtractionButtonClick();
    }
}
