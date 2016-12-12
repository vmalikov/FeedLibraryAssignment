package com.feedlibraryassignment.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * Created by mac on 12/11/16.
 */

public abstract class BasicItemView extends RelativeLayout {

    public BasicItemView(Context context) {
        super(context);
        init();
    }

    public BasicItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BasicItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutId(), this);
    }

    public void bind(Object o) {

    }

    protected abstract int getLayoutId();

}
