package com.fbb.jjzprocess.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * Created by fengbb on 2018/1/2.
 */

public class DeviceUtil {
    @SuppressLint("MissingPermission")
    public static String getDeviceUUID(Context context) {
        String deviceId = ((TelephonyManager) (context.getSystemService("phone"))).getDeviceId();
        if(deviceId != null && !deviceId.equalsIgnoreCase("")){
            return UUID.nameUUIDFromBytes(deviceId.getBytes()).toString();
        }
        return UUID.randomUUID().toString();
    }

    @SuppressLint("MissingPermission")
    public static String getIMSI(Context context) {
        return((TelephonyManager) (context.getSystemService("phone"))).getSubscriberId();
    }
}
