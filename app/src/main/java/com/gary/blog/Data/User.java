package com.gary.blog.Data;

import java.io.Serializable;

/**
 * Created by hasee on 2016/12/12.
 */

public class User implements Serializable{

    private int id;
    private int postCount;
    private String username, email;
    private String lastSeen, memberSince;
    private String aboutMe, name, location;
    private String followedPostsURL, postsURL, gravatarURL;

    public int getId() {
        return id;
    }

    public int getPostCount() {
        return postCount;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public String getMemberSince() {
        return memberSince;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFollowedPostsURL() {
        return followedPostsURL;
    }

    public String getPostsURL() {
        return postsURL;
    }

    public String getGravatarURL() {
        return gravatarURL;
    }
}
