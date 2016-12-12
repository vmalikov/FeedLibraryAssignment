package com.feedlibraryassignment;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by mac on 12/12/16.
 */

public class FeedApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
