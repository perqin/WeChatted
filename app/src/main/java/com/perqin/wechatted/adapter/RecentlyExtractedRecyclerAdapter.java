package com.perqin.wechatted.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.perqin.wechatted.R;

import java.util.ArrayList;


public class RecentlyExtractedRecyclerAdapter extends RecyclerView.Adapter<RecentlyExtractedRecyclerAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private ArrayList<Object> mDataSet = new ArrayList<>();

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_header_recently_extracted, parent, false));
            case TYPE_ITEM:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_item_recently_extracted, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_ITEM:
                // TODO: Fill data
                ItemViewHolder vh = (ItemViewHolder) holder;
                vh.nameText.setText("This is the name " + String.valueOf(position));
                vh.timeText.setText("This is the time " + String.valueOf(position));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 1 + mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class HeaderViewHolder extends ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ItemViewHolder extends ViewHolder {
        public TextView nameText;
        public TextView timeText;

        public ItemViewHolder(View itemView) {
            super(itemView);

            nameText = (TextView) itemView.findViewById(R.id.name_text);
            timeText = (TextView) itemView.findViewById(R.id.time_text);
        }
    }
}
