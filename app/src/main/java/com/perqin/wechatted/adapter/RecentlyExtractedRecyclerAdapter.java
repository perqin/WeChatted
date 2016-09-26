package com.perqin.wechatted.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.perqin.wechatted.R;
import com.perqin.wechatted.adapter.bean.Extraction;

import java.util.ArrayList;
import java.util.List;


public class RecentlyExtractedRecyclerAdapter extends RecyclerView.Adapter<RecentlyExtractedRecyclerAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private ArrayList<Extraction> mDataSet = new ArrayList<>();

    private OnItemClickListener mListener;

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
                final ItemViewHolder vh = (ItemViewHolder) holder;
                vh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) mListener.onItemClick(v, mDataSet.get(vh.getAdapterPosition()));
                    }
                });
                vh.nameText.setText(mDataSet.get(position - 1).getName());
                vh.timeText.setText(mDataSet.get(position - 1).getTime());
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 1 + mDataSet.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void reloadExtractions(List<Extraction> extractions) {
        mDataSet.clear();
        mDataSet.addAll(extractions);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class HeaderViewHolder extends ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class ItemViewHolder extends ViewHolder {
        public TextView nameText;
        public TextView timeText;

        public ItemViewHolder(View itemView) {
            super(itemView);

            nameText = (TextView) itemView.findViewById(R.id.name_text);
            timeText = (TextView) itemView.findViewById(R.id.time_text);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View item, Extraction extraction);
    }
}
