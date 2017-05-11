package com.gary.blog.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gary.blog.Constant;
import com.gary.blog.Data.User;
import com.gary.blog.R;
import com.gary.blog.Utils.JsonUtil;
import com.gary.blog.WebService.BaseClient;
import com.gary.blog.Wedgit.CircleImage;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.gary.blog.Constant.FOLLOW;
import static com.gary.blog.Constant.ISFOLLOWED;
import static com.gary.blog.Constant.UNFOLLOW;
import static com.gary.blog.Constant.USERS;

/**
 * Created by hasee on 2016/12/23.
 */

public class UserInfoActivity extends AppCompatActivity{

    private User user;

    private SweetAlertDialog loadingDialog;

    private Toolbar toolbar;
    private FloatingActionButton edit_bt;
    private MaterialEditText locationText, aboutmeText, nameText;
    private TextView usernameText;
    private ShineButton followeState;
    private CircleImage userIcon;

    final RequestParams params = new RequestParams();

    private boolean mfollowed;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.Follow:
//                    Boolean flag = (Boolean) msg.obj;
//                    if (flag) {
//                        Bitmap bitmap = BitmapFactory.decodeResource(UserInfoActivity.this.getResources()
//                                , R.drawable.follow);
//                        followeState.setImageBitmap(bitmap);
//                    } else {
//                        Bitmap bitmap = BitmapFactory.decodeResource(UserInfoActivity.this.getResources()
//                                , R.drawable.unfollow);
//                        followeState.setImageBitmap(bitmap);
//                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        locationText = (MaterialEditText) findViewById(R.id.user_location);
        aboutmeText = (MaterialEditText) findViewById(R.id.user_aboutme);
        userIcon = (CircleImage) findViewById(R.id.user_icon);
        followeState = (ShineButton) findViewById(R.id.follow_state);
        nameText = (MaterialEditText) findViewById(R.id.user_name);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        edit_bt = (FloatingActionButton) findViewById(R.id.edit_bt);
        usernameText = (TextView) toolbar.findViewById(R.id.toolbar_text);
        usernameText.setTextColor(this.getResources().getColor(R.color
            .white));
        toolbar.setTitle("");
        loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        loadingDialog.setTitleText("Loading...");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameText.setEnabled(false);
//        nameText.setFocusable(false);
        locationText.setEnabled(false);
//        locationText.setFocusable(false);
        aboutmeText.setEnabled(false);
//        aboutmeText.setFocusable(false);

        edit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameText.isEnabled()) {
                    //edit_info
                    RequestParams upParams = new RequestParams();
                    upParams.put("location", locationText.getText().toString());
                    upParams.put("about_me", aboutmeText.getText().toString());
                    upParams.put("name", nameText.getText().toString());
                    BaseClient.post("update_info/" + Constant.user.getId() + "/",
                            upParams, new JsonHttpResponseHandler() {
                                boolean code = false;

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    try {
                                        code = JsonUtil.getEntity(response.getString("code"), Boolean.class);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    if (code) {
                                        Toast.makeText(UserInfoActivity.this, "修改成功!",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(UserInfoActivity.this, "修改失败...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                    Toast.makeText(UserInfoActivity.this, "修改失败...",
                                            Toast.LENGTH_SHORT).show();
                                }

                            });
                }

//                nameText.setFocusable(!nameText.isFocusable());
                nameText.setEnabled(!nameText.isEnabled());
//                nameText.setFocusableInTouchMode(true);
//                locationText.setFocusable(!locationText.isFocusable());
                locationText.setEnabled(!locationText.isEnabled());
//                locationText.setFocusableInTouchMode(true);
//                aboutmeText.setFocusable(!aboutmeText.isFocusable());
                aboutmeText.setEnabled(!aboutmeText.isEnabled());
//                aboutmeText.setFocusableInTouchMode(true);
            }
        });

        if (getIntent().hasExtra("user")) {
            user = (User)getIntent().getSerializableExtra("user");
            params.put("poster_id", user.getId());
        }
        if (user != null) {
            initView();
        }

    }



    private void initView() {
        if (Constant.user == null || user == null || user.getId() == Constant.user.getId()) {
            followeState.setVisibility(View.GONE);
            if (Constant.user != null && user != null &&
                    user.getId() == Constant.user.getId()) {
                edit_bt.setVisibility(View.VISIBLE);
            }
        } else {
            BaseClient.get(USERS + Constant.user.getId() + ISFOLLOWED, params, new JsonHttpResponseHandler() {

                Boolean followed;

                @Override
                public void onStart() {
                    super.onStart();
//                    loadingDialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        followed = JsonUtil.getEntity(response.getString("followed"), Boolean.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mfollowed = followed;
                    followeState.setChecked(followed);
//                    Message msg = new Message();
//                    msg.obj = code;
//                    msg.what = Constant.Follow;
//                    handler.sendMessage(msg);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
//                    loadingDialog.dismiss();
                }
            });
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Message msg = new Message();
//                    msg.obj = BoolService.isFollowed(Constant.user.getId(), user.getId());
//                    msg.what = Constant.Followed;
//                    handler.sendMessage(msg);
//                }
//            });
//            t.start();
        }


        if (user != null) {
            usernameText.setText(user.getUsername());

            nameText.setEnabled(true);
            locationText.setEnabled(true);
            aboutmeText.setEnabled(true);

            nameText.setText(user.getName());
            locationText.setText(user.getLocation());
            aboutmeText.setText(user.getAboutMe());

            nameText.setEnabled(false);
            locationText.setEnabled(false);
            aboutmeText.setEnabled(false);

            followeState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mfollowed = !mfollowed;
                    if (!mfollowed) {
                        BaseClient.post(USERS + Constant.user.getId() + UNFOLLOW,
                                params, new JsonHttpResponseHandler() {
                                    Boolean followed;

                                    @Override
                                    public void onStart() {
                                        super.onStart();
//                                        loadingDialog.show();
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        try {
                                            followed = JsonUtil.getEntity(response.getString("followed"), Boolean.class);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

//                                        followeState.setChecked(followed);
//                                        Message msg = new Message();
//                                        msg.obj = code;
//                                        msg.what = Constant.Follow;
//                                        handler.sendMessage(msg);

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        super.onFailure(statusCode, headers, responseString, throwable);

                                    }

                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
//                                        loadingDialog.dismiss();
                                    }
                                });
//                        Thread t = new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Message msg = new Message();
//                                msg.obj = BoolService.unfollow(Constant.user.getId(), user.getId());
//                                msg.what = Constant.Follow;
//                                handler.sendMessage(msg);
//                            }
//                        });
//                        t.start();
                    } else {
                        BaseClient.post(USERS + Constant.user.getId() + FOLLOW,
                                params, new JsonHttpResponseHandler() {
                                    Boolean followed;

                                    @Override
                                    public void onStart() {
                                        super.onStart();
//                                        loadingDialog.show();
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        try {
                                            followed = JsonUtil.getEntity(response.getString("followed"), Boolean.class);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

//                                        followeState.setChecked(followed);
//                                        Message msg = new Message();
//                                        msg.obj = code;
//                                        msg.what = Constant.Follow;
//                                        handler.sendMessage(msg);

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        super.onFailure(statusCode, headers, responseString, throwable);
                                    }

                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
//                                        loadingDialog.dismiss();
                                    }
                                });
                    }
                }
            });
            ImageLoader.getInstance().displayImage(user.getGravatarURL(), userIcon);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
