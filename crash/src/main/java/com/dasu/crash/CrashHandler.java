package com.dasu.crash;

import android.content.Context;

import java.lang.Thread.UncaughtExceptionHandler;


/**
 * Created by suxq on 2018/9/12.
 *
 * Crash 处理，将 crash 信息以及设备信息写入 crash 文件，默认在 sd/{包名}/crash.log 文件中
 * 如果 sd 不可用，那么会写到 /data/data/{包名}/files/crash.log
 *
 * 另外，支持以下操作：
 * @see #getCrashLogs() 获取 crash 的文本信息，多个 crash 信息以数组分开
 * @see #clearCrashLogFile() 清空 crash 文件
 * @see #getCrashFilePath() 获取 crash 文件的路径
 * @see #setOnCrashListener(OnCrashListener) 扩展接口
 */

public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";

    private static CrashHandler sInstance = new CrashHandler();

    private UncaughtExceptionHandler mDefaultHandler;
    private OnCrashListener mOnCrashListener;
    private CrashHandlerHelper mHandlerHelper;
    private boolean mIsInit = false;

    private CrashHandler() {

    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context) {
        mIsInit = true;
        mHandlerHelper = new CrashHandlerHelper(context);
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (ex == null) {
            if (mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(thread, ex);
            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        } else {
            ex.printStackTrace();
            mHandlerHelper.saveCrashInfo2File(ex);
            if (mOnCrashListener != null) {
                mOnCrashListener.onCrash(ex);
            }
            if (mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(thread, ex);
            }
        }

    }

    /**
     * 获取 crash 的文本信息，多个 crash 信息以数组分开
     * @return
     */
    public String[] getCrashLogs() {
        checkInit();
        return mHandlerHelper.getCrashLogs();
    }

    /**
     * 清空 crash 文件
     */
    public void clearCrashLogFile() {
        checkInit();
        mHandlerHelper.clearCrashLogFile();
    }

    /**
     * 获取 crash 文件的路径
     * @return
     */
    public String getCrashFilePath() {
        checkInit();
        return mHandlerHelper.mLogPathDir + mHandlerHelper.mCrashFileName;
    }

    public void setOnCrashListener(OnCrashListener listener) {
        mOnCrashListener = listener;
    }

    private void checkInit() {
        if (!mIsInit) {
            throw new UnsupportedOperationException("u should call init() before use CrashHandle's function...");
        }
    }

}