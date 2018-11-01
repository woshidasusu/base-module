package com.dasu.log;

import android.content.Context;
import android.util.Log;

import com.dasu.thread.WorkerThread;

import org.apache.log4j.Logger;

import java.io.File;


/**
 * Created by suxq on 2018/9/11.
 */

class LogHelper {
    public static final int VERBOSE = Log.VERBOSE;
    public static final int DEBUG = Log.DEBUG;
    public static final int INFO = Log.INFO;
    public static final int WARN = Log.WARN;
    public static final int ERROR = Log.ERROR;

    static String sLogFile = "android_log.log";
    private Logger mLogger;
    private Log4jConfig mLog4jConfig;
    private boolean mIsLoggable = Log.isLoggable("DLOG", Log.VERBOSE);
    private boolean mIsDebugLoggable = Log.isLoggable("DLOG", Log.DEBUG);

    void initLog4j() {
        if (mLogger == null) {
            mLogger = Logger.getLogger("android-log");
            String fileName = LogConfig.sDiskCachePath + File.separator + sLogFile;
            mLog4jConfig = new Log4jConfig(fileName);
            mLog4jConfig.setMaxFileSize(LogConfig.sMaxLogFileSize);
            mLog4jConfig.configure(mLogger);
        }
    }

    void updateLog4jConfig() {
        String fileName = LogConfig.sDiskCachePath + File.separator + sLogFile;
        mLog4jConfig.setFileName(fileName);
        mLog4jConfig.setMaxFileSize(LogConfig.sMaxLogFileSize);
        mLog4jConfig.configure(mLogger);
    }

    void v(final String tag, String msg) {
        log(VERBOSE, tag, msg);
    }

    void d(final String tag, String msg) {
        log(DEBUG, tag, msg);
    }

    void i(final String tag, String msg) {
        log(INFO, tag, msg);
    }

    void w(final String tag, String msg) {
        log(WARN, tag, msg);
    }

    void e(final String tag, String msg) {
        log(ERROR, tag, msg);
    }

    void e(final String tag, final String msg, final Exception e) {
        if (ERROR >= LogConfig.sPrintLogLevel || isLoggable(Log.ERROR)) {
            Log.e(tag, msg, e);
            if (LogConfig.sIsSave2Disk) {
                WorkerThread.getInstance().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        LogConfig.checkInit();
                        mLogger.error(tag + ": " + msg, e);
                    }
                });
            }
        }
    }

    String mergeLog4jFiles(Context context, String dirPath, String fileName) {
        return mLog4jConfig.mergeLog4jFiles(context, dirPath, fileName);
    }



    private void log(final int level, final String tag, final String msg) {
        if (level >= LogConfig.sPrintLogLevel || isLoggable(level)) {
            final String wMsg = wrapperMsg(msg);
            Log.println(level, tag, wMsg);
            if (LogConfig.sIsSave2Disk) {
                WorkerThread.getInstance().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        LogConfig.checkInit();
                        switch (level) {
                            case VERBOSE:
                            case DEBUG:
                                mLogger.debug(tag + ": " + wMsg);
                                break;
                            case INFO:
                                mLogger.info(tag + ": " + wMsg);
                                break;
                            case WARN:
                                mLogger.warn(tag + ": " + wMsg);
                                break;
                            case ERROR:
                                mLogger.error(tag + ": " + wMsg);
                                break;
                            default:
                                mLogger.debug(tag + ": " + wMsg);
                                break;
                        }

                    }
                });
            }
        }
    }

    private boolean isLoggable(int level) {
        if (mIsLoggable) {
            return true;
        }
        if (mIsDebugLoggable && level >= Log.DEBUG) {
            return true;
        }
        return false;
    }

    private String wrapperMsg(String msg) {
        if (LogConfig.sIsAutoWrapperMsg) {
            final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            StackTraceElement targetElement = stackTrace[4];
            return msg + "(" + targetElement.getFileName() + ":" + targetElement.getLineNumber() + ")";
        }
        return msg;
    }
}
