package com.gary.blog.Utils;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import java.net.HttpURLConnection;
import java.util.UUID;

/**
 * Created by hasee on 2016/12/9.
 */

public class WebUtil {

    private static final String TAG = "WebUtil";


    public static InputStream connection(String path, String param) {
        try {
            URL url = new URL(path);
            HttpURLConnection httpConect = (HttpURLConnection) url.openConnection();
            httpConect.setDoOutput(true);
            httpConect.setDoInput(true);
            httpConect.setUseCaches(false);
//            httpConect.setRequestMethod("POST");
            httpConect.setConnectTimeout(5 * 1000);
//            httpConect.setRequestProperty("Content-length", "" + entityData.length);
            httpConect.setRequestProperty("Content-type", "application/json");
            OutputStream os = httpConect.getOutputStream();
            os.write(param.getBytes());
            os.flush();
            os.close();
            int responseCode = httpConect.getResponseCode();
            if (httpConect.HTTP_OK == responseCode) {
                InputStream is = httpConect.getInputStream();
                return is;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

//    public static InputStream jsonConnection(String path, String param) {
//        try {
//            URL url = new URL(path);
//            HttpURLConnection httpConect = (HttpURLConnection) url.openConnection();
//            httpConect.setDoOutput(true);
//            httpConect.setDoInput(true);
//            httpConect.setUseCaches(false);
////            httpConect.setRequestMethod("POST");
//            httpConect.setConnectTimeout(5 * 1000);
////            httpConect.setRequestProperty("Content-length", "" + entityData.length);
//            httpConect.setRequestProperty("Content-type", "application/json");
//            OutputStream os = httpConect.getOutputStream();
//            os.write(param.getBytes());
//            os.flush();
//            os.close();
//            int responseCode = httpConect.getResponseCode();
//            if (httpConect.HTTP_OK == responseCode) {
//                InputStream is = httpConect.getInputStream();
//                return is;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return null;
//    }

    public static InputStream connection(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection httpConect = (HttpURLConnection) url.openConnection();
            httpConect.setDoInput(true);
            httpConect.setUseCaches(false);
            httpConect.setConnectTimeout(5 * 1000);
//            httpConect.setRequestProperty("Content-length", "" + entityData.length);
//            httpConect.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            httpConect.connect();
            int responseCode = httpConect.getResponseCode();
            if (httpConect.HTTP_OK == responseCode) {
                InputStream is = httpConect.getInputStream();
                return is;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public static InputStream connection(String path, File file, String param) {
        String BOUNDARY = UUID.randomUUID().toString();
        String CONTENT_TYPE = "multipart/form-data";
        String PREFIX = "--";
        String LINE_END = "\r\n";
        try {
            URL url = new URL(path);
            HttpURLConnection httpConect = (HttpURLConnection) url.openConnection();
            httpConect.setDoOutput(true);
            httpConect.setDoInput(true);
            httpConect.setUseCaches(false);
            httpConect.setRequestMethod("POST");
            httpConect.setConnectTimeout(10 * 10000000);
            httpConect.setReadTimeout(10 * 10000000);
            httpConect.setRequestProperty("Charset", "utf-8");
            httpConect.setRequestProperty("connection", "keep-alive");
//            httpConect.setRequestProperty("Content-length", "" + entityData.length);
            httpConect.setRequestProperty("Content-type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            OutputStream os = httpConect.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            StringBuffer sb = new StringBuffer();
            if (param != null) {
                sb.setLength(0);
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\"data\"; filename=\"" + "datafile"
                        + "\"" + LINE_END);
                sb.append("Content-Type: application/json" + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                dos.write(param.getBytes());
                dos.write(LINE_END.getBytes());
//                byte[] endData = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
//                dos.write(endData);
                dos.flush();
            }
            if (file != null) {
                sb.setLength(0);
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName()
                        + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=utf-8" + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while((len=is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                dos.flush();
            }
            byte[] endData = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(endData);
            dos.flush();
            dos.close();
            int responseCode = httpConect.getResponseCode();
            if (httpConect.HTTP_OK == responseCode) {
                return httpConect.getInputStream();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
//    public static InputStream PostConnection(String path, String param) {
//        try {
//            URL url = new URL(path);
//            HttpURLConnection httpConect = (HttpURLConnection) url.openConnection();
//            httpConect.setDoOutput(true);
//            httpConect.setDoInput(true);
//            httpConect.setUseCaches(false);
////            httpConect.setRequestMethod("POST");
//            httpConect.setConnectTimeout(5 * 1000);
////            httpConect.setRequestProperty("Content-length", "" + entityData.length);
////            httpConect.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
//            OutputStream os = httpConect.getOutputStream();
//            os.write(param.getBytes());
//            os.flush();
//            os.close();
//            int responseCode = httpConect.getResponseCode();
//            if (httpConect.HTTP_OK == responseCode) {
//                InputStream is = httpConect.getInputStream();
//                return is;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return null;
//    }


    public static String getStringFormIs(InputStream is) {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            int len = -1;
            while((len = is.read(buffer)) != -1) bos.write(buffer, 0, len);
            bos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String reString = new String(bos.toByteArray());
//        Log.e(TAG, "getStringFromIs reString =========== " + reString);

        return reString;
    }
}
