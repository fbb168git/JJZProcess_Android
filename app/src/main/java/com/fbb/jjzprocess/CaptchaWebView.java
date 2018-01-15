package com.fbb.jjzprocess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by fengbb on 2018/1/2.
 */

public class CaptchaWebView
        extends WebView {
    private CaptchaDialog captchaDialog;
    private CaptchaListener captchaListener;
    private Context context;
    private boolean debug = false;
    private int mTimeout = 10000;
    private WebChromeClient mWebChromeClient = null;
    private WebViewClientBase mWebViewClientBase = null;
    private ScheduledExecutorService scheduledExecutorService = null;
    private WebView webView = this;

    public CaptchaWebView(Context paramContext, CaptchaListener paramCaptchaListener, CaptchaDialog paramCaptchaDialog) {
        super(paramContext);
        this.context = paramContext;
        this.captchaDialog = paramCaptchaDialog;
        this.captchaListener = paramCaptchaListener;
        this.debug = paramCaptchaDialog.isDebug();
        this.mWebViewClientBase = new WebViewClientBase();
        this.mWebChromeClient = new WebChromeClientBase();
        setWebView();
    }

    private void setWebView() {
        WebSettings localWebSettings = getSettings();
        if (Build.VERSION.SDK_INT >= 21) {
            localWebSettings.setMixedContentMode(0);
        }
        localWebSettings.setJavaScriptEnabled(true);
        localWebSettings.setLoadWithOverviewMode(true);
        localWebSettings.setDomStorageEnabled(true);
        localWebSettings.setDatabaseEnabled(true);
        localWebSettings.setUseWideViewPort(true);
        setOverScrollMode(2);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        setWebViewClient(this.mWebViewClientBase);
        setWebChromeClient(this.mWebChromeClient);
        onResume();
    }

    private class WebChromeClientBase
            extends WebChromeClient {
        private WebChromeClientBase() {
        }

        public void onCloseWindow(WebView paramWebView) {
            CaptchaWebView.this.captchaDialog.cancel();
            super.onCloseWindow(paramWebView);
        }
    }

    private class WebViewClientBase
            extends WebViewClient {
        private WebViewClientBase() {
        }

        public void onPageFinished(WebView paramWebView, String paramString) {
            if (CaptchaWebView.this.captchaDialog.isShowing()) {
                if ((CaptchaWebView.this.scheduledExecutorService != null) && (!CaptchaWebView.this.scheduledExecutorService.isShutdown())) {
                    CaptchaWebView.this.scheduledExecutorService.shutdown();
                }
                Log.i("myCaptcha", "webview did Finished");
            }
            super.onPageFinished(paramWebView, paramString);
        }

        public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap) {
            Log.i("myCaptcha", "webview did start");
            super.onPageStarted(paramWebView, paramString, paramBitmap);
            TimerTask task = new TimerTask() {
                public void run() {
                    ((Activity) CaptchaWebView.this.context).runOnUiThread(new Runnable() {
                        public void run() {
                            if (CaptchaWebView.this.webView.getProgress() < 100) {
                                CaptchaWebView.this.webView.stopLoading();
                                CaptchaWebView.this.captchaDialog.dismiss();
                                if (CaptchaWebView.this.captchaListener != null) {
                                    Log.d("myCaptcha", "time out 2");
                                    CaptchaWebView.this.captchaListener.onReady(false);
                                }
                            }
                        }
                    });
                }
            };
            if (CaptchaWebView.this.scheduledExecutorService == null) {
                CaptchaWebView.this.scheduledExecutorService = Executors.newScheduledThreadPool(2);
            }
            CaptchaWebView.this.scheduledExecutorService.schedule(task, CaptchaWebView.this.mTimeout, TimeUnit.MILLISECONDS);
        }

        public void onReceivedError(WebView paramWebView, int paramInt, String paramString1, String paramString2) {
            if ((paramInt == -2) || (paramInt == -14)) {
                CaptchaWebView.this.captchaListener.onError("errorERROR_FILE_NOT_FOUND" + paramInt);
            }
            CaptchaWebView.this.captchaDialog.show();
            super.onReceivedError(paramWebView, paramInt, paramString1, paramString2);
        }

        public void onReceivedSslError(WebView paramWebView, SslErrorHandler paramSslErrorHandler, SslError paramSslError) {
            paramSslErrorHandler.proceed();
            if (CaptchaWebView.this.captchaListener != null) {
                Log.d("myCaptcha", "onReceivedHttpError ");
            }
            if (CaptchaWebView.this.captchaDialog.getProgressDialog() != null) {
                CaptchaWebView.this.captchaDialog.getProgressDialog().dismiss();
            }
            CaptchaWebView.this.captchaDialog.show();
            super.onReceivedSslError(paramWebView, paramSslErrorHandler, paramSslError);
        }

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(paramString));
            CaptchaWebView.this.context.startActivity(intent);
            return true;
        }
    }
}
