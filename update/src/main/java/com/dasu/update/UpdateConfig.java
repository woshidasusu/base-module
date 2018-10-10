package com.dasu.update;

import android.content.Context;

/**
 * 升级组件的配置项
 * Created by suxq on 2018/7/6.
 */

public abstract class UpdateConfig {

    static boolean isDebugLogEnable = false;
    boolean isAutoDownload = false;
    String mApkUrl;
    String mApkFilePath;
    Object mNewVersionInfo;

    /**
     * 返回 true，组件内部会去触发 apk 下载
     * @param context
     * @return
     */
    public abstract boolean checkVersion(Context context);

    /**
     * 设置是否打印debug日志
     * @param debugLogEnable
     * @return
     */
    public UpdateConfig setDebugLogEnable(boolean debugLogEnable) {
        isDebugLogEnable = debugLogEnable;
        return this;
    }

    /**
     * 设置 {@link UpdateController#checkUpdate(Context, UpdateConfig, OnUpdateListener)}
     * 之后是否自动去下载 apk
     * @param autoDownload
     * @return
     */
    public UpdateConfig setAutoDownload(boolean autoDownload) {
        isAutoDownload = autoDownload;
        return this;
    }

    /**
     * 设置 apk 下载的 url
     * @param apkUrl
     * @return
     */
    public UpdateConfig setApkUrl(String apkUrl) {
        mApkUrl = apkUrl;
        return this;
    }

    /**
     * 设置 apk 下载到本地的路径
     * @param apkFilePath
     * @return
     */
    public UpdateConfig setApkFilePath(String apkFilePath) {
        mApkFilePath = apkFilePath;
        return this;
    }

    /**
     * 设置版本信息，用于 {@link OnUpdateListener#onPreUpdate(Object)} 通知给 Ui 的新版本信息
     * @param newVersionInfo
     * @return
     */
    public UpdateConfig setNewVersionInfo(Object newVersionInfo) {
        mNewVersionInfo = newVersionInfo;
        return this;
    }
}
