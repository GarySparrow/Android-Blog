package com.gary.blog.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;
import com.gary.blog.Constant;
import com.gary.blog.Data.Comment;
import com.gary.blog.Data.Post;
import com.gary.blog.Data.User;
import com.gary.blog.Dialog.LoadingDialog;
import com.gary.blog.R;
import com.gary.blog.Utils.JsonUtil;
import com.gary.blog.WebService.BaseClient;
import com.gary.blog.Wedgit.CircleImage;
import com.gary.blog.Wedgit.MyRecyclerView;
import com.gary.blog.Wedgit.MyRefreshLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.gary.blog.Constant.FAILURE;
import static com.gary.blog.Constant.SUCCESS;

/**
 * Created by hasee on 2016/12/16.
 */

public class PostActivity extends AppCompatActivity{

    private static final String TAG = "PostActivity";
    private static final int EDIT = R.id.edit_post;
    private static final int DONE = R.id.edit_done;

    private GridLayout imgsLayout;
    private LinearLayout emptyView;
    private CircleImage posterIcon;
    private TextView posterName, postTime, toolbarText;
    private MyRefreshLayout refreshLayout;
    private EditText postSubject;
    private MyRecyclerView recyclerView;
    private TextView newComment;
    private Menu menu;
    private LinearLayout posterLayout;
    private RelativeLayout videoView;
    private Toolbar toolbar;
    private FloatingActionButton floatingButton;
    private SweetAlertDialog loadingDialog;

    private CommentAdapter commentAdapter;
    private adapterObserver observer;

    private SwipeRefreshLayout.OnRefreshListener listener;

