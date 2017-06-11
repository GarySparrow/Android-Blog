package com.gary.blog.WebService.BoolService;

import android.text.TextUtils;

import com.gary.blog.Utils.JsonUtil;
import com.gary.blog.WebService.PostsService.PostsResponse;

import java.io.InputStream;

import static com.gary.blog.Utils.WebUtil.connection;
import static com.gary.blog.Utils.WebUtil.getStringFormIs;

/**
 * Created by hasee on 2016/12/26.
 */

public class BoolService {

    private static final String WEB_ROOT =
            "https://garyflask.herokuapp.com/api/v1.0/";

    private static final String USERS = "users/";

    private static final String ISFOLLOWED = "/isfollowed/";
    private static final String FOLLOW = "/follow/";
    private static final String UNFOLLOW = "/unfollow/";

    public static BoolResponse isFollowed(int user_id, int poster_id) {
        String path = WEB_ROOT + USERS + user_id + ISFOLLOWED;
        String param = "{"
                + "\"poster_id\": " + poster_id
                + "}";
        InputStream is = connection(path, param);
        if (is != null) {
            String content = getStringFormIs(is);
            if (content != null) return parseResponse(content);
//            else Log.e(TAG, "contents == null");
        } else {
//            Log.e(TAG, "InputStream == null");
        }
        return null;
    }

    public static BoolResponse follow(int user_id, int poster_id) {
        String path = WEB_ROOT + USERS + user_id + FOLLOW;
        String param = "{"
                + "\"poster_id\": " + poster_id
                + "}";
        InputStream is = connection(path, param);
        if (is != null) {
            String content = getStringFormIs(is);
            if (content != null) return parseResponse(content);
//            else Log.e(TAG, "contents == null");
        } else {
//            Log.e(TAG, "InputStream == null");
        }
        return null;
    }

    public static BoolResponse unfollow(int user_id, int poster_id) {
        String path = WEB_ROOT + USERS + user_id + UNFOLLOW;
        String param = "{"
                + "\"poster_id\": " + poster_id
                + "}";
        InputStream is = connection(path, param);
        if (is != null) {
            String content = getStringFormIs(is);
            if (content != null) return parseResponse(content);
//            else Log.e(TAG, "contents == null");
        } else {
//            Log.e(TAG, "InputStream == null");
        }
        return null;
    }


    private static BoolResponse parseResponse(String content) {
//        Log.e(TAG, "state =========== " + content);
        if (TextUtils.isEmpty(content)) return null;
        return JsonUtil.getEntity(content, BoolResponse.class);
    }
}
