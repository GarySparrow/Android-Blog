package com.gary.blog.WebService.CommentsService;

import com.gary.blog.Data.Comment;

import java.util.ArrayList;

/**
 * Created by hasee on 2016/12/16.
 */

public class CommentsResponse {
    private ArrayList<Comment> comments;
    private boolean code;

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public boolean isCode() {
        return code;
    }

    public void setCode(boolean code) {
        this.code = code;
    }
}