    private User poster;
    private Post post;
    private ArrayList<Comment> comments;
    private boolean isRefresh;
    private String preSubject;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
//                case Constant.UpdateRecyclerView:
//                    CommentsResponse commentsResponse = (CommentsResponse) msg.obj;
//                    if (commentsResponse == null) return ;
//                    comments = commentsResponse.getComments();
//                    if (comments != null) {
//                        commentAdapter.setComments(comments);
//                    }
//                    commentAdapter.notifyDataSetChanged();
//                    refreshLayout.setRefreshing(false);
//                    isRefresh = false;
//                    break;
//                case Constant.UpdateData:
//                    UserResponse userResponse = (UserResponse) msg.obj;
//                    if (userResponse != null) {
//                        poster = userResponse.getUser();
//                        if (poster != null) {
//                            if (Constant.user != null &&
//                                    poster.getId() == Constant.user.getId()) {
//                                for (int i = 0; i < menu.size(); i++) {
//                                    if (menu.getItem(i).getItemId() == EDIT) {
//                                        menu.getItem(i).setEnabled(true);
//                                        menu.getItem(i).setVisible(true);
//                                        break;
//                                    }
//                                }
//                            } else {
//                                for (int i = 0; i < menu.size(); i++) {
//                                    if (menu.getItem(i).getItemId() == EDIT ||
//                                            menu.getItem(i).getItemId() == DONE) {
//                                        menu.getItem(i).setEnabled(false);
//                                        menu.getItem(i).setVisible(false);
//                                    }
//                                }
//                            }
//                            posterName.setText(poster.getUsername());
//                            Thread t = new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (NetWorkUtil.isNetWorkOpened(PostActivity.this)) {
//                                        Bitmap bitmap = BitmapService.getBitmap(poster.getGravatarURL());
//                                        Message msg = new Message();
//                                        msg.what = Constant.UpdateIcon;
//                                        msg.obj = bitmap;
//                                        handler.sendMessage(msg);
//                                    }
//                                }
//                            });
//                            t.start();
//                        }
//                    }
//                    break;
//                case Constant.UpdateIcon:
//                    Bitmap bitmap = (Bitmap)msg.obj;
//                    if (bitmap != null) {
//                        posterIcon.setImageBitmap(bitmap);
//                    }
//                    break;
//                case Constant.PostEdit:
//                    if (menu != null) {
//                        switch (msg.arg1) {
//                            case SUCCESS:
//                                handler.sendEmptyMessage(Constant.LoadingDialog);
//                                Toast.makeText(PostActivity.this,
//                                        "修改成功", Toast.LENGTH_SHORT).show();
//                                break;
//                            case FAILURE:
//                                handler.sendEmptyMessage(Constant.LoadingDialog);
//                                if (preSubject != null && postSubject != null) {
//                                    postSubject.setText(preSubject);
//                                }
//                                Toast.makeText(PostActivity.this,
//                                        "修改失败", Toast.LENGTH_SHORT).show();
//
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                    break;
//                case Constant.CommentEdit:
//                    PopupWindow popupWindow = (PopupWindow) msg.obj;
//                    switch (msg.arg1) {
//                        case SUCCESS:
//                            handler.sendEmptyMessage(Constant.LoadingDialog);
//                            if (popupWindow != null) {
//                                popupWindow.dismiss();
//                            }
//                            Toast.makeText(PostActivity.this,
//                                    "发布成功!", Toast.LENGTH_SHORT).show();
//                            updateUI();
//                            break;
//                        case FAILURE:
//                            handler.sendEmptyMessage(Constant.LoadingDialog);
//                            if (popupWindow != null) {
//                                popupWindow.dismiss();
//                            }
//                            Toast.makeText(PostActivity.this,
//                                    "发布失败!", Toast.LENGTH_SHORT).show();
//                            break;
//                        default:
//                            break;
//                    }
//                    break;
//                case Constant.LoadingDialog:
//                    LoadingDialog.closeDialog();
//                    break;
                case Constant.Toast:
                    String s = (String) msg.obj;
                    Toast.makeText(PostActivity.this, s, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

//        emptyView = (LinearLayout) findViewById(R.id.empty_view);
//        refreshLayout = (MyRefreshLayout) findViewById(R.id.refresh_layout);
        posterLayout = (LinearLayout) findViewById(R.id.poster_layout);
        posterIcon = (CircleImage) findViewById(R.id.poster_icon);
        posterName = (TextView) findViewById(R.id.poster_name);
        postTime = (TextView) findViewById(R.id.post_time);
        postSubject = (EditText) findViewById(R.id.post_subject);
        recyclerView = (MyRecyclerView) findViewById(R.id.post_comments);
//        newComment = (TextView) findViewById(R.id.new_comment);
        videoView = (RelativeLayout) findViewById(R.id.video_layout);
        imgsLayout = (GridLayout) findViewById(R.id.imgs_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingButton = (FloatingActionButton) findViewById(R.id.post_comment);
        toolbarText = (TextView) toolbar.findViewById(R.id.toolbar_text);
        toolbar.setTitle("");

        loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.setTitleText("loading...");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        recyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(PostActivity.this,
//                DividerItemDecoration.VERTICAL));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        newComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Constant.user != null) {
//                    showPopupWindow(v);
//                } else {
//                    Toast.makeText(PostActivity.this,
//                            "请先登录", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        posterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (poster != null) {
                    Intent intent = UserInfoActivity.newIntent(PostActivity.this);
                    intent.putExtra("user", poster);
                    startActivity(intent);
                }
            }
        });

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.user == null) {
                    Message msg = new Message();
                    msg.what = Constant.Toast;
                    msg.obj = new String("尚未登录...");
                    handler.sendMessage(msg);
                    return ;
                }
                final AlertDialog dialog = new AlertDialog.Builder(PostActivity.this)
                        .setTitle("评论")
                        .setCancelable(true)
                        .setView(R.layout.dialog_comment)
                        .setPositiveButton("发布", null).create();
                dialog.show();

                Window window = dialog.getWindow();

                final MaterialEditText text = (MaterialEditText) window.findViewById(R.id.comment_text);

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String s = text.getText().toString();
                        if (s.length() < 1) {
                            Message msg = new Message();
                            msg.obj = new String("输入不合法...");
                            msg.what = Constant.Toast;
                            handler.sendMessage(msg);
                            return ;
                        }
                        RequestParams params = new RequestParams();
                        params.put("author_id", Constant.user.getId());
                        params.put("body", s);
                        BaseClient.postAbs(post.getCommentsURL(), params, new JsonHttpResponseHandler() {
                            Boolean code;

                            @Override
                            public void onStart() {
                                super.onStart();
                                loadingDialog.show();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    code = JsonUtil.getEntity(response.getString("code"), Boolean.class);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Message msg = new Message();
                                msg.obj = new String("发布成功!");
                                msg.what = Constant.Toast;
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                Message msg = new Message();
                                msg.obj = new String("发布失败...");
                                msg.what = Constant.Toast;
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                loadingDialog.dismiss();
                                dialog.dismiss();
                                refreshComment();
                            }
                        });
                    }
                });
            }
        });


        updateUI();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshComment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.edit_post:
