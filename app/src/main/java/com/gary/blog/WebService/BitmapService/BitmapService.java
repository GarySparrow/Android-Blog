package com.gary.blog.WebService.BitmapService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

import static com.gary.blog.Utils.WebUtil.connection;

/**
 * Created by hasee on 2016/12/14.
 */

public class BitmapService {

    private static final String TAG = "BitmapService";


    public static Bitmap getBitmap(String url) {
        Bitmap bitmap;
        InputStream is = connection(url);

        if (is != null) {
            bitmap =  BitmapFactory.decodeStream(is);
            return bitmap;
        } else {
//            Log.e(TAG, "InputStream == null");
        }
        return null;
    }

}
