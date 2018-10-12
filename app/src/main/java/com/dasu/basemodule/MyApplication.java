package com.dasu.basemodule;

import android.app.Application;
import android.os.SystemClock;

import com.dasu.crash.CrashHandler;
import com.dasu.log.LogUtils;
import com.dasu.utils.DeviceUtils;


/**
 * Created by suxq on 2018/10/9.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.init(this).setIsSave2Disk(true);
        long time = SystemClock.uptimeMillis();
        CrashHandler.getInstance().init(this);
        LogUtils.e("!!!!!!!!", "init: " + (SystemClock.uptimeMillis() - time) + "ms");
        String[] logs = CrashHandler.getInstance().getCrashLogs();
        for (String str : logs) {
//            LogUtils.e("!!!!!!!!!", str);
        }
        LogUtils.e("!!!!!", "crash count: " + logs.length);
        LogUtils.e("!!!!!", DeviceUtils.getDeviceInfo(this));
    }
}
