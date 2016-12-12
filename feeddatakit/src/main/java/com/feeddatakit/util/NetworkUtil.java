package com.feeddatakit.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mac on 12/11/16.
 */

public class NetworkUtil {

    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}