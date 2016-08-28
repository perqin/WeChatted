package com.perqin.wechatted.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.perqin.wechatted.R;
import com.perqin.wechatted.adapter.ExtractReadingRecyclerAdapter;

public class ExtractReadingFragment extends Fragment {
    private ExtractReadingRecyclerAdapter mRecyclerAdapter;
    private OnFragmentInteractionListener mListener;

    public ExtractReadingFragment() {
        // Required empty public constructor
    }

    public static ExtractReadingFragment newInstance() {
        return new ExtractReadingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extract_reading, container, false);

        prepareRecyclerAdapter();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(mRecyclerAdapter);

        return view;
    }

    private void prepareRecyclerAdapter() {
        mRecyclerAdapter = new ExtractReadingRecyclerAdapter();
        mRecyclerAdapter.appendTaskInfo(ExtractReadingRecyclerAdapter.TaskInfo.NOT_START, "Request root permission");
        mRecyclerAdapter.appendTaskInfo(ExtractReadingRecyclerAdapter.TaskInfo.NOT_START, "");
        mRecyclerAdapter.appendTaskInfo(ExtractReadingRecyclerAdapter.TaskInfo.NOT_START, "");
        mRecyclerAdapter.appendTaskInfo(ExtractReadingRecyclerAdapter.TaskInfo.NOT_START, "");
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