//                preSubject = postSubject.getText().toString();
//                for (int i = 0; i < menu.size(); i++) {
//                    if (menu.getItem(i).getItemId() == EDIT) {
//                        menu.getItem(i).setEnabled(false);
//                        menu.getItem(i).setVisible(false);
//                    } else if (menu.getItem(i).getItemId() == DONE) {
//                        menu.getItem(i).setVisible(true);
//                        menu.getItem(i).setEnabled(true);
//                    }
//                }
//                postSubject.setEnabled(true);
//                break;
//            case R.id.edit_done:
//                LoadingDialog.showWaitDialog(PostActivity.this);
//                postSubject.setEnabled(false);
//                for (int i = 0; i < menu.size(); i++) {
//                    if (menu.getItem(i).getItemId() == EDIT) {
//                        menu.getItem(i).setEnabled(true);
//                        menu.getItem(i).setVisible(true);
//                    } else if (menu.getItem(i).getItemId() == DONE) {
//                        menu.getItem(i).setVisible(false);
//                        menu.getItem(i).setEnabled(false);
//                    }
//                }
//                RequestParams requestParams = new RequestParams();
//                requestParams.put("author_id", Integer.toString(Constant.user.getId()));
//                requestParams.put("body", postSubject.getText().toString());

                // waiting for updating: a JsonString convert class needed here!
