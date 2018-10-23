package com.dasu.volley.wrapper;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by dasu on 2018/10/22.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 */
class StringRequestWrapper extends StringRequest {

    private Map<String, String> mParamsMap;
    private Map<String, String> mHeadersMap;

    public StringRequestWrapper(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public Map<String, String> getParams() {
        return mParamsMap;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeadersMap;
    }

    public void setParamsMap(Map<String, String> paramsMap) {
        mParamsMap = paramsMap;
    }

    public void setHeadersMap(Map<String, String> headersMap) {
        mHeadersMap = headersMap;
    }

    @Override
    public Priority getPriority() {
        return Priority.HIGH;
    }
}
