package com.dasu.ftp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.dasu.thread.WorkerThread;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by suxq on 2018/9/12.
 */

class FtpHelper {

    private Handler mUiHandler = new Handler(Looper.getMainLooper());
    //同一FTP地址的，过滤掉短时间内的连续上传事件
    private Set<String> mUploadedFtpUrl = new HashSet<>();
    private long mMinUploadInterval = 30 * 1000;// 两次同一地址的FTP上传需间隔至少30s

    private static class Singleton {
        static FtpHelper sInstance = new FtpHelper();
    }

    static FtpHelper getInstance() {
        return Singleton.sInstance;
    }

    void ftpUpload(final Context context, final String filePath, final String ftpUrl,
                   final String destPath, final OnUploadListener listener) {
        final String fileCode = ftpUrl + destPath + filePath;
        if (mUploadedFtpUrl.contains(fileCode)) {
            Log.w(FtpController.LOG_TAG, "ftpUpload too often, retry after 1 min");
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.onError(FtpController.CODE_FTP_UPLOAD_TOO_OFTEN, new Exception("FTP上传太频繁，请30秒后重试！"));
                    }
                }
            });
            return;
        }
        if (TextUtils.isEmpty(filePath)) {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.onError(FtpController.CODE_LOG_FILE_NOT_FOUND, new Exception("日志文件不存在！"));
                    }
                }
            });
            return;
        }
        mUploadedFtpUrl.add(fileCode);
        FtpUploadTask uploadTask = new FtpUploadTask(filePath, ftpUrl, destPath, new OnUploadListener() {

            @Override
            public void onSuccess() {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onSuccess();
                        }
                    }
                });
                WorkerThread.getInstance().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mUploadedFtpUrl.remove(fileCode);
                    }
                }, mMinUploadInterval);
            }

            @Override
            public void onError(final int code, final Exception description) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mUploadedFtpUrl.remove(fileCode);
                        if (listener != null) {
                            switch (code) {
                                case FtpController.CODE_NETWORK_TIMEOUT:
                                    if (!isNetConnected(context)) {
                                        listener.onError(FtpController.CODE_NETWORK_ERROR, new Exception("网络请求失败，请稍后重试！"));
                                    } else {
                                        listener.onError(code, new Exception("系统繁忙，请稍后重试！"));
                                    }
                                    break;
                                case FtpController.CODE_URL_ERROR:
                                    listener.onError(code, new Exception("上传url出错！"));
                                    break;
                                case FtpController.CODE_LOG_FILE_NOT_FOUND:
                                    listener.onError(code, new Exception("日志文件不存在！"));
                                    break;
                                case FtpController.CODE_FTP_LOGIN_FAIL:
                                    listener.onError(code, new Exception("FTP登录失败，请检查配置或稍后重试！"));
                                    break;
                                case FtpController.CODE_UNKNOWN_ERROR:
                                    if (!isNetConnected(context)) {
                                        listener.onError(FtpController.CODE_NETWORK_ERROR, new Exception("网络请求失败，请稍后重试！"));
                                    } else {
                                        listener.onError(code, new Exception("系统繁忙，请稍后重试！"));
                                    }
                                    break;
                                default:
                                    listener.onError(code, new Exception("系统繁忙，请稍后重试！"));
                                    break;
                            }
                        }
                    }
                });
            }
        });
        WorkerThread.getInstance().getHandler().post(uploadTask);
    }


    private boolean isNetConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable() && info.isConnected()) {
            return true;
        }
        return false;
    }
}
