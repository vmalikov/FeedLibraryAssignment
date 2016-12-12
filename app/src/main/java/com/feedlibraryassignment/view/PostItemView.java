package com.feedlibraryassignment.view;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.feeddatakit.model.Post;
import com.feedlibraryassignment.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by mac on 12/11/16.
 */

public class PostItemView extends BasicItemView {
    private static final String TAG = PostItemView.class.getSimpleName();

    public static final String DATE_FORMAT = "EEE, d MMM yyyy HH:mm";

    private TextView mName;
    private TextView mDate;
    private TextView mText;
    protected SimpleDraweeView mUserImage;
    protected SimpleDraweeView mImage;

    public PostItemView(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.post_item_layout;
    }

    @Override
    protected void init() {
        super.init();
        mName = (TextView) findViewById(R.id.name);
        mUserImage = (SimpleDraweeView) findViewById(R.id.user_image);
        mImage = (SimpleDraweeView) findViewById(R.id.image);
        mDate = (TextView) findViewById(R.id.date);
        mText = (TextView) findViewById(R.id.text);
    }

    @Override
    public void bind(Object o) {
        if(o instanceof Post) {
            Post post = (Post) o;
            mName.setText(post.getSenderName());
            mText.setText(post.getText());
            loadImage(mUserImage, post.getSenderProfileImage());
            loadImage(mImage, post.getMedia());

            String dateText = getFormattedDate(post);
            mDate.setText(dateText);
        }
    }

    private String getFormattedDate(Post post) {
        long date = post.getDate();
        SimpleDateFormat dateFormat =  new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return dateFormat.format(date);
    }

    protected void loadImage(SimpleDraweeView imageView, String url) {
        if(!TextUtils.isEmpty(url)) {
            imageView.setVisibility(VISIBLE);
            imageView.setImageURI(Uri.parse(url));
        } else {
            imageView.setVisibility(GONE);
        }
    }
}
