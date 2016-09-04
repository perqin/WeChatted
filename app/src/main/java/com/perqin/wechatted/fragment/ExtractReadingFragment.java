package com.perqin.wechatted.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.perqin.wechatted.R;
import com.perqin.wechatted.adapter.ExtractReadingRecyclerAdapter;
import com.perqin.wechatted.bean.TaskInfo;

public class ExtractReadingFragment extends Fragment {
    private ExtractReadingRecyclerAdapter mRecyclerAdapter;
    private OnExtractReadingInteractionListener mListener;
    private LinearLayout mErrorMessagePanel;
    private TextView mErrorMessageText;

    public ExtractReadingFragment() {
        // Required empty public constructor
    }

    public static ExtractReadingFragment newInstance() {
        return new ExtractReadingFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExtractReadingInteractionListener) {
            mListener = (OnExtractReadingInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareRecyclerAdapter();
        if (mListener != null)
            mListener.onCanStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extract_reading, container, false);

        mErrorMessagePanel = (LinearLayout) view.findViewById(R.id.error_message_panel);
        mErrorMessageText = (TextView) view.findViewById(R.id.error_message_text);
        view.findViewById(R.id.retry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onRetryClick();
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(mRecyclerAdapter);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setState(int step, int state) {
        mRecyclerAdapter.setState(step, state);
    }

    public void showErrorMessage(String message) {
        mErrorMessageText.setText(message);
        mErrorMessagePanel.setVisibility(View.VISIBLE);
    }

    public void hideErrorMessage() {
        mErrorMessagePanel.setVisibility(View.GONE);
    }

    private void prepareRecyclerAdapter() {
        mRecyclerAdapter = new ExtractReadingRecyclerAdapter();
        mRecyclerAdapter.appendTaskInfo(TaskInfo.NOT_START, getString(R.string.request_root_permission));
        mRecyclerAdapter.appendTaskInfo(TaskInfo.NOT_START, getString(R.string.read_imei));
        mRecyclerAdapter.appendTaskInfo(TaskInfo.NOT_START, getString(R.string.read_uin));
        mRecyclerAdapter.appendTaskInfo(TaskInfo.NOT_START, getString(R.string.copy_history));
        mRecyclerAdapter.appendTaskInfo(TaskInfo.NOT_START, getString(R.string.read_history));
    }

    public interface OnExtractReadingInteractionListener {
        void onCanStart();
        void onRetryClick();
    }
}
