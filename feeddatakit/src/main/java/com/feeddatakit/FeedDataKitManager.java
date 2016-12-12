package com.feeddatakit;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.feeddatakit.model.Post;
import com.feeddatakit.provider.FeedCachedDataProvider;
import com.feeddatakit.provider.FeedFileServerDataProvider;
import com.feeddatakit.util.NetworkUtil;

import java.util.List;

/**
 * Created by mac on 12/11/16.
 */

public abstract class FeedDataKitManager {
    private static final String TAG = FeedDataKitManager.class.getSimpleName();

    FeedFileServerDataProvider networkProvider;
    FeedCachedDataProvider cacheProvider;

    private Handler handler = new Handler();

    private long SECOND = 1000;
    private long period = SECOND * 3; // period for trying to fetch new posts

    /**
     * Indicator to continue fetching data from server in periodic task
     */
    private boolean mShouldContinuePeriodicTask = true;

    protected FeedDataKitManager(Context context) {
        networkProvider = new FeedFileServerDataProvider();
        cacheProvider = new FeedCachedDataProvider(context);
    }

    public interface FeedPostsCallback {
        void onPosts(List<Post> list);
        void onError(Throwable e);
    }

    public abstract void onNewPosts(List<Post> list);

    /**
     * Tries to fetch data from network, and if got an error - fetch data from cache provider
     * On success - add new posts to cache
     */
    public void fetch(FeedPostsCallback callback) {
        Log.i(TAG, "fetch: ");
        fetchFromNetwork(callback);
    }

    private void fetchFromNetwork(final FeedPostsCallback callback) {

        networkProvider.fetch(new FeedPostsCallback() {
            @Override
            public void onPosts(List<Post> list) {
                Log.i(TAG, "onPosts: fetched from Network!");
                callback.onPosts(list);
                cacheProvider.set(list);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: didn't fetched from Network!");
                fetchFromCache(callback);
            }
        });
    }

    private void fetchFromCache(final FeedPostsCallback callback) {
        cacheProvider.fetch(new FeedPostsCallback() {
            @Override
            public void onPosts(List<Post> list) {
                Log.i(TAG, "onPosts: fetched from DB!");
                callback.onPosts(list);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: didn't fetched from DB!");
                callback.onError(e);
            }
        });
    }

    /**
     * Launch periodic task
     */
    public void startPeriodicTask(Context context) {
        if (NetworkUtil.isConnected(context)) {
            Log.i(TAG, "startPeriodicTask: connected");
            handler.postDelayed(() -> networkProvider.fetchNewPosts(new FeedPostsCallback() {
                @Override
                public void onPosts(List<Post> list) {
                    onNewPosts(list);
                    cacheProvider.add(list);
                    startPeriodicTaskIfNeed(context);
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    startPeriodicTaskIfNeed(context);
                }
            }), period);
        } else {
            Log.i(TAG, "startPeriodicTask: not connected. can not load new posts");
        }
    }

    private void startPeriodicTaskIfNeed(Context context) {
        if (mShouldContinuePeriodicTask) {
            startPeriodicTask(context);
        }
    }

    public void shouldContinuePeriodicTask(boolean should) {
        mShouldContinuePeriodicTask = should;
    }
    
}
