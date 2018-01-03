package com.netease.nis.captcha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 本地Captcha1 其中包括请求url与二次校验函数，需要与服务器协商再写
 * Created by hzhudingyao on 2016/12/1.
 */

public class Captcha {
    public final static String TAG = "myCaptcha";
    public final static String SDKVER = "2.4.2";

    public static final String baseURL = "http://cstaticdun.126.net/api/v2/mobile_2_4_2.html";
    //https://c.dun.163yun.com/api/v1/mobile.html
    //private static final String baseURL = "http://nctest-captcha.nis.netease.com/v2.x/test/mobile.html";
    public final static int NONETWROK = 0;
    public final static int INITTIMEOUT = 1;
    public final static int VALIDATETIMEOUT = 2;
    private String deviceId = "";
    private String captchaId = "";
    private CaptchaListener caListener = null;
    private Context context;
    private boolean debug;
    private CaptchaDialog captchaDialog;
    private Handler handler = null;
    private int mTimeout = 10000;
    private CaptchaProgressDialog progressDialog = null;
    private Timer timer = null;
    private boolean isProgressDialogCanceledOnTouchOutside = true;

    public Captcha(Context context) {
        this.context = context;
    }

    private int mPositionX = -1;
    private int mPositionY = -1;
    private int mPositionW = -1;
    private int mPositionH = -1;
    private boolean backgroundDimEnabled = true;
    private boolean isCanceledOnTouchOutside = true;
    private boolean isAlreadySendNetMsg;

