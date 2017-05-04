package com.gary.blog.WebService;

import android.content.Context;

import com.gary.blog.MyApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by hasee on 2017/3/15.
 */

public class BaseClient {
//    private static final String BASE_URL = "https://garyflask.herokuapp.com/api/v1.0/";
    private static final String BASE_URL = "http://120.25.254.10/api/v1.0/";

    private static AsyncHttpClient client = new AsyncHttpClient();
    static Context context = MyApplication.getInstance();


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void getAbs(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (params != null && (params.has("video") || params.has("img1"))) {
            client.setConnectTimeout(36000);
            client.setMaxConnections(12);
            client.setResponseTimeout(36000);
            client.setTimeout(36000);
        }
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void postAbs(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

//    public static void postJsonParams(String url, String params, AsyncHttpResponseHandler responseHandler) {
////        client.addHeader(AsyncHttpClient.HEADER_CONTENT_TYPE, APPLICATION_JSON);
//        StringEntity entity = null;
//        try {
//            entity = new StringEntity(params.toString());
//            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        client.post(null, getAbsoluteUrl(url), entity, APPLICATION_JSON, responseHandler);
////        client.post(getAbsoluteUrl(url), params, responseHandler);
//    }
//
//    public static void postJsonParamsAbs(String url, String params, AsyncHttpResponseHandler responseHandler) {
////        client.addHeader(AsyncHttpClient.HEADER_CONTENT_TYPE, APPLICATION_JSON);
//        StringEntity entity = null;
//        try {
//            entity = new StringEntity(params.toString());
//            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        client.post(null, url, entity, APPLICATION_JSON, responseHandler);
////        client.post(getAbsoluteUrl(url), params, responseHandler);
//    }

    public static void put(String url,RequestParams params,AsyncHttpResponseHandler responseHandler){

        client.put(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void delete(String url,RequestParams params,AsyncHttpResponseHandler responseHandler){

        client.delete(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void postFile(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setConnectTimeout(600000);
        client.setMaxConnections(3);
        client.setResponseTimeout(600000);
        client.setTimeout(600000);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
