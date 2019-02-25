package com.netease.nis.smartercaptcha;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.netease.nis.captcha.Captcha;
import com.netease.nis.captcha.CaptchaConfiguration;
import com.netease.nis.captcha.CaptchaListener;

public class MainActivity extends AppCompatActivity {
    String noSenseCaptchaId = "6a5cab86b0eb4c309ccb61073c4ab672";
    String traditionalCaptchaId = "deecf3951a614b71b4b1502c072be1c1";
    // String traditionalCaptchaId = "7389c04befff44f08f5d3c37fae22957"; //短信上行
    private Button btnNoSenseCaptcha;
    private Button btnTraditionalCaptcha;
    private EditText etControlBarSlide, etControlBarSlideMove, etControlBarSlideError;
    private EditText etCaptchaUrl;
    private EditText etDialogBackgroundDim;
    private Spinner langSpinner;
    private CaptchaListener captchaListener;
    private final Context context = this;
    // 默认为简体中文;
    private CaptchaConfiguration.LangType langType = CaptchaConfiguration.LangType.LANG_ZH_CN;
    private boolean isUsedCustomControlBarStyle = false, isOpenDeveloperMode = false;
    private Switch switchControlBar, switchDeveloperMode;
    private String captchaUrl, controlBarStartUrl, controlBarMovingUrl, controlBarErrorUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initClickListener(context);
        initData();

    }

    private void initView() {
        langSpinner = (Spinner) findViewById(R.id.lang_spin_list);
        etControlBarSlide = (EditText) findViewById(R.id.et_slide_icon);
        etControlBarSlideMove = (EditText) findViewById(R.id.slide_icon_move);
        etControlBarSlideError = (EditText) findViewById(R.id.slide_icon_error);
        etDialogBackgroundDim = (EditText) findViewById(R.id.et_bg_dim);
        etCaptchaUrl = (EditText) findViewById(R.id.et_captcha_url);
        btnNoSenseCaptcha = (Button) findViewById(R.id.btn_no_sense_captcha);
        btnTraditionalCaptcha = (Button) findViewById(R.id.btn_traditional_captcha);
        switchControlBar = (Switch) findViewById(R.id.switch_control_bar);
        switchControlBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isUsedCustomControlBarStyle = true;
                } else {
                    isUsedCustomControlBarStyle = false;
                }
            }
        });
        switchDeveloperMode = (Switch) findViewById(R.id.switch_dev_url);
        switchDeveloperMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isOpenDeveloperMode = true;
                } else {
                    isOpenDeveloperMode = false;
                }
            }
        });
    }

    private void initClickListener(final Context context) {
        // 传统验证码
        btnTraditionalCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUsedCustomControlBarStyle) {
                    controlBarStartUrl = etControlBarSlide.getText().toString();
                    controlBarMovingUrl = etControlBarSlideMove.getText().toString();
                    controlBarErrorUrl = etControlBarSlideError.getText().toString();
                }
                if (isOpenDeveloperMode) {
                    captchaUrl = etCaptchaUrl.getText().toString();
                } else {
                    captchaUrl = null;
                }
                float dimAmount = 0.5f;
                String dimString = etDialogBackgroundDim.getText().toString();
                if (TextUtils.isDigitsOnly(dimString)) {
                    dimAmount = Float.parseFloat(dimString);
                }
                final CaptchaConfiguration configuration = new CaptchaConfiguration.Builder()
                        .captchaId(traditionalCaptchaId)
                        .url(captchaUrl) // 接入者无需设置，该接口为调试接口
                        .listener(captchaListener)
                        .languageType(langType)
                        .debug(true)
                        .position(-1, -1, 0, 0)
                        .controlBarImageUrl(controlBarStartUrl, controlBarMovingUrl, controlBarErrorUrl)
                        .backgroundDimAmount(dimAmount)
                        .build(context);
                final Captcha captcha = Captcha.getInstance().init(configuration);
                captcha.validate();
            }
        });
        // 智能无感知验证码
        btnNoSenseCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUsedCustomControlBarStyle) {
                    controlBarStartUrl = etControlBarSlide.getText().toString();
                    controlBarMovingUrl = etControlBarSlideMove.getText().toString();
                    controlBarErrorUrl = etControlBarSlideError.getText().toString();
                }
                if (isOpenDeveloperMode) {
                    captchaUrl = etCaptchaUrl.getText().toString();
                } else {
                    captchaUrl = null;
                }
                float dimAmount = 0.5f;
                String dimString = etDialogBackgroundDim.getText().toString();
                if (TextUtils.isDigitsOnly(dimString)) {
                    dimAmount = Float.parseFloat(dimString);
                }
                final CaptchaConfiguration configuration = new CaptchaConfiguration.Builder()
                        .captchaId(noSenseCaptchaId)// 验证码业务id
                        .url(captchaUrl) // 接入者无需设置，该接口为调试接口
                        // 验证码类型，默认为传统验证码，如果要使用无感知请设置以下类型
                        .mode(CaptchaConfiguration.ModeType.MODE_INTELLIGENT_NO_SENSE)
                        .listener(captchaListener)
                        .timeout(1000 * 10) // 超时时间，一般无需设置
                        .languageType(langType) // 验证码语言类型，一般无需设置
                        .debug(true) // 是否启用debug模式，一般无需设置
                        // 设置验证码框的位置和宽度，一般无需设置，不推荐设置宽高，后面将逐步废弃该接口
                        .position(-1, -1, 0, 0)
                        // 自定义验证码滑动条滑块的不同状态图片
                        .controlBarImageUrl(controlBarStartUrl, controlBarMovingUrl, controlBarErrorUrl)
                        .backgroundDimAmount(dimAmount) // 验证码框遮罩层透明度，一般无需设置
                        .build(context);
                final Captcha captcha = Captcha.getInstance().init(configuration);
                captcha.validate();
            }
        });

        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        langType = CaptchaConfiguration.LangType.LANG_ZH_CN;
                    }
                    break;
                    case 1: {
                        langType = CaptchaConfiguration.LangType.LANG_ZH_TW;
                    }
                    break;
                    case 2: {
                        langType = CaptchaConfiguration.LangType.LANG_EN;
                    }
                    break;
                    case 3: {
                        langType = CaptchaConfiguration.LangType.LANG_KO;
                    }
                    break;
                    case 4: {
                        langType = CaptchaConfiguration.LangType.LANG_JA;
                    }
                    break;
                    case 5: {
                        langType = CaptchaConfiguration.LangType.LANG_VI;
                    }
                    break;
                    case 6: {
                        langType = CaptchaConfiguration.LangType.LANG_TH;
                    }
                    break;
                    case 7: {
                        langType = CaptchaConfiguration.LangType.LANG_FR;
                    }
                    break;
                    case 8: {
                        langType = CaptchaConfiguration.LangType.LANG_RU;
                    }
                    break;
                    case 9: {
                        langType = CaptchaConfiguration.LangType.LANG_AR;
                    }
                    break;
                    default: {
                        langType = CaptchaConfiguration.LangType.LANG_ZH_CN;
                    }
                    break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initData() {
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
    }
}
