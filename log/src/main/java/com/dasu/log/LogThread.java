package com.dasu.log;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by suxq on 2018/9/11.
 *
 * 使用单线程消息队列机制，1.解决文件并发读写问题，2.将日志组件的工作都交由子线程处理，提升主线程效率
 */

class LogThread extends HandlerThread {

    private static LogThread sInstance = new LogThread("log-thread");
    private static boolean sIsStart = false;
    private Handler mWorkerHandler;

    private LogThread(String name) {
        super(name);
    }

    public static LogThread getInstance() {
        if (!sIsStart) {
            sIsStart = true;
            sInstance.start();
        }
        return sInstance;
    }

    @Override
    protected void onLooperPrepared() {
        mWorkerHandler = new Handler(getLooper());
    }

    @Override
    public boolean quit() {
        sIsStart = false;
        return super.quit();
    }

    @Override
    public boolean quitSafely() {
        sIsStart = false;
        return super.quitSafely();
    }

    public Handler getHandler() {
        if (mWorkerHandler == null) {
            mWorkerHandler = new Handler(getLooper());
        }
        return mWorkerHandler;
    }
}
