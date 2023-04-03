package com.netease.nis.smartercaptcha.ui.home;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.netease.nis.captcha.Captcha;
import com.netease.nis.captcha.CaptchaConfiguration;
import com.netease.nis.captcha.CaptchaListener;
import com.netease.nis.smartercaptcha.R;
import com.netease.nis.smartercaptcha.bean.CustomStyleBean;
import com.netease.nis.smartercaptcha.bean.PopupStyleBean;
import com.netease.nis.smartercaptcha.utils.HttpUtil;
import com.netease.nis.smartercaptcha.utils.LanguageTools;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author liuxiaoshuai
 * @date 2022/7/19
 * @desc
 * @email liulingfeng@mistong.com
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "Captcha";
    private Captcha captcha;
    private final String captchaId = "易盾业务id";
    private final Gson gson = new Gson();
    private String validate;
    private String apiServer;
    private String demoServer;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Button btnShow = root.findViewById(R.id.btn_show);
        Button btnVerify = root.findViewById(R.id.btn_verify);
        btnShow.setOnClickListener(v -> {
            if (captcha != null) {
                captcha.validate();
            }
        });
        btnVerify.setOnClickListener(v -> {
            // 最好放在服务端做，demo为了流程完整性直接在客户端
            if (TextUtils.isEmpty(this.validate)) {
                Toast.makeText(getActivity(), "请先在当前页面完成验证码验证", Toast.LENGTH_SHORT).show();
                return;
            }
            String url = this.demoServer + "/v2/login";
            Log.d(TAG, "twice check url:" + url);
            Map<String, String> args = new HashMap<String, String>();
            args.put("captchaId", captchaId);
            args.put("NECaptchaValidate", this.validate);
            args.put("apiServer", this.apiServer);
            HttpUtil.doPostRequest(url, args, new HttpUtil.ResponseCallBack() {
                @Override
                public void onSuccess(final String result) {
                    Log.i("Captcha", result);
                    final String tip;
                    if (result.contains("success")) {
                        tip = "二次校验通过";
                    } else {
                        tip = "二次校验失败:" + result;
                    }
                    requireActivity().runOnUiThread(() -> Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onError(int errorCode, final String msg) {
                    Log.e(TAG, "错误码" + errorCode + "错误信息" + msg);
                    requireActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "网络请求出现错误: " + msg, Toast.LENGTH_SHORT).show());
                }
            });
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        captcha = Captcha.getInstance();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        this.apiServer = preferences.getString("settings_env_apiServer", "c.dun.163yun.com");
        String staticServer = preferences.getString("settings_env_staticServer", "cstaticdun.126.net");
        this.demoServer = preferences.getString("settings_env_demoServer", "https://nctest-captcha.dun.163yun.com");
        CaptchaConfiguration.Builder builder = new CaptchaConfiguration.Builder();
        builder.captchaId(captchaId);
        // 打开debug开关，注意上线需关闭
        builder.debug(true);
        // 超时时间，一般无需设置
        builder.timeout(1000 * 10);
        // 验证码框遮罩层透明度，一般无需设置
        builder.backgroundDimAmount(0.5f);
        builder.touchOutsideDisappear(preferences.getBoolean("settings_switch_background", true));
        builder.hideCloseButton(preferences.getBoolean("settings_switch_btn", false));
        builder.isCloseButtonBottom(preferences.getBoolean("settings_switch_btn_bottom", true));
        builder.isShowLoading(preferences.getBoolean("settings_switch_loading", true));
        builder.theme(preferences.getBoolean("settings_switch_theme", false) ? CaptchaConfiguration.Theme.DARK : CaptchaConfiguration.Theme.LIGHT);
        if (preferences.getBoolean("settings_switch_loading_img", false)) {
            builder.loadingAnimResId(R.drawable.captcha_demo_anim_loading);
        }
        String language = preferences.getString("settings_language", "default");
        if (!"default".equals(language)) {
            builder.languageType(LanguageTools.string2LangType(language));
        }
        String size = preferences.getString("settings_size", "small");
        builder.size(size);

        try {
            if (preferences.getBoolean("settings_switch_super", false)) {
                CustomStyleBean customStyleBean = gson.fromJson(preferences.getString("settings_super_custom", ""), CustomStyleBean.class);
                PopupStyleBean popupStyleBean = gson.fromJson(preferences.getString("settings_super_popup", ""), PopupStyleBean.class);

                if (customStyleBean != null) {
                    if (customStyleBean.imagePanel != null) {
                        builder.setImagePanelAlign(customStyleBean.imagePanel.align)
                                .setImagePanelBorderRadius(customStyleBean.imagePanel.borderRadius);
                    }
                    if (customStyleBean.controlBar != null) {
                        builder.setControlBarHeight(customStyleBean.controlBar.height)
                                .setControlBarBorderRadius(customStyleBean.controlBar.borderRadius)
                                .setControlBarBorderColor(customStyleBean.controlBar.borderColor)
                                .setControlBarBackground(customStyleBean.controlBar.background)
                                .setControlBarBorderColorMoving(customStyleBean.controlBar.borderColorMoving)
                                .setControlBarBackgroundMoving(customStyleBean.controlBar.backgroundMoving)
                                .setControlBarBorderColorSuccess(customStyleBean.controlBar.borderColorSuccess)
                                .setControlBarBackgroundSuccess(customStyleBean.controlBar.backgroundSuccess)
                                .setControlBarBorderColorError(customStyleBean.controlBar.borderColorError)
                                .setControlBarBackgroundError(customStyleBean.controlBar.backgroundError)
                                .setControlBarSlideBackground(customStyleBean.controlBar.slideBackground)
                                .setControlBarTextSize(customStyleBean.controlBar.textSize)
                                .setControlBarTextColor(customStyleBean.controlBar.textColor)
                        ;
                    }
                    builder.setGap(customStyleBean.gap)
                            .setExecuteBorderRadius(customStyleBean.executeBorderRadius)
                            .setExecuteBackground(customStyleBean.executeBackground)
                            .setExecuteTop(customStyleBean.executeTop)
                            .setExecuteRight(customStyleBean.executeRight);
                }

                if (popupStyleBean != null) {
                    builder.setCapBarHeight(popupStyleBean.capBarHeight)
                            .setCapBarTextAlign(popupStyleBean.capBarTextAlign)
                            .setCapBarBorderColor(popupStyleBean.capBarBorderColor)
                            .setCapBarTextColor(popupStyleBean.capBarTextColor)
                            .setCapBarTextSize(popupStyleBean.capBarTextSize)
                            .setCapBarTextWeight(popupStyleBean.capBarTextWeight)
                            .setCapPadding(popupStyleBean.capPadding)
                            .setCapPaddingBottom(popupStyleBean.capPaddingBottom)
                            .setCapPaddingTop(popupStyleBean.capPaddingTop)
                            .setCapPaddingRight(popupStyleBean.capPaddingRight)
                            .setCapPaddingLeft(popupStyleBean.capPaddingLeft)
                            .setOpacity(popupStyleBean.opacity)
                            .setRadius(popupStyleBean.radius)
                            .setPaddingTop(popupStyleBean.paddingTop)
                            .setPaddingBottom(popupStyleBean.paddingBottom);
                }
            }

        } catch (Exception e) {
            Log.i(TAG, "json解析出错");
            e.printStackTrace();
        }

        builder.listener(new CaptchaListener() {

            @Override
            public void onValidate(String result, String validate, String msg) {
                HomeFragment.this.validate = validate;
                Log.i(TAG, "验证成功: 校验码" + validate + " 错误信息:" + msg);
            }

            @Override
            public void onError(int code, String msg) {
                Log.e(TAG, "验证出错，错误码:" + code + " 错误信息:" + msg);
            }

            @Override
            public void onClose(Captcha.CloseType closeType) {
                if (closeType == Captcha.CloseType.USER_CLOSE) {
                    Toast.makeText(getActivity(), "用户关闭验证码", Toast.LENGTH_SHORT).show();
                } else if (closeType == Captcha.CloseType.VERIFY_SUCCESS_CLOSE) {
                    Log.i(TAG, "校验通过，流程自动关闭");
                } else if (closeType == Captcha.CloseType.TIP_CLOSE) {
                    Log.i(TAG, "loading关闭显示验证码弹窗");
                }
            }

            @Override
            public void onCaptchaShow() {
                Log.i(TAG, "验证码弹窗已显示");
            }
        });
        // 智能无感知需要设置
//        builder.mode(CaptchaConfiguration.ModeType.MODE_INTELLIGENT_NO_SENSE);
        // 以下为私有化部署相关接口，非私有化场景无需设置
        if (!TextUtils.isEmpty(this.apiServer)) {
            builder.apiServer(this.apiServer);
        }
        if (!TextUtils.isEmpty(staticServer)) {
            builder.staticServer(staticServer);
        }

        CaptchaConfiguration configuration = builder.build(getActivity());
        captcha.init(configuration);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (captcha != null) {
            captcha.destroy();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "onConfigurationChanged: " + newConfig.orientation);
        if (captcha != null) {
            captcha.changeDialogLayout();
        }
    }
}
