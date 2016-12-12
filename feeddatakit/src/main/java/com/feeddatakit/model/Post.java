package com.feeddatakit.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 12/11/16.
 */

public class Post {
    @SerializedName("id")
    long id;
    @SerializedName("senderName")
    String senderName;
    @SerializedName("senderProfileImage")
    String senderProfileImage;
    @SerializedName("text")
    String text;
    @SerializedName("date")
    long date;
    @SerializedName("mediaType")
    int mediaType;
    @SerializedName("media")
    String media;
    @SerializedName("likeCount")
    int likeCount;

    public long getId() {
        return id;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getText() {
        return text;
    }

    public long getDate() {
        return date;
    }

    public int getMediaType() {
        return mediaType;
    }

    public String getMedia() {
        return media;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public Post setId(long id) {
        this.id = id;
        return this;
    }

    public Post setSenderName(String senderName) {
        this.senderName = senderName;
        return this;
    }

    public Post setText(String text) {
        this.text = text;
        return this;
    }

    public Post setDate(long date) {
        this.date = date;
        return this;
    }

    public Post setMediaType(int mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public Post setMedia(String media) {
        this.media = media;
        return this;
    }

    public Post setLikeCount(int likeCount) {
        this.likeCount = likeCount;
        return this;
    }

    public String getSenderProfileImage() {
        return senderProfileImage;
    }

    public Post setSenderProfileImage(String senderProfileImage) {
        this.senderProfileImage = senderProfileImage;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (id != post.id) return false;
        if (date != post.date) return false;
        return senderName != null ? senderName.equals(post.senderName) : post.senderName == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (senderName != null ? senderName.hashCode() : 0);
        result = 31 * result + (int) (date ^ (date >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", senderName='" + senderName + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", mediaType=" + mediaType +
                ", media='" + media + '\'' +
                ", likeCount=" + likeCount +
                '}';
    }
}
