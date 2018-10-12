package com.dasu.update;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import static com.dasu.update.ConstValue.LOG_TAG;

/**
 * 负责相关逻辑处理
 * Created by dasu on 2017/4/8.
 *
 */

class UpdateHelper {
    private static final String TAG  = "UpdateHelper";

    public static void checkUpdate(final Context context,
                                   final UpdateConfig config,
                                   final OnUpdateListener listener) {
        if (config.checkVersion(context) && config.isAutoDownload) {
            if (TextUtils.isEmpty(config.mApkUrl) || TextUtils.isEmpty(config.mApkFilePath)) {
                Log.w(LOG_TAG, TAG + " : UpdateConfig.mApkUrl == null or mApkFilePath == null");
                return;
            }
            if (listener != null) {
                listener.onPreUpdate(config.mNewVersionInfo);
            }
            downloadApk(context, config.mApkUrl, config.mApkFilePath, listener);
        }
    }

    static void downloadApk(final Context context,
                            final String apkUrl,
                            final String apkFilePath,
                            final OnUpdateListener listener) {

        UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(apkUrl, apkFilePath, listener);
        updateAsyncTask.execute();
    }
}
