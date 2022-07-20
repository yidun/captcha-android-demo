package com.netease.nis.smartercaptcha;

import android.app.Application;
import android.content.MutableContextWrapper;
import android.webkit.WebView;
import com.netease.nis.smartercaptcha.utils.DarkModeUtils;

/**
 * @author liuxiaoshuai
 * @date 2021/8/27
 * @desc
 * @email liulingfeng@mistong.com
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // WebView 第一次初始化会reset UI mode。在设置暗黑模式之前先手动初始化一个WebView
        WebView webView = new WebView(new MutableContextWrapper(this));
        DarkModeUtils.init(this);
    }
}
