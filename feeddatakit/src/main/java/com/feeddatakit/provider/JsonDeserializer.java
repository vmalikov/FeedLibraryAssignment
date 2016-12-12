package com.feeddatakit.provider;

import com.google.gson.Gson;

import com.feeddatakit.model.Post;
import com.feeddatakit.response.FeedResponse;

import java.util.List;

/**
 * Created by mac on 12/11/16.
 */

public class JsonDeserializer {

    public static List<Post> getFeeds(String json) {
        Gson gson = new Gson();
        FeedResponse response = gson.fromJson(json, FeedResponse.class);

        return response.getPostList();
    }
}