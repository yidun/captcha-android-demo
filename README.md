# 易盾验证码Android SDK接入指南

## 一、SDK集成
### 1、自动导入（推荐）
在Module的build.gradle文件中添加依赖和属性配置：
```
dependencies {
    compile xxxxxxx //如果指定版本号升级版本后请clean下工程
}
```

### 2、手动导入
直接将jar包导入到工程目录下

## 二、SDK接口
```
//可以自定义deviceid，若不填写则默认获取手机的imei值
public void setDeviceId();  

//可以自定义sdk框架超时时间，单位毫秒，默认是10000即10秒
public void setTimeout();

//是否开启调试，设置为true后可以看到部分调试信息
public void setDebug();

//简单测试是否填写captcha 是否初始化，包括captchaId与设置监听对象
public boolean checkParams();
```

## 三、集成说明
### 1、初始化
```
Captcha mCaptcha = new Captcha(context);

//设置CaptchaId：这里填入从易盾官网申请到的验证码id
mCaptcha.setCaptchaId(captchaid);

//设置监听对象captchaListener
mCaptcha.setCaListener(captchalistener);


//其中captchalistener接口形式：
public interface CaptchaListener {
    //通知验证已准备完毕,true准备完成/false未准备完成
    void onReady(Boolean status); 

    //通知native关闭验证
    void closeWindow();

    //通知javascript发生严重错误，msg为错误信息
    void onError(String msg);

    //通知验证结果，其中validate值为返回值。可以在该函数中进行用户自定义二次校验
    void onValidate(String result, String validate, String message);

    //用户取消验证
    void onCancel();
}
```

示例：
```
/*验证码SDK,该Demo采用异步获取方式*/
private UserLoginTask mLoginTask = null;
//自定义Listener格式如下
CaptchaListener myCaptchaListener = new CaptchaListener() {

    @Override
    public void onValidate(String result, String validate, String message) {
        //验证结果，valiadte，可以根据返回的三个值进行用户自定义二次验证
        if (validate.length() > 0) {
            toastMsg("验证成功，validate = " + validate);
        } else {
            toastMsg("验证失败：result = " + result + ", validate = " + validate + ", message = " + message);

        }
    }

    @Override
    public void closeWindow() {
        //请求关闭页面
        toastMsg("关闭页面");
    }

    @Override
    public void onError(String errormsg) {
        //出错
        toastMsg("错误信息：" + errormsg);
    }

    @Override
    public void onCancel() {
        toastMsg("取消线程");
        //用户取消加载或者用户取消验证，关闭异步任务，也可根据情况在其他地方添加关闭异步任务接口
        if (mLoginTask != null) {
            if (mLoginTask.getStatus() == AsyncTask.Status.RUNNING) {
                Log.i(TAG, "stop mLoginTask");
                mLoginTask.cancel(true);
            }
        }
    }

    @Override
    public void onReady(boolean ret) {
        //该为调试接口，ret为true表示加载Sdk完成
        if (ret) {
            toastMsg("验证码sdk加载成功");
        }
    }

};

@Override
protected void onCreate(Bundle savedInstanceState) {
	....
	....

    //初始化验证码SDK相关参数，设置CaptchaId、Listener最后调用start初始化。
    if (mCaptcha == null) {
        mCaptcha = new Captcha(mContext);
    }
    mCaptcha.setCaptchaId(testCaptchaId);
    mCaptcha.setCaListener(myCaptchaListener);
    //可选：开启debug
    mCaptcha.setDebug(false);
    //可选：设置超时时间
    mCaptcha.setTimeout(10000);
    //登陆操作
    button_login.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
            mLoginTask = new UserLoginTask();
            //关闭mLoginTask任务可以放在myCaptchaListener的onCancel接口中处理
            mLoginTask.execute();
            //必填：初始化 captcha框架
            mCaptcha.start();
            //可直接调用验证函数Validate()，本demo采取在异步任务中调用（见UserLoginTask类中）
            //mCaptcha.Validate();
        }
    });

}

public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    UserLoginTask() {
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        //可选：简单验证DeviceId、CaptchaId、Listener值
        return mCaptcha.checkParams();
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            //必填：开始验证
            mCaptcha.Validate();
        } else {
            toastMsg("验证码SDK参数设置错误,请检查配置");
        }
    }

    @Override
    protected void onCancelled() {
        mLoginTask = null;
    }
}
```
### 2、弹出验证码
```
//启动验证码sdk框架，主要为loading界面
public void start();

//开始验证，主要为验证码界面
public void Validate();
```

示例
```
mCaptcha.start();
//可直接调用验证函数Validate()，本demo采取在异步任务中调用（见UserLoginTask类中）
//mCaptcha.Validate();
```

## 效果演示
![](https://github.com/yidun/captcha-android-demo/raw/master/screenshots/Screenshot_1482991322.png)
![](https://github.com/yidun/captcha-android-demo/raw/master/screenshots/Screenshot_1482991359.png)
![](https://github.com/yidun/captcha-android-demo/raw/master/screenshots/Screenshot_1482991371.png)
