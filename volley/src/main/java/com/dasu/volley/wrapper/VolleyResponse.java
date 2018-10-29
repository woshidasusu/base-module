package com.dasu.volley.wrapper;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.dasu.volley.DVolleyCode;
import com.dasu.volley.ICommonResultStruct;
import com.dasu.volley.VolleyListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class VolleyResponse<T> implements Response.Listener<String>, Response.ErrorListener {
    private final String TAG = "VolleyResponse";

    private VolleyListener<T> mVolleyListener;
    private VolleyRequest mRequestWrapper;
    private Handler mUiHandler = new Handler(Looper.getMainLooper());


    public VolleyResponse(VolleyListener<T> volleyListener, VolleyRequest requestWrapper) {
        mVolleyListener = volleyListener;
        mRequestWrapper = requestWrapper;
        if (mRequestWrapper == null) {
            throw new NullPointerException("VolleyRequest can't be null.");
        }
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
        if (VolleyLog.DEBUG) {
            Log.d("volley", "[onErrorResponse-> url:" + mRequestWrapper.mUrl + ", params: " + mRequestWrapper.mParamsMap + ", code:" + code + ", msg: " + description + "]");
        }

        onError(code, description);
    }

    @Override
    public void onResponse(final String response) {
        if (VolleyLog.DEBUG) {
            Log.d("volley", "[onResponse-> url:" + mRequestWrapper.mUrl + ", params: " + mRequestWrapper.mParamsMap + ", data:" + response + "]");
        }
        if (!TextUtils.isEmpty(response)) {
            try {
                final Gson gson = new Gson();
                Type type = null;
                if (mVolleyListener != null) {
                    type = mVolleyListener.getClass().getGenericInterfaces()[0];
                    if (type instanceof ParameterizedType) {
                        type = ((ParameterizedType)type).getActualTypeArguments()[0];
                    } else {
                        type = new TypeToken<String>(){}.getType();
                    }

                    ICommonResultStruct result = null;
                    if (mRequestWrapper.mInterceptor != null && mRequestWrapper.mInterceptor.getCommonResultStruct() != null) {
                        Class cls = mRequestWrapper.mInterceptor.getCommonResultStruct().getClass();
                        type = new ParameterizedTypeImpl(cls, new Type[]{type});
                        result = gson.fromJson(response, type);
                    }

                    if (type.toString().contains("java.lang.String")
                            || type.toString().contains("java.lang.Object")) {

                        final T data;
                        if (result != null) {
                            data = (T) result.getData();
                        } else {
                            data = (T) response;
                        }

                        boolean interceptor = false;
                        if (mRequestWrapper.mInterceptor != null) {
                            interceptor = mRequestWrapper.mInterceptor.onSuccess(result);
                        }
                        if (!interceptor) {
                            mUiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (!mRequestWrapper.mRequest.isCanceled()) {
                                        mVolleyListener.onSuccess(data);
                                    }
                                }
                            });
                        }
                    } else {
                        final T data;
                        if (result != null) {
                            data = (T) result.getData();
                        } else {
                            data = gson.fromJson(response, type);
                        }

                        boolean interceptor = false;
                        if (mRequestWrapper.mInterceptor != null) {
                            interceptor = mRequestWrapper.mInterceptor.onSuccess(result);
                        }
                        if (!interceptor) {
                            mUiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (!mRequestWrapper.mRequest.isCanceled()) {
                                        mVolleyListener.onSuccess(data);
                                    }
                                }
                            });
                        }
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

        if (mRequestWrapper.mInterceptor != null) {
            mRequestWrapper.mInterceptor.onError(code, description);
        }

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