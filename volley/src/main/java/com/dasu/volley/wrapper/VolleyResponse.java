package com.dasu.volley.wrapper;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.dasu.volley.DVolleyCode;
import com.dasu.volley.VolleyListener;
import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class VolleyResponse implements Response.Listener<String>, Response.ErrorListener {
    private final String TAG = "VolleyResponse";

    private VolleyListener mVolleyListener;
    private VolleyRequest mRequestWrapper;
    private Handler mUiHandler = new Handler(Looper.getMainLooper());


    public <T> VolleyResponse(VolleyListener<T> volleyListener) {
        mVolleyListener = volleyListener;
    }

    public void setRequestWrapper(VolleyRequest requestWrapper) {
        mRequestWrapper = requestWrapper;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        int code = DVolleyCode.UNKNOWN_ERROR;
        String description = "";
        if (error.networkResponse != null) {
            code = error.networkResponse.statusCode;
            description = error.networkResponse.toString();
        } else {
            code = DVolleyCode.UNKNOWN_ERROR;
            description = "network timeout";
        }
        final int finalCode = code;
        final String finalDescription = description;
        VolleyLog.d("volley", "[onErrorResponse-> url:" + mRequestWrapper.mUrl + ", params: " + mRequestWrapper.mParamsMap + ", code:" + finalCode + ", msg: " + finalDescription + "]");
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!mRequestWrapper.mRequest.isCanceled()) {
                    mVolleyListener.onError(finalCode, finalDescription);
                }
            }
        });
    }

    @Override
    public void onResponse(final String response) {
        VolleyLog.d("volley", "[onResponse-> url:" + mRequestWrapper.mUrl + ", params: " + mRequestWrapper.mParamsMap + ", data:" + response + "]");
        if (!TextUtils.isEmpty(response)) {
            try {
                final Gson gson = new Gson();
                Type type = null;
                if (mVolleyListener != null) {
                    type = mVolleyListener.getClass().getGenericInterfaces()[0];
                    if (type instanceof ParameterizedType) {
                        type = ((ParameterizedType)type).getActualTypeArguments()[0];
                    } else {
                        type = null;
                    }

                    final Type finalType = type;
                    if (finalType == null || finalType.toString().contains("java.lang.String")
                            || finalType.toString().contains("java.lang.Object")) {
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!mRequestWrapper.mRequest.isCanceled()) {
                                    mVolleyListener.onSuccess(response);
                                }
                            }
                        });
                    } else {
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!mRequestWrapper.mRequest.isCanceled()) {
                                    mVolleyListener.onSuccess(gson.fromJson(response, finalType));
                                }
                            }
                        });
                    }
                }
            } catch (Exception e) {
                onError(DVolleyCode.PARSE_FAIL, "解析出错!");
                e.printStackTrace();
            }
        } else {
            onError(DVolleyCode.RESPONSE_NULL, "Response is null!");
        }
    }

    private void onError(final int code, final String description) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!mRequestWrapper.mRequest.isCanceled()) {
                    mVolleyListener.onError(code, description);
                }
            }
        });
    }

}