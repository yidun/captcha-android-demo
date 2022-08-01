package com.netease.nis.smartercaptcha;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.os.Build;
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

        // WebView多进程处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(this);
            String packageName = this.getPackageName();
            if (!packageName.equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }
        // WebView 第一次初始化会reset UI mode。在设置暗黑模式之前先手动初始化一个WebView
        WebView webView = new WebView(new MutableContextWrapper(this));
        DarkModeUtils.init(this);
    }

    private String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }
}
