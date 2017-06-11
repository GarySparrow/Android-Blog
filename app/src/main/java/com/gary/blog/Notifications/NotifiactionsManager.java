package com.gary.blog.Notifications;

import com.gary.blog.Constant;
import com.gary.blog.WebService.BaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by hasee on 2017/6/10.
 */

public class NotifiactionsManager {

    NotifiactionsManager() {

    }

    public static void pushSingleAccount(String account, String message) {
        RequestParams params = ParamsGenerator.getParams();

        params.add("account", account);
        params.put("message_type", 1);
        params.add("message", message);


        params.put("sign", ParamsGenerator.getSign(Constant.PUSH_SINGLE_ACCOUNT_SECRET, params));

        BaseClient.postAbs(Constant.PUSH_SINGLE_ACCOUNT, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public static void pushSingleAccount(String account) {
        RequestParams params = ParamsGenerator.getParams();

        String message = "{" +
                "\"title\":\"default\"," +
                "\"content\":\"default\"," +
                "\"builder_id\":0" +
                "}";
        params.add("account", account);
        params.put("message_type", 1);
        params.add("message", message);


        params.put("sign", ParamsGenerator.getSign(Constant.PUSH_SINGLE_ACCOUNT_SECRET, params));

        BaseClient.postAbs(Constant.PUSH_SINGLE_ACCOUNT, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }



}
