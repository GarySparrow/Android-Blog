package com.gary.blog.WebService.PostsService;

import android.text.TextUtils;

import com.gary.blog.Data.Post;
import com.gary.blog.Data.User;
import com.gary.blog.Utils.JsonUtil;

import java.io.File;
import java.io.InputStream;

import static com.gary.blog.Utils.WebUtil.connection;
import static com.gary.blog.Utils.WebUtil.getStringFormIs;

/**
 * Created by hasee on 2016/12/15.
 */

public class PostsService {

    private static final String TAG = "PostsService";

    private static final String WEB_ROOT =
            "https://garyflask.herokuapp.com/api/v1.0/";

    private static final String Posts = "posts/";

    public static PostsResponse getPosts() {
        String path = WEB_ROOT + Posts;
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

    public static PostsResponse getPosts(String path) {
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

    public static PostsResponse newPost(String subject, int userId) {
        String path = WEB_ROOT + Posts;
        String param = "{" +
                "\"body\": \"" + subject + "\","
                + "\"id\": " + userId
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

    public static PostsResponse newPost(File file, String subject, int userId) {
        String path = WEB_ROOT + Posts + "withvideo";
        String param = "{" +
                "\"body\": \"" + subject + "\","
                + "\"id\": " + userId
                + "}";
        InputStream is = connection(path, file, param);
        if (is != null) {
            String content = getStringFormIs(is);
            if (content != null) return parseResponse(content);
//            else Log.e(TAG, "contents == null");
        } else {
//            Log.e(TAG, "InputStream == null");
        }
        return null;
    }

    public static PostsResponse editPost(int id, String body, int authorId) {
        String path = WEB_ROOT + Posts + id;
        String param = "{" +
                "\"author_id\": " + authorId + ","
                + "\"body\": \"" + body + "\""
                + "}";
        InputStream is = connection(path, param);
        if (is != null) {
            String content = getStringFormIs(is);
            if (content != null) {
                return parseResponse(content);
            }
//            else Log.e(TAG, "contents == null");
        } else {
//            Log.e(TAG, "InputStream == null");
        }
        return null;
    }

    private static PostsResponse parseResponse(String content) {
//        Log.e(TAG, "state =========== " + content);
        if (TextUtils.isEmpty(content)) return null;
        return JsonUtil.getEntity(content, PostsResponse.class);
    }
}
