package com.gary.blog.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hasee on 2016/12/9.
 */

public class Post implements Serializable{

    private ArrayList<String> imgsPath = new ArrayList<>();
    private String videoPath;
    private String subject;
    private String authorURL;
    private String commentsURL;
    private String timeStamp;
    private int commentCount;
    private int id;

    public Post() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAuthorURL() {
        return authorURL;
    }

    public void setAuthorURL(String authorURL) {
        this.authorURL = authorURL;
    }

    public String getCommentsURL() {
        return commentsURL;
    }

    public void setCommentsURL(String commentsURL) {
        this.commentsURL = commentsURL;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "body=" + subject;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public ArrayList<String> getImgsPath() {
        return imgsPath;
    }

    public void setImgsPath(ArrayList<String> imgsPath) {
        this.imgsPath = imgsPath;
    }
}
