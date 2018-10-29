package com.dasu.event;

/**
 * Created by dasu on 2018/10/26.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 * 事件订阅者（观察者）
 */
public interface EventObserver<T> {

    /**
     * 事件发生时回调
     * @param data
     */
    void onEvent(T data);

}
