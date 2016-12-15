package com.netease.nis.captcha;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
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
    //定时操作线程池
    private ScheduledExecutorService scheduledExecutorService = null;
    public CaptchaWebView(Context context, CaptchaListener captchaListener, CaptchaDialog captchaDialog) {
        super(context);
        this.context = context;
        this.captchaDialog = captchaDialog;
        this.captchaListener = captchaListener;
        this.debug = captchaDialog.isDebug();
        mWebViewClientBase = new WebViewClientBase();
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
        this.onResume();
    }

    private class WebViewClientBase extends WebViewClient {

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
                                    captchaListener.onReady(false);
                                }
                            }
                        }
                    });
                }
            };
            if(scheduledExecutorService == null){
                scheduledExecutorService = Executors.newScheduledThreadPool(2);
            }

            scheduledExecutorService.schedule(timerTask, mTimeout, TimeUnit.MILLISECONDS);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i(Captcha.TAG, "webview did Finished");
            if(scheduledExecutorService!=null){
               if(!scheduledExecutorService.isShutdown())
                   scheduledExecutorService.shutdown();
            }
          if (debug) {
                if (captchaListener != null) {
                    captchaListener.onReady(false);
                }
                captchaDialog.show();
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            if (captchaListener != null) {
                captchaListener.onReady(false);
            }
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedHttpError(
                WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            if (captchaListener != null) {
                captchaListener.onReady(false);
            }
            captchaDialog.show();
            super.onReceivedHttpError(view, request, errorResponse);
        }
    }

}