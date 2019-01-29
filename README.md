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
                        .mode(CaptchaConfiguration.ModeType.MODE_INTELLIGENT_NO_SENSE) // 验证码类型，默认为传统验证码，如果要使用无感知请设置该类型，否则无需设置
                        .listener(captchaListener) // 验证码回调监听器
                        .timeout(1000 * 10) // 超时时间，一般无需设置
                        .languageType(langType) // 验证码语言类型，一般无需设置，可设置值请参看下面验证码语言枚举类介绍
                        .debug(true) // 是否启用debug模式，一般无需设置
                        .position(-1, -1, 0, 0) // 设置验证码框的位置和宽度，一般无需设置，不推荐设置宽高，后面将会将逐步废弃该接口
                        .controlBarImageUrl(controlBarStartUrl, controlBarMovingUrl, controlBarErrorUrl) // 自定义验证码滑动条滑块的不同状态图片
                        .backgroundDimAmount(dimAmount) // 验证码框遮罩层透明度，一般无需设置
                        .build(context); // Context，请使用Activity实例的Context
```
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
                    Toast.makeText(getApplicationContext(), "验证成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "验证失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(getApplicationContext(), "验证出错" + msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {

            }
        };
	
// 创建构建验证码的配置类，可配置详细选项请参看上面SDK接口 验证码属性配置类：CaptchaConfiguration
final CaptchaConfiguration configuration = new CaptchaConfiguration.Builder()
                        .captchaId(noSenseCaptchaId)// 验证码业务id
                        .mode(CaptchaConfiguration.ModeType.MODE_INTELLIGENT_NO_SENSE)  // 验证码类型，默认为传统验证码，如果要使用无感知请设置该类型，否则无需设置
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


## 效果演示
### 1、拖动
![](https://github.com/yidun/captcha-android-demo/raw/master/screenshots/Screenshot_1482991322.png)
![](https://github.com/yidun/captcha-android-demo/raw/master/screenshots/style_drag.png)
![](https://github.com/yidun/captcha-android-demo/raw/master/screenshots/Screenshot_1482991371.png)

### 2、点选
![](https://github.com/yidun/captcha-android-demo/raw/master/screenshots/style_select.png)

### 3、短信
![](https://github.com/yidun/captcha-android-demo/raw/master/screenshots/style_msg.png)

