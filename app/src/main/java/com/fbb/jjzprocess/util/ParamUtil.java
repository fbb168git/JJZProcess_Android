package com.fbb.jjzprocess.util;

import android.util.Log;

import com.fbb.jjzprocess.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengbb on 2018/1/2.
 */

public class ParamUtil {
    private static Map<String, String> map = new HashMap();
    public static String createYDCodeParams(String phone, String NECaptchaValidate) {
        map.clear();
//        String deviceId = DeviceUtil.getDeviceUUID();
//        String deviceId = "526bdde9-3bda-314d-9743-305c70f92451";
//        deviceId = "82e22736-ce3f-3a03-a790-56c0634abd40";

        String deviceId = MyApplication.getInstance().imei;

        Object localObject1 = null;
        JSONObject localJSONObject = new JSONObject();

        try {
            localJSONObject.put("phone", phone);
            localJSONObject.put("smsflag", "01");
            localJSONObject.put("deviceId", deviceId);
            map.put("phone", phone);
            map.put("smsflag", "01");
            map.put("deviceId", deviceId);
            localJSONObject.put("sign", CipherUtil.sortSignParameters(map));
            JSONObject commJSONObject = CipherUtil.addCommonParameters(localJSONObject);
            commJSONObject.put("NECaptchaValidate", NECaptchaValidate);
            Log.d("fbb","before UrlEncode:"+commJSONObject.toString());
            String result = URLEncoder.encode((commJSONObject).toString(), "UTF-8");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }
}
