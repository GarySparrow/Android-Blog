package com.gary.blog.Notifications;

/**
 * Created by hasee on 2017/6/10.
 */

public class Message {

    private String message = "";

    public Message() {

    }

    public String newPostMessage() {
        message = "{" +
                "\"title\":\"Blog\"," +
                "\"content\":\"你关注的人发布了一条新消息！\"," +
                "\"builder_id\":0" +
                "}";
        return message;
    }


    public String newCommentMessage() {
        message = "{" +
                "\"title\":\"Blog\"," +
                "\"content\":\"你收到了新的评论！\"," +
                "\"builder_id\":0" +
                "}";
        return message;
    }

    public String buildMessage() {
        message = "{" +
                "\"title\":\"default\"," +
                "\"content\":\"default\"," +
                "\"builder_id\":0" +
                "}";
        return message;
    }

    public String setTitle(String title) {
        StringBuilder s = new StringBuilder(message);


        return s.toString();
    }

}
