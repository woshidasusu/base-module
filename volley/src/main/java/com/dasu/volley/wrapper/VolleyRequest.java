package com.dasu.volley.wrapper;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.dasu.volley.VolleyListener;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequest {
    int mTimeOutMs = DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
    int mMaxNumRetries = 3;
    Map<String, String> mParamsMap;
    Map<String, String> mHeadersMap;
    Object mTag;
    int mMethod;
    String mUrl;
    Request mRequest;

    public VolleyRequest(String url) {
        mUrl = url;
        mParamsMap = new HashMap<>();
        mHeadersMap = new HashMap<>();
    }

    public void addParam(String key, String value) {
        mParamsMap.put(key, value);
    }

    public void addParams(Map<String, String> params) {
        mParamsMap.putAll(params);
    }

    public void addHeader(String key, String value) {
        mHeadersMap.put(key, value);
    }

    public void addHeaders(Map<String, String> headers) {
        mHeadersMap.putAll(headers);
    }

    public void setTag(Object tag) {
        mTag = tag;
    }

    public void setMethod(int method) {
        mMethod = method;
    }

    public <T> void createRequest(VolleyListener<T> listener) {
        VolleyResponse volleyResponse = new VolleyResponse(listener);
        StringRequestWrapper request = new StringRequestWrapper(mMethod, mUrl, volleyResponse, volleyResponse);
        mRequest = request;
        volleyResponse.setRequestWrapper(this);
        if (VolleyManager.sGlobalTimeout > 0) {
            mTimeOutMs = VolleyManager.sGlobalTimeout * 1000;
        }
        if (VolleyManager.sGlobalMaxRetryCount > 0) {
            mMaxNumRetries = VolleyManager.sGlobalMaxRetryCount;
        }
        if (mTimeOutMs != DefaultRetryPolicy.DEFAULT_TIMEOUT_MS || mMaxNumRetries != DefaultRetryPolicy.DEFAULT_MAX_RETRIES) {
            request.setRetryPolicy(new DefaultRetryPolicy(mTimeOutMs, mMaxNumRetries, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
        mParamsMap.putAll(VolleyManager.sGlobalParamsMap);
        mHeadersMap.putAll(VolleyManager.sGlobalHeadersMap);
        request.setHeadersMap(mHeadersMap);
        request.setParamsMap(mParamsMap);
        request.setTag(mTag);

        VolleyQueueSingleton.getInstance().add(request);
    }
}
