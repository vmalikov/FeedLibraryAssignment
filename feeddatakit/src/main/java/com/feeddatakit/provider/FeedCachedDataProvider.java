package com.feeddatakit.provider;

import android.content.Context;

import com.feeddatakit.FeedDataKitManager;
import com.feeddatakit.cache.DataBaseHelper;
import com.feeddatakit.model.Post;

import java.util.List;

/**
 * Created by mac on 12/11/16.
 */

public class FeedCachedDataProvider implements IFeedProvider {

    DataBaseHelper dataBaseHelper;

    public FeedCachedDataProvider(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
    }

    @Override
    public void fetch(FeedDataKitManager.FeedPostsCallback feedPostsCallback) {
        try {
            List<Post> list = dataBaseHelper.getAllPosts();
            feedPostsCallback.onPosts(list);
        } catch (Throwable e) {
            feedPostsCallback.onError(e);
        }
    }

    public void add(List<Post> feeds) {
        for(Post post : feeds) {
            dataBaseHelper.addPost(post);
        }
    }

    public void set(List<Post> feeds) {
        dataBaseHelper.clearFeed();
        add(feeds);
    }
}
