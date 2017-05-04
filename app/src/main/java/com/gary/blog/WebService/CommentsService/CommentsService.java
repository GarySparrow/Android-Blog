package com.gary.blog.WebService.CommentsService;

import android.text.TextUtils;

import com.gary.blog.Utils.JsonUtil;

import java.io.InputStream;

import static com.gary.blog.Utils.WebUtil.connection;
import static com.gary.blog.Utils.WebUtil.getStringFormIs;

/**
 * Created by hasee on 2016/12/16.
 */

public class CommentsService {

    private static final String TAG = "CommentsService";

    private static final String WEB_ROOT =
            "https://garyflask.herokuapp.com/api/v1.0/";

    private static final String Comments = "comments/";

    public static CommentsResponse getComments(String path) {
        InputStream is = connection(path);
        if (is != null) {
            String content = getStringFormIs(is);
            if (content != null) return parseResponse(content);
//            else Log.e(TAG, "contents == null");
        } else {
//            Log.e(TAG, "InputStream == null");
        }
        return null;
    }

    public static CommentsResponse getComment() {
        String path = WEB_ROOT + Comments;
        InputStream is = connection(path);
        if (is != null) {
            String content = getStringFormIs(is);
            if (content != null) return parseResponse(content);
//            else Log.e(TAG, "contents == null");
        } else {
//            Log.e(TAG, "InputStream == null");
        }
        return null;
    }

    public static CommentsResponse newComment(String path, int authorId,
                                              String body) {
        String param = "{" +
                "\"body\": \"" + body + "\","
                + "\"author_id\": " + authorId
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

    private static CommentsResponse parseResponse(String content) {
//        Log.e(TAG, "state =========== " + content);
        if (TextUtils.isEmpty(content)) return null;
        return JsonUtil.getEntity(content, CommentsResponse.class);
    }
}
