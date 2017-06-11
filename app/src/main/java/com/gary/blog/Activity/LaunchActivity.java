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
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.gary.blog.Constant.POSTS;

/**
 * Created by hasee on 2017/4/30.
 */

public class LaunchActivity extends AppCompatActivity{

    private static final String TAG = "LaunchActivity";

    public static final int REQUEST_UPDATE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        // 开启logcat输出，方便debug，发布时请关闭
        XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback);
        //带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
//        Context context = getApplicationContext();
//        XGPushManager.registerPush(context);


        XGPushManager.registerPush(getApplicationContext());



        // 其它常用的API：
        // 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account, XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
        // 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
        // 反注册（不再接收消息，用户没有业务要求，尽量不要调用此接口）：unregisterPush(context)
        // 设置标签：setTag(context, tagName)
        // 删除标签：deleteTag(context, tagName)

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
