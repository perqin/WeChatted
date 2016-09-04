package com.perqin.wechatted.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.perqin.wechatted.R;
import com.perqin.wechatted.bean.TaskInfo;

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
        Context context = holder.itemView.getContext().getApplicationContext();
        switch (mDataSet.get(position).state) {
            case TaskInfo.NOT_START:
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.stateImage.setVisibility(View.INVISIBLE);
                holder.taskText.setTextColor(ContextCompat.getColor(context, R.color.grey500));
                break;
            case TaskInfo.RUNNING:
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.stateImage.setVisibility(View.INVISIBLE);
                holder.taskText.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                break;
            case TaskInfo.SUCCEED:
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.stateImage.setVisibility(View.VISIBLE);
                holder.stateImage.setImageResource(R.drawable.ic_succeed_white_24dp);
                holder.taskText.setTextColor(ContextCompat.getColor(context, R.color.green500));
                break;
            case TaskInfo.FAIL:
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.stateImage.setVisibility(View.VISIBLE);
                holder.stateImage.setImageResource(R.drawable.ic_fail_white_24dp);
                holder.taskText.setTextColor(ContextCompat.getColor(context, R.color.red500));
                break;
        }
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
        public ProgressBar progressBar;
        public ImageView stateImage;
        public TextView taskText;

        public ViewHolder(View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            stateImage = (ImageView) itemView.findViewById(R.id.state_image);
            taskText = (TextView) itemView.findViewById(R.id.task_text);
        }
    }
}
