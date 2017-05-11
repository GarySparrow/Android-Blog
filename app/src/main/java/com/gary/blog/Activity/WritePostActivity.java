package com.gary.blog.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gary.blog.Constant;
import com.gary.blog.R;
import com.gary.blog.WebService.BaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.gary.blog.Constant.FAILURE;
import static com.gary.blog.Constant.POSTS;
import static com.gary.blog.Constant.SUCCESS;

/**
 * Created by hasee on 2016/12/18.
 */

public class WritePostActivity extends AppCompatActivity{

    private final static int MESSAGE_VIDEO = 1;
    private final static int REQUEST_IMG = 2;

    private TextView activity_title;
    private FloatingActionButton videoButton, imgsButton;
    private LinearLayout imgsView, videoLayout;
    private MaterialEditText postSubject;
    private File video;
    private Toolbar toolbar;
    private SweetAlertDialog loadingDialog;

    private ImgSelConfig imageConfig;
    private ArrayList<File> bitmaps = new ArrayList<>();
    private ArrayList<String> imgList = new ArrayList<>();
    private ArrayList<String> paths = new ArrayList<>();

    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            // TODO 在这边可以自定义图片加载库来加载ImageView，例如Glide、Picasso、ImageLoader等
            Glide.with(context).load(path).into(imageView);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.NewPost:
                    switch (msg.arg1) {
                        case SUCCESS:
                            Toast.makeText(WritePostActivity.this,
                                    "发布成功!", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        case FAILURE:
                            Toast.makeText(WritePostActivity.this,
                                    "发布失败...", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, WritePostActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        getWindow().setEnterTransition(new Explode().setDuration(500));

        videoLayout = (LinearLayout) findViewById(R.id.video_layout);
        videoButton = (FloatingActionButton) findViewById(R.id.video_add);
        imgsButton = (FloatingActionButton) findViewById(R.id.img_add);
        postSubject = (MaterialEditText) findViewById(R.id.edit_post);
        imgsView = (LinearLayout) findViewById(R.id.imgs_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        activity_title = (TextView) toolbar.findViewById(R.id.toolbar_left_text);
        activity_title.setText("发布博客");
        activity_title.setTextColor(getResources().getColor(
                R.color.white));
        toolbar.setTitle("");

        loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.setTitleText("Loading...");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        initImageSelector();
    }

    private void initImageSelector (){
        imageConfig = new ImgSelConfig.Builder(WritePostActivity.this, loader)
                // 是否多选, 默认true
                .multiSelect(true)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.parseColor("#3F51B5"))
                // “确定”按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                // 裁剪大小。needCrop为true的时候配置
                .cropSize(1, 1, 200, 200)
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(true)
                // 最大选择图片数量，默认9
                .maxNum(9)
                .build();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return ;
        }
        switch (requestCode) {
            case MESSAGE_VIDEO:
                try {
                    bitmaps.clear();
                    imgsView.removeAllViews();
                    Uri uri = data.getData();
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    cursor.moveToFirst();
                    String videoPath = cursor.getString(1);
//                String videoSize = cursor.getString(2);
//                String videoName = cursor.getString(3);
                    File file = new File(videoPath);
                    video = file;
//                    menu.findItem(R.id.video_mark).setVisible(true);
//                    menu.findItem(R.id.upload_video).setVisible(false);
                    Bitmap bitmap = getBitmapFromFile(video);
                    ImageView img = new ImageView(this);
                    img.setImageBitmap(bitmap);
                    videoLayout.addView(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_IMG:
                imgList.clear();
                bitmaps.clear();
                videoLayout.removeAllViews();
                video = null;
                if (data == null) {
                    break;
                }
                imgList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
                for (int i = 0; i < imgList.size(); i++) {
                    File file = new File(imgList.get(i));
                    bitmaps.add(file);
                }
                imgsView.removeAllViews();
                for (int i = 0; i < imgList.size(); i++) {
                    ImageView imageView = new ImageView(this);
                    Bitmap bitmap = getBitmapFromPath(imgList.get(i));
                    Bitmap newBitmap = scaleBitmap(bitmap, 120, 120);
                    imageView.setImageBitmap(newBitmap);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            120,
                            FrameLayout.LayoutParams.MATCH_PARENT);
                    imgsView.setLayoutParams(params);
                    imgsView.addView(imageView);
                }
                break;
            default:
                break;
        }
    }

    private Bitmap getBitmapFromFile(File file) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(file.getPath(),
                MediaStore.Images.Thumbnails.MINI_KIND);
        return bitmap;
    }

    private Bitmap getBitmapFromPath(String path) {
        if (!new File(path).exists()) {
            return null;
        }
        byte[] buf = new byte[1024 * 1024];
        Bitmap bitmap = null;

        try {
            FileInputStream fis = new FileInputStream(path);
            int len = fis.read(buf, 0, buf.length);
            bitmap = BitmapFactory.decodeByteArray(buf, 0, len);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap scaleBitmap(Bitmap bitmap, int towidth, int toheight) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) towidth) / (float) width;
        float scaleHeight = ((float) toheight) / (float) height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap ret = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return ret;
    }

