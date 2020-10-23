# 易盾验证码Android SDK接入指南

## 一、SDK集成
### 1、获取SDK
从github上下载验证码sdk的aar包
[点我下载sdk](https://github.com/yidun/captcha-android-demo/tree/master/sdk)


### 2、手动导入SDK
将获取的sdk的aar文件放到工程中的libs文件夹下，然后在app的build.gradle文件中增加如下代码
```
repositories {
    flatDir {
        dirs 'libs'
    }
}
```
在dependencies依赖中增加对aar包的引用
```
compile(name:'captcha-release', ext: 'aar')//aar名称和版本号以下载下来的最新版为准
```

## 二、SDK接口
### 1）验证码属性配置类：CaptchaConfiguration
```
final CaptchaConfiguration configuration = new CaptchaConfiguration.Builder()
                        .captchaId(noSenseCaptchaId)// 验证码业务id
                        .url(captchaUrl) // 接入者无需设置，该接口为调试接口
                        // 验证码类型，默认为传统验证码，如果要使用无感知请设置以下类型,否则请不要设置
                        .mode(CaptchaConfiguration.ModeType.MODE_INTELLIGENT_NO_SENSE)
                        .listener(captchaListener) // 验证码回调监听器
                        .timeout(1000 * 10) // 超时时间，一般无需设置
                        .languageType(langType) // 验证码语言类型，一般无需设置，可设置值请参看下面验证码语言枚举类介绍
                        .debug(true) // 是否启用debug模式，一般无需设置
                        // 设置验证码框的位置和宽度，一般无需设置，不推荐设置宽高，后面将逐步废弃该接口
                        .position(-1, -1, 0, 0)
                        // 自定义验证码滑动条滑块的不同状态图片
                        .controlBarImageUrl(controlBarStartUrl, controlBarMovingUrl, controlBarErrorUrl)
                        .backgroundDimAmount(dimAmount) // 验证码框遮罩层透明度，一般无需设置
                        .touchOutsideDisappear(isTouchOutsideDisappear)  // 点击验证码框外部是否消失，默认为系统默认配置(消失)，设置false不消失
                        .useDefaultFallback(true) // 是否使用默认降级方案，默认开启
                        .failedMaxRetryCount(failedMaxRetryCount) // 当出现服务不可用时，尝试加载的最大次数，超过此次数仍然失败将触发降级，默认3次
                        .hideCloseButton(false)
                        .loadingText(etLoadingText.getText().toString()) // 设置loading文案
                        .loadingAnimResId(loadingAnimResId) // 设置loading动画，传入动画资源id
                         // 以下为私有化部署相关接口，非私有化场景无需设置
                         // -------私有化相关配置开始-------
                        .apiServer(apiServer) // 私有化部署时apiServer配置项
                        .staticServer(staticServer) // 私有化部署时staticServer配置项
                        .protocol("http") // 私有化部署时网络协议配置项，只支持"http"与"https",默认为https
                         // -------私有化相关配置结束-------
                        .ipv6(isIpv6) // 是否为ipv6网络
                        .build(context); // Context，请使用Activity实例的Context
```
**注意：**

对于loading文案自定义而言，如果需要支持多语言，请使用通过资源id设置文案的接口：

```
/**
* 通过资源id的方式设置加载文案
* 主要是为了多语言考虑，优先级高于 {@link #loadingText(String)}
*
* @param loadingTextId
* @return
*/
public Builder loadingTextId(int loadingTextId)
```

将多语言资源文件放置到对应的语言文件夹下，调用该接口传入对应文案的语言资源id即可

### 2）验证码语言枚举类

在上述构建验证码属性配置类CaptchaConfiguration的languageType属性时，其值为CaptchaConfiguration.LangType类型，可使用如下值
```
 public static enum LangType {
        LANG_ZH_CN, // 中文简体
        LANG_ZH_TW, // 中文繁体
        LANG_EN,    // 英文
        LANG_JA,    // 日文
        LANG_KO,    // 韩文
        LANG_TH,    // 泰语
        LANG_VI,    // 越南语
        LANG_FR,    // 法语
        LANG_AR,    // 阿拉伯语
        LANG_RU;    // 俄语 
        LANG_DE,    // 德语
        LANG_IT,    // 意大利语
        LANG_HE,    // 希伯来语
        LANG_HI,    // 印地语
        LANG_ID,    // 印尼语
        LANG_MY,    // 缅甸语
        LANG_LO,    // 老挝语
        LANG_MS,    // 马来语
        LANG_PL,    // 波兰语
        LANG_PT,    // 葡萄牙语
        LANG_ES,    // 西班牙语
        LANG_TR,    // 土耳其语

        private LangType() {
        }
    }
```
### 3）验证码功能提供类：Captcha
```
Captcha captcha = Captcha.getInstance().init(configuration);
captcha.validate();
```

## 三、集成说明
### 1、初始化
```
 // 创建验证码回调监听器
 captchaListener = new CaptchaListener() {
            @Override
            public void onReady() {

            }

            @Override
            public void onValidate(String result, String validate, String msg) {
                if (!TextUtils.isEmpty(validate)) {
                    Toast.makeText(getApplicationContext(), "验证成功:" + validate, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "验证失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(int code, String msg) {
                Log.e("Captcha", "验证出错，错误码:" + code + " 错误信息:" + msg);
                Toast.makeText(getApplicationContext(), "验证出错，错误码:" + code + " 错误信息:" + msg, Toast.LENGTH_LONG).show();
            }


            @Override
            public void onClose(Captcha.CloseType closeType) {
                if (closeType == Captcha.CloseType.USER_CLOSE) {
                    Toast.makeText(getApplication(), "用户关闭验证码", Toast.LENGTH_LONG).show();
                } else if (closeType == Captcha.CloseType.VERIFY_SUCCESS_CLOSE) {
                    Toast.makeText(getApplication(), "校验通过，流程自动关闭", Toast.LENGTH_LONG).show();
                }
            }
        };
	
// 创建构建验证码的配置类，可配置详细选项请参看上面SDK接口 验证码属性配置类：CaptchaConfiguration
final CaptchaConfiguration configuration = new CaptchaConfiguration.Builder()
                        .captchaId(noSenseCaptchaId)// 验证码业务id
                        .mode(CaptchaConfiguration.ModeType.MODE_INTELLIGENT_NO_SENSE)  // 验证码类型，默认为普通验证码，如果要使用无感知请设置该类型，否则无需设置
                        .listener(captchaListener) //设置验证码回调监听器
                        .build(context); // Context，请使用Activity实例的Context
// 初始化验证码
final Captcha captcha = Captcha.getInstance().init(configuration);
```
**NOTE:【重要】如果您在易盾官网申请的业务id为智能无感知类型，请务必设置mode参数，否则请不要设置**

### 2、弹出验证码
```
captcha.validate();
```
### 3、Destroy验证码资源
```
@Override
protected void onDestroy() {  // 在初始化验证码的Activity的onDestroy方法中调用，避免内存泄漏
    super.onDestroy();
    Captcha.getInstance().destroy();
}
```

## 四、混淆配置
proguard混淆配置文件增加：
```
-keepattributes *Annotation*
-keep public class com.netease.nis.captcha.**{*;}

-keep public class android.webkit.**

-keepattributes SetJavaScriptEnabled
-keepattributes JavascriptInterface

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
```

## 五、常见问题
### 1、js错误找不到onValidate
```
"Uncaught TypeError: JSInterface.onValidate is not a function", source:xxxx
```
原因：JSInterface被混淆导致，请参考[混淆配置]keep验证码相关的类。

### 2、验证码加载不出来
原因1：如果生成出的url地址在电脑上可以访问并正常显示，在手机浏览器上不能访问，而且浏览器显示“您的时钟慢了”，说明手机系统时间与证书时间不匹配。主要出现场景在测试时拿到的手机可能会有问题。

### 3、验证码加载不出来，log输出："Uncaught TypeError: Object [object Object] has no method
```
"Uncaught TypeError: Object [object Object] has no method 'onReady'", source:xxxx
```
原因：Android4.2之后使用JS接口时必须添加注解 **@JavascriptInterface**，但是有时会被混淆掉，混淆配置添加：
```
-keepattributes *Annotation*
```
请参考[混淆配置]一节。

## [Change Log](./ChangeLog.md)

## 效果演示
### 1、拖动
![](https://github.com/yidun/captcha-android-demo/blob/master/screenshots/Screenshot_demo_slide.jpg)
### 2、自定义语言类型
![](https://github.com/yidun/captcha-android-demo/blob/master/screenshots/Screenshot_demo_custom_lang.jpg)
### 3、点选
![](https://github.com/yidun/captcha-android-demo/blob/master/screenshots/Screenshot_demo_click.jpg)
### 4、短信上行
![](https://github.com/yidun/captcha-android-demo/blob/master/screenshots/Screenshot_demo_sms.jpg)
