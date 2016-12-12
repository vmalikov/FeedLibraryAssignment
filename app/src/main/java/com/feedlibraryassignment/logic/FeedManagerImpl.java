package com.feedlibraryassignment.logic;

import android.content.Context;

import com.feeddatakit.FeedDataKitManager;
import com.feeddatakit.model.Post;

import java.util.List;

/**
 * Created by mac on 12/11/16.
 */

public class FeedManagerImpl extends FeedDataKitManager {

    public interface OnNewPostListener {
        void onNewPost(List<Post> list);
    }

    public static class Holder {
        public static FeedManagerImpl HOLDER_INSTANCE;
    }

    public static FeedManagerImpl getInstance(Context context) {
        if(FeedManagerImpl.Holder.HOLDER_INSTANCE == null) {
            FeedManagerImpl.Holder.HOLDER_INSTANCE = new FeedManagerImpl(context);
        }
        return FeedManagerImpl.Holder.HOLDER_INSTANCE;
    }

    private OnNewPostListener mOnNewPostListener;

    private FeedManagerImpl(Context context) {
        super(context);
    }

    public void setOnNewPostListener(OnNewPostListener mOnNewPostListener) {
        this.mOnNewPostListener = mOnNewPostListener;
    }

    @Override
    public void onNewPosts(List<Post> list) {
        if (mOnNewPostListener != null) {
            mOnNewPostListener.onNewPost(list);
        }
    }
}
