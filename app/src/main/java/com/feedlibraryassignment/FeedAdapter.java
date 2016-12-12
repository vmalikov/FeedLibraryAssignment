package com.feedlibraryassignment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.feeddatakit.model.Post;
import com.feedlibraryassignment.view.BasicItemView;
import com.feedlibraryassignment.view.PostItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 12/11/16.
 */

public class FeedAdapter extends RecyclerView.Adapter {

    private final Context mContext;

    private List<Post> mItems = new ArrayList<>();

    public FeedAdapter(Context context) {
        mContext = context;
    }

    public void setItems(List<Post> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public void addItemsToStart(List<Post> list) {
        if(!list.isEmpty()) {
            mItems.addAll(0, list);
            notifyDataSetChanged();
        }
    }

    public List<Post> getItems() {
        return mItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedItemViewHolder(new PostItemView(mContext));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FeedItemViewHolder)holder).bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class FeedItemViewHolder extends RecyclerView.ViewHolder {

        private BasicItemView itemView;

        public FeedItemViewHolder(BasicItemView itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void bind(Post post) {
            itemView.bind(post);
        }
    }
}
