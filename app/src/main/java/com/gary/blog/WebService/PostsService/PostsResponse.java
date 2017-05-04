package com.gary.blog.WebService.PostsService;

import com.gary.blog.Data.Post;

import java.util.ArrayList;

/**
 * Created by hasee on 2016/12/15.
 */

public class PostsResponse {
    private ArrayList<Post> posts;
    private boolean code;
//    private String prev, next;
//    private int count;

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public boolean isCode() {
        return code;
    }

    public void setCode(boolean code) {
        this.code = code;
    }

    //    public String getPrev() {
//        return prev;
//    }
//
//    public void setPrev(String prev) {
//        this.prev = prev;
//    }
//
//    public String getNext() {
//        return next;
//    }
//
//    public void setNext(String next) {
//        this.next = next;
//    }
//
//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }
}
