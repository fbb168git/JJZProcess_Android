//package com.fbb.jjzprocess;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.Window;
//import android.view.WindowManager;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.widget.Button;
//import android.widget.Toast;
//
//
//import java.io.File;
//import java.util.Calendar;
//
///**
// * Created by fengbb on 2017/10/19.
// */
//public class LoginActivity extends Activity {
//
//    private WebView mWebView;
//    private Button mButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_h5);
//        mWebView = (WebView) findViewById(R.id.webview);
//        mButton = (Button) findViewById(R.id.button);
//
//
//        initWebView();
//
//
//
//
//        //for text
////        imInterface.connectIMServer("fbb","tokenString");
//
//    }
//
//    private void initWebView() {
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        if (Build.VERSION.SDK_INT >= 19) {
//            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        }
//        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        //设置缓存模式
//        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        //webview.setSaveEnabled(true);
////        mWebView.setWebChromeClient(new BuChromeClient(webview));
////        mWebView.addJavascriptInterface(new NormalInterface(webview), "android_normal");
////        mWebView.setWebViewClient(new BuWebClient());
//    }
//
//
//}
//
