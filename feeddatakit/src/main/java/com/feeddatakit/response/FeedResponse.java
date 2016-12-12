package com.feeddatakit.response;

import com.google.gson.annotations.SerializedName;

import com.feeddatakit.model.Post;

import java.util.List;


/**
 * Created by mac on 12/11/16.
 */

public class FeedResponse {
    @SerializedName("feed")
    private List<Post> postList;

    public List<Post> getPostList() {
        return postList;
    }
}
