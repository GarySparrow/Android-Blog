package com.gary.blog.Notifications;

import android.support.annotation.NonNull;

import com.gary.blog.Constant;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by hasee on 2017/6/10.
 */

public class ParamsGenerator {
    public static RequestParams getParams() {
        RequestParams params = new RequestParams();
        params.put("access_id", 2100260507);

        params.put("timestamp", System.currentTimeMillis() / 1000);

        return params;
    }

    public static String getSign(String url, RequestParams params) {
        String s = "POST";
        s += url + formatParams(params) + Constant.secretKey;
        s = getMD5(s);

        return s;
    }


    public static String getMD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    private static String formatParams(RequestParams params) {
        ArrayList<StringMap> sMap = new ArrayList<>();
        String s = params.toString();
        String ret = "";
        int idx = 0;

        while(idx < s.length()) {
            String key = "", value = "";
            while(idx < s.length() && s.charAt(idx) != '=') {
                key += s.charAt(idx++);
            }
            idx++;
            while( idx < s.length() && s.charAt(idx) != '&') {
                value += s.charAt(idx++);
            }
            idx++;
            sMap.add(new StringMap(key, value));
        }

        Collections.sort(sMap);

        for (int i = 0; i < sMap.size(); i++) {
            ret += sMap.get(i).key + "=" + sMap.get(i).value;
        }
        return ret;
    }

    static class StringMap implements Comparable<StringMap>{
        String key, value;

        StringMap () {

        }

        StringMap(String key, String value) {
            this.key = key;
            this.value = value;
        }


        @Override
        public int compareTo(@NonNull StringMap o) {
            return key.compareTo(o.key);
        }
    }
}
