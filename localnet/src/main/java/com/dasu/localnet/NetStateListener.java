package com.dasu.localnet;

/**
 * Created by dasu on 2017/4/7.
 *
 * 手机网络状态改变时回调
 */

public interface NetStateListener {

    void onNetChanged(boolean isConnected);

}
