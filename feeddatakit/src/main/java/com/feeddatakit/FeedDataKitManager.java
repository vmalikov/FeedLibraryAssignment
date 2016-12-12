package com.feeddatakit;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.feeddatakit.model.Post;
import com.feeddatakit.repository.FeedPostRepository;
import com.feeddatakit.util.NetworkUtil;

import java.util.List;

/**
 * Created by mac on 12/11/16.
 */

public abstract class FeedDataKitManager {
    private static final String TAG = FeedDataKitManager.class.getSimpleName();

    private Handler handler;
    private FeedPostRepository feedPostRepository;

    /**
     * Indicator to continue fetching data from server in periodic task
     */
    private boolean mShouldContinuePeriodicTask = true;

    private long SECOND = 1000;
    private long period = SECOND * 3; // period for trying to fetch new posts

    public interface FeedPostsCallback {
        void onPosts(List<Post> list);
        void onError(Throwable e);
    }

    public abstract void onNewPosts(List<Post> list);

    protected FeedDataKitManager(Context context) {
        HandlerThread thread = new HandlerThread("HandlerThread");
        thread.start();
        handler = new Handler(thread.getLooper());

        feedPostRepository = RepositoryFacade.getFeedPostRespository(context);
    }

    /**
     * Tries to fetch data from network, and if got an error - fetch data from cache provider
     * On success - add new posts to cache
     */
    public void fetch(FeedPostsCallback callback) {
        handler.post(() -> feedPostRepository.fetch(callback));
    }

    /**
     * Launch periodic task
     */
    public void startPeriodicTask(Context context) {
        if (NetworkUtil.isConnected(context)) {
            handler.postDelayed(() -> feedPostRepository.startPeriodicTask(getFeedPostsCallback(context)), period);
        } else {
            Log.i(TAG, "startPeriodicTask: not connected. can not load new posts");
        }
    }

    @NonNull
    private FeedPostsCallback getFeedPostsCallback(final Context context) {
        return new FeedPostsCallback() {
            @Override
            public void onPosts(List<Post> list) {
                onNewPosts(list);
                if (mShouldContinuePeriodicTask) {
                    startPeriodicTask(context);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        };
    }

    public void shouldContinuePeriodicTask(boolean should) {
        mShouldContinuePeriodicTask = should;
    }
}
