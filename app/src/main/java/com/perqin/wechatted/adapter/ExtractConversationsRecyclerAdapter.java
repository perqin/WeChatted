package com.perqin.wechatted.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.perqin.wechatted.R;
import com.perqin.wechatted.bean.RecentConversation;

import java.util.ArrayList;
import java.util.List;


public class ExtractConversationsRecyclerAdapter extends RecyclerView.Adapter<ExtractConversationsRecyclerAdapter.ViewHolder> {
    private ArrayList<RecentConversation> mDataSet = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_extract_conversations, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.lastMessageText.setText(mDataSet.get(position).getDigest());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setDataSet(List<RecentConversation> conversations) {
        mDataSet.clear();
        mDataSet.addAll(conversations);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatarImage;
        public TextView titleText;
        public TextView timeText;
        public TextView lastMessageText;
        public CheckBox selectCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);

            avatarImage = (ImageView) itemView.findViewById(R.id.avatar_image);
            titleText = (TextView) itemView.findViewById(R.id.title_text);
            timeText = (TextView) itemView.findViewById(R.id.time_text);
            lastMessageText = (TextView) itemView.findViewById(R.id.last_message_text);
            selectCheckBox = (CheckBox) itemView.findViewById(R.id.select_check_box);
        }
    }
}
