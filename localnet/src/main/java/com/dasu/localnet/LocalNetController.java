package com.dasu.localnet;

import android.content.Context;
import android.content.IntentFilter;

/**
 * Created by suxq on 2018/5/5.
 *
 * 本地网络状况变化组件，用法：
 * 1. 可在 application 中注册监听手机网络广播
 * 2. 可在 BaseActivity 中注册监听手机网络广播
 * 3. 记得要在对应的生命周期取消注册
 */

public class LocalNetController {

    /**
     * 注册监听手机网络变化广播
     * @param context
     */
    public static void registerNetStateBroadcast(Context context) {
        IntentFilter filter = new IntentFilter(NetBroadcastReceiver.NET_CHANGED_ACTION);
        context.getApplicationContext().registerReceiver(NetBroadcastReceiver.getInstance(), filter);
    }

    /**
     * 取消监听手机网络变化广播
     * @param context
     */
    public static void unregisterNetStateBroadcast(Context context) {
        context.getApplicationContext().unregisterReceiver(NetBroadcastReceiver.getInstance());
    }

    /**
     * 注册网络变化的回调通知
     * @param listener
     */
    public static void addNetListener(NetStateListener listener) {
        NetBroadcastReceiver.addListener(listener);
    }

    /**
     * 取消网络变化的回调通知
     * @param listener
     */
    public static void removeNetListener(NetStateListener listener) {
        NetBroadcastReceiver.removeListener(listener);
    }
}
