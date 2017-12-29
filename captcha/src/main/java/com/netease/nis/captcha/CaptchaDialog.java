package com.netease.nis.captcha;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

/**
 * 自定义Dialog 主要用于获取url地址然后加载
 * Created by hzhudingyao on 2016/12/1.
 */

public class CaptchaDialog extends Dialog {
    private CaptchaWebView dwebview = null;
    private CaptchaListener dcaListener = null;
    private Context dcontext = null;
    private String dDeviceId = "";
    private String dCaptchaId = "";
    private String dTitle = "";
    private int dWidth;
    private float dScale;
    private boolean debug = false;
    private boolean isShowing = false;
    private int mPositionX = -1;
    private int mPositionY = -1;
    private int mPositionW = -1;
    private int mPositionH = -1;
    private ProgressDialog progressDialog = null;

    public CaptchaDialog(Context context) {
        super(context);
        this.dcontext = context;
    }

    public CaptchaDialog(Context context, int themeResId) {
        super(context, R.style.DialogStyle);
        this.dcontext = context;
    }

    public void setPosition(int left, int top, int w, int h) {
        mPositionX = left;
        mPositionY = top;
        mPositionW = w;
        mPositionH = h;
    }

    //验证标题, 默认无标题, 不宜过长.
    public CaptchaDialog setTitle(String title) {
        this.dTitle = title;
        return this;
    }

    public CaptchaDialog setCaListener(CaptchaListener caListener) {
        this.dcaListener = caListener;
        return this;
    }

    public CaptchaDialog setCaptchaId(String captchaId) {
        this.dCaptchaId = captchaId;
        return this;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public CaptchaDialog setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    private String getDeviceId() {

        try {
            if (this.dDeviceId.equals("")) {
                TelephonyManager tel = (TelephonyManager) this.dcontext.getSystemService(Context.TELEPHONY_SERVICE);
                if (tel != null) {
                    this.dDeviceId = tel.getDeviceId();
                }
            }
        } catch (Exception e) {
            Log.e(Captcha.TAG, "getImei failed");
        }
        return this.dDeviceId;
    }

    public CaptchaDialog setDeviceId(String deviceId) {
        this.dDeviceId = deviceId;
        return this;
    }

    public ProgressDialog getProgressDialog() {
        return this.progressDialog;
    }

    public CaptchaDialog setProgressDialog(ProgressDialog progressDialog) {
        if (this.progressDialog == null && progressDialog != null) {
            this.progressDialog = progressDialog;
        }
        return this;
    }

    public boolean isShowing() {
        return this.isShowing;
    }

    public void initDialog() {
        Log.d(Captcha.TAG, "start init dialog");
        getDialogWidth();
        setWebView();
    }

    private void getDialogWidth() {

        try {

            DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            float scale = metrics.density;
            dScale = scale;

            final int MINWIDTH = 270; // 组件的最小宽度

            if (mPositionW > MINWIDTH) {
                dWidth = mPositionW;
            } else {
                if (height < width) {
                    width = height * 3 / 4;
                }
                width = width * 4 / 5;
                if ((int) (width / scale) < MINWIDTH) {
                    width = (int) (MINWIDTH * scale);
                }
                dWidth = width;
            }

        } catch (Exception e) {
            Log.e(Captcha.TAG, "getDialogWidth failed");
        }
    }

    private void setWebView() {
        if (dwebview == null) {
            dwebview = new CaptchaWebView(dcontext, dcaListener, this);
        }
        StringBuffer sburl = new StringBuffer();
        sburl.append(Captcha.baseURL);
        sburl.append("?captchaId=" + this.dCaptchaId);
        sburl.append("&deviceId=" + getDeviceId());
        sburl.append("&os=android");
        sburl.append("&osVer=" + Build.VERSION.RELEASE);
        sburl.append("&sdkVer=" + Captcha.SDKVER);
        sburl.append("&title=" + this.dTitle);
        sburl.append("&debug=" + this.debug);
        sburl.append("&width=" + (int) (dWidth / dScale));
        String requrl = sburl.toString();
        Log.d(Captcha.TAG, "url: " + requrl);
        dwebview.addJavascriptInterface(new JSInterface(dcontext, dcaListener, this), "JSInterface");
        dwebview.loadUrl(requrl);
        dwebview.buildLayer();

        WindowManager.LayoutParams params = getWindow().getAttributes();
        //params.alpha = (float)1.0; //0.0-1.0
        if (mPositionX != -1) {
            params.gravity = Gravity.LEFT;
//            params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            params.x = mPositionX;
        }
        if (mPositionY != -1) {
            params.gravity |= Gravity.TOP;
            params.y = mPositionY;
        }
        if (mPositionW > 0) {
            params.width = mPositionW;
        }
        if (mPositionH > 0) {
            params.height = mPositionH;
        }
        getWindow().setAttributes(params);
    }

    //webview加载完成后，再把进度条关闭
    public void onPageFinished() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dwebview);

        final LayoutParams layoutParams = dwebview.getLayoutParams();
        layoutParams.width = dWidth;
        int height = (int) ((float) dWidth / 2.0 + 52 * dScale + 15);
        layoutParams.height = height;
        //layoutParams.height = LayoutParams.WRAP_CONTENT;
        dwebview.setLayoutParams(layoutParams);
    }

    @Override
    public void show() {
        isShowing = true;
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
        try {
            if (dcontext != null && !((Activity) dcontext).isFinishing()) {
                super.show();
            }
        } catch (Exception e) {
            Log.e(Captcha.TAG, "Captcha Dialog show Error:" + e.toString());
        }

    }

    @Override
    public void dismiss() {
        isShowing = false;
        super.dismiss();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


}
