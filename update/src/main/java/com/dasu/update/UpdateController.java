package com.dasu.update;

import android.content.Context;


/**
 * Created by dasu on 2017/4/8.
 * 升级组件对外的接口
 */
public class UpdateController {
    private static final String TAG = "UpdateController";

    /**
     * 检查是否需要升级，是否需要升级由 {@link UpdateConfig#checkVersion(Context)} 决定
     * 当需要升级时，再由 {@link UpdateConfig#setAutoDownload(boolean)} 来决定内部是否直接去下载
     * @param context
     * @param config
     * @param listener
     */
    public static void checkUpdate(final Context context,
                                   final UpdateConfig config,
                                   final OnUpdateListener listener) {
        UpdateHelper.checkUpdate(context, config, listener);
    }

    /**
     * 手动去下载 apk
     * @param context context
     * @param apkUrl apk下载的url
     * @param apkFilePath apk下载到本地的路径
     * @param listener 下载过程回调
     */
    public static void downloadApk(final Context context,
                                   final String apkUrl,
                                   final String apkFilePath,
                                   final OnUpdateListener listener) {
        UpdateHelper.downloadApk(context, apkUrl, apkFilePath, listener);
    }
}
