﻿# 行为式验证码
全新人机验证方式，高效拦截机器行为，业务安全第一道防线。搭载风险感知引擎，智能切换验证难度，安全性高，极致用户体验。读屏软件深度适配，视障群体也可轻松使用，符合工信部无障碍适配要求

## 兼容性
| 条目        | 说明                                                                      |
| ----------- | -----------------------------------------------------------------------  |
| 适配版本    | minSdkVersion 16 及以上版本 |

## 注意事项
<font color=red>***和其他易盾产品一起使用需要考虑版本兼容性，若同时接多个易盾sdk，需要测试回归下是否有异常***</font>

## 资源引入

### 远程仓库依赖(推荐)
从 3.2.2 版本开始，提供远程依赖的方式，本地依赖的方式逐步淘汰。本地依赖集成替换为远程依赖请先去除干净本地包，避免重复依赖冲突

确认 Project 根目录的 build.gradle 中配置了 mavenCentral 支持

```
buildscript {
    repositories {
        mavenCentral()
    }
    ...
}

allprojects {
    repositories {
        mavenCentral()
    }
}
```
在对应 module 的 build.gradle 中添加依赖

```
implementation 'io.github.yidun:captcha:3.6.3'
```
### 本地手动依赖

#### 获取 SDK 

从易盾官网下载验证码 sdk 的 aar 包 [包地址](https://github.com/yidun/captcha-android-demo/tree/master/sdk)

#### 添加 aar 包依赖

将获取到的 aar 文件拷贝到对应 module 的 libs 文件夹下（如没有该目录需新建），然后在 build.gradle 文件中增加如下代码

```
android{
    repositories {
        flatDir {
            dirs 'libs'
        }
    } 
}    

dependencies {
    implementation(name: 'captcha-release-3.6.3', ext: 'aar') // aar包版本以官网下载下来为准
    implementation(name: 'base-core-1.1.4', ext: 'aar')
    implementation 'com.github.bumptech.glide:glide:4.9.0' // 项目中有则无需添加
}
```

## 各种配置

### 混淆配置

在 proguard-rules.pro 文件中添加如下混淆规则（从 3.5.1 版本开始可以不用配置）

```
-keepattributes *Annotation*
-keep public class com.netease.nis.captcha.**{*;}

-keep public class android.webkit.**

-keepattributes SetJavaScriptEnabled
-keepattributes JavascriptInterface

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
// 项目中使用了 glide 4.9.0 版本，注意一下 glide 的混淆规则
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.**{*;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
```

## 快速调用示例

```
public class DemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CaptchaConfiguration captchaConfiguration = new CaptchaConfiguration.Builder().
                captchaId("业务id").
                listener(new CaptchaListener() {
                    @Override
                    public void onCaptchaShow() {
                    }
                    @Override
                    public void onValidate(String result, String validate, String msg, String captchaType, String errorValidate) {
                    }

                    @Override
                    public void onError(int code, String msg) {
                    }

                    @Override
                    public void onClose(Captcha.CloseType closeType) {
                    }
                })
                .build(this);
        Captcha captcha = Captcha.getInstance();
        captcha.init(captchaConfiguration);
        captcha.validate();
    }
}
```

更多使用场景请参考
[demo](https://github.com/yidun/captcha-android-demo)

## SDK 方法说明

### 1. 获取 Captcha 单例对象

#### 代码说明

```
Captcha captcha = Captcha.getInstance()
```

### 2. 初始化

#### 代码说明

```
captcha.init(CaptchaConfiguration configuration)
```

#### CaptchaConfiguration 可配置元素说明

CaptchaConfiguration 采用建造者模式，可配置项通过 CaptchaConfiguration.Builder() 配置

| 配置项 |参数/类型|是否必须|默认值|描述|
|----|----|--------|------|----|
| captchaId | captchaId:String | 是 | 无 | 业务id |
| listener | listener:CaptchaListener | 是 | 无 | 回调监听 |
| timeout | timeout:long | 否 | 10s | 超时时间，单位ms |
| backgroundDimAmount | amount:float | 否 | 0.5 | 验证码框遮罩层透明度 |
| controlBarImageUrl | startIconUrl:String,movingIconUrl:String,errorIconUrl:String | 否 | 无 | 验证码控制条的滑块的图片 |
| position | xCoordinate:int,yCoordinate:int | 否 | -1，-1 | 验证码弹窗位置/已废弃 |
| debug | isEnableDebug:boolean | 否 | false | 是否启用debug模式 |
| languageType | langType:LangType | 否 | 系统语言 | 语言类型：枚举值 |
| theme | theme:Theme | 否 | Theme.LIGHT | 主题 |
| touchOutsideDisappear | isDisappear:boolean | 否 | true | 触摸外部是否关闭弹窗 |
| useDefaultFallback | useDefaultFallback:boolean | 否 | true | 是否采用默认降级 |
| failedMaxRetryCount | failedMaxRetryCount:int | 否 | 3 | 触发降级的最大错误次数，当超过这个错误次数时触发降级 |
| hideCloseButton | isHideCloseButton:boolean | 否 | false | 是否隐藏关闭按钮 |
| protocol | protocol:String | 否 | https | 资源加载协议: http 或 https |
| ipv6 | ipv6:boolean | 否 | false | 网络是否ipv6 |
| loadingText | text:String | 否 | 无 | 加载弹窗的加载文案 |
| loadingTextId | loadingTextId:int | 否 | 无 | 资源id的方式设置加载文案，优先级高于loadingText |
| loadingAnimResId | loadingAnimResId:int | 否 | 无 | 加载动画资源id，支持gif |
| extraData | extraData:String | 否 | 无 | 额外参数，在二次校验result返回/3.3.3版本加入 |
| isCloseButtonBottom | isCloseButtonBottom:boolean | 否 | false | 关闭按钮是否在下方 |
| isShowLoading | isShowLoading:boolean | 否 | true | 是否显示loading效果 |
| isMourningDay | isMourningDay:boolean | 否 | false | 是否开启黑白模式 |
| size | size:String | 否 | 系统字体 | 字体大小设置，支持small、medium、large、x-large |
| apiServer | apiServer:String | 否 | 无 | 私有化接口域名，私有化部署必须，协议需要和protocol对应 |
| staticServer | staticServer:String | 否 | 无 | 私有化资源域名，私有化部署必须，协议需要和protocol对应 |
| isShowInnerClose | isShowInnerClose:boolean | 否 | false | 是否显示验证码内部关闭按钮 |
| disableReport | disableReport:boolean | 否 | false | 是否禁止数据上报和崩溃收集 |
| keyCodeBackEnable | isKeyCodeBackEnable:boolean | 否 | true | 弹窗物理返回键是否可用 |
| openPreLoad | isOpenPreLoad:boolean | 否 | false | 是否打开预加载，打开预加载可加快显示速度 |
| setDialogStyle | dialogStyle:String | 否 | 无 | 自定义弹窗主题，这里设置主题名 |

高级 ui 配置

<img src="https://github.com/yidun/captcha-android-demo/blob/master/screenshots/Popup.png">
<img src="https://github.com/yidun/captcha-android-demo/blob/master/screenshots/Custom.png">

从 3.5.7 版本开始高级 ui 用 json 字符串来设置
| 配置项 |参数/类型|是否必须|默认值|描述|
|----|----|--------|------|----|
| setUiParams | uiParamsJson:String | 否 | 无 | 高级 ui json字符串 |

json 字段支持范围如下

```
{
   "customStyles":{
       "imagePanel":{
           "align":"top", // 仅在 float 模式下有效，可选项为 top 和 bottom，指定 imagePanel 出现在 controlBar 上方还是下方
           "borderRadius":"2px", // 边框圆角大小，当传入数字时，默认单位 px；当传入字符串时，支持单位 px、rem、em、%
           "loadBackgroundImage":"xxx", // 加载中时的背景图片地址，v2.21.4 后新增，需要填入一个 url
           "loadBackgroundColor" :"#f00"  // 加载中时的背景颜色，v2.21.4 后新增，支持 CSS 所有颜色值
       },
       "controlBar":{
          "height": "40px", // 高度，当传入数字时，默认单位 px；当传入字符串时，支持单位 px、rem、em，不支持 %，会影响功能
          "borderRadius": "20px", // 边框圆角大小，当传入数字时，默认单位 px；当传入字符串时，支持单位 px、rem、em、%
          "paddingLeft": "40px", // 左边距，如果相对于整个 bar 居中提示文字，设为 0； v2.21.4 后新增，当传入数字时，默认单位 px；当传入字符串时，支持单位 px、rem、%，不支持 em，会影响功能
          "borderColor": "transparent", // 边框颜色，支持 CSS 所有颜色值
          "background": "#e1c1ff", // 背景颜色，占满整个 controlBar，支持 CSS 所有颜色值
          "borderColorMoving": "#0000ff", // 滑动时已滑动区域边框、滑块颜色，仅在滑动类型验证码下有效，支持 CSS 所有颜色值
          "backgroundMoving": "rgba(0, 0, 255, 0.1)", // 滑动时已滑动区域背景颜色，仅在滑动类型验证码下有效，支持 CSS 所有颜色值
          "borderColorSuccess": "#00ff00", // 验证成功时边框、文字、滑块背景颜色，如果是滑动，已滑动区域有效，支持 CSS 所有颜色值
          "backgroundSuccess": "rgba(0, 255, 0, 0.1)", // 验证成功时背景颜色，如果是滑动，已滑动区域有效，支持 CSS 所有颜色值
          "borderColorError": "#ff0000", // 验证失败时边框、文字、滑块背景颜色，如果是滑动，已滑动区域有效，支持 CSS 所有颜色值
          "backgroundError": "rgba(255, 0, 0, 0.1)", // 验证失败时背景颜色，如果是滑动，已滑动区域有效，支持 CSS 所有颜色值
          "slideBackground": "#fff", // 滑块滑动前背景颜色，支持 CSS 所有颜色值
          "textSize": "14px", // 滑块内文本大小，当传入数字时，默认单位 px；当传入字符串时，支持单位 px、rem，不支持 em、%，会影响功能
          "textColor": "orange", // 滑块内文本颜色（滑块滑动前、失败、成功前的颜色），支持 CSS 所有颜色值
          "slideIcon": "", // 滑块滑动前 icon，需要是一个 url，如果未提供则按照默认icon显示
          "slideIconMoving": "", // 滑块滑动过程 icon，需要一个 url，如果未提供则按照默认 icon 显示
          "slideIconSuccess": "", // 滑块验证成功时 icon，需要一个 url，如果未提供则按照默认 icon 显示
          "slideIconError": "" // 滑块验证失败时 icon，需要一个 url，如果未提供则按照默认 icon 显示                          
       },
        "icon": {
          "intellisenseLogo": "" // 智能无感知的 icon，需要一个 url，如果未提供则按照默认 icon 显示
       },
        "gap": "30px", // imagePanel 相对 controlBar 的间距大小，当传入数字时，默认单位 px；当传入字符串时，支持单位 px、rem、em、%
        "executeBorderRadius": "4px", // imagePanel 右边顶部的操作按钮最外层容器圆角大小，当传入数字时，默认单位 px；当传入字符串时，支持单位 px、rem、em、%
        "executeBackground": "rgba(0, 0, 0, 0.2)", // imagePanel 右边顶部的操作按钮最外层容器背景颜色，支持 CSS 所有颜色值
        "executeTop": "4px", // imagePanel 右边顶部的操作按钮最外层容器顶部距离，当传入数字时，默认单位 px；当传入字符串时，支持单位 px、rem、em、%
        "executeRight": "4px" // imagePanel 右边顶部的操作按钮最外层容器右侧距离，当传入数字时，默认单位 px；当传入字符串时，支持单位 px、rem、em、%
   }，
   "popupStyles":{
        "capBarHeight": "50px", // 弹框头部标题所在容器高度，当传入数字时，默认单位 px；当传入字符串时，支持单位 px，v2.21.4 后支持 rem；不支持 %、em，会影响功能
        "capBarTextAlign": "left", // 弹框头部标题文字对齐方式，可选值为 left、center、right
        "capBarBorderColor": "#fff",  // 弹框头部标题下边框颜色，想要去掉的话可取 transparent 或者与背景色同色，支持 CSS 所有颜色值
        "capBarTextColor": "#333", // 弹框头部标题文字颜色，支持 CSS 所有颜色值
        "capBarTextSize": 14, // 弹框头部标题文字字体大小，当传入数字时，默认单位 px；v2.21.4 后支持字符串，单位 px、rem；不支持 %、em，会影响功能
        "capBarTextWeight": 600, // 弹框头部标题文字字体体重，可设置粗细，也就是 css 样式的 font-weight 属性
        "capPadding": 15, //  弹框 imagePanel 与外边框距离，相当于总体设置 capPaddingTop，capPaddingRight，capPaddingBottom，capPaddingLeft，当传入数字时，默认单位 px；当传入字符串时，支持单位 px；不支持 %、rem，em，如果传入的单位是 %、rem、em 时，保留数字并强转为 px 单位
        "capPaddingTop": 0, // 弹框 imagePanel 与上外边框距离，会覆盖 capPadding 上边距，当传入数字时，默认单位 px；v2.21.4 后支持字符串，单位 px，如果传入的单位是 %、rem、em 时，保留数字并强转为 px 单位
        "capPaddingRight": 15, // 弹框 imagePanel 与右外边框距离，会覆盖 capPadding 右边距，当传入数字时，默认单位 px；v2.21.4 后支持字符串，单位 px，如果传入的单位是 %、rem、em 时，保留数字并强转为 px 单位
        "capPaddingBottom": 0, // 弹框 imagePanel 与下外边框距离，会覆盖 capPadding 下边距，当传入数字时，默认单位 px；v2.21.4 后支持字符串，单位 px，如果传入的单位是 %、rem、em 时，保留数字并强转为 px 单位
        "capPaddingLeft": 15, // 弹框 imagePanel 与左外边框距离，会覆盖 capPadding 左边距，当传入数字时，默认单位 px；v2.21.4 后支持字符串，单位 px，如果传入的单位是 %、rem、em 时，保留数字并强转为 px 单位
        "opacity": 0.1, // 弹框遮罩层透明度,0 ~ 1 之间的数字
        "radius": 20, // 弹框最外层容器圆角，当传入数字时，默认单位 px；v2.21.4 后支持字符串，单位 px、%、rem，em
        "borderColor": "transparent", // 弹框最外层容器边框颜色，想要去掉的话可取 transparent 或者与背景色同色，支持 CSS 所有颜色值
        "paddingTop": 20, // 弹框最外层容器上内边距，实践时可与 capPaddingTop 配合，当传入数字时，默认单位 px；v2.21.4 后支持字符串，单位 px、%、rem，em
        "paddingBottom": 40, // 弹框最外层容器下内边距，实践时候可与 capPaddingBottom 配合，当传入数字时，默认单位 px；v2.21.4 后支持字符串，单位 px、%、rem，em
        "position": "absolute", // 弹框的 css 定位，可填 absolute、fixed、static 等，默认时 absolute
        "top": "20%" // 弹框相对顶部的位置，也就是 css 样式的 top 属性   
   }，
   "customTexts":{
        "slideTip": "向右拖动滑块填充拼图" // 滑动模块文案      
   }
}
```

3.5.7 版本以下沿用如下配置项

| 配置项 |参数/类型|是否必须|默认值|描述|
|----|----|--------|------|----|
| setImagePanelAlign | imagePanelAlign:String | 否 | 无 | imagePanel 的对齐方式 |
| setImagePanelLoadBackgroundImage | imagePanelLoadBackgroundImage:String | 否 | 无 | imagePanel 加载中时的背景图片地址 |
| setImagePanelLoadBackgroundColor | imagePanelLoadBackgroundColor:String | 否 | 无 | imagePanel 加载中时的背景颜色 |
| setImagePanelBorderRadius | imagePanelBorderRadius:String | 否 | 无 | imagePanel 的圆角 |
| setControlBarHeight | controlBarHeight:String | 否 | 无 | controlBar高度 | 
| setControlBarBorderRadius | controlBarBorderRadius:String | 否 | 无 | controlBar圆角 |
| setControlBarPaddingLeft | controlBarPaddingLeft:String | 否 | 无 | controlBar提示文本左边距 |
| setControlBarBorderColor | controlBarBorderColor:String | 否 | 无 | controlBar边框颜色 |
| setControlBarBackground | controlBarBackground:String | 否 | 无 | controlBar背景颜色 |
| setControlBarBorderColorMoving | controlBarBorderColorMoving:String | 否 | 无 | controlBar滑动时边框颜色，滑动类型验证码下有效 |
| setControlBarBackgroundMoving | controlBarBackgroundMoving:String | 否 | 无 | controlBar滑动时背景颜色，滑动类型验证码下有效 |
| setControlBarBorderColorSuccess | controlBarBorderColorSuccess:String | 否 | 无 | controlBar 成功时边框颜色，此颜色同步了文字成功时文字颜色、滑块背景颜色|
| setControlBarBackgroundSuccess | controlBarBackgroundSuccess:String | 否 | 无 | controlBar 成功时背景颜色 |
| setControlBarBorderColorError | controlBarBorderColorError:String | 否 | 无 | controlBar 失败时边框颜色 |
| setControlBarBackgroundError | controlBarBackgroundError:String | 否 | 无 | controlBar 失败时背景颜色 |
| setControlBarSlideBackground | controlBarSlideBackground:String | 否 | 无 | controlBar 滑块背景颜色 |
| setControlBarTextSize | controlBarTextSize:String | 否 | 无 | controlBar 内容文本大小 |
| setControlBarTextColor | controlBarTextColor:String | 否 | 无 | controlBar 内容文本颜色（滑块滑动前的颜色，失败、成功前的颜色） |
| setGap | gap:String | 否 | 无 | imagePanel 相对 controlBar 的间距大小 |
| setExecuteBorderRadius | executeBorderRadius:String | 否 | 无 | imagePanel 顶部的操作按钮圆角大小 |
| setExecuteBackground | executeBackground:String | 否 | 无 | imagePanel 顶部的操作按钮背景色 |
| setExecuteTop | executeTop:String | 否 | 无 | imagePanel 顶部的操作按钮外层容器距离 imgagePanel 顶部距离 |
| setExecuteRight | executeRight:String | 否 | 无 | imagePanel 顶部的操作按钮外层容器距离 imgagePanel 右侧距离 |
| setCapBarHeight | capBarHeight:int | 否 | 无 | 弹框头部标题所在容器高度 |
| setCapBarTextAlign | capBarTextAlign:String | 否 | 无 | 弹框头部标题文字对齐方式，可选值为 left center right |
| setCapBarBorderColor | capBarBorderColor:String | 否 | 无 | 弹框头部下边框颜色，想要去掉的话可取 transparent 或者与背景色同色 #fff |
| setCapBarTextColor | capBarTextColor:String | 否 | 无 | 弹框头部标题文字颜色 |
| setCapBarTextSize | capBarTextSize:int | 否 | 无 | 弹框头部标题文字字体大小 |
| setCapBarTextWeight | capBarTextWeight:String | 否 | 无 | 弹框头部标题文字字体体重，可设置粗细，参考：https://developer.mozilla.org/en-US/docs/Web/CSS/font-weight |
| setCapPadding | capPadding:int | 否 | 无 | 验证码弹框 body 部分的内边距，相当于总体设置 capPaddingTop，capPaddingRight，capPaddingBottom，capPaddingLeft |
| setCapPaddingTop | capPaddingTop:int | 否 | 无 | 验证码弹框 body 部分的【上】内边距，覆盖 capPadding 对于上内边距的设置 |
| setCapPaddingRight | capPaddingRight:int | 否 | 无 | 验证码弹框 body 部分的【右】内边距，覆盖 capPadding 对于右内边距的设置 |
| setCapPaddingBottom | capPaddingBottom:int | 否 | 无 | 验证码弹框 body 部分的【底】内边距，覆盖 capPadding 对于底内边距的设置 |
| setCapPaddingLeft | capPaddingLeft:int | 否 | 无 | 验证码弹框 body 部分的【左】内边距，覆盖 capPadding 对于左内边距的设置 |
| setRadius | radius:int | 否 | 无 | 弹框圆角 |
| setPaddingTop | paddingTop:int | 否 | 无 | 弹框【上】内边距，实践时候可与 capPaddingTop 配合 |
| setPaddingBottom | paddingBottom:int | 否 | 无 | 弹框【下】内边距，实践时候可与 capPaddingBottom 配合 |
| setBorderColor | borderColor:String | 否 | 无 | 弹框外层容器颜色 |
| setSlideTip | slideTip:String | 否 | 无 | 滑动模块文案 |
| setRefreshInterval | refreshInterval:int | 否 | 300 | 错误提示时长/ms |
| isDisableFocus | disableFocus:boolean | 否 | false | input focus状态是否高亮 |
| setUser | user:String | 否 | 无 | 用户标识 |
| setCaptchaType | captchaType:CaptchaType | 否 | 无 | 验证码类型，可以修改业务id默认的类型 |

##### Theme 枚举类说明

```
    // 主题
    public enum Theme {
        // 正常
        LIGHT,
        // 暗黑
        DARK
    }
```

#### LangType 枚举值说明

```
public enum LangType {
        LANG_AM,// 阿姆哈拉语
        LANG_AR,//阿拉伯语
        LANG_AS,//阿萨姆语
        LANG_AZ,//阿塞拜疆语
        LANG_BE,//白俄罗斯语
        LANG_BG,//保加利亚语
        LANG_BN,//孟加拉语
        LANG_BO,//藏语
        LANG_BS,//波斯尼亚语
        LANG_CA,//加泰罗尼亚语
        LANG_CS,//捷克语
        LANG_DA,//丹麦语
        LANG_DE,//德语
        LANG_EL,//希腊语
        LANG_EN,//英文
        LANG_EN_US,//英语/美国
        LANG_ES,//西班牙语
        LANG_ES_LA,//西班牙语/拉美
        LANG_ET,//爱沙尼亚语
        LANG_EU,//巴斯克语
        LANG_FA,//波斯语
        LANG_FI,//芬兰语
        LANG_FR,//法语
        LANG_GL,//加利西亚语
        LANG_GU,//古吉拉特语
        LANG_HI,//印地语
        LANG_HR,//克罗地亚
        LANG_HU,//匈牙利语
        LANG_ID,//印尼语
        LANG_IT,//意大利语
        LANG_HE,//希伯来语
        LANG_JA,//日文
        LANG_JV,//爪哇语
        LANG_KA,//格鲁吉亚语
        LANG_KK,//哈萨克语
        LANG_KM,//高棉语
        LANG_KN,//卡纳达语
        LANG_KO,//韩文
        LANG_LO,//老挝语
        LANG_LT,//立陶宛语
        LANG_LV,//拉脱维亚语
        LANG_MAI,//迈蒂利语
        LANG_MI,//毛利语
        LANG_MK,//马其顿语
        LANG_ML,//马拉亚拉姆语
        LANG_MN,//蒙古语
        LANG_MR,//马拉地语
        LANG_MS,//马来西亚语
        LANG_MY,//缅甸语
        LANG_NO,//挪威语
        LANG_NE,//尼泊尔语
        LANG_NL,//荷兰语
        LANG_OR,//欧里亚语
        LANG_PA,//旁遮普语
        LANG_PL,//波兰语
        LANG_PT,//葡萄牙语
        LANG_PT_BR,//葡萄牙语/巴西
        LANG_RO,//罗马西亚语
        LANG_RU,//俄语
        LANG_SI,//僧加罗语
        LANG_SK,//斯洛伐克语
        LANG_SL,//斯洛文尼亚语
        LANG_SR,//塞尔维亚语
        LANG_SV,//瑞典语
        LANG_SW,//斯瓦希里语
        LANG_TA,//泰米尔语
        LANG_TE,//泰卢固语
        LANG_TH,//泰语
        LANG_FIL,//菲律宾语
        LANG_TR,//土耳其语
        LANG_UG,//维吾尔语
        LANG_UK,//乌克兰语
        LANG_UR,//乌尔都语
        LANG_UZ,//乌兹别克语
        LANG_VI,//越南语
        LANG_ZH_CN,//中文简体
        LANG_ZH_HK,//中国香港
        LANG_ZH_TW,//中国台湾
}
```

#### CaptchaType 枚举值说明
```
public enum CaptchaType {
        DEFAULT(-1),
        // 滑块拼图
        JIGSAW(2),
        // 文字点选
        POINT(3),
        // 短信上行
        SMS(4),
        // 智能无感知
        INTELLISENSE(5),
        // 障碍躲避
        AVOID(6),
        // 图标点选
        ICON_POINT(7),
        // 词组
        WORD_GROUP(8),
        // 图片推理
        INFERENCE(9),
        // 语序选词
        WORD_ORDER(10),
        // 空间推理
        SPACE(11),
        // 语音识别
        VOICE(12);

        private final int type;

        CaptchaType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
}
```

##### CaptchaListener 接口说明

```
public interface CaptchaListener {
    /**
     * 验证之后结果回调 validate 不为空则验证通过
     * @param result 结果
     * @param validate 检验码
     * @param msg 结果信息，包括验证错误信息
     * @param captchaType 验证码类型（jigsaw：滑动拼图验证码 point：文字点选验证码 
     sms：短信上行验证码 intellisense：智能无感知
     avoid：躲避障碍验证码 icon_point：图标点选验证码
     word_group：词组验证码 inference：图片推理验证码
     word_order：语序选词验证码 space：空间推理验证码 voice：语音验证码）
     * @param errorValidate 错误校验码，用于二次校验查看本次错误原因
     */
    void onValidate(String result, String validate, String msg, String captchaType, String errorValidate);

    /**
     * 异常回调
     * @param code 异常码
     * @param msg 异常信息
     */
    void onError(int code, String msg);

    /**
     * 当验证码框关闭时回调
     *
     * @param closeType 关闭类型枚举值，{@see Captcha#CloseType}
     */
    void onClose(Captcha.CloseType closeType);
    /**
     * 验证码弹窗显示回调
     */
    void onCaptchaShow();
}
```

##### CloseType 枚举值说明

```
public enum CloseType {
        /**
         * 用户主动关闭验证码弹窗
         */
        USER_CLOSE,
        /**
         * 验证码验证成功，流程自动关闭
         */
        VERIFY_SUCCESS_CLOSE,

        /**
         * loading或者错误弹窗关闭
         */
        TIP_CLOSE,
        
         /**
         * 两次validate间隔少于一秒
         */
        VALIDATE_QUICK_CLOSE
}
```

##### ⚠️注意

从 Android 9.0 开始 webView 默认不支持 http 资源，若私有化配置是 http 资源需要在 Manifest 添加如下配置

```
 <application
    android:usesCleartextTraffic="true"
 />
```
### 3. 弹出验证码

#### 代码说明

```
captcha.validate()
```

### 4. 横竖屏折叠屏切换

在 AndroidManifest 设置对应 Activity 的 configChanges 为

```
 android:configChanges="keyboardHidden|orientation|screenSize"
```

#### 代码说明

在 onConfigurationChanged 生命周期中调用

```
captcha.changeDialogLayout()
```

### 5. 释放验证码相关资源（建议放在Activity的onDestroy，建议调用）

#### 代码说明

```
captcha.destroy()
```

### 6. 单独关闭所有验证码相关 Dialog（正常情况无需调用）

#### 代码说明

```
captcha.dismissAllDialog()
```

## 错误码
| code | 含义 |
|------|------|
| 200  | 校验未通过，是因为业务错误，包含超限 |
| 300  | 校验未通过，包含轨迹错误等|
| 432  | 非法业务ID，包含业务到期等|
| 501  | 请求失败，包括网络原因等 |
| 502  | 请求脚本资源失败 |
| 503  | 请求图片资源失败 |
| 505  | 请求音频资源失败 |
| 1000 | 未知错误 |
| 1004  |初始化失败，接口超时 |
| 2000  |json解析异常 |
| 2001  |网络未连接 |

| webview 异常| 含义 |
|------|------|
|-1|未知错误|
|-2|服务器或代理主机无法解析|
|-3|不支持的身份验证方案（非基本或摘要）|
|-4|用户身份验证失败|
|-5|代理身份验证失败|
|-6|无法连接到服务器|
|-7 |读取或写入文件时发生 I/O 错误|
|-8|连接超时|
|-9|重定向过多|
|-10|不支持的 URI 方案|
|-11|SSL 握手失败|
|-12|无法解析 URL 地址|
|-13|无法打开文件|
|-14|文件不存在|
|-15|并发请求过多|
|0|SslError，SSL 证书尚未生效|
|1|SslError，SSL 证书过期|
|2|SslError，SSL 证书的主机名与请求的主机名不匹配|
|3|SslError，SSL 证书不受信任|
|4|SslError，SSL 证书的日期无效|
|5|SslError，SSL 证书无效，即证书格式、内容或签名存在问题|
