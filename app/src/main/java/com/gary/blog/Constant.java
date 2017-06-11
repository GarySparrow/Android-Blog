package com.gary.blog;

import com.gary.blog.Data.User;

import java.text.SimpleDateFormat;

/**
 * Created by hasee on 2016/12/15.
 */

public class Constant {
    //handle
    public static final String PUSH_SINGLE_ACCOUNT = "http://openapi.xg.qq.com/v2/push/single_account";
    public static final String PUSH_SINGLE_ACCOUNT_SECRET = "openapi.xg.qq.com/v2/push/single_account";
    public static final String PUSH_SINGLE_DEVICE = "http://openapi.xg.qq.com/v2/push/single_device";
    public static final String PUSH_SINGLE_DEVICE_SECRET = "openapi.xg.qq.com/v2/push/single_device";
    public static final String CHECK_PUSH_STATE = "http://openapi.xg.qq.com/v2/push/get_msg_status";
    public static final String CHECK_PUSH_STATE_SECRET = "openapi.xg.qq.com/v2/push/get_msg_status";


    public static final String LOGIN = "login/";
    public static final String REGISTER = "register/";
    public static final String POSTS = "posts/";
    public static final String USERS = "users/";
    public static final String ISFOLLOWED = "/isfollowed/";
    public static final String FOLLOW = "/follow/";
    public static final String UNFOLLOW = "/unfollow/";
    public static final String ISLIKED = "/isliked/";
    public static final String LIKE = "/like/";
    public static final String UNLIKE = "/unlike/";
    public static final String LIKE_COUNT = "/like_count/";
    public static final String UPDATE_INFO = "update_info/";
    public static final String FOLLOWED_POSTS = "/timeline/";

    //Request
    public static final int PHOTO_GALLERY = 1;
    public static final int TAKE_PHOTO = 2;

    //Intent
    public static final String IMAGE_PATH = "image_path";

    public static final int bkDic[] = {
            R.drawable.pure_c1, R.drawable.pure_c2, R.drawable.pure_c3};

    public static User user = null;
    public static String token = "";
    public static String secretKey = "780915adbd2dd9ab2195aaefb43ee09b";

    public final static int UpdateRecyclerView = 1;
    public final static int UpdateData = 2;
    public final static int UpdateIcon = 3;
    public final static int UserLogin = 4;
    public final static int PostEdit = 5;
    public final static int CommentEdit = 6;
    public final static int Toast = 7;
    public final static int SUCCESS = 8;
    public final static int FAILURE = 9;
    public final static int NewPost = 10;
    public final static int LoadingDialog = 11;
    public final static int NetProblem = 12;
    public final static int Follow = 14;
    public final static int Dialog = 15;


    public final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final static SimpleDateFormat psdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");

}
