package com.gary.blog.Data;


import android.content.Context;

import java.util.ArrayList;

/**
 * Created by hasee on 2016/12/9.
 */

public class DataManager {

    private ArrayList<User> users;
    private ArrayList<Post> posts;
    private Context context;
    private static DataManager dataManager;

    private DataManager(Context context) {
        this.context = context;
        posts = new ArrayList<>();
        users = new ArrayList<>();
    }

    public static DataManager getInstance (Context context) {
        if (dataManager != null) return dataManager;
        return dataManager = new DataManager(context);
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void addPost (Post post) {
        posts.add(post);
    }

    public void deletePost (Post post) {
        posts.remove(post);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
