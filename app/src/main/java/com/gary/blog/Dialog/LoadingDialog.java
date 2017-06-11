package com.gary.blog.Dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gary.blog.R;

/**
 * Created by hasee on 2016/12/23.
 */

public class LoadingDialog {

    private static Dialog dialog;

    public static void showWaitDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.dialog_view);


        ImageView loadingImg = (ImageView) v.findViewById(R.id.loading_img);
        TextView loadingText = (TextView) v.findViewById(R.id.loading_text);

        Animation anim = AnimationUtils.loadAnimation(context, R.anim.rotate_animation);

        loadingImg.startAnimation(anim);
        loadingText.setText(msg);

        Dialog loadingDialog = new Dialog(context, R.style.TransDialogStyle);    // 创建自定义样式dialog
        loadingDialog.setContentView(layout);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);

        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
        dialog = loadingDialog;
    }


    public static void showWaitDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.dialog_view);


        ImageView loadingImg = (ImageView) v.findViewById(R.id.loading_img);

        Animation anim = AnimationUtils.loadAnimation(context, R.anim.rotate_animation);

        loadingImg.startAnimation(anim);

        Dialog loadingDialog = new Dialog(context, R.style.TransDialogStyle);    // 创建自定义样式dialog
        loadingDialog.setContentView(layout);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);

        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
        dialog = loadingDialog;
    }

    public static void closeDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
