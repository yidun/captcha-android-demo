package com.netease.nis.captcha;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 本地Captcha1 其中包括请求url与二次校验函数，需要与服务器协商再写
 * Created by hzhudingyao on 2016/12/1.
 */

public class Captcha {
    public final static String TAG = "myCaptcha";
    public final static String SDKVER = "1.0.0";
    private String deviceId = "";
    private String captchaId = "";
    private CaptchaListener caListener = null;
    private Context context;
    private boolean debug;

    public Captcha(Context context) {
        this.context = context;
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

    public void Validate() {
        try {
            //获取网络状态：未联网提示：
            boolean isnetworkEnabel = IsNetWorkEnable(context);
            if(!isnetworkEnabel){
                caListener.onReady(false);
                return ;
            }
            CaptchaDialog cadialog = new CaptchaDialog(context)
                    .setDebug(debug)
                    .setDeviceId(deviceId)
                    .setCaptchaId(captchaId)
                    .setCaListener(caListener);
            cadialog.initDialog();
            cadialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    //TODO 用户取消验证
                    caListener.onCancel();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Captcha SDK Validate Error:" + e.toString());
        }
    }
}
