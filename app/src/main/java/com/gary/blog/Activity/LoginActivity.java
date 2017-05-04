package com.gary.blog.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gary.blog.Constant;
import com.gary.blog.Data.User;
import com.gary.blog.R;
import com.gary.blog.Utils.JsonUtil;
import com.gary.blog.Utils.NetWorkUtil;
import com.gary.blog.WebService.BaseClient;
import com.gary.blog.Wedgit.ImgEditText;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.gary.blog.Constant.user;

/**
 * Created by hasee on 2016/12/10.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private Toolbar toolbar;
    private TextView registerBt;
    private Button loginBt;
    private ImgEditText emailEdit, pswEdit;
    private SweetAlertDialog loadingDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.UserLogin:
//                    UserResponse userResponse = (UserResponse) msg.obj;
//                    if (userResponse == null) {
//                        Toast.makeText(LoginActivity.this, "服务器无响应",
//                                Toast.LENGTH_SHORT).show();
//                    } else {
////                                Log.e(TAG, "user ========== " + userResponse.toString());
//                        if (userResponse == null) {
//                            Toast.makeText(LoginActivity.this, "响应为空",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            User user = userResponse.getUser();
//                            if (user == null && user != null) {
//                                Toast.makeText(LoginActivity.this, "登录成功",
//                                        Toast.LENGTH_SHORT).show();
//                                Intent intent = MainActivity.newIntent(LoginActivity.this);
//                                intent.putExtra("user", user);
//                                startActivity(intent);
//                            } else {
//                                Toast.makeText(LoginActivity.this, "账号或密码错误",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initView();
        initEvent();
    }

    private void initView() {
        loginBt = (Button) findViewById(R.id.login);
        registerBt = (TextView) findViewById(R.id.register);
        emailEdit = (ImgEditText) findViewById(R.id.user_account);
        pswEdit = (ImgEditText) findViewById(R.id.user_psw);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.setTitleText("Loading...");

        //Debug
        emailEdit.setText("690131179@qq.com");
        pswEdit.setText("cat");
        pswEdit.setTextInvisiable();
    }

    private void initEvent() {

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) return;
                String username = emailEdit.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(LoginActivity.this, "账号不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String psw = pswEdit.getText().toString();
                if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(LoginActivity.this, "密码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                login(username, psw);
            }
        });

        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisterActivity.newIntent(LoginActivity.this));
            }
        });
    }

    private void login(String username, String psw) {
        if (NetWorkUtil.isNetWorkOpened(LoginActivity.this)) {
            RequestParams params = new RequestParams();
            params.put("email", username);
            params.put("psw", psw);
            BaseClient.post(Constant.LOGIN, params, new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    super.onStart();
                    loadingDialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    User user = null;
                    try {
                        user = JsonUtil.getEntity(response.getString("user"), User.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, "登录成功",
                            Toast.LENGTH_SHORT).show();

                    Constant.user = user;
                    Intent intent = MainActivity.newIntent(LoginActivity.this);
                    startActivity(intent);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Toast.makeText(LoginActivity.this, "登录失败",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    loadingDialog.dismiss();
                }
            });
//                    Thread t = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            UserResponse response = UserService.login(username, psw);
//                            Message msg = new Message();
//                            msg.obj = response;
//                            msg.what = Constant.UserLogin;
//                            handler.sendMessage(msg);
//                        }
//                    });
//                    t.start();
        } else {
            Toast.makeText(LoginActivity.this, "请检查你的网络",
                    Toast.LENGTH_SHORT).show();
        }
    }
}