package com.gary.blog.Wedgit;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gary.blog.Animation.CubeAnimation.CubeAnimation;
import com.gary.blog.Animation.CubeAnimation.CubeToBack;
import com.gary.blog.Animation.CubeAnimation.CubeToFore;
import com.gary.blog.R;

/**
 * Created by hasee on 2017/5/8.
 */

public class CubeLayout extends FrameLayout implements View.OnTouchListener{

//    private BaseInterpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private Thread rotateThread = new Thread();
    private GestureDetector mGestureDetector;
    private static int FLING_MIN_DISTANCE = 5;
    private static int FLING_MIN_VELOCITY = 100;
    private static final int time = 500;
    private CubeUnit cntUnit;

    public CubeLayout(Context context) {
        this(context, null);
    }

    public CubeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnTouchListener(this);
        this.setLongClickable(true);

        mGestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                if (e1 == null || e2 == null) {
                    return false;
                }

                if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE &&
                        e1.getY() - e2.getY() > Math.abs(e1.getX() - e2.getX())) {   //向上

                    rotate(CubeAnimation.UP);
                } else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE &&
                        e2.getY() - e1.getY() > Math.abs(e1.getX() - e2.getX())) {    //向下

                    rotate(CubeAnimation.DOWN);
                } else if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE &&
                        e1.getX() - e2.getX() > Math.abs(e1.getY() - e2.getY())) {    //向左

                    rotate(CubeAnimation.LEFT);
                } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE &&
                        e2.getX() - e1.getX() > Math.abs(e1.getY() - e2.getY())) {    //向右

                    rotate(CubeAnimation.RIGHT);
                }

                return false;
            }
        });
    }

    public CubeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getChildCount() > 0 && cntUnit == null) {
            cntUnit = new CubeUnit(getChildAt(0), null, null, null, null);
        }
        if (getChildCount() > 1 && (cntUnit.left == null && cntUnit.right == null
                && cntUnit.up == null && cntUnit.down == null)) {

            CubeUnit leftCube = cntUnit, upCube = cntUnit;
            for (int i = 1; i < getChildCount(); i++) {
                View v = getChildAt(i);
                LayoutParams lp = (LayoutParams) v.getLayoutParams();
                CubeUnit tmpCube = new CubeUnit(v, null, null, null, null);
                if (lp.group == LayoutParams.HORIZONTAL) {
                    leftCube.left = tmpCube;
                    tmpCube.right = leftCube;
                    leftCube = tmpCube;
                } else if (lp.group == LayoutParams.VERTICAL) {
                    upCube.up = tmpCube;
                    tmpCube.down = upCube;
                    upCube = tmpCube;
                }
                v.setVisibility(GONE);
//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int dir[] = {CubeAnimation.LEFT, CubeAnimation.LEFT, CubeAnimation.UP, CubeAnimation.DOWN};
//                        int idx = 0;
//                        while(true) {
//                            try {
//                                Thread.sleep(5000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//                            rotate(dir[idx]);
//                            idx = (idx + 1) % dir.length;
//                        }
//                    }
//                });
//                t.start();
            }
            if (leftCube == cntUnit) {
                cntUnit.left = cntUnit.right = null;
            } else {
                leftCube.left = cntUnit;
                cntUnit.right = leftCube;
            }

            if (upCube == cntUnit) {
                cntUnit.up = cntUnit.down = null;
            } else {
                upCube.up = cntUnit;
                cntUnit.down = upCube;
            }

        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        } else if (ev.getAction() == MotionEvent.ACTION_DOWN){
            mGestureDetector.onTouchEvent(ev);
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private void rotate(int dir) {

        if (dir == CubeAnimation.UP) {
            View foregroundView;
            View backgroundView = cntUnit.getView();

            if (cntUnit.up == null) {
                return ;
            }

            if (cntUnit.left != null && cntUnit.right != null) {
                cntUnit.left.right = cntUnit.up;
                cntUnit.right.left = cntUnit.up;
            }
            cntUnit.up.left = cntUnit.left;
            cntUnit.up.right = cntUnit.right;
            cntUnit.left = null;
            cntUnit.right = null;

            cntUnit = cntUnit.up;

            foregroundView = cntUnit.getView();

            backgroundView.setVisibility(VISIBLE);
            foregroundView.setVisibility(VISIBLE);
            backgroundView.setClickable(false);
            foregroundView.setClickable(false);

            CubeToFore cubeToFore = new CubeToFore(CubeAnimation.UP);
            cubeToFore.setDuration(time);
            cubeToFore.setFillAfter(true);

            CubeToBack cubeToBack = new CubeToBack(CubeAnimation.UP);
            cubeToBack.setDuration(time);
            cubeToBack.setFillAfter(true);

            foregroundView.startAnimation(cubeToFore);
            backgroundView.startAnimation(cubeToBack);

            backgroundView.setVisibility(GONE);
            foregroundView.setClickable(true);
        } else if (dir == CubeAnimation.DOWN) {
            View foregroundView;
            View backgroundView = cntUnit.getView();

            if (cntUnit.down == null) {
                return ;
            }

            if (cntUnit.left != null && cntUnit.right != null) {
                cntUnit.left.right = cntUnit.down;
                cntUnit.right.left = cntUnit.down;
            }
            cntUnit.down.left = cntUnit.left;
            cntUnit.down.right = cntUnit.right;
            cntUnit.left = null;
            cntUnit.right = null;

            cntUnit = cntUnit.down;

            foregroundView = cntUnit.getView();

            backgroundView.setVisibility(VISIBLE);
            foregroundView.setVisibility(VISIBLE);

            backgroundView.setClickable(false);
            foregroundView.setClickable(false);

            CubeToFore cubeToFore = new CubeToFore(CubeAnimation.DOWN);
            cubeToFore.setDuration(time);
            cubeToFore.setFillAfter(true);

            CubeToBack cubeToBack = new CubeToBack(CubeAnimation.DOWN);
            cubeToBack.setDuration(time);
            cubeToBack.setFillAfter(true);

            foregroundView.startAnimation(cubeToFore);
            backgroundView.startAnimation(cubeToBack);

            backgroundView.setVisibility(GONE);
            foregroundView.setClickable(true);
        } else if (dir == CubeAnimation.LEFT) {
            View foregroundView;
            View backgroundView = cntUnit.getView();

            if (cntUnit.left == null) {
                return ;
            }

            if (cntUnit.up != null && cntUnit.down != null) {
                cntUnit.up.down = cntUnit.left;
                cntUnit.down.up = cntUnit.left;
            }

            cntUnit.left.up = cntUnit.up;
            cntUnit.left.down = cntUnit.down;
            cntUnit.up = null;
            cntUnit.down = null;

            cntUnit = cntUnit.left;

            foregroundView = cntUnit.getView();
            if (foregroundView == null) {
                return ;
            }


            backgroundView.setVisibility(VISIBLE);
            foregroundView.setVisibility(VISIBLE);

            backgroundView.setClickable(false);
            foregroundView.setClickable(false);

            CubeToFore cubeToFore = new CubeToFore(CubeAnimation.LEFT);
            cubeToFore.setDuration(time);
            cubeToFore.setFillAfter(true);

            CubeToBack cubeToBack = new CubeToBack(CubeAnimation.LEFT);
            cubeToBack.setDuration(time);
            cubeToBack.setFillAfter(true);

            foregroundView.startAnimation(cubeToFore);
            backgroundView.startAnimation(cubeToBack);

            backgroundView.setVisibility(GONE);
            foregroundView.setClickable(true);
        } else if (dir == CubeAnimation.RIGHT) {
            View foregroundView;
            View backgroundView = cntUnit.getView();

            if (cntUnit.right == null) {
                return ;
            }

            if (cntUnit.up != null && cntUnit.down != null) {
                cntUnit.up.down = cntUnit.right;
                cntUnit.down.up = cntUnit.right;
            }
            cntUnit.right.up = cntUnit.up;
            cntUnit.right.down = cntUnit.down;
            cntUnit.up = null;
            cntUnit.down = null;

            cntUnit = cntUnit.right;

            foregroundView = cntUnit.getView();

            if (foregroundView == null) {
                return ;
            }

            backgroundView.setVisibility(VISIBLE);
            foregroundView.setVisibility(VISIBLE);

            backgroundView.setClickable(false);
            foregroundView.setClickable(false);

            CubeToFore cubeToFore = new CubeToFore(CubeAnimation.RIGHT);
            cubeToFore.setDuration(time);
            cubeToFore.setFillAfter(true);

            CubeToBack cubeToBack = new CubeToBack(CubeAnimation.RIGHT);
            cubeToBack.setDuration(time);
            cubeToBack.setFillAfter(true);

            foregroundView.startAnimation(cubeToFore);
            backgroundView.startAnimation(cubeToBack);

            backgroundView.setVisibility(GONE);

            foregroundView.setClickable(true);
        }

    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new LayoutParams(lp);
    }

    @Override
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }


    private static class LayoutParams extends FrameLayout.LayoutParams{

        public int group;

        public static final int HORIZONTAL = 1, VERTICAL = 2;

        public LayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);

            TypedArray td = c.obtainStyledAttributes(attrs, R.styleable.CubeLayoutParams);
            group = td.getInteger(R.styleable.CubeLayoutParams_group, LayoutParams.HORIZONTAL);
            td.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(@NonNull ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(@NonNull ViewGroup.MarginLayoutParams source) {
            super(source);
        }


    }
}
