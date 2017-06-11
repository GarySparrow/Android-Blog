package com.gary.blog.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.gary.blog.Constant;
import com.gary.blog.Data.User;
import com.gary.blog.R;
import com.gary.blog.Utils.JsonUtil;
import com.gary.blog.Utils.NetWorkUtil;
import com.gary.blog.WebService.BaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by hasee on 2017/4/29.
 */

public class RegisterActivity extends AppCompatActivity{

    private MaterialEditText emailText, usernameText, pswText, checkpswText;
//    private Button finish;
    private Toolbar toolbar;
    private SweetAlertDialog loadingDialog;
    private TextView titleText;


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailText = (MaterialEditText) findViewById(R.id.user_email);
        usernameText = (MaterialEditText) findViewById(R.id.user_username);
        pswText = (MaterialEditText) findViewById(R.id.user_psw);
        checkpswText = (MaterialEditText) findViewById(R.id.user_checkpsw);
        titleText = (TextView) findViewById(R.id.toolbar_left_text);
        titleText.setText("注册");
//        finish = (Button) findViewById(R.id.finish);

        pswText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        checkpswText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.setTitleText("Loading...");

//        finish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_done:
                String email = emailText.getText().toString();
                final String psw = pswText.getText().toString();
                String checkPsw = checkpswText.getText().toString();
                final String username = usernameText.getText().toString();

                if (username.length() < 1 || psw.length() < 1 ||
                        email.length() < 1 || checkPsw.length() < 1) {
                    Toast.makeText(this, "请填写完整信息...", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (!checkPsw.equals(psw)) {
                    Toast.makeText(this, "两次密码输入不一致...", Toast.LENGTH_SHORT).show();
                    break;
                }

                RequestParams params = new RequestParams();
                params.put("email", email);
                params.put("psw", psw);
                params.put("username", username);
                BaseClient.post(Constant.REGISTER, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        loadingDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Toast.makeText(RegisterActivity.this, "注册成功! 请在邮箱确认", Toast.LENGTH_SHORT).show();
//                        login(username, psw);
                        Intent i = LoginActivity.newIntent(RegisterActivity.this);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Toast.makeText(RegisterActivity.this, "注册失败... ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        loadingDialog.dismiss();
                    }
                });
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void login(String username, String psw) {
        if (NetWorkUtil.isNetWorkOpened(RegisterActivity.this)) {
            RequestParams params = new RequestParams();
            params.put("email", username);
            params.put("psw", psw);
            BaseClient.post(Constant.LOGIN, params, new JsonHttpResponseHandler() {

                @Override
                public void onStart() {
                    super.onStart();
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
                    Toast.makeText(RegisterActivity.this, "登录成功",
                            Toast.LENGTH_SHORT).show();

                    Constant.user = user;
                    Intent intent = MainActivity.newIntent(RegisterActivity.this);
                    startActivity(intent);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Toast.makeText(RegisterActivity.this, "登录失败",
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
            loadingDialog.dismiss();
            Toast.makeText(RegisterActivity.this, "请检查你的网络",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

