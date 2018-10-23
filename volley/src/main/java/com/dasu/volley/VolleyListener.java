package com.dasu.volley;

/**
 * Created by dasu on 2018/10/22.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 */
public interface VolleyListener<T> {

    /**
     * 请求成功回调
     * @param data
     */
    void onSuccess(T data);

    /**
     * 请求失败的回调
     * @param code
     * @param description
     */
    void onError(int code, String description);

}
