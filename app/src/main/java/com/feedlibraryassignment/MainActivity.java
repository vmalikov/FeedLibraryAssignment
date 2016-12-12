package com.feedlibraryassignment;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.feeddatakit.FeedDataKitManager;
import com.feeddatakit.model.Post;
import com.feedlibraryassignment.logic.FeedManagerImpl;
import com.feedlibraryassignment.receiver.UpdateReceiver;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FeedDataKitManager.FeedPostsCallback,
        FeedManagerImpl.OnNewPostListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private FeedAdapter mAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

        setContentView(R.layout.activity_main);
        Nammu.init(getApplicationContext());

        setupSwipeContainer();
        setupRecyclerView();

        registerReceiver(UpdateReceiver.getInstance(), UpdateReceiver.getIntentFilter());
    }

    private void setupRecyclerView() {
        mAdapter = new FeedAdapter(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.feed_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(mAdapter);
    }

    private void setupSwipeContainer() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> {
            FeedManagerImpl.getInstance(getApplicationContext()).fetch(MainActivity.this);
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    protected void onPause() {
        FeedManagerImpl.getInstance(getApplicationContext()).shouldContinuePeriodicTask(false);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(UpdateReceiver.getInstance());
        } catch (Exception ignore) {
        }
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();

        UpdateReceiver.getInstance().setOnConnectionChangedListener(isConnected -> {
            FeedManagerImpl.getInstance(getApplicationContext()).shouldContinuePeriodicTask(isConnected);
            FeedManagerImpl.getInstance(getApplicationContext()).startPeriodicTask(getApplicationContext());
        });


        FeedManagerImpl.getInstance(getApplicationContext()).setOnNewPostListener(this);

        FeedManagerImpl.getInstance(getApplicationContext()).shouldContinuePeriodicTask(true);
        checkPermissionsAndFetchData();
    }

    private void startFetchingData() {
        FeedManagerImpl.getInstance(getApplicationContext()).fetch(MainActivity.this);
        FeedManagerImpl.getInstance(getApplicationContext()).startPeriodicTask(getApplicationContext());
    }

    @Override
    public void onPosts(List<Post> list) {
        mAdapter.setItems(list);
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        swipeContainer.setRefreshing(false);
        Toast.makeText(getApplicationContext(), "Got an error! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewPost(List<Post> list) {
        // filter if we have already loaded posts - don't need to add them
        List<Post> existingPosts = mAdapter.getItems();
        for (Post post : existingPosts) {
            if (list.contains(post)) {
                list.remove(post);
            }
        }
        mAdapter.addItemsToStart(list);
    }

    // Permissions section
    private void checkPermissionsAndFetchData() {
        if (Nammu.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            startFetchingData();
        } else {
            Nammu.askForPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    permissionCallback);
        }
    }

    PermissionCallback permissionCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            startFetchingData();
        }

        @Override
        public void permissionRefused() {
            Toast.makeText(getApplicationContext(), getString(R.string.refused_permission_message),
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
