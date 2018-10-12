package com.dasu.update;


/**
 * Created by dasu on 2017/4/7.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 */

public interface OnUpdateListener {

    /**
     * apk下载的进度
     *
     * @param progress 取值范围 0--100
     */
    void onDownloading(int progress);

    /**
     * apk是否成功下载
     *
     * @param isSucceed ture: 下载成功
     */
    void onDownloadFinish(boolean isSucceed, String apkPath);

    /**
     * 准备升级，在这阶段做点升级前的准备，比如初始化弹窗等
     */
    void onPreUpdate(Object newVersionInfo);
}
