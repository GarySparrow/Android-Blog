package com.gary.blog.Utils;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by hasee on 2016/12/11.
 */

public class JsonUtil {
    private final static String TAG = "JsonUtil";

    public static String toJson(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
        return new Gson().toJson(obj);
    }

    public static String ready4Json(String jsonStr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
//                case '\'':
//                    sb.append("\\\\\'");
//                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

//    public static String StringDanYinToJSON(String ors) {
//        ors= ors == null ? "" : ors;
//        StringBuffer buffer = new StringBuffer(ors);
//        int i = 0;
//        while(i < buffer.length()) {
//            if(buffer.charAt(i) == '\'' || buffer.charAt(i) == '\\') {
//                buffer.insert(i,'\\');
//                i+= 2;
//            }else {
//                i++;
//            }
//        }
//        return buffer.toString();
//    }

    public static <T>T getEntity(String jsonString, final Class<T> cls) {
        T tmp;
        try {
            
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new StringReader(jsonString));
            reader.setLenient(true);
            tmp = gson.fromJson(reader, cls);
        } catch (Exception e) {
            e.printStackTrace();
//            Log.e(TAG, jsonString + " 无法转换为 " + cls.getSimpleName() + " 对象");
            return null;
        }

        return tmp;
    }

    public static <T>ArrayList<T> getEntityList(String jsonString, final Class<T> cls) {
        ArrayList<T> list = new ArrayList<>();

        JsonArray array = new JsonParser().parse(jsonString).getAsJsonArray();

        for (final JsonElement elem : array) list.add(new Gson().fromJson(elem, cls));

        return list;
    }
}
