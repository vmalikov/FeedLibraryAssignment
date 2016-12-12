package com.feedlibraryassignment.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.feeddatakit.util.NetworkUtil;

/**
 * Created by mac on 12/11/16.
 */

public class UpdateReceiver extends BroadcastReceiver {

    private OnConnectionChangedListener onConnectionChangedListener;

    public interface OnConnectionChangedListener {
        void onChanged(boolean isConnected);
    }

    private static UpdateReceiver sInstance = new UpdateReceiver();

    public static UpdateReceiver getInstance() {
        return sInstance;
    }

    private UpdateReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = NetworkUtil.isConnected(context);
        if(onConnectionChangedListener != null) {
            onConnectionChangedListener.onChanged(isConnected);
        }
    }

    public static IntentFilter getIntentFilter() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        return filter;
    }

    public void setOnConnectionChangedListener(OnConnectionChangedListener onConnectionChangedListener) {
        this.onConnectionChangedListener = onConnectionChangedListener;
    }
}
