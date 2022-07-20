package com.netease.nis.smartercaptcha.ui.env;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.netease.nis.smartercaptcha.R;

import java.util.Objects;

/**
 * @author liuxiaoshuai
 * @date 2022/7/19
 * @desc 环境设置
 * @email liulingfeng@mistong.com
 */
public class EnvironmentFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
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

    }

    @Override
    public void onCreatePreferences(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState, @Nullable @org.jetbrains.annotations.Nullable String rootKey) {
        setPreferencesFromResource(R.xml.env_preferences, rootKey);
    }
}
