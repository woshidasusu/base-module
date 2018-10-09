package com.dasu.basemodule;

import android.app.Application;
import android.os.SystemClock;
import android.util.Log;

import com.dasu.crash.CrashHandler;

/**
 * Created by suxq on 2018/10/9.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        long time = SystemClock.uptimeMillis();
        CrashHandler.getInstance().init(this);
        Log.e("!!!!!!!!", "init: " + (SystemClock.uptimeMillis() - time) + "ms");
        String[] logs = CrashHandler.getInstance().getCrashLogs();
        for (String str : logs) {
            Log.e("!!!!!!!!!", str);
        }
        Log.e("!!!!!", "crash count: " + logs.length);
    }
}
