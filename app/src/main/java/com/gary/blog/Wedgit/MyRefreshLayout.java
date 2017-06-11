package com.gary.blog.Wedgit;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hasee on 2016/12/17.
 */

public class MyRefreshLayout extends SwipeRefreshLayout{

    private boolean mMeasured = false;
    private boolean mPreMeasuredRefreshing = false;

    public MyRefreshLayout(Context context) {
        super(context);
    }

    public MyRefreshLayout(Context context, AttributeSet attrs) {
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
        return super.onTouchEvent(e);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mMeasured) {
            mMeasured = true;
            setRefreshing(mPreMeasuredRefreshing);
        }
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        if (mMeasured) {
            super.setRefreshing(refreshing);
        } else {
            mPreMeasuredRefreshing = refreshing;
        }
    }
}
