package com.dasu.ftp;

import android.content.Context;

/**
 * Created by suxq on 2018/10/10.
 *
 * @see #upload(Context, String, String, String, OnUploadListener) FTP 上传文件
 */

public class FtpController {
    //FTP上传的一些状态码
    public static final int CODE_SUCCESS = 1;
    public static final int CODE_URL_ERROR = -100;
    public static final int CODE_NETWORK_ERROR = -1;
    public static final int CODE_NETWORK_TIMEOUT = -2;
    public static final int CODE_LOG_FILE_NOT_FOUND = -3;
    public static final int CODE_FTP_LOGIN_FAIL = -4;
    public static final int CODE_UNKNOWN_ERROR = -5;
    public static final int CODE_FTP_UPLOAD_TOO_OFTEN = -6;

    static final String LOG_TAG = "ftp";
    static boolean sIsPrintDebugLog = BuildConfig.DEBUG;
    private static FtpHelper sFtpHelper = new FtpHelper();

    /**
     * FTP 上传文件
     * @param context
     * @param filePath 本地文件地址
     * @param ftpUrl   FTP地址
     * @param destPath 上传到FTP的目录地址
     * @param listener 回调
     */
    public static void upload(final Context context, final String filePath, final String ftpUrl,
                              final String destPath, final OnUploadListener listener) {
        sFtpHelper.ftpUpload(context, filePath, ftpUrl, destPath, listener);
    }
}
