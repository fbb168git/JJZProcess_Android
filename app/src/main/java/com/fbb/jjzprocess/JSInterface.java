package com.fbb.jjzprocess;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by fengbb on 2018/1/2.
 */

public class JSInterface {
    private CaptchaDialog captchaDialog;
    private CaptchaListener captchaListener;
    private Context context;

    public JSInterface(Context paramContext, CaptchaListener paramCaptchaListener, CaptchaDialog paramCaptchaDialog) {
        this.captchaListener = paramCaptchaListener;
        this.captchaDialog = paramCaptchaDialog;
        this.context = paramContext;
    }

    @JavascriptInterface
    public void closeWindow() {
        this.captchaDialog.dismiss();
        if (this.captchaListener != null) {
            this.captchaListener.closeWindow();
        }
        if (this.captchaDialog.getProgressDialog() != null) {
            this.captchaDialog.getProgressDialog().dismiss();
        }
    }

    @JavascriptInterface
    public void onError(String paramString) {
        this.captchaDialog.dismiss();
        if (this.captchaListener != null) {
            this.captchaListener.onError(paramString);
        }
        if (this.captchaDialog.getProgressDialog() != null) {
            this.captchaDialog.getProgressDialog().dismiss();
        }
    }

    @JavascriptInterface
    public void onReady() {
        ((Activity) this.context).runOnUiThread(new Runnable() {
            public void run() {
                if (!JSInterface.this.captchaDialog.isShowing()) {
                    JSInterface.this.captchaDialog.show();
                }
            }
        });
        if (this.captchaListener != null) {
            this.captchaListener.onReady(true);
        }
        if (this.captchaDialog.getProgressDialog() != null) {
            this.captchaDialog.getProgressDialog().dismiss();
        }
    }

    @JavascriptInterface
    public void onValidate(String paramString1, String paramString2, String paramString3) {
        Log.i("myCaptcha", "result = " + paramString1 + ", validate = " + paramString2 + ", message = " + paramString3);
        if ((paramString2 != null) && (paramString2.length() > 0)) {
            this.captchaDialog.dismiss();
        }
        if (this.captchaListener != null) {
            this.captchaListener.onValidate(paramString1, paramString2, paramString3);
        }
    }
}
