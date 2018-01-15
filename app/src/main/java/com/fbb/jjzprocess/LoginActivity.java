package com.fbb.jjzprocess;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fbb.jjzprocess.bean.ResultLogin;
import com.fbb.jjzprocess.bean.ResultPhoneCode;
import com.fbb.jjzprocess.util.HttpUtil;
import com.fbb.jjzprocess.util.JsonUtil;
import com.fbb.jjzprocess.util.ParamUtil;
import com.google.gson.JsonObject;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CaptchaListener {
    CaptchaDialog captchaDialog = null;
    EditText mEditTextPhoneNum = null;
    EditText mEditTextVertifyNum = null;
    Button mButtonVertify = null;
    Button mButtonLogin = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEditTextPhoneNum = findViewById(R.id.edit_phone_num);
        mEditTextVertifyNum = findViewById(R.id.edit_vertify_num);
        mButtonVertify = findViewById(R.id.button_vertify);
        mButtonLogin = findViewById(R.id.button_login);
        mButtonVertify.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);
    }

    private boolean initDiolog() {
        this.captchaDialog = new CaptchaDialog(this).setDebug(false)
                .setDeviceId(MyApplication.getInstance().imei)
                .setCaListener(this).setProgressDialog(this.progressDialog);
        this.captchaDialog.initDialog();
        this.captchaDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface paramAnonymousDialogInterface) {
                LoginActivity.this.onCancel();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        });
        return true;
    }

    private void showProgressDialog() {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this);
        }
        this.progressDialog.setMessage("Loading");
        this.progressDialog.setCancelable(true);
        this.progressDialog.setTitle(null);
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface paramAnonymousDialogInterface) {
                LoginActivity.this.onCancel();
            }
        });
        this.progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface paramAnonymousDialogInterface) {
            }
        });
        this.progressDialog.show();
    }

    public static String phoneno = "^((1[3,5,8][0-9])|(14[5,7])|(17[0,3,5,6,7,8]))\\d{8}$";
    public String toVertifyPhoneNum = "";
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_vertify:
                if(TextUtils.isEmpty(mEditTextPhoneNum.getText())){
                    Toast.makeText(this, "请您输入手机号", Toast.LENGTH_SHORT).show();
                } else if(!Pattern.matches(phoneno, mEditTextPhoneNum.getText().toString())){
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else {
                    toVertifyPhoneNum = mEditTextPhoneNum.getText().toString();
                    showProgressDialog();
                    initDiolog();
                }
                break;
            case R.id.button_login:
                if(TextUtils.isEmpty(mEditTextVertifyNum.getText())){
                    Toast.makeText(this, "请您输入验证码", Toast.LENGTH_SHORT).show();
                } else {
                    requestLogin(toVertifyPhoneNum, mEditTextVertifyNum.getText().toString());
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void closeWindow() {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(String paramString) {

    }

    @Override
    public void onReady(boolean paramBoolean) {

    }

    @Override
    public void onValidate(final String paramString1, final String NECaptchaValidate, final String paramString3) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if ((!TextUtils.isEmpty(paramString1)) && (paramString1.equals("true")) && (NECaptchaValidate.length() > 0)) {
//                    requestYDcode(toVertifyPhoneNum, "01", NECaptchaValidate);
                    requestPhoneCode(toVertifyPhoneNum,NECaptchaValidate);
                    return;
                }
            }
        });
    }

    String url = "https://bjjj.zhongchebaolian.com/industryguild_mobile_standard_self2.1.2/mobile/standard/neteaseImageValidate/v2";
    private void requestYDcode(final String phone, String paramString2, final String NECaptchaValidate) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String params = ParamUtil.createYDCodeParams(phone, NECaptchaValidate);
                Log.d("fbb", "params:" + params);
                String response = HttpUtil.sendHtpps(url, params);
                Log.d("fbb", "response:" + response);
                if(response != null && !response.equalsIgnoreCase("")) {
                }
