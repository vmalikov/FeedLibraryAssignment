package com.feeddatakit.provider;

import android.os.Environment;

import com.feeddatakit.FeedDataKitManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by mac on 12/11/16.
 */

public class FeedFileServerDataProvider implements IFeedProvider {

    private static final String DATA_FILE_NAME = "posts.txt";
    private static final String DATA_FILE_NAME_2 = "newposts.txt";

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
