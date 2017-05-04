package com.gary.blog;

import com.gary.blog.Data.User;

/**
 * Created by hasee on 2016/12/15.
 */

public class Constant {
    //handle
    public static final String LOGIN = "login/";
    public static final String REGISTER = "register/";
    public static final String Posts = "posts/";
    public static final String USERS = "users/";
    public static final String ISFOLLOWED = "/isfollowed/";
    public static final String FOLLOW = "/follow/";
    public static final String UNFOLLOW = "/unfollow/";

    //Request
    public static final int PHOTO_GALLERY = 1;
    public static final int TAKE_PHOTO = 2;

    //Intent
    public static final String IMAGE_PATH = "image_path";

    public static User user = null;
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
}
