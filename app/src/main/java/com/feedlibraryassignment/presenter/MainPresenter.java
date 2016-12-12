package com.feedlibraryassignment.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.feeddatakit.FeedDataKitManager;
import com.feedlibraryassignment.ILifecycleListener;
import com.feedlibraryassignment.R;
import com.feedlibraryassignment.logic.FeedManagerImpl;
import com.feedlibraryassignment.receiver.UpdateReceiver;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * Created by mac on 12/12/16.
 */

public class MainPresenter implements ILifecycleListener {

    private final FeedDataKitManager.FeedPostsCallback feedPostsCallback;
    private FeedManagerImpl.OnNewPostListener onNewPostListener;

    public MainPresenter(FeedManagerImpl.OnNewPostListener onNewPostListener, FeedDataKitManager.FeedPostsCallback feedPostsCallback) {
        this.onNewPostListener = onNewPostListener;
        this.feedPostsCallback = feedPostsCallback;
    }

    @Override
    public void onCreate(Activity activity) {
        initThirdParty(activity);
        activity.registerReceiver(UpdateReceiver.getInstance(), UpdateReceiver.getIntentFilter());
    }

    @Override
    public void onResume(Activity activity) {
        checkPermissionsAndFetchData(activity);

        UpdateReceiver.getInstance().setOnConnectionChangedListener(isConnected -> {
            FeedManagerImpl.getInstance(activity.getApplicationContext()).shouldContinuePeriodicTask(isConnected);
            FeedManagerImpl.getInstance(activity.getApplicationContext()).startPeriodicTask(activity.getApplicationContext());
        });


        FeedManagerImpl.getInstance(activity.getApplicationContext()).setOnNewPostListener(onNewPostListener);
        FeedManagerImpl.getInstance(activity.getApplicationContext()).shouldContinuePeriodicTask(true);
    }

    @Override
    public void onPause(Context applicationContext) {
        FeedManagerImpl.getInstance(applicationContext).shouldContinuePeriodicTask(false);
    }

    @Override
    public void onDestroy(Activity activity) {
        try {
            activity.unregisterReceiver(UpdateReceiver.getInstance());
        } catch (Exception ignore) {
        }
    }

    @Override
    public void checkPermission(Activity activity) {

    }

    // Permissions section
    private void checkPermissionsAndFetchData(Activity activity) {
        if (Nammu.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            startFetchingData(activity);
        } else {
            Nammu.askForPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    new PermissionCallback() {
                        @Override
                        public void permissionGranted() {
                            startFetchingData(activity);
                        }

                        @Override
                        public void permissionRefused() {
                            Toast.makeText(activity, activity.getString(R.string.refused_permission_message),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startFetchingData(Context context) {
        FeedManagerImpl.getInstance(context).fetch(feedPostsCallback);
        FeedManagerImpl.getInstance(context).startPeriodicTask(context);
    }

    private void initThirdParty(Activity activity) {
        Fresco.initialize(activity);
        Nammu.init(activity.getApplicationContext());
    }
}