//                String param = "{" +
//                        "\"author_id\": " + Constant.user.getId() + ","
//                        + "\"body\": \"" + JsonUtil.ready4Json(postSubject.getText().toString()) + "\""
//                        + "}";
//                RequestParams params = new RequestParams();
//                params.put("author_id", Integer.toString(Constant.user.getId()));
//                params.put("body", postSubject.getText().toString());
//                BaseClient.post(Posts + post.getId(), params, new JsonHttpResponseHandler() {
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        super.onSuccess(statusCode, headers, response);
//                        handler.sendEmptyMessage(Constant.LoadingDialog);
//                        Toast.makeText(PostActivity.this,
//                                "修改成功", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        super.onFailure(statusCode, headers, responseString, throwable);
//                        handler.sendEmptyMessage(Constant.LoadingDialog);
//                        if (preSubject != null && postSubject != null) {
//                            postSubject.setText(preSubject);
//                        }
//                        Toast.makeText(PostActivity.this,
//                                "修改失败", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//                Thread t = new Thread( new Runnable() {
//                    @Override
//                    public void run() {
//                        PostsResponse postsResponse = PostsService.editPost(post.getId(),
//                                postSubject.getText().toString(), Constant.user.getId());
//                        if (postsResponse != null) {
//                            ArrayList<Post> posts = postsResponse.getPosts();
//                            if (posts != null) {
//                                Message msg = new Message();
//                                msg.arg1 = SUCCESS;
//                                msg.what = Constant.PostEdit;
//                                handler.sendMessage(msg);
//                            } else {
//                                Message msg = new Message();
//                                msg.arg1 = FAILURE;
//                                msg.what = Constant.PostEdit;
//                                handler.sendMessage(msg);
//                            }
//                        } else {
//                            Message msg = new Message();
//                            msg.arg1 = FAILURE;
//                            msg.what = Constant.PostEdit;
//                            handler.sendMessage(msg);
//                        }
//                    }
//                });
//                t.start();
//                break;
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }

    //custom methods

    private void init() {
        observer = new adapterObserver();
        commentAdapter = new CommentAdapter(new ArrayList<Comment>());
        commentAdapter.registerAdapterDataObserver(observer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(commentAdapter);

        refreshComment();
    }

    private void refreshComment() {
        BaseClient.getAbs(post.getCommentsURL(), null,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        ArrayList<Comment> comments = null;
                        try {
                            comments = JsonUtil.getEntityList(
                                    response.getString("comments"), Comment.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (comments == null) {
                            Log.e(TAG, "comments == null");
                        } else {
                            commentAdapter.setComments(comments);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
    }

    private void updateUI() {

//        if (comments == null) {
//            comments = new ArrayList<>();
//        }
//
//        if (observer == null) {
//            observer = new adapterObserver();
//        }
//
//        if (commentAdapter == null) {
//            commentAdapter = new CommentAdapter(comments);
//            commentAdapter.registerAdapterDataObserver(observer);
//        }
//
//
//        if (recyclerView != null && recyclerView.getAdapter() == null) {
//            recyclerView.setAdapter(commentAdapter);
//        }
//
//        isRefresh = false;
//
//        listener = new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (isRefresh) {
//                    Toast.makeText(PostActivity.this, "正在加载",
//                            Toast.LENGTH_SHORT);
//                } else {
//                    BaseClient.getAbs(post.getCommentsURL(), null, new JsonHttpResponseHandler() {
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                            super.onSuccess(statusCode, headers, response);
//                            try {
//                                comments = JsonUtil.getEntityList(response.getString("comments"), Comment.class);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            commentAdapter.setComments(comments);
//                            commentAdapter.notifyDataSetChanged();
//                            refreshLayout.setRefreshing(false);
//                            isRefresh = false;
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                            super.onFailure(statusCode, headers, responseString, throwable);
//                            refreshLayout.setRefreshing(false);
//                            isRefresh = false;
//                        }
//                    });
//                    Thread thread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (NetWorkUtil.isNetWorkOpened(PostActivity.this)) {
//                                isRefresh = true;
//                                CommentsResponse response = CommentsService.getComments(post.getCommentsURL());
//                                Message message = new Message();
//                                message.obj = response;
//                                message.what = Constant.UpdateRecyclerView;
//                                handler.sendMessage(message);
//                            }
//                        }
//                    });
//                    thread.start();
//                }
//            }
//        };

//        if (refreshLayout != null) {
//            refreshLayout.setOnRefreshListener(listener);
//
//            refreshLayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    refreshLayout.setRefreshing(true);
//                    listener.onRefresh();
//                }
//            });
//        }

        if (post == null) {
            post = (Post) getIntent().getSerializableExtra("post");
        }

        if (post != null) {
            postSubject.setText(post.getSubject());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(post.getTimeStamp());
            String dateString = sdf.format(date);
            postTime.setText(dateString);

            if (post.getVideoPath() != null) {
                videoView.setVisibility(View.VISIBLE);
                String videoPath = post.getVideoPath().toString();
                PlayerView video = new PlayerView(this, videoView)
                        .setTitle("Video")
                        .setScaleType(PlayStateParams.fitparent)
                        .forbidTouch(false)
                        .hideMenu(true)
                        .setPlaySource(videoPath);
//                        .startPlay();
            }

            if (post.getImgsPath() != null) {
                imgsLayout.setVisibility(View.VISIBLE);
                final ArrayList<String> path = post.getImgsPath();
                imgsLayout.removeAllViews();
                for (int i = 0; i < path.size(); i++) {
                    final int idx = i;
                    ImageView img = new ImageView(this);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.height = 100; params.width = 100;
                    params.leftMargin = params.topMargin = params.bottomMargin = params.rightMargin = 10;
                    img.setLayoutParams(params);
                    imgsLayout.addView(img);
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = ImageActivity.newIntent(PostActivity.this);
                            intent.putExtra(Constant.IMAGE_PATH, path.get(idx));
                            startActivity(intent);
                        }
                    });
                    ImageLoader.getInstance().displayImage(path.get(i), img);
                }
            }

            BaseClient.getAbs(post.getAuthorURL(), null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        poster = JsonUtil.getEntity(response.getString("user"), User.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    for (int i = 0; i < menu.size(); i++) {
//                        if (menu.getItem(i).getItemId() == EDIT) {
//                            menu.getItem(i).setEnabled(true);
//                            menu.getItem(i).setVisible(true);
//                            break;
//                        }
//                    }
                    posterName.setText(poster.getUsername());
                    ImageLoader.getInstance().displayImage(poster.getGravatarURL(), posterIcon);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
//                    for (int i = 0; i < menu.size(); i++) {
//                        if (menu.getItem(i).getItemId() == EDIT ||
//                                menu.getItem(i).getItemId() == DONE) {
//                            menu.getItem(i).setEnabled(false);
//                            menu.getItem(i).setVisible(false);
//                        }
//                    }
                }
            });

//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    if (NetWorkUtil.isNetWorkOpened(PostActivity.this)) {
//                        UserResponse response = UserService.getUsers(post.getAuthorURL());
//                        Message msg = new Message();
//                        msg.what = Constant.UpdateData;
//                        msg.obj = response;
//                        handler.sendMessage(msg);
//                    }
//                }
//            });
//            t.start();
        }

    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, PostActivity.class);
        return intent;
    }

    private void checkIfEmpty() {
        if (recyclerView.getAdapter() != null) {
            boolean visible = recyclerView.getAdapter().getItemCount() == 0;
            recyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);
            emptyView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(PostActivity.this)
                .inflate(R.layout.activity_write_comment, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, true);

        final EditText subject = (EditText) contentView.findViewById(R.id.edit_comment);
        Button commit = (Button) contentView.findViewById(R.id.edit_done);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String body = subject.getText().toString();
//                String param = "{" +
//                        "\"body\": \"" + JsonUtil.ready4Json(body) + "\","
//                        + "\"author_id\": " + Constant.user.getId()
//                        + "}";
                RequestParams params = new RequestParams();
                params.put("author_id", Constant.user.getId());
                params.put("body", subject.getText().toString());
                BaseClient.postAbs(post.getCommentsURL(), params, new JsonHttpResponseHandler() {
                    Boolean code;

                    @Override
                    public void onStart() {
                        super.onStart();
                        LoadingDialog.showWaitDialog(PostActivity.this);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            code = JsonUtil.getEntity(response.getString("code"), Boolean.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (code) {
                            Message msg = new Message();
                            msg.what = Constant.CommentEdit;
                            msg.arg1 = SUCCESS;
                            msg.obj = popupWindow;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = Constant.CommentEdit;
                            msg.arg1 = FAILURE;
                            msg.obj = popupWindow;
                            handler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Message msg = new Message();
                        msg.what = Constant.CommentEdit;
                        msg.arg1 = FAILURE;
                        msg.obj = popupWindow;
                        handler.sendMessage(msg);
                    }
                });
//                if (NetWorkUtil.isNetWorkOpened(PostActivity.this)) {
//                    if (Constant.user == null) {
//                        Toast.makeText(PostActivity.this,
//                                "请先登录!", Toast.LENGTH_SHORT).show();
//                        return ;
//                    }
//                    new AsyncTask<String, Integer, CommentsResponse>() {
//                        String body;
//
//                        @Override
//                        protected void onPreExecute() {
//                            LoadingDialog.showWaitDialog(PostActivity.this);
//                            body = subject.getText().toString();
//                            super.onPreExecute();
//                        }
//
//                        @Override
//                        protected CommentsResponse doInBackground(String... params) {
//                            return CommentsService.newComment(post.getCommentsURL(),
//                                    Constant.user.getId(), body);
//                        }
//
//                        @Override
//                        protected void onPostExecute(CommentsResponse commentsResponse) {
//                            if (commentsResponse == null) {
//                                Message msg = new Message();
//                                msg.what = Constant.CommentEdit;
//                                msg.arg1 = FAILURE;
//                                msg.obj = popupWindow;
//                                handler.sendMessage(msg);
//                                return;
//                            }
//                            if (commentsResponse.isCode()) {
//                                Message msg = new Message();
//                                msg.what = Constant.CommentEdit;
//                                msg.arg1 = SUCCESS;
//                                msg.obj = popupWindow;
//                                handler.sendMessage(msg);
//                            } else {
//                                Message msg = new Message();
//                                msg.what = Constant.CommentEdit;
//                                msg.arg1 = FAILURE;
//                                msg.obj = popupWindow;
//                                handler.sendMessage(msg);
//                            }
//                        }
//                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                }
            }
        });

        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable
                (R.color.white));
        popupWindow.showAsDropDown(view);
    }

    private class CommentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Comment comment;
        private TextView name, time, subject;
        private CircleImage icon;
        private User user;

