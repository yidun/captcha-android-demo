package nis.netease.com.captcha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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


        String testCaptchaId = "ede087b9bdb0447e8ef64655785aab49";

        //初始化验证码SDK，并设置CaptchaId、Listener
        if (captcha==null){
            captcha = new Captcha(mcontext);
        }
        captcha.setCaptchaId(testCaptchaId);
        captcha.setCaListener(mytestCaListener);
        //登陆按钮
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    //自定义Listener格式如下
    CaptchaListener mytestCaListener  = new CaptchaListener() {
        @Override
        public void onReady(Boolean status) {
            //加载完成
            progressDialog.dismiss();
            if (status) {
                //加载完成
                toastMsg("load finished");
            }else {
                //加载超时,可能是加载时网络出错等原因
                toastMsg("validate timeout");
            }
        }

        @Override
        public void onValidate(String result, String validate, String message) {
            //验证结果，valiadte，可以根据返回的三个值进行用户自定义二次验证
            toastLongTimeMsg("result = " + result + ", validate = " + validate + ", message = " + message);
        }

        @Override
        public void closeWindow() {
            //请求关闭页面
            toastMsg("close windows");
        }

        @Override
        public void onError(String errormsg) {
            //出错
            progressDialog.dismiss();
            toastLongTimeMsg("error");
        }

        @Override
        public void onCancel() {
            //用户取消验证
            toastMsg("user cancel");
        }
    };

    //登陆操作
    private void attemptLogin() {

        mLoginTask = new UserLoginTask();
        mLoginTask.execute();
        if (!((Activity) mcontext).isFinishing()) {
            //渲染Loading
            progressDialog = ProgressDialog.show(mcontext, null, "Loading", true, true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    toastMsg("user cancel progress dialog");
                    if (mLoginTask.getStatus() == AsyncTask.Status.RUNNING) {
                        Log.i(TAG, "stop mLoginTask");
                        mLoginTask.cancel(true);
                    }
                }
            });
        }
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
            //简单验证DeviceId、CaptchaId、Listener值
            return captcha.checkParams();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                //开始验证
                captcha.Validate();
            } else {
                toastMsg("captcha check params wrong");
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
        mEmailView.setText("captcha@dun.163.com");
        mEmailView.setInputType(InputType.TYPE_NULL);
        mPasswordView.setText("********");
        mPasswordView.setInputType(InputType.TYPE_NULL);
    }

    private void toastMsg(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void toastLongTimeMsg(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }
}

