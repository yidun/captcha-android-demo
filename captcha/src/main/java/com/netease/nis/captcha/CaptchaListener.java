package com.netease.nis.captcha;

/**
 * Dialog验证接口
 * Created by hzhudingyao on 2016/12/1.
 */

public interface CaptchaListener {

    //调试用
    void onReady(boolean ret);

    //通知native关闭验证
    void closeWindow();

    //通知javascript发生严重错误
    void onError(String msg);

    //通知验证结果
    void onValidate(String result, String validate, String message);

    //用户取消验证
    void onCancel();
}
