package com.gary.blog.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gary.blog.Data.DataManager;
import com.gary.blog.Data.Post;
import com.gary.blog.R;
import com.gary.blog.Utils.JsonUtil;
import com.gary.blog.WebService.BaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.gary.blog.Constant.POSTS;

/**
 * Created by hasee on 2017/4/30.
 */

public class LaunchActivity extends AppCompatActivity{

    public static final int REQUEST_UPDATE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        loadingThread();
    }

    private void loadingThread() {

        BaseClient.get(POSTS, null, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                ArrayList<Post> posts = null;
                try {
                    posts = JsonUtil.getEntityList(
                            response.getString("posts"), Post.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DataManager.getInstance(LaunchActivity.this).setPosts(posts);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Intent i = MainActivity.newIntent(LaunchActivity.this);
                startActivity(i);
            }
        });
    }

}
