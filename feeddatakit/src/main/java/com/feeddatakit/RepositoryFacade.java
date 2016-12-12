package com.feeddatakit;

import android.content.Context;

import com.feeddatakit.repository.FeedPostRepository;

/**
 * Created by mac on 12/12/16.
 */

public class RepositoryFacade {
    public static FeedPostRepository getFeedPostRespository(Context context) {
        return new FeedPostRepository(context);
    }
}
