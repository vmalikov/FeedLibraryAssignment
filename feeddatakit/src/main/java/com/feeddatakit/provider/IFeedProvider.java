package com.feeddatakit.provider;

import com.feeddatakit.FeedDataKitManager;

/**
 * Created by mac on 12/11/16.
 */

public interface IFeedProvider {
    void fetch(FeedDataKitManager.FeedPostsCallback feedPostsCallback);
}
