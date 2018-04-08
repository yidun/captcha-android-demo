package com.netease.nis.captcha;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hzhuqi on 2017/12/8.
 */

public class CaptchaProgressDialog extends ProgressDialog {

    private int mPositionX = -1;
    private int mPositionY = -1;
    private int mPositionW = -1;
    private int mPositionH = -1;
    private int dWidth;
    private float dScale;
    private TextView mStatusTip;
    private ProgressBar mProgressBar;
    private ImageView mErrorIcon;
    public boolean isCanClickDisappear = false;
    private Context mContext;
    private View bgView;
    public boolean isCancelLoading = false;

    public CaptchaProgressDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        isCanClickDisappear = false;
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorIcon.setVisibility(View.INVISIBLE);
        mStatusTip.setText(R.string.tip_loading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        setDialogPositionAndParmars();
        setContentView(R.layout.captcha_progress_dialog_layout);
        mStatusTip = (TextView) findViewById(R.id.status_tip);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mErrorIcon = (ImageView) findViewById(R.id.error_pic);
        bgView = findViewById(R.id.bg);
        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanClickDisappear) {
                    try {
                        dismiss();
                    } catch (Exception e) {
                        Log.e(Captcha.TAG, "Captcha Progress Dialog dismiss Error:" + e.toString());
                    }

                }
            }
        });
    }

    public void setPosition(int left, int top, int w, int h) {
        mPositionX = left;
        mPositionY = top;
        mPositionW = w;
        mPositionH = h;
    }

    //进度框的大小和位置属性设置为和验证码框相同
    private void setDialogPositionAndParmars() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        if (mPositionX != -1) {
            layoutParams.gravity = Gravity.LEFT;
            layoutParams.x = mPositionX;
        }
        if (mPositionY != -1) {
            layoutParams.gravity |= Gravity.TOP;
            layoutParams.y = mPositionY;
        }
        if (mPositionW > 0) {
            layoutParams.width = mPositionW;
        } else {
            getDialogWidth();
            layoutParams.width = dWidth + 60;
        }
        if (mPositionH > 0) {
            layoutParams.height = mPositionH;
        } else {
            layoutParams.height = getDialogHeight(dWidth) + 60;
        }

        getWindow().setAttributes(layoutParams);
    }

    private void getDialogWidth() {

        try {
            DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            float scale = metrics.density;
            dScale = scale;

            final int MINWIDTH = 270; // 组件的最小宽度

            if (mPositionW > MINWIDTH) {
                dWidth = mPositionW;
            } else {
                if (height < width) {
                    width = height * 3 / 4;
                }
                width = width * 4 / 5;
                if ((int) (width / scale) < MINWIDTH) {
                    width = (int) (MINWIDTH * scale);
                }
                dWidth = width;
            }

        } catch (Exception e) {
            Log.e(Captcha.TAG, "getDialogWidth failed");
        }
    }

    //根据宽度得到高度
    private int getDialogHeight(float width) {
        int height = (int) ((float) (width / 2.0) + 52 * dScale + 15);
        return height;
    }


    public void setProgressTips(String mProgressTips) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorIcon.setVisibility(View.VISIBLE);
        mStatusTip.setText(mProgressTips);
    }

    public void setProgressTips(int mProgressTips) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorIcon.setVisibility(View.VISIBLE);
        mStatusTip.setText(mProgressTips);
    }

    //重载show函数，做预处理，安卓Dialog原生show经常导致各种问题
    @Override
    public void show() {
        try {
            if (mContext != null && !((Activity) mContext).isFinishing()) {
                super.show();
            }
        } catch (Exception e) {
            Log.e(Captcha.TAG, "Captcha Progress Dialog show Error:" + e.toString());
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            Log.e(Captcha.TAG, "Captcha Progress Dialog dismiss Error:" + e.toString());
        }
    }
}
