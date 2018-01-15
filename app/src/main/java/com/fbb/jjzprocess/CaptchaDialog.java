package com.fbb.jjzprocess;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by fengbb on 2018/1/2.
 */

public class CaptchaDialog
        extends Dialog {
    private static final String baseURL = "https://c.dun.163yun.com/api/v1/mobile.html";
    private String dCaptchaId = "5622fb225dcd429c95808563b059cf21";
//    private String dDeviceId = "353627073803879";
    private String dDeviceId = "";
    private float dScale;
    private String dTitle = "";
    private int dWidth;
    private CaptchaListener dcaListener = null;
    private Context dcontext = null;
    private boolean debug = false;
    private CaptchaWebView dwebview = null;
    private boolean isShowing = false;
    private ProgressDialog progressDialog = null;

    public CaptchaDialog(Context paramContext) {
        super(paramContext);
        this.dcontext = paramContext;
    }

    @SuppressLint("MissingPermission")
    private String getDeviceId() {
        try {
            if (this.dDeviceId.equals("")) {
                TelephonyManager localTelephonyManager = (TelephonyManager) this.dcontext.getSystemService("phone");
                if (localTelephonyManager != null) {
                    this.dDeviceId = localTelephonyManager.getDeviceId();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.dDeviceId;
    }

    private void getDilogWidth() {
        try {
            DisplayMetrics localDisplayMetrics = getContext().getResources().getDisplayMetrics();
            int j = localDisplayMetrics.widthPixels;
            int k = localDisplayMetrics.heightPixels;
            float f = localDisplayMetrics.density;
            this.dScale = f;
            int i = j;
            if (k < j) {
                i = k * 3 / 4;
            }
            j = i * 4 / 5;
            i = j;
            if ((int) (j / f) < 270) {
                i = (int) (270.0F * f);
            }
            this.dWidth = i;
            return;
        } catch (Exception localException) {
            Log.e("myCaptcha", "getDilogWidth failed");
        }
    }

    private void setWebView() {
        if (this.dwebview == null) {
            this.dwebview = new CaptchaWebView(this.dcontext, this.dcaListener, this);
        }
        Object localObject = new StringBuffer();
        ((StringBuffer) localObject).append("https://c.dun.163yun.com/api/v1/mobile.html");
        ((StringBuffer) localObject).append("?captchaId=" + this.dCaptchaId);
        ((StringBuffer) localObject).append("&deviceId=" + getDeviceId());
        ((StringBuffer) localObject).append("&os=android");
        ((StringBuffer) localObject).append("&osVer=" + Build.VERSION.RELEASE);
        ((StringBuffer) localObject).append("&sdkVer=1.0.0");
        ((StringBuffer) localObject).append("&title=" + this.dTitle);
        ((StringBuffer) localObject).append("&debug=" + this.debug);
        ((StringBuffer) localObject).append("&width=" + (int) (this.dWidth / this.dScale));
        localObject = ((StringBuffer) localObject).toString();
        Log.d("myCaptcha", "url: " + (String) localObject);
        this.dwebview.addJavascriptInterface(new JSInterface(this.dcontext, this.dcaListener, this), "JSInterface");
        this.dwebview.loadUrl((String) localObject);
        this.dwebview.buildLayer();
    }

    public void dismiss() {
        this.isShowing = false;
        super.dismiss();
    }

    public ProgressDialog getProgressDialog() {
        return this.progressDialog;
    }

    public void initDialog() {
        Log.d("myCaptcha", "start init dialog");
        getDilogWidth();
        setWebView();
    }

    public boolean isDebug() {
        return this.debug;
    }

    public boolean isShowing() {
        return this.isShowing;
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        requestWindowFeature(1);
        LinearLayout layout = new LinearLayout(this.dcontext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.parseColor("#ffffff"));
        Object localObject1 = new RelativeLayout(this.dcontext);
        ((RelativeLayout) localObject1).setGravity(Gravity.CENTER_VERTICAL);
        Object localObject2 = new LinearLayout.LayoutParams(-1, 100);
        ((LinearLayout.LayoutParams) localObject2).topMargin = 20;
        ((RelativeLayout) localObject1).setLayoutParams((ViewGroup.LayoutParams) localObject2);
        localObject2 = new ImageView(this.dcontext);
//        ((ImageView) localObject2).setImageResource(17301533);
        ((RelativeLayout) localObject1).addView((View) localObject2, new LinearLayout.LayoutParams(-2, -1));
        ((ImageView) localObject2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                CaptchaDialog.this.dismiss();
            }
        });
        localObject2 = new TextView(this.dcontext);
        ((TextView) localObject2).setText("请滑动完成拼图");
        ((TextView) localObject2).setGravity(17);
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -1);
        localLayoutParams.leftMargin = 10;
        ((RelativeLayout) localObject1).addView((View) localObject2, localLayoutParams);
        layout.addView((View) localObject1);
        localObject1 = new LinearLayout.LayoutParams(-2, -2);
        ((LinearLayout.LayoutParams) localObject1).topMargin = 10;
        ((LinearLayout.LayoutParams) localObject1).bottomMargin = 20;
        layout.addView(this.dwebview, (ViewGroup.LayoutParams) localObject1);
        setContentView(layout);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.dwebview.getLayoutParams();
        layoutParams.width = this.dWidth;
        layoutParams.height = -2;
        this.dwebview.setLayoutParams(layoutParams);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public CaptchaDialog setCaListener(CaptchaListener paramCaptchaListener) {
        this.dcaListener = paramCaptchaListener;
        return this;
    }

    public CaptchaDialog setCaptchaId(String paramString) {
        this.dCaptchaId = paramString;
        return this;
    }

    public CaptchaDialog setDebug(boolean paramBoolean) {
        this.debug = paramBoolean;
        return this;
    }

    public CaptchaDialog setDeviceId(String paramString) {
        this.dDeviceId = paramString;
        return this;
    }

    public CaptchaDialog setProgressDialog(ProgressDialog paramProgressDialog) {
        if ((this.progressDialog == null) && (paramProgressDialog != null)) {
            this.progressDialog = paramProgressDialog;
        }
        return this;
    }

    public CaptchaDialog setTitle(String paramString) {
        this.dTitle = paramString;
        return this;
    }

    public void show() {
        this.isShowing = true;
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
        super.show();
    }
}
