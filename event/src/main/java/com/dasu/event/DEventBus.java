package com.dasu.event;

/**
 * Created by dasu on 2018/10/26.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 * 事件通信入口
 */
public class DEventBus {
    private static EventHelper sEventHelper = new EventHelper();

    public static void register(String event, EventObserver observer) {
        sEventHelper.register(event, observer);
    }

    public static void unregister(EventObserver observer) {
        sEventHelper.unregister(observer);
    }

    public static void notifyEvent(String event, Object data) {
        sEventHelper.notifyEvent(event);
    }
}
