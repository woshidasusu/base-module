package com.dasu.okhttp;

/**
 * Created by suxq on 2017/4/13.
 */

public interface RetrofitListener<T> {

    /**
     * 请求成功时回调
     *
     * @param data 网络请求的数据对象
     */
    void onSuccess(T data);

    /**
     * 请求失败时回调，网络问题造成的连接失败以及服务端返回的code不在200-300内，比如404
     * 都会触发该回调
     *
     * @param description 失败的描述
     */
    void onError(String description);

}