//                {"ssidcode":null,"rescode":"4001","resdes":"老用户登录成功","userid":"0E0CDBD816C846A9A9BF71AA57C94851"}
            }
        }).start();

    }

    String loginUrl = "http://192.168.0.113:8080/JJZAutoHandle/login";
    private void requestPhoneCode(final String phone, final String NECaptchaValidate) {
        AsyncTask<String, Integer, String> task = new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... strings) {
                MyApplication app = MyApplication.getInstance();
                String params ="type=0"+ "&imei="+app.imei+"&imsi="+app.imsi+"&gpslat"+app.gpslon+"&gpslat"+app.gpslat+
                        "&phone="+phone+"&validate="+NECaptchaValidate;
                Log.d("fbb", "params:" + params);
                String response = HttpUtil.sendPost(loginUrl, params);
                Log.d("fbb", "response:" + response);
                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                if(response != null && !response.equalsIgnoreCase("")) {
                    ResultPhoneCode resultPhoneCode = JsonUtil.objFromJson(response, ResultPhoneCode.class);
                    if(resultPhoneCode.rescode.equalsIgnoreCase("200")) {
                        Toast.makeText(LoginActivity.this,resultPhoneCode.resdes,Toast.LENGTH_SHORT).show();
                    } else if(resultPhoneCode.rescode.equalsIgnoreCase("4001")){
                        MyApplication.getInstance().saveUserId(resultPhoneCode.userid);
                        Toast.makeText(LoginActivity.this,"老用户跳过验证",Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this,resultPhoneCode.resdes,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        task.execute(phone,NECaptchaValidate);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                MyApplication app = MyApplication.getInstance();
//                String params ="type=0"+ "&imei="+app.imei+"&imsi="+app.imsi+"&gpslat"+app.gpslon+"&gpslat"+app.gpslat+
//                        "&phone="+phone+"&validate="+NECaptchaValidate;
//                Log.d("fbb", "params:" + params);
//                String response = HttpUtil.sendPost(loginUrl, params);
//                Log.d("fbb", "response:" + response);
//                if(response != null && !response.equalsIgnoreCase("")) {
//                    ResultPhoneCode resultPhoneCode = JsonUtil.objFromJson(response, ResultPhoneCode.class);
//                    if(resultPhoneCode.rescode.equalsIgnoreCase("200")) {
//                        Toast.makeText(LoginActivity.this,resultPhoneCode.resdes,Toast.LENGTH_SHORT).show();
//                    } else if(resultPhoneCode.rescode.equalsIgnoreCase("4001")){
//                        MyApplication.getInstance().saveUserId(resultPhoneCode.userid);
//                        Toast.makeText(LoginActivity.this,"老用户跳过验证",Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(LoginActivity.this,resultPhoneCode.resdes,Toast.LENGTH_SHORT).show();
//                    }
//                }
////                {"ssidcode":null,"rescode":"4001","resdes":"老用户登录成功","userid":"0E0CDBD816C846A9A9BF71AA57C94851"}
//            }
//        }).start();

    }


    private void requestLogin(final String phone, final String logincode) {
        AsyncTask<String, Integer, String> task = new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... strings) {
                MyApplication app = MyApplication.getInstance();
                String params ="type=1&imei="+app.imei+"&imsi="+app.imsi+"&gpslat="+app.gpslon+"&gpslat="+app.gpslat+
                        "&phone="+phone+"&logincode="+logincode;
                Log.d("fbb", "params:" + params);
                String response = HttpUtil.sendPost(loginUrl, params);
                Log.d("fbb", "response:" + response);
                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                if(response != null && !response.equalsIgnoreCase("")) {
                    ResultLogin resultLogin = JsonUtil.objFromJson(response, ResultLogin.class);
                    if(resultLogin != null){
                        if(resultLogin.rescode.equalsIgnoreCase("200")){
                            Toast.makeText(LoginActivity.this,resultLogin.resdes,Toast.LENGTH_SHORT).show();
                            MyApplication.getInstance().saveUserId(resultLogin.userid);
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    }
                }
            }
        };
        task.execute(phone,logincode);
    }

}