    private static boolean isValid(String param) {
        return (param != null) && (param.length() > 0);
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean IsNetWorkEnable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 判断当前网络是否已经连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCaptchaId() {
        return this.captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }

    public CaptchaListener getCaListener() {
        return this.caListener;
    }

    public void setCaListener(CaptchaListener caListener) {
        this.caListener = caListener;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setTimeout(int timeout) {
        this.mTimeout = timeout;
    }

    public int getTimeout() {
        return this.mTimeout;
    }

    public boolean checkParams() {
        boolean ret = false;
        ret = isValid(captchaId) && (caListener != null);

        if (!isValid(captchaId)) {
            Log.d(TAG, "captchaId is wrong");
        }
        if (caListener == null) {
            Log.d(TAG, "never set caListener");
        }
        return ret;
    }

    public void setPosition(int left, int top, int w, int h) {
        mPositionX = left;
        mPositionY = top;
        mPositionW = w;
        mPositionH = h;
    }

    /**
     * 设置弹框时背景页面是否模糊，默认为模糊，也是Android的默认风格。
     *
     * @param dimEnabled，true：模糊（默认风格），false：不模糊
     */
    public void setBackgroundDimEnabled(boolean dimEnabled) {
        backgroundDimEnabled = dimEnabled;
    }

    /**
     * 设置弹框时点击对话框之外区域是否自动消失，默认为消失
     *
     * @param canceled：如果设置不自动消失请设置为false
     */
    public void setCanceledOnTouchOutside(boolean canceled) {
        isCanceledOnTouchOutside = canceled;
    }

    /**
     * 设置进度框点击对话框之外区域是否自动消失，默认为消失
     *
     * @param canceled：如果设置不自动消失请设置为false
     */
    public void setProgressDialogCanceledOnTouchOutside(boolean canceled) {
        isProgressDialogCanceledOnTouchOutside = canceled;
    }

    private boolean initDialog() {
        try {
            if (backgroundDimEnabled) {
                captchaDialog = new CaptchaDialog(context);
            } else {
                captchaDialog = new CaptchaDialog(context, R.style.DialogStyle);
            }
            captchaDialog.setPosition(mPositionX, mPositionY, mPositionW, mPositionH);
            captchaDialog.setDebug(debug);
            captchaDialog.setDeviceId(deviceId);
            captchaDialog.setCaptchaId(captchaId);
            captchaDialog.setCaListener(caListener);
            captchaDialog.setProgressDialog(progressDialog);
            captchaDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
            captchaDialog.initDialog();
            captchaDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    //用户取消验证
                    caListener.onCancel();
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Log.d(TAG, "用户取消验证");
                }
            });
        } catch (Exception e) {

        }
        return true;
    }

    private void setSchedule(int type, ProgressDialog p, int timeout) {
        //添加超时操作
        Log.d(TAG, "setSchedule start");
        MyTask timerTask = new MyTask(type, p);
        timer = new Timer();
        //timer.schedule(timerTask, timeout, 1);
        timer.schedule(timerTask, timeout);
    }

    public void start() {
        if (!checkParams()) {
            return;
        }
        Log.d(TAG, "start");
        //loading框架：
        if (!((Activity) context).isFinishing()) {
            if (progressDialog == null) {
                progressDialog = new CaptchaProgressDialog(context);
            }
            //渲染Loading
            progressDialog.setPosition(mPositionX, mPositionY, mPositionW, mPositionH);
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(isProgressDialogCanceledOnTouchOutside);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (timer != null) {
                        timer.cancel();
                        timer.purge();
                    }
                    //这里注释掉，后面captchaDialog.setOnCancelListener会有调用caListener.onCancel();
                    //caListener.onCancel();
                }
            });
            progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (timer != null) {
                        timer.cancel();
                        timer.purge();
                    }
                }
            });
            progressDialog.show();
            if (handler == null) {
                handler = new MyHandler((Activity) context, progressDialog);
            }
            isAlreadySendNetMsg = false;
            setSchedule(INITTIMEOUT, progressDialog, mTimeout);
        }
    }

    public void Validate() {

        try {
            Log.d(TAG, "validate start");
            if (!((Activity) context).isFinishing()) {
                //获取网络状态：未联网提示：
                if (!IsNetWorkEnable(context)) {
                    caListener.onError("no network!");
                    setSchedule(NONETWROK, progressDialog, 500);
                } else {

                    initDialog();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Captcha SDK Validate Error:" + e.toString());
        }
    }

    private static class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;
        CaptchaProgressDialog mp;

        MyHandler(Activity activity, CaptchaProgressDialog p) {
            mActivityReference = new WeakReference<Activity>(activity);
            mp = p;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mp != null && mp.isShowing()) {
                switch (msg.what) {
                    case NONETWROK:
                        mp.setCanceledOnTouchOutside(true);
                        mp.setProgressTips("网络异常，请检查网络后重试");
                        break;
                    case VALIDATETIMEOUT:
                        mp.setCanceledOnTouchOutside(true);
                        mp.setProgressTips("验证超时，请关闭并检查网络");
                        break;
                    case INITTIMEOUT:
                        mp.setCanceledOnTouchOutside(true);
                        mp.setProgressTips("初始化超时，请关闭并检查网络");
                        break;
                    default:
                        break;
                }
                mp.show();
                Log.d(TAG, "handleMessage end");
            }
        }
    }

    private class MyTask extends TimerTask {
        private int type;
        private ProgressDialog p;


        public MyTask(int type, ProgressDialog p) {
            this.type = type;
            this.p = p;
        }

        @Override
        public void run() {
            Log.d(TAG, "MyTask start");
            Message message = new Message();
            switch (type) {
                case INITTIMEOUT:
                    message.what = INITTIMEOUT;
                    break;
                case NONETWROK:
                    message.what = NONETWROK;
                    break;
                case VALIDATETIMEOUT:
                    message.what = VALIDATETIMEOUT;
                    break;
                default:
                    return;

            }
            //避免重复发送网络状态消息，比如因为无网导致的网络超时，只需提示无网络连接，而无需提示初始化超时
            if (!isAlreadySendNetMsg) {
                handler.sendMessage(message);
                isAlreadySendNetMsg = true;
            }

            Log.d(TAG, "MyTask end");
        }

    }
}