//        private Handler holderHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case Constant.UpdateData:
//                        UserResponse userResponse = (UserResponse)msg.obj;
//                        if (userResponse != null) {
//                            user = userResponse.getUser();
//                            if (user != null) {
//                                name.setText(user.getUsername());
//                                Thread t = new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Bitmap bitmap = BitmapService.getBitmap(user.getGravatarURL());
//                                        Message msg = new Message();
//                                        msg.obj = bitmap;
//                                        msg.what = Constant.UpdateIcon;
//                                        holderHandler.sendMessage(msg);
//                                    }
//                                });
//                                t.start();
//                            }
//                        }
//                        break;
//                    case Constant.UpdateIcon:
//                        Bitmap bitmap = (Bitmap)msg.obj;
//                        if (bitmap != null) {
//                            icon.setImageBitmap(bitmap);
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }
//        };

        public CommentHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.comment_name);
            time = (TextView) itemView.findViewById(R.id.comment_time);
            subject = (TextView) itemView.findViewById(R.id.comment_subject);
            icon = (CircleImage) itemView.findViewById(R.id.comment_icon);
        }

        public void bindData(final Comment comment) {
            this.comment = comment;
            BaseClient.getAbs(comment.getAuthorURL(), null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        user = JsonUtil.getEntity(response.getString("user"), User.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    name.setText(user.getUsername());
                    ImageLoader.getInstance().displayImage(user.getGravatarURL(), icon);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = UserInfoActivity.newIntent(PostActivity.this);
                                    intent.putExtra("user", user);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    UserResponse response = UserService.getUsers(comment.getAuthorURL());
//                    Message msg = new Message();
//                    msg.obj = response;
//                    msg.what = Constant.UpdateData;
//                    holderHandler.sendMessage(msg);
//                }
//            });
//            t.start();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = sdf.format(new Date(comment.getTimeStamp()));
            time.setText(dateString);
            subject.setText(comment.getSubject());
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

        ArrayList<Comment> comments = new ArrayList<>();

        public CommentAdapter (ArrayList<Comment> comments) {
            this.comments = comments;
        }

        @Override
        public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(PostActivity.this);
            View view = inflater.inflate(R.layout.list_item_comment, parent, false);
            return new CommentHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentHolder holder, int position) {
            holder.bindData(comments.get(position));
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        @Override
        public void onViewRecycled(CommentHolder holder) {
            super.onViewRecycled(holder);
            ImageLoader.getInstance().cancelDisplayTask(holder.icon);
        }

        public void setComments(ArrayList<Comment> comments) {
            this.comments.clear();
            this.comments.addAll(comments);
            notifyDataSetChanged();
//            Log.e("GaryJ", "Debug");
        }
    }

    private class adapterObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            super.onChanged();
//            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
 //           checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
//            checkIfEmpty();
        }
    }
}
