package com.gary.blog.Animation.CubeAnimation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by hasee on 2017/5/7.
 */

public abstract class CubeAnimation extends Animation{


    public static final int UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4;
    protected static final int sFinalDegree = 90;
    protected int mWidth, mHeight, dir;
    protected Camera mCamera;
    protected Matrix mMatrix;

    public CubeAnimation() {
        mCamera = new Camera();
        dir = LEFT;
    }

    public CubeAnimation(int dir) {
        mCamera = new Camera();
        this.dir = dir;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
        mMatrix = new Matrix();
        mWidth = width;
        mHeight = height;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
    }
}
