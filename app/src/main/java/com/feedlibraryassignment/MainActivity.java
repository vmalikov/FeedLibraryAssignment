package com.feedlibraryassignment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.feeddatakit.FeedDataKitManager;
import com.feeddatakit.model.Post;
import com.feedlibraryassignment.logic.FeedManagerImpl;
import com.feedlibraryassignment.presenter.MainPresenter;

import pl.tajchert.nammu.Nammu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FeedDataKitManager.FeedPostsCallback,
        FeedManagerImpl.OnNewPostListener {
    
    private static final String TAG = MainActivity.class.getSimpleName();

    private FeedAdapter mAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;

    private ILifecycleListener lifecycleListener;

    @Override
    public void onPosts(List<Post> list) {
        runOnUiThread(() -> {
            mAdapter.setItems(list);
            swipeContainer.setRefreshing(false);
        });
    }

    @Override
    public void onError(Throwable e) {
        runOnUiThread(() -> {
            e.printStackTrace();
            swipeContainer.setRefreshing(false);
            Toast.makeText(getApplicationContext(), "Got an error! ", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onNewPost(List<Post> list) {
        runOnUiThread(() -> {
            // filter if we have already loaded posts - don't need to add them
            List<Post> existingPosts = mAdapter.getItems();
            List<Post> newPosts = new ArrayList<>(list);

            for (Post post : existingPosts) {
                if (newPosts.contains(post)) {
                    newPosts.remove(post);
                }
            }
            mAdapter.addItemsToStart(newPosts);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lifecycleListener = new MainPresenter(this, this);
        lifecycleListener.onCreate(this);

        setContentView(R.layout.activity_main);

        setupSwipeContainer();
        setupRecyclerView();

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
        lifecycleListener.onPause(getApplicationContext());
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        lifecycleListener.onDestroy(this);
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        lifecycleListener.onResume(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        lifecycleListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
