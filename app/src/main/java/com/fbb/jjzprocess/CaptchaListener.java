package com.fbb.jjzprocess;

/**
 * Created by fengbb on 2018/1/2.
 */

public abstract interface CaptchaListener
{
    public abstract void closeWindow();

    public abstract void onCancel();

    public abstract void onError(String paramString);

    public abstract void onReady(boolean paramBoolean);

    public abstract void onValidate(String paramString1, String paramString2, String paramString3);
}
