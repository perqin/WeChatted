package com.perqin.wechatted.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.perqin.wechatted.R;

import java.util.ArrayList;


public class ExtractReadingRecyclerAdapter extends RecyclerView.Adapter<ExtractReadingRecyclerAdapter.ViewHolder> {
    private ArrayList<TaskInfo> mDataSet = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_extract_reading, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.taskText.setText(mDataSet.get(position).description);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void addTaskInfo(int position, int state, String description) {
        mDataSet.add(position, new TaskInfo(state, description));
        notifyItemInserted(position);
    }

    public void appendTaskInfo(int state, String description) {
        addTaskInfo(mDataSet.size(), state, description);
    }

    public void setState(int position, int state) {
        mDataSet.get(position).state = state;
        notifyItemChanged(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskText;

        public ViewHolder(View itemView) {
            super(itemView);

            taskText = (TextView) itemView.findViewById(R.id.task_text);
        }
    }

    public static class TaskInfo {
        public static final int NOT_START = 0;
        public static final int RUNNING = 1;
        public static final int SUCCEED = 2;
        public static final int FAIL = 3;

        public int state;
        public String description;

        public TaskInfo() {
            this(NOT_START, "");
        }

        public TaskInfo(int state, String description) {
            this.state = state;
            this.description = description;
        }
    }
}
