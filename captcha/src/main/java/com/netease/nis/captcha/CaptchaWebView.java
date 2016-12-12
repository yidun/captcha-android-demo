package com.netease.nis.captcha;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hzhudingyao on 2016/12/6.
 */

public class CaptchaWebView extends WebView {
    private CaptchaListener captchaListener;
    private CaptchaDialog captchaDialog;
    private WebView webView = this;
    private Timer timer = null;
    private Context context;
    private int mTimeout = 10000;
    private boolean debug = false;
    private WebViewClientBase mWebViewClientBase = new WebViewClientBase();

    public CaptchaWebView(Context context, CaptchaListener captchaListener, CaptchaDialog captchaDialog) {
        super(context);
        this.context = context;
        this.captchaDialog = captchaDialog;
        this.captchaListener = captchaListener;
        this.debug = captchaDialog.isDebug();
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
            super.onPageStarted(view, url, favicon);
            timer = new Timer();
            timer.schedule(new TimerTask() {
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
            }, mTimeout, 1);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (timer != null) {
                timer.cancel();
                timer.purge();
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