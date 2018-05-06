package com.dasu.localnet;

import android.content.Context;
import android.content.IntentFilter;

/**
 * Created by suxq on 2018/5/5.
 */

public class LocalNetController {

    public static void registerNetStateBroadcast(Context context) {
        IntentFilter filter = new IntentFilter(NetBroadcastReceiver.NET_CHANGED_ACTION);
        context.getApplicationContext().registerReceiver(NetBroadcastReceiver.getInstance(), filter);
    }

    public static void unregisterNetStateBroadcast(Context context) {
        context.getApplicationContext().unregisterReceiver(NetBroadcastReceiver.getInstance());
    }

    public static void addNetListener(NetStateListener listener) {
        NetBroadcastReceiver.addListener(listener);
    }

    public static void removeNetListener(NetStateListener listener) {
        NetBroadcastReceiver.removeListener(listener);
    }

}
