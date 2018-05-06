package com.dasu.localnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by dasu on 2017/4/7.
 *
 * 手机网络状态变化的监听
 */

public class NetBroadcastReceiver extends BroadcastReceiver {

    /**
     * 系统关于网络变化发出的广播action
     */
    public static final String NET_CHANGED_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    public static NetBroadcastReceiver getInstance() {
        return Singleton.sInstance;
    }

    private static final class Singleton {
        static NetBroadcastReceiver sInstance = new NetBroadcastReceiver();
    }

    /**
     * 支持并发读，为什么选择使用这个，因为这个支持在读的过程中对 list 进行修改，
     * 它是线程安全的
     */
    private static CopyOnWriteArrayList<NetStateListener> mListeners = new CopyOnWriteArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(NET_CHANGED_ACTION)) {
            for (NetStateListener listener : mListeners) {
                listener.onNetChanged(LocalNetHelper.isConnected(context.getApplicationContext()));
            }
        }
    }

    static synchronized void addListener(NetStateListener listener) {
        if (mListeners.contains(listener)) {
            return;
        }
        mListeners.add(listener);
    }

    static synchronized void removeListener(NetStateListener listener) {
        mListeners.remove(listener);
    }

}
