package com.dasu.basemodule.wams;

import com.dasu.volley.VolleyListener;

/**
 * Created by dasu on 2018/10/26.
 * 微信公众号：dasuAndroidTv
 * https://github.com/woshidasusu/DWanAndroid
 */
public class WamsResListener<T> {
    private static final String TAG = "WAMS";

    public VolleyListener<WamsResEntity<T>> mVolleyListener;

    public WamsResListener() {
        mVolleyListener = new VolleyListener<WamsResEntity<T>>() {
            @Override
            public void onSuccess(WamsResEntity<T> data) {

            }

            @Override
            public void onError(int code, String description) {

            }
        };
    }
}
