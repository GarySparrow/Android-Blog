package com.gary.blog.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by hasee on 2016/12/11.
 */

public class NetWorkUtil {

    public static boolean isNetWorkOpened(Context context) {
        ConnectivityManager connectManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectManager.getActiveNetworkInfo() != null)
            return connectManager.getActiveNetworkInfo().isAvailable();
        return false;
    }
}
