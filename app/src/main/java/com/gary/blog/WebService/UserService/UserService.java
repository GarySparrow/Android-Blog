package com.gary.blog.WebService.UserService;

import android.text.TextUtils;

import com.gary.blog.Utils.JsonUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.InputStream;

import static com.gary.blog.Utils.WebUtil.connection;
import static com.gary.blog.Utils.WebUtil.getStringFormIs;

/**
 * Created by hasee on 2016/12/14.
 */

public class UserService {
    private static final String TAG = "UserService";

//    private static final String WEB_ROOT =
//            "http://127.0.0.1:5000/api/v1.0/";

    private static final String WEB_ROOT =
            "https://garyflask.herokuapp.com/api/v1.0/";

    private static final String LOGIN = "login/";
    private static final String REGISTER = "register/";
    private static final String USERS = "users/";

    public static UserResponse login(String email, String password) {
        String path = WEB_ROOT + LOGIN;
//        String param = "email=" + email + "&psw=" + password;
        String param = "{"
                + "\"email\": \"" + email + "\","
                + "\"psw\": \"" + password + "\""
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

    public static UserResponse register(String email, String psw, String username) {
        String path = WEB_ROOT + REGISTER;
        String param = "{"
                + "\"email\": \"" + email + "\","
                + "\"psw\": \"" + psw + "\","
                + "\"username\": \"" + username + "\""
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

    public static UserResponse getUsers(String path) {
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


    private static UserResponse parseResponse(String content) {
//        Log.e(TAG, "state =========== " + content);
        if (TextUtils.isEmpty(content)) return null;
        return JsonUtil.getEntity(content, UserResponse.class);
    }
}
