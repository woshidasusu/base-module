package com.dasu.volley.wrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dasu on 2018/10/22.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 */
public class VolleyManager {
    static Map<String, String> sGlobalParamsMap = new HashMap<>();
    static Map<String, String> sGlobalHeadersMap = new HashMap<>();
    static int sGlobalTimeout = -1;
    static int sGlobalMaxRetryCount = -1;

    public static void cancelRequests(Object tag) {
        VolleyQueueSingleton.getInstance().cancelAll(tag);
    }

    public static void addParam(String key, String value) {
        sGlobalParamsMap.put(key, value);
    }

    public static void addParams(Map<String, String> params) {
        sGlobalParamsMap.putAll(params);
    }

    public static void addHeader(String key, String value) {
        sGlobalHeadersMap.put(key, value);
    }

    public static void addHeaders(Map<String, String> headers) {
        sGlobalHeadersMap.putAll(headers);
    }

    public static void setGlobalTimeout(int timeSeconds) {
        sGlobalTimeout = timeSeconds;
    }

    public static void setGlobalMaxRetryCount(int maxRetryCount) {
        sGlobalMaxRetryCount = maxRetryCount;
    }

}
