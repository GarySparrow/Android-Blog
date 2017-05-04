package com.gary.blog.Dialog;


import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gary.blog.Data.User;
import com.gary.blog.R;
import com.gary.blog.WebService.UserService.UserResponse;
import com.gary.blog.WebService.UserService.UserService;

/**
 * Created by hasee on 2016/12/22.
 */

public class RegisterDialog extends DialogFragment {

    private Dialog loading;

    private enum STATUS{

    }

    private static final int PSWERROR = 1;
    private static final int EMPTYERROR = 2;
    private static final int NONERESPONSE = 3;
    private static final int FAILURE = 4;
    private static final int SUCCESS = 5;

    private EditText emailText, usernameText, pswText, checkpswText;
    private AlertDialog dialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case PSWERROR:
                    Toast.makeText(getContext(), "两次密码输入不一致",
                            Toast.LENGTH_SHORT).show();
                    LoadingDialog.closeDialog();
                    break;
                case EMPTYERROR:
                    Toast.makeText(getContext(), "请输入完整信息",
                            Toast.LENGTH_SHORT).show();
                    LoadingDialog.closeDialog();
                    break;
                case FAILURE:
                    Toast.makeText(getContext(), "注册失败!",
                            Toast.LENGTH_SHORT).show();
                    LoadingDialog.closeDialog();
                    break;
                case SUCCESS:
                    Toast.makeText(getContext(), "注册成功!, " +
                                    "请在邮箱确认",
                            Toast.LENGTH_SHORT).show();
                    LoadingDialog.closeDialog();
                    break;
                case NONERESPONSE:
                    Toast.makeText(getContext(), "服务器响应为空!",
                            Toast.LENGTH_SHORT).show();
                    LoadingDialog.closeDialog();
                default:
                    break;
            }
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.activity_register, null);
        emailText = (EditText) v.findViewById(R.id.user_email);
        usernameText = (EditText) v.findViewById(R.id.user_username);
        pswText = (EditText) v.findViewById(R.id.user_psw);
        checkpswText = (EditText) v.findViewById(R.id.user_checkpsw);

        pswText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        checkpswText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("注册账号")
                .setPositiveButton("确认", null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String, Integer, UserResponse>() {
                    String email, psw, checkpsw, username;
                    @Override
                    protected void onPreExecute() {
                        LoadingDialog.showWaitDialog(getContext());
                        email = emailText.getText().toString();
                        psw = pswText.getText().toString();
                        checkpsw = checkpswText.getText().toString();
                        username = usernameText.getText().toString();
                        super.onPreExecute();
                    }

                    @Override
                    protected UserResponse doInBackground(String... params) {
                        if (email.equals("") || psw.equals("") || checkpsw.equals("") ||
                                username.equals("")) {
                            Message msg = new Message();
                            msg.arg1 = EMPTYERROR;
                            handler.sendMessage(msg);
                            return null;
                        } else if (!psw.equals(checkpsw)) {
                            Message msg = new Message();
                            msg.arg1 = PSWERROR;
                            handler.sendMessage(msg);
                            return null;
                        }
                        return UserService.register(email, psw, username);
                    }

                    @Override
                    protected void onPostExecute(UserResponse userResponse) {
                        if (userResponse == null) {
                            Message msg = new Message();
                            msg.arg1 = NONERESPONSE;
                            handler.sendMessage(msg);
                            return ;
                        }
                        User user = userResponse.getUser();
                        if (user != null) {
                            Message msg = new Message();
                            msg.arg1 = SUCCESS;
                            handler.sendMessage(msg);
                            dialog.dismiss();
                        } else {
                            Message msg = new Message();
                            msg.arg1 = FAILURE;
                            handler.sendMessage(msg);
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        return dialog;
    }
}
