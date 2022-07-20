package com.netease.nis.smartercaptcha.utils;

import android.util.Log;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    private static final int CONNECT_TIMEOUT_TIME = 10000;
    private static final OkHttpClient sClient = new OkHttpClient().newBuilder().readTimeout(CONNECT_TIMEOUT_TIME, TimeUnit.SECONDS).build();

    public static void doPostRequest(final String url, final Map<String, String> parameters, final ResponseCallBack callBack) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.d("Captcha", "post request url:" + url + " post parameters :" + parameters.toString());
                    doPostRequest2(url, parameters, callBack);
                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onError(10003, e.toString());
                }
            }
        }.start();
    }

    public static void doPostRequest2(String url, Map<String, String> parameters, ResponseCallBack callBack) throws IOException {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            bodyBuilder.add(entry.getKey(), entry.getValue());
        }
        FormBody requestBody = bodyBuilder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = sClient.newCall(request).execute();
        if (response.isSuccessful()) {
            callBack.onSuccess(Objects.requireNonNull(response.body()).string());
        } else {
            callBack.onError(response.code(), response.message());
        }

    }

    /**
     * 进行网络请求的回调
     */
    public interface ResponseCallBack {
        void onSuccess(String result);

        void onError(int errorCode, String msg);
    }
}
