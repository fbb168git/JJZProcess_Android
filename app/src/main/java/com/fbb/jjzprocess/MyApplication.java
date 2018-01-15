package com.fbb.jjzprocess;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fengbb on 2018/1/4.
 */

public class MyApplication extends Application {
    private static MyApplication instance = null;

    public String imei = "";
    public String imsi = "";
    public String gpslon = "";
    public String gpslat = "";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public boolean saveUserId(String userid) {
        SharedPreferences preference = getApplicationContext().getSharedPreferences(
                "normal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("login_user",userid);
        editor.commit();
        return true;
    }
    public String getUserId() {
        SharedPreferences preference = getApplicationContext().getSharedPreferences(
                "normal", Context.MODE_PRIVATE);
        String login_user = preference.getString("login_user", null);
        return login_user;
    }

    public static MyApplication getInstance() {
        return instance;
    }

}
