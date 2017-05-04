package com.gary.blog.Wedgit;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gary.blog.R;

/**
 * Created by hasee on 2017/4/3.
 */

public class ImgEditText extends RelativeLayout {

    private ImageView icon;
    private EditText editText;
    private Paint paint;

    public ImgEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(getResources().getColor(android.R.color.black));
        paint.setStyle(Paint.Style.STROKE);

        LayoutInflater.from(getContext()).inflate(R.layout.img_edittext, this, true);
        icon = (ImageView) findViewById(R.id.icon);
        editText = (EditText) findViewById(R.id.edit_text);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ImgEditText);
        if (attributes != null) {
            int ImgSrc =attributes.getResourceId(R.styleable.ImgEditText_img_src, -1);
            if (ImgSrc != -1) {
                icon.setBackgroundResource(ImgSrc);
            } else {
                icon.setBackgroundResource(R.drawable.ic_default);
            }
        }
    }

    public void setText(String s) {
        editText.setText(s);
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setTextInvisiable() {
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
