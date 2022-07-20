package com.netease.nis.smartercaptcha.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatDelegate;


/**
 * @author liuxiaoshuai
 * @date 2021/8/27
 * @desc 正常/暗黑模式切换工具
 * @email liulingfeng@mistong.com
 */
public class DarkModeUtils {
    public static final String KEY_CURRENT_MODEL = "night_mode_state_sp";

    public static int getNightModel(Context context) {
        SharedPreferences sp = context.getSharedPreferences(KEY_CURRENT_MODEL, Context.MODE_PRIVATE);
        return sp.getInt(KEY_CURRENT_MODEL, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public static void setNightModel(Context context, int nightMode) {
        SharedPreferences sp = context.getSharedPreferences(KEY_CURRENT_MODEL, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_CURRENT_MODEL, nightMode).apply();
    }

    /**
     * ths method should be called in Application onCreate method
     *
     * @param application application
     */
    public static void init(Application application) {
        int nightMode = getNightModel(application);
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }

    /**
     * 应用夜间模式
     */
    public static void applyNightMode(Activity context) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        // support 必须，AndroidX 不必调用
//        context.recreate();
        setNightModel(context, AppCompatDelegate.MODE_NIGHT_YES);
    }

    /**
     * 应用日间模式
     */
    public static void applyDayMode(Activity context) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // support 必须，AndroidX 不必调用
//        context.recreate();
        setNightModel(context, AppCompatDelegate.MODE_NIGHT_NO);
    }

    /**
     * 跟随系统主题时需要动态切换
     */
    public static void applySystemMode(Context context) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        setNightModel(context, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    /**
     * 判断App当前是否处于暗黑模式状态
     *
     * @param context 上下文
     * @return 返回
     */
    public static boolean isDarkMode(Context context) {
        int nightMode = getNightModel(context);
        if (nightMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            int applicationUiMode = context.getResources().getConfiguration().uiMode;
            int systemMode = applicationUiMode & Configuration.UI_MODE_NIGHT_MASK;
            return systemMode == Configuration.UI_MODE_NIGHT_YES;
        } else {
            return nightMode == AppCompatDelegate.MODE_NIGHT_YES;
        }
    }
}
