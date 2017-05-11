package com.gary.blog.Animation.CubeAnimation;

import android.view.animation.Transformation;

/**
 * Created by hasee on 2017/5/7.
 */

public class CubeToFore extends CubeAnimation {



    public CubeToFore() {
        super(LEFT);
    }

    public CubeToFore(int dir) {
        super(dir);
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float rotate;

        switch (dir) {
            case UP:
                rotate = (sFinalDegree * interpolatedTime - sFinalDegree);
                mCamera.save();
                mCamera.translate(0, mHeight * interpolatedTime, 0);
                mCamera.rotateX(rotate);
                mCamera.getMatrix(mMatrix);
                mCamera.restore();

                mMatrix.postTranslate(mWidth / 2, mHeight);
                mMatrix.preTranslate(-mWidth / 2, 0);

                t.getMatrix().postConcat(mMatrix);
                break;
            case DOWN:
                rotate = (sFinalDegree - sFinalDegree * interpolatedTime);
                mCamera.save();
                mCamera.translate(0, -mHeight * interpolatedTime, 0);
                mCamera.rotateX(rotate);
                mCamera.getMatrix(mMatrix);
                mCamera.restore();

                mMatrix.postTranslate(mWidth / 2, 0);
                mMatrix.preTranslate(-mWidth / 2, -mHeight);

                t.getMatrix().postConcat(mMatrix);
                break;
            case LEFT:
                rotate = (sFinalDegree - sFinalDegree * interpolatedTime);
                mCamera.save();
                mCamera.translate(-mWidth * interpolatedTime, 0, 0);
                mCamera.rotateY(rotate);
                mCamera.getMatrix(mMatrix);
                mCamera.restore();

                mMatrix.postTranslate(mWidth, mHeight / 2);
                mMatrix.preTranslate(0, -mHeight / 2);

                t.getMatrix().postConcat(mMatrix);
                break;
            case RIGHT:
                rotate = (sFinalDegree * interpolatedTime - sFinalDegree);
                mCamera.save();
                mCamera.translate(mWidth * interpolatedTime, 0, 0);
                mCamera.rotateY(rotate);
                mCamera.getMatrix(mMatrix);
                mCamera.restore();

                mMatrix.postTranslate(0, mHeight / 2);
                mMatrix.preTranslate(-mWidth, -mHeight / 2);

                t.getMatrix().postConcat(mMatrix);
                break;
            default:
                break;
        }
    }
}