    void init() {
        imgsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImgSelActivity.startActivity(WritePostActivity.this,
                        imageConfig, REQUEST_IMG);
            }
        });
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                startActivityForResult(i, MESSAGE_VIDEO);
            }
        });
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
                RequestParams params = new RequestParams();
                params.put("body", postSubject.getText().toString());
                params.put("author_id", Constant.user.getId());
                if (video != null) {
                    try {
                        params.put("video", video);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (bitmaps.size() > 0) {
                    for (int i = 0; i < bitmaps.size(); i++) {
                        try {
                            System.out.println(bitmaps.get(i).length());
                            params.put("img" + Integer.toString(i + 1), bitmaps.get(i));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (postSubject.getText().length() < 1 ||
                        postSubject.getText().length() > 140) {
                    Toast.makeText(this, "输入不合法...", Toast.LENGTH_SHORT).show();
                    break;
                }
//                    String param = "{" +
//                            "\"body\": \"" + JsonUtil.ready4Json(postSubject.getText().toString()) + "\","
//                            + "\"id\": " + Constant.user.getId()
//                            + "}";
                BaseClient.post(POSTS, params, new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        loadingDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Message msg = new Message();
                        msg.what = Constant.NewPost;
                        msg.arg1 = Constant.SUCCESS;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Message msg = new Message();
                        msg.what = Constant.NewPost;
                        msg.arg1 = Constant.FAILURE;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        loadingDialog.dismiss();
                    }
                });
                break;

//                new AsyncTask<String, Integer, PostsResponse>() {
//                    String subject;
//
//                    @Override
//                    protected void onPreExecute() {
//                        LoadingDialog.showWaitDialog(WritePostActivity.this);
//                        subject = postSubject.getText().toString();
//                        super.onPreExecute();
//                    }
//
//                    @Override
//                    protected PostsResponse doInBackground(String... params) {
//                        if (video != null) {
//                            return PostsService.newPost(video, subject, Constant.user.getId());
//                        } else {
//                            return PostsService.newPost(subject, Constant.user.getId());
//                        }
//
//                    }
//
//                    @Override
//                    protected void onPostExecute(PostsResponse postsResponse) {
//                        if (postsResponse == null) {
//                            Message msg = new Message();
//                            msg.what = Constant.PostEdit;
//                            msg.arg1 = Constant.FAILURE;
//                            handler.sendMessage(msg);
//                            return ;
//                        }
//                        if (postsResponse.isCode()) {
//                            Message msg = new Message();
//                            msg.what = Constant.PostEdit;
//                            msg.arg1 = Constant.SUCCESS;
//                            handler.sendMessage(msg);
//                        } else {
//                            Message msg = new Message();
//                            msg.what = Constant.PostEdit;
//                            msg.arg1 = Constant.FAILURE;
//                            handler.sendMessage(msg);
//                        }
//                    }
//                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            case R.id.upload_video:
//                Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//                startActivityForResult(i, MESSAGE_VIDEO);
//                break;
//            case R.id.video_mark:
//                this.video = null;
//                menu.findItem(R.id.upload_video).setVisible(true);
//                menu.findItem(R.id.video_mark).setVisible(false);
//                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
