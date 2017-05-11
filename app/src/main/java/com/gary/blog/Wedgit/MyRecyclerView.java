package com.gary.blog.Wedgit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hasee on 2016/12/17.
 */

public class MyRecyclerView extends RecyclerView{


    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (getParent() != null) {
//            getParent().requestDisallowInterceptTouchEvent(true);
//        }
//        return false;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
//        if (getParent() != null) {
//            getParent().requestDisallowInterceptTouchEvent(true);
//        }
//        return true;
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
//        if (getParent() != null) {
//            getParent().requestDisallowInterceptTouchEvent(true);
//        }
//        return true;
        return super.onTouchEvent(e);
    }
}
