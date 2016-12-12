package com.feedlibraryassignment;

import android.app.Activity;
import android.content.Context;

import com.feedlibraryassignment.logic.FeedManagerImpl;

/**
 * Created by mac on 12/12/16.
 */

public interface ILifecycleListener {
    void onCreate(Activity activity);
    void onResume(Activity activity);
    void onPause(Context applicationContext);
    void onDestroy(Activity activity);

    void checkPermission(Activity activity);

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
}
