package com.dasu.log;

import android.content.Context;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Runtime.getRuntime;


/**
 * Created by suxq on 2018/9/11.
 */

class LogcatHelper {
    static String sLogcatFile = "logcat.log";
    private Logger mLogger;
    private Log4jConfig mLog4jConfig;

    private boolean mIsStart = false;
    private ConcurrentHashMap<Integer, Process> mProcessMap = new ConcurrentHashMap<>();

    void initLog4j() {
        if (mLogger == null) {
            mLogger = Logger.getLogger("logcat");
            String fileName = LogConfig.sDiskCachePath + File.separator + sLogcatFile;
            mLog4jConfig = new Log4jConfig(fileName);
            mLog4jConfig.setFilePattern("[running(%rms)] %m%n");
            mLog4jConfig.setMaxFileSize(LogConfig.sMaxLogFileSize);
            mLog4jConfig.configure(mLogger);
        }
    }

    void updateLog4jConfig() {
        String fileName = LogConfig.sDiskCachePath + File.separator + sLogcatFile;
        mLog4jConfig.setFileName(fileName);
        mLog4jConfig.setMaxFileSize(LogConfig.sMaxLogFileSize);
        mLog4jConfig.configure(mLogger);
    }

    void start() {
        LogThread.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (LogConfig.sIsPrintDebugLog) {
                    LogUtils.d(LogConstValue.LOG_TAG, "LogcatHelper > start() mIsStart =" + mIsStart + ", mProcessMap.size()=" + mProcessMap.size() + ", logcatEnable=" + LogConfig.sIsLogcatEnable);
                }
                //确保只有1个logcat进程启动
                if (mIsStart || mProcessMap.size() >= 1 || !LogConfig.sIsLogcatEnable) {
                    if (LogConfig.sIsPrintDebugLog) {
                        LogUtils.d(LogConstValue.LOG_TAG, "LogcatHelper > start fail, check your LogConfig");
                    }
                    return;
                }
                mIsStart = true;
                LogcatCmd logcatCmd = new LogcatCmd();
                logcatCmd.start();
            }
        });
    }

    void stop() {
        LogThread.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {
                mIsStart = false;
            }
        });
    }

    void cleanup() {
        stop();
        LogThread.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {
                if(mProcessMap != null && mProcessMap.size() > 0) {
                    Set<Integer> keySet = mProcessMap.keySet();
                    for (Integer key : keySet) {
                        if (key != null && mProcessMap.get(key) != null) {
                            mProcessMap.get(key).destroy();
                        }
                    }
                    mProcessMap.clear();
                }
            }
        });
    }

    String mergeLog4jFiles(Context context, String dirPath, String fileName) {
        return mLog4jConfig.mergeLog4jFiles(context, dirPath, fileName);
    }

    private class LogcatCmd extends Thread {

        public void run() {
            Process process = null;
            InputStreamReader inputReader = null;
            BufferedReader bufferedReader = null;
            try {
                Process clearP = getRuntime().exec("logcat -c");
                clearP.waitFor();
                Process checkCacheP = getRuntime().exec("logcat -g");
                byte[] out = new byte[255];
                checkCacheP.getInputStream().read(out);
                if (LogConfig.sIsPrintDebugLog) {
                    LogUtils.d(LogConstValue.LOG_TAG, "logcat -g: ");
                    LogUtils.d(LogConstValue.LOG_TAG, new String(out));
                }
                checkCacheP.waitFor();
                process = getRuntime().exec("logcat -v time");
                mProcessMap.put(process.hashCode(), process);
                inputReader = new InputStreamReader(process.getInputStream());
                bufferedReader = new BufferedReader(inputReader, 1024);
                if (LogConfig.sIsPrintDebugLog) {
                    LogUtils.d(LogConstValue.LOG_TAG, "start shell cmd: logcat -v time...");
                }
                String line;
                while (mIsStart && (line = bufferedReader.readLine()) != null) {
                    final String log = line;
                    LogThread.getInstance().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            mLogger.debug(log);
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e(LogConstValue.LOG_TAG, "LogcatHelper > LogcatCmd Runtime.getRuntime().exec() error", e);
            } catch (InterruptedException e) {
                e.printStackTrace();
                LogUtils.e(LogConstValue.LOG_TAG, "LogcatHelper > LogcatCmd Runtime.getRuntime().exec() error", e);
            } finally {
                try{
                    if(inputReader != null) {
                        inputReader.close();
                    }
                    if(bufferedReader != null) {
                        bufferedReader.close();
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }

                if(process != null){
                    if (LogConfig.sIsPrintDebugLog) {
                        LogUtils.d(LogConstValue.LOG_TAG, "stop shell cmd: logcat -v time...");
                    }
                    process.destroy();
                    mProcessMap.remove(process.hashCode());
                }
            }
        }
    }
}
