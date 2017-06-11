package com.gary.blog.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gary.blog.Constant;
import com.gary.blog.R;
import com.gary.blog.Wedgit.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by hasee on 2017/4/25.
 */

public class ImageActivity extends AppCompatActivity{
    private PhotoView photoView;

    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, ImageActivity.class);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        photoView = (PhotoView) findViewById(R.id.image);
        photoView.enable();
        String path = getIntent().getExtras().getString(Constant.IMAGE_PATH);
        ImageLoader.getInstance().displayImage(path, photoView);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
