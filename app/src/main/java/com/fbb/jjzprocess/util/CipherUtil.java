package com.fbb.jjzprocess.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by fengbb on 2018/1/2.
 */


public class CipherUtil {
    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private static String timestamp;

    public static String sortSignParameters(Map<String, String> paramMap) {
        timestamp = DateUtil.getDateTime();
        paramMap.put("timestamp", timestamp);
        StringBuilder localStringBuilder = new StringBuilder();
        String[] arrayOfString = (String[]) paramMap.keySet().toArray(new String[0]);
        Arrays.sort(arrayOfString);
        for(int i =0;i<arrayOfString.length;i++){
            String str1 = arrayOfString[i];
            String str2 = (String) paramMap.get(str1);
            if ((str1.trim().equals("")) || (str2 == null) || (str2.trim().equals(""))) {
            } else {
                localStringBuilder.append(str1).append(str2);
            }
        }
        return aliSign(localStringBuilder.toString());
    }

    public static JSONObject addCommonParameters(JSONObject paramJSONObject) {
        try {
            paramJSONObject.put("timestamp", timestamp);
            paramJSONObject.put("platform", "02");
            Log.i("ALIVERIFY", paramJSONObject.toString());
            return paramJSONObject;
        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();
        }
        return paramJSONObject;
    }

    public static String aliSign(String paramString) {
        return SignUtil.createSign(paramString);
    }

//    public static String aliSign(JSONObject paramJSONObject) {
//        try {
//            paramJSONObject = URLEncoder.encode(paramJSONObject.toString(), "UTF-8");
//            MyLogUtils.i("--------������������������----------->" + paramJSONObject);
//            paramJSONObject = MyApplication.getSecuritySignatueInstance().atlasSign(paramJSONObject, "4ae9bea8-6b2d-4d1f-afe2-52e7e4d2fe42");
//            return paramJSONObject;
//        } catch (JAQException paramJSONObject) {
//            MyLogUtils.e("errorCode =" + paramJSONObject.getErrorCode());
//            return "";
//        } catch (UnsupportedEncodingException paramJSONObject) {
//            for (; ; ) {
//                paramJSONObject.printStackTrace();
//            }
//        }
//    }


//    public static String parserToMap(String paramString) {
//        HashMap localHashMap = new HashMap();
//        for (; ; ) {
//            try {
//                paramString = new JSONObject(paramString);
//                try {
//                    Iterator localIterator = paramString.keys();
//                    boolean bool = localIterator.hasNext();
//                    if (!bool) {
//                        return signTopRequest(localHashMap, localHashMap.size(), localHashMap.size());
//                    }
//                    str1 = (String) localIterator.next();
//                    str2 = paramString.get(str1).toString();
//                    if ((!str2.startsWith("{")) || (!str2.endsWith("}"))) {
//                        continue;
//                    }
//                    localHashMap.put(str1, parserToMap(str2));
//                    continue;
//                    paramString.printStackTrace();
//                } catch (JSONException paramString) {
//                }
//            } catch (JSONException paramString) {
//                String str1;
//                String str2;
//                continue;
//            }
//            return "";
//            localHashMap.put(str1, str2);
//        }
//    }



}

