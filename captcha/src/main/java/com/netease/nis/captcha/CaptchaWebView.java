package com.netease.nis.captcha;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by hzhudingyao on 2016/12/6.
 */

public class CaptchaWebView extends WebView {
    private CaptchaListener captchaListener;
    private CaptchaDialog captchaDialog;
    private WebView webView = this;
    private Context context;
    private int mTimeout = 10000;
    private boolean debug = false;
    private WebViewClientBase mWebViewClientBase = null;
    private WebChromeClient mWebChromeClient = null;

    //定时操作线程池
    private ScheduledExecutorService scheduledExecutorService = null;

    public CaptchaWebView(Context context, CaptchaListener captchaListener, CaptchaDialog captchaDialog) {
        super(context);
        this.context = context;
        this.captchaDialog = captchaDialog;
        this.captchaListener = captchaListener;
        this.debug = captchaDialog.isDebug();
        this.mWebViewClientBase = new WebViewClientBase();
        this.mWebChromeClient = new WebChromeClientBase();
        setWebView();
    }

    private void setWebView() {
        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);
        this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        this.setHorizontalScrollBarEnabled(false);
        this.setVerticalScrollBarEnabled(false);
        this.setWebViewClient(mWebViewClientBase);
        this.setWebChromeClient(mWebChromeClient);
        this.onResume();
    }

    private class WebChromeClientBase extends WebChromeClient {

        @Override
        public void onCloseWindow(WebView window) {
            captchaDialog.cancel();
            super.onCloseWindow(window);
        }
    }

    private class WebViewClientBase extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);

            return true;
        }

        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            Log.i(Captcha.TAG, "webview did start");
            super.onPageStarted(view, url, favicon);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (webView.getProgress() < 100) {
                                webView.stopLoading();
                                captchaDialog.dismiss();
                                if (captchaListener != null) {
                                    Log.d(Captcha.TAG, "time out 2");
                                    captchaListener.onReady(false);
                                }
                            }
                        }
                    });
                }
            };
            if (scheduledExecutorService == null) {
                scheduledExecutorService = Executors.newScheduledThreadPool(2);
            }

            scheduledExecutorService.schedule(timerTask, mTimeout, TimeUnit.MILLISECONDS);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            if (captchaDialog.isShowing()) {
                if (scheduledExecutorService != null) {
                    if (!scheduledExecutorService.isShutdown())
                        scheduledExecutorService.shutdown();
                }
                Log.i(Captcha.TAG, "webview did Finished");
                //captchaDialog.onPageFinished();
            }
            super.onPageFinished(view, url);
        }


        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_FILE_NOT_FOUND) {
                captchaListener.onError("error" + "ERROR_FILE_NOT_FOUND" + errorCode);
            }
            captchaDialog.show();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }


        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
            // Redirect to deprecated method, so you can use it in all SDK versions
            captchaListener.onError(req.toString() + rerr.toString());
            if (captchaDialog.getProgressDialog() != null) {
                captchaDialog.getProgressDialog().dismiss();
            }
            captchaDialog.show();
            super.onReceivedError(view, req, rerr);
        }

        @Override
        public void onReceivedHttpError(
                WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            Log.d(Captcha.TAG, request.toString() + errorResponse.toString());
            if (captchaListener != null) {
                Log.d(Captcha.TAG, "onReceivedHttpError ");
            }
            if (captchaDialog.getProgressDialog() != null) {
                captchaDialog.getProgressDialog().dismiss();
            }
            captchaDialog.show();
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (captchaListener != null) {
                Log.d(Captcha.TAG, "onReceivedHttpError ");
            }
            if (captchaDialog.getProgressDialog() != null) {
                captchaDialog.getProgressDialog().dismiss();
            }
            captchaDialog.show();
            handler.proceed(); //fix for: SSL Error. Failed to validate the certificate chain，不要调用super.xxxx
            //ref: http://blog.csdn.net/LABLENET/article/details/52683893
            //super.onReceivedSslError(view, handler, error);
        }
    }

}