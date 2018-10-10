package com.dasu.ftp;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * Created by suxq on 2018/9/11.
 */

class FtpUploadTask implements Runnable {
    private String mFtpUrl;
    private String mFilePath;
    private String mDestPath;
    private OnUploadListener mUploadListener;

    FtpUploadTask(@NonNull String filePath, String ftpUrl, String destPath, OnUploadListener listener) {
        mFtpUrl = ftpUrl;
        mFilePath = filePath;
        mUploadListener = listener;
        mDestPath = destPath;
    }

    @Override
    public void run() {
        File file = new File(mFilePath);
        if (!file.exists()) {
            if (mUploadListener != null) {
                mUploadListener.onError(FtpController.CODE_LOG_FILE_NOT_FOUND, null);
            }
            return;
        }
        if (!mFtpUrl.endsWith(File.separator)) {
            mFtpUrl += File.separator;
        }
        String url = mFtpUrl + mDestPath;
        if (FtpController.sIsPrintDebugLog) {
            Log.d(FtpController.LOG_TAG, "FtpUploadTask > init ftpUrl：" + url);
        }
        if (TextUtils.isEmpty(url)) {
            if (mUploadListener != null) {
                mUploadListener.onError(FtpController.CODE_URL_ERROR, null);
            }
            return;
        }

        Uri uri = Uri.parse(url);
        String hostUrl = uri.getHost();
        int port = uri.getPort();
        String userInfo = uri.getUserInfo();
        String remotePath = uri.getPath();
        if (TextUtils.isEmpty(hostUrl) || TextUtils.isEmpty(userInfo) || TextUtils.isEmpty(remotePath)) {
            Log.w(FtpController.LOG_TAG, "FtpUploadTask > ftpUpload error, check ftpUrl:" + url);
            if (mUploadListener != null) {
                mUploadListener.onError(FtpController.CODE_URL_ERROR, null);
            }
            return;
        }
        String[] userInfoStr = userInfo.split(":");
        String username = userInfoStr[0];
        String password = userInfoStr[1];

        long startTime = System.currentTimeMillis();
        if (FtpController.sIsPrintDebugLog) {
            Log.d(FtpController.LOG_TAG, "FtpUploadTask > start upload time：" + startTime
                    + ", filePath = " + mFilePath
                    + ", hostUrl = " + hostUrl
                    + ", port = " + port
                    + ", userName = " + username
                    + ", passWord = " + password
                    + ", remotePath = " + remotePath);
        }

        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(5000);
        FileInputStream fis = null;
        int code = FtpController.CODE_UNKNOWN_ERROR;
        String exception = "unknown error";
        try {
            ftpClient.connect(hostUrl, port);
            boolean loginResult = ftpClient.login(username, password);
            int returnCode = ftpClient.getReplyCode();
            if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功
                ftpClient.makeDirectory(remotePath);
                // 设置上传目录
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.enterLocalPassiveMode();
                fis = new FileInputStream(mFilePath);
                boolean uploadResult = ftpClient.storeFile(file.getName(), fis);

                if (uploadResult) {
                    code = FtpController.CODE_SUCCESS;   //上传成功
                } else {
                    code = FtpController.CODE_NETWORK_TIMEOUT;   //上传失败
                }
            } else {// 如果登录失败
                code = FtpController.CODE_FTP_LOGIN_FAIL;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(FtpController.LOG_TAG, "FtpUploadTask > ftpUpload error", e);
            exception = "FTPClient IOException, msg:" + e.getMessage();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        if (FtpController.sIsPrintDebugLog) {
            Log.d(FtpController.LOG_TAG, "FtpUploadTask > endUpload:" + endTime + ", code:" + code + ", msg:" + exception);
        }
        if (code == FtpController.CODE_SUCCESS) {
            mUploadListener.onSuccess();
        } else {
            mUploadListener.onError(code, new Exception(exception));
        }
    }
}
