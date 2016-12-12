package com.feeddatakit.repository;

import android.content.Context;

import com.feeddatakit.FeedDataKitManager;
import com.feeddatakit.model.Post;
import com.feeddatakit.provider.FeedCachedDataProvider;
import com.feeddatakit.provider.FeedFileServerDataProvider;

import java.util.List;

/**
 * Created by mac on 12/12/16.
 */

public class FeedPostRepository {
    private static final String TAG = FeedPostRepository.class.getSimpleName();

    private FeedFileServerDataProvider networkProvider;
    private FeedCachedDataProvider cacheProvider;

    public FeedPostRepository(Context context) {
        networkProvider = new FeedFileServerDataProvider();
        cacheProvider = new FeedCachedDataProvider(context);
    }

    public void fetch(FeedDataKitManager.FeedPostsCallback callback) {
        fetchFromNetwork(callback);
    }

    private void fetchFromNetwork(final FeedDataKitManager.FeedPostsCallback callback) {

        networkProvider.fetch(new FeedDataKitManager.FeedPostsCallback() {
            @Override
            public void onPosts(List<Post> list) {
                callback.onPosts(list);
                cacheProvider.set(list);
            }

            @Override
            public void onError(Throwable e) {
                fetchFromCache(callback);
            }
        });
    }

    private void fetchFromCache(final FeedDataKitManager.FeedPostsCallback callback) {
        cacheProvider.fetch(new FeedDataKitManager.FeedPostsCallback() {
            @Override
            public void onPosts(List<Post> list) {
                callback.onPosts(list);
            }

            @Override
            public void onError(Throwable e) {
                callback.onError(e);
            }
        });
    }

    /**
     * Launch periodic task
     */
    public void startPeriodicTask(FeedDataKitManager.FeedPostsCallback feedPostsCallback) {

        networkProvider.fetchNewPosts(new FeedDataKitManager.FeedPostsCallback() {
            @Override
            public void onPosts(List<Post> list) {
                feedPostsCallback.onPosts(list);
                cacheProvider.add(list);
            }

            @Override
            public void onError(Throwable e) {
                feedPostsCallback.onError(e);
            }
        });
    }
}

