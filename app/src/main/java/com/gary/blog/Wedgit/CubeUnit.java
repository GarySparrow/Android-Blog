package com.gary.blog.Wedgit;

import android.view.View;

/**
 * Created by hasee on 2017/5/9.
 */

public class CubeUnit {
    private View view;
    public CubeUnit left, right, up, down;

    public CubeUnit(View v, CubeUnit left, CubeUnit right, CubeUnit up, CubeUnit down) {
        this.view = v;
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
    }

    public View getView() {
        return view;
    }

    public void setView(View v) {
        this.view = v;
    }
}
