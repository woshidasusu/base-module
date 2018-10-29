package com.dasu.event;

import com.dasu.thread.WorkerThread;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dasu on 2018/10/26.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 */
class EventHelper {

    //观察者
    private CopyOnWriteArrayList<EventObserver> mEventObservers = new CopyOnWriteArrayList<>();
    //被观察者
    private CopyOnWriteArrayList<String> mEventObservables = new CopyOnWriteArrayList<>();


    public void register(String event, EventObserver observer) {
        WorkerThread.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public void unregister(EventObserver observer) {

    }

    public void notifyEvent(String event) {

    }
}
