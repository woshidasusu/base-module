package com.dasu.utils;

import android.text.TextUtils;

/**
 * Created by suxq on 2018/5/5.
 */

public class LogUtils {
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;

    public static int LEVEL = VERBOSE;
    public static boolean logVerbose = false;
    public static boolean isAutoLogClassAndMethod = false;
    public static boolean isAutoLogLineNumber = true;

    public static void v(String tag, String msg) {
        if (logVerbose) {
            logInternal(VERBOSE, tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG >= LEVEL) {
            logInternal(DEBUG, tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (INFO >= LEVEL) {
            logInternal(INFO, tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (WARN >= LEVEL) {
            logInternal(WARN, tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (ERROR >= LEVEL) {
            logInternal(ERROR, tag, msg);
        }
    }

    private static void logInternal(int type, String tag, String msg) {
        if (isAutoLogClassAndMethod || isAutoLogLineNumber) {
            String methodName = "";
            String className = "";
            int lineNumber = 0;
            try {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                StackTraceElement targetElement = stackTrace[4];
                className = targetElement.getFileName().replace(".java", "");
                methodName = targetElement.getMethodName();
                lineNumber = targetElement.getLineNumber();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isAutoLogClassAndMethod) {
                String innerTag = "";
                if (!TextUtils.isEmpty(methodName)) {
                    innerTag = className + "#" + methodName + "()#line-" + lineNumber;
                }
                switch (type) {
                    case VERBOSE:
                        android.util.Log.v("verbose/" + innerTag, tag + ": " + msg);
                        break;
                    case DEBUG:
                        android.util.Log.d(innerTag, tag + ": " + msg);
                        break;
                    case INFO:
                        android.util.Log.i(innerTag, tag + ": " + msg);
                        break;
                    case WARN:
                        android.util.Log.w(innerTag, tag + ": " + msg);
                        break;
                    case ERROR:
                        android.util.Log.e(innerTag, tag + ": " + msg);
                        break;
                    default:
                        break;
                }
            } else if (isAutoLogLineNumber) {
                switch (type) {
                    case VERBOSE:
                        android.util.Log.v(tag + "#" + lineNumber , msg);
                        break;
                    case DEBUG:
                        android.util.Log.d(tag + "#" + lineNumber, msg);
                        break;
                    case INFO:
                        android.util.Log.i(tag + "#" + lineNumber, msg);
                        break;
                    case WARN:
                        android.util.Log.w(tag + "#" + lineNumber, msg);
                        break;
                    case ERROR:
                        android.util.Log.e(tag + "#" + lineNumber, msg);
                        break;
                    default:
                        break;
                }
            }
        } else {
            switch (type) {
                case VERBOSE:
                    android.util.Log.v(tag, msg);
                    break;
                case DEBUG:
                    android.util.Log.d(tag, msg);
                    break;
                case INFO:
                    android.util.Log.i(tag, msg);
                    break;
                case WARN:
                    android.util.Log.w(tag, msg);
                    break;
                case ERROR:
                    android.util.Log.e(tag, msg);
                    break;
                default:
                    break;
            }
        }
    }
}
