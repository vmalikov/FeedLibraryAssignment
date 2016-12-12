package com.feeddatakit.provider;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.AsyncTaskLoader;

import com.feeddatakit.FeedDataKitManager;
import com.feeddatakit.model.Post;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by mac on 12/11/16.
 */

public class FeedFileServerDataProvider implements IFeedProvider {

    private static final String DATA_FILE_NAME = "feed.txt";
    private static final String DATA_FILE_NAME_2 = "feed_new.txt";

    @Override
    public void fetch(FeedDataKitManager.FeedPostsCallback callback) {
        fetchFromLocalFile(callback, DATA_FILE_NAME);
    }

    public void fetchNewPosts(FeedDataKitManager.FeedPostsCallback callback) {
        fetchFromLocalFile(callback, DATA_FILE_NAME_2);
    }

    private void fetchFromLocalFile(FeedDataKitManager.FeedPostsCallback callback, String dataFileName) {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File file = new File(externalStorageDirectory, dataFileName);

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            callback.onError(e);
        }

        callback.onPosts(JsonDeserializer.getFeeds(text.toString()));
    }
}
