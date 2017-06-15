package nis.netease.com.captcha;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.netease.nis.captcha.Captcha;
import com.netease.nis.captcha.CaptchaListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "Captcha";
    Captcha mCaptcha = null;
    // UI references.
    private AutoCompleteTextView text_email;
    private EditText edit_password;
    private Button button_login;
    private Context mContext = null;
    /*验证码SDK,该Demo采用异步获取方式*/
    private UserLoginTask mLoginTask = null;
    //自定义Listener格式如下
    CaptchaListener myCaptchaListener = new CaptchaListener() {

        @Override
        public void onValidate(String result, String validate, String message) {
            //验证结果，valiadte，可以根据返回的三个值进行用户自定义二次验证
            if (validate.length() > 0) {
                toastMsg("验证成功，validate = " + validate);
            } else {
                toastMsg("验证失败：result = " + result + ", validate = " + validate + ", message = " + message);

            }
        }

        @Override
        public void closeWindow() {
            //请求关闭页面
            toastMsg("关闭页面");
        }

        @Override
        public void onError(String errormsg) {
            //出错
            toastMsg("错误信息：" + errormsg);
        }

        @Override
        public void onCancel() {
            toastMsg("取消线程");
            //用户取消加载或者用户取消验证，关闭异步任务，也可根据情况在其他地方添加关闭异步任务接口
            if (mLoginTask != null) {
                if (mLoginTask.getStatus() == AsyncTask.Status.RUNNING) {
                    Log.i(TAG, "stop mLoginTask");
                    mLoginTask.cancel(true);
                }
            }
        }

        @Override
        public void onReady(boolean ret) {
            //该为调试接口，ret为true表示加载Sdk完成
            if (ret) {
                toastMsg("验证码sdk加载成功");
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
        * v2.0
        * 拖动 a05f036b70ab447b87cc788af9a60974
        * */
        String testCaptchaId = "a05f036b70ab447b87cc788af9a60974"; // TODO: 这里填入从易盾官网申请到的验证码id，上面是几种测试风格的id。 0489d7c00eff49089c56dfcd4b67f250

        mContext = this;
        initLoginView();

        //初始化验证码SDK相关参数，设置CaptchaId、Listener最后调用start初始化。
        if (mCaptcha == null) {
            mCaptcha = new Captcha(mContext);
        }
        mCaptcha.setCaptchaId(testCaptchaId);
        mCaptcha.setCaListener(myCaptchaListener);
        //可选：开启debug
        mCaptcha.setDebug(false);
        //可选：设置超时时间
        mCaptcha.setTimeout(10000);
        //登陆操作
        button_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginTask = new UserLoginTask();
                //关闭mLoginTask任务可以放在myCaptchaListener的onCancel接口中处理
                mLoginTask.execute();
                //必填：初始化 captcha框架
                mCaptcha.start();
                //可直接调用验证函数Validate()，本demo采取在异步任务中调用（见UserLoginTask类中）
                //mCaptcha.Validate();
            }
        });

    }

    //初始化登陆页面
    private void initLoginView() {
        text_email = (AutoCompleteTextView) findViewById(R.id.text_email);
        edit_password = (EditText) findViewById(R.id.edit_password);
        button_login = (Button) findViewById(R.id.button_login);
        text_email.setText("dev@dun.163.com");
        text_email.setInputType(InputType.TYPE_NULL);
        edit_password.setText("******");
        edit_password.setInputType(InputType.TYPE_NULL);
    }

    private void toastMsg(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        UserLoginTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //可选：简单验证DeviceId、CaptchaId、Listener值
            return mCaptcha.checkParams();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                //必填：开始验证
                mCaptcha.Validate();
            } else {
                toastMsg("验证码SDK参数设置错误,请检查配置");
            }
        }

        @Override
        protected void onCancelled() {
            mLoginTask = null;
        }
    }
}