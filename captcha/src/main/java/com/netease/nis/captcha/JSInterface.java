package com.netease.nis.captcha;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by hzhudingyao on 2016/12/5.
 */

public class JSInterface {
    private CaptchaListener captchaListener;
    private CaptchaDialog captchaDialog;
    private Context context;

    public JSInterface(Context context, CaptchaListener captchaListener, CaptchaDialog captchaDialog) {
        this.captchaListener = captchaListener;
        this.captchaDialog = captchaDialog;
        this.context = context;
    }

    /**
     * 完成验证之后的回调
     *
     * @param result   验证结果 true/false
     * @param validate 二次校验数据，如果验证结果为false，validate返回空
     * @param message  结果描述信息
     */
    @JavascriptInterface
    public void onValidate(String result, String validate, String message) {
        Log.i(Captcha.TAG, "result = " + result + ", validate = " + validate + ", message = " + message);
        if (validate != null && validate.length() > 0) {
            captchaDialog.dismiss();
        }
        if (captchaListener != null) {
            captchaListener.onValidate(result, validate, message);
        }
    }

    /**
     * 关闭webview窗口
     */
    @JavascriptInterface
    public void closeWindow() {
        captchaDialog.dismiss();
        if (captchaListener != null) {
            captchaListener.closeWindow();
        }
        if (captchaDialog.getProgressDialog() != null) {
            captchaDialog.getProgressDialog().dismiss();
        }
    }

    /**
     * 验证码组件组件初始化完成
     */
    @JavascriptInterface
    public void onReady() {
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                if (!captchaDialog.isShowing()) {

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            captchaDialog.show();
                        }
                    }, 100);
                }
            }
        });
        if (captchaListener != null) {
            captchaListener.onReady(true);
        }
        if (captchaDialog.getProgressDialog() != null) {
            captchaDialog.getProgressDialog().dismiss();
        }


    }

    /**
     * 验证码组件初始化出错
     */
    @JavascriptInterface
    public void onError(String msg) {
        captchaDialog.dismiss();
        if (captchaListener != null) {
            captchaListener.onError(msg);
        }
        final CaptchaProgressDialog dialog = (CaptchaProgressDialog) captchaDialog.getProgressDialog();
        if (dialog != null && (dialog.isCancelLoading)) {
            ((Activity) context).runOnUiThread(new Runnable() {
                public void run() {
                    if (!dialog.isShowing()) {
                        dialog.show();
                        dialog.setProgressTips("验证码加载失败");
                        dialog.isCanClickDisappear = true;
                    }
                }
            });
        }

    }
}