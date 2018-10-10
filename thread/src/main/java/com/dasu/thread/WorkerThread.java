package com.dasu.thread;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by suxq on 2018/9/28.
 * 工作线程，用于处理耗时的工作
 */

public class WorkerThread extends HandlerThread {

    private static WorkerThread sInstance = new WorkerThread("worker-thread");
    private static boolean sIsStart = false;
    private Handler mWorkerHandler;

    private WorkerThread(String name) {
        super(name);
    }

    public static WorkerThread getInstance() {
        if (!sIsStart) {
            sIsStart = true;
            sInstance.start();
        }
        Context context;
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
