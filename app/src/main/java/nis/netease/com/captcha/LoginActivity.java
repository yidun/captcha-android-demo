package nis.netease.com.captcha;

import android.app.ProgressDialog;
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
public class LoginActivity extends AppCompatActivity{

    private final  static String TAG = "Captcha";
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button signInButton;
    private ProgressDialog progressDialog;
    private Context mcontext= LoginActivity.this;
    /*验证码SDK,该Demo采用异步获取方式*/
    private UserLoginTask mLoginTask = null;
    Captcha captcha = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setLoginView();

        String testCaptchaId = "0489d7c00eff49089c56dfcd4b67f250"; // TODO: 这里填入从易盾官网申请到的验证码id

        //初始化验证码SDK相关参数，设置CaptchaId、Listener最后调用start初始化。
        if (captcha==null){
            captcha = new Captcha(mcontext);
        }
        captcha.setCaptchaId(testCaptchaId);
        captcha.setCaListener(mytestCaListener);
        //可选：开启debug
        captcha.setDebug(false);

        //登陆操作
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginTask = new UserLoginTask();
                //关闭mLoginTask任务可以放在mytestCaListener的onCancel接口中处理
                mLoginTask.execute();
                //必需：初始化 captcha框架
                captcha.start();
                //可直接调用验证函数Validate()，本demo采取在异步任务中调用（见UserLoginTask类中）
               //captcha.Validate();
            }
        });

    }

    //自定义Listener格式如下
    CaptchaListener mytestCaListener  = new CaptchaListener() {

        @Override
        public void onValidate(String result, String validate, String message) {
            //验证结果，valiadte，可以根据返回的三个值进行用户自定义二次验证
            if(validate.length()>0){
                toastMsg("验证成功，validate = "+validate);
            }else{
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
            toastMsg("错误信息："+errormsg);
        }

        @Override
        public void onCancel() {
            toastMsg("取消线程");
            //用户取消加载或者用户取消验证，关闭异步任务
            if(mLoginTask!=null){
                if (mLoginTask.getStatus() == AsyncTask.Status.RUNNING) {
                    Log.i(TAG, "stop mLoginTask");
                    mLoginTask.cancel(true);
                }
            }
        }

        @Override
        public void onReady(boolean ret) {
            //该为调试接口，ret为true表示加载Sdk完成
            if(ret){
                toastMsg("验证码sdk加载成功");
            }
        }

    };


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
            return captcha.checkParams();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                //必需：开始验证
                captcha.Validate();
            } else {
                toastMsg("验证码SDK参数设置错误,请检查配置");
            }
        }

        @Override
        protected void onCancelled() {
            mLoginTask = null;
        }
    }

    //初始化登陆页面
    private void setLoginView(){
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        signInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailView.setText("dev@dun.163.com");
        mEmailView.setInputType(InputType.TYPE_NULL);
        mPasswordView.setText("******");
        mPasswordView.setInputType(InputType.TYPE_NULL);
    }

    private void toastMsg(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

