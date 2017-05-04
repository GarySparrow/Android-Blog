package com.gary.blog.Dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gary.blog.Activity.MainActivity;
import com.gary.blog.Constant;
import com.gary.blog.Data.User;
import com.gary.blog.R;
import com.gary.blog.Utils.JsonUtil;
import com.gary.blog.WebService.BaseClient;
import com.gary.blog.Wedgit.ImgEditText;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by hasee on 2017/4/3.
 */

public class LoginDialog extends DialogFragment {

    private AlertDialog dialog;
    private ImgEditText account, psw;
    private SweetAlertDialog loadingDialog;
    private Button login;

    private enum status {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.activity_login, null);
        login = (Button) v.findViewById(R.id.login);
        account = (ImgEditText) v.findViewById(R.id.user_account);
        psw = (ImgEditText) v.findViewById(R.id.user_psw);

        loadingDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        loadingDialog.setTitleText("Loading...");

        psw.setTextInvisiable();
        account.setText("690131179@qq.com");
        psw.setText("cat");

        dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.put("email", account.getText().toString());
                params.put("psw", psw.getText().toString());
                BaseClient.post(Constant.LOGIN, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        loadingDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Toast.makeText(getContext(), "welcome back!", Toast.LENGTH_SHORT).show();
                        User user = null;
                        try {
                            user = JsonUtil.getEntity(response.getString("user"), User.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Constant.user = user;
                        Intent i = MainActivity.newIntent(getContext());
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Toast.makeText(getContext(), "It is something wrong...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        loadingDialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
        return dialog;
    }
}
