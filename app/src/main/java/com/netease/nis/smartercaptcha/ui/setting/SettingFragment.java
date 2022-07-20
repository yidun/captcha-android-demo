package com.netease.nis.smartercaptcha.ui.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.netease.nis.smartercaptcha.R;
import com.netease.nis.smartercaptcha.utils.DarkModeUtils;

import java.util.Objects;

/**
 * @author liuxiaoshuai
 * @date 2022/7/19
 * @desc 配置设置
 * @email liulingfeng@mistong.com
 */
public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getPreferenceManager().getSharedPreferences()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getPreferenceManager().getSharedPreferences()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (TextUtils.equals(key, "settings_switch_theme")) {
            if (sharedPreferences.getBoolean("settings_switch_theme", false)) {
                DarkModeUtils.applyNightMode(Objects.requireNonNull(getActivity()));
            } else {
                DarkModeUtils.applyDayMode(Objects.requireNonNull(getActivity()));
            }
        }
    }

    @Override
    public void onCreatePreferences(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState, @Nullable @org.jetbrains.annotations.Nullable String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}
