package com.dasu.log;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;


/**
 * Created by suxq on 2018/9/12.
 *
 * Log配置项，通过 LogUtils.init()进入配置
 * {@link #setInDebug(boolean)} 使用默认行为
 * {@link #setLogcatEnable(boolean)} 设置是否开启Logcat功能
 * {@link #setIsSave2Disk(boolean)}  设置是否开启缓存到磁盘文件功能
 * {@link #setPrintLogLevel(int)}    控制日志输出级别
 * {@link #setDiskCachePath(String)} 设置日志缓存到磁盘的目录
 * {@link #setIsAutoWrapperMsg(boolean)} 设置是否对日志信息加工
 * {@link #setMaxLogFileSize(int)}   设置日志文件最大缓存大小，单位MB
 * {@link #setIsPrintDebugLog(boolean)} 设置是否开启内部debug日志
 */

public final class LogConfig {

    static boolean sIsLogcatEnable = false;
    static boolean sIsInDebug = true;
    static boolean sIsSave2Disk = false;
    static boolean sIsAutoWrapperMsg = true;
    static boolean sIsPrintDebugLog = true;
    static String sDiskCachePath;
    static int sPrintLogLevel = LogHelper.VERBOSE;
    static int sMaxLogFileSize = 5;

    private static boolean sIsInit = false;
    private static LogConfig sInstance = new LogConfig();
    private static final long MINIMUM_DISK_SIZE = 50 * 1024 * 1024;//50M

    private LogConfig() {

    }

    static LogConfig create(Context context) {
        sIsInit = true;
        sDiskCachePath = getDefaultDiskCachePath(context.getApplicationContext());
        setDefaultConfig();
        if (sIsPrintDebugLog) {
            LogUtils.d(LogConstValue.LOG_TAG, "LogUtils init disk cache path:" + sDiskCachePath);
        }
        return sInstance;
    }

    private static void setDefaultConfig() {
        if (sIsInDebug) {
            sInstance.setIsSave2Disk(false);
            sInstance.setPrintLogLevel(LogHelper.VERBOSE);
            sInstance.setIsPrintDebugLog(true);
        } else {
            sInstance.setIsSave2Disk(true);
            sInstance.setPrintLogLevel(LogHelper.INFO);
            sInstance.setIsPrintDebugLog(false);
        }
    }

    private static String getDefaultDiskCachePath(Context context) {
        boolean permission = (PackageManager.PERMISSION_GRANTED == context.getPackageManager()
                .checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", context.getPackageName()));
        if (!permission) {
            LogUtils.e(LogConstValue.LOG_TAG, "LogUtils.init() error, u need declare android.permission.WRITE_EXTERNAL_STORAGE in AndroidManifest.xml");
        }

        if (isExternalStorageWritable() && permission) {
            String path =  Environment.getExternalStorageDirectory()+ File.separator
                    + context.getPackageName() + File.separator ;
            File dir = new File(path);
            if(!dir.exists()){
                dir.mkdirs();
            }
            if (getPathAvailableSize(path) > MINIMUM_DISK_SIZE) {
                return path;
            }
        }
        return context.getFilesDir().toString()+ File.separator;
    }

    /**
     * 设置 logcat 功能的启动与否
     */
    public LogConfig setLogcatEnable(boolean logcatEnable) {
        sIsLogcatEnable = logcatEnable;
        if (sIsLogcatEnable) {
            LogThread.getInstance().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    LogcatUtils.initLog4j();
                }
            });
        }
        return this;
    }

    /**
     * 使用默认的配置行为，debug默认只输出全部级别日志到控制台，release默认输出INFO级别及以上日志到控制台和文件中
     * @see #setDefaultConfig()
     */
    public LogConfig setInDebug(boolean inDebug) {
        sIsInDebug = inDebug;
        setDefaultConfig();
        return this;
    }

    /**
     * 控制日志的输出级别
     */
    public LogConfig setPrintLogLevel(int printLogLevel) {
        LogConfig.sPrintLogLevel = printLogLevel;
        return this;
    }

    /**
     * 设置是否将日志保存到文件
     */
    public LogConfig setIsSave2Disk(boolean save2Disk) {
        sIsSave2Disk = save2Disk;
        if (sIsSave2Disk) {
            LogThread.getInstance().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    LogUtils.initLog4j();
                }
            });
        }
        return this;
    }

    /**
     * 设置日志文件的目录，默认 sd/{包名}_file/
     */
    public LogConfig setDiskCachePath(final String path) {
        LogThread.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {
                updateDiskCachePathInternal(path);
            }
        });
        return this;
    }

    private void updateDiskCachePathInternal(String path) {
        LogConfig.sDiskCachePath = createDir(path);
        if (sIsPrintDebugLog) {
            LogUtils.d(LogConstValue.LOG_TAG, "LogUtils change disk cache path:" + sDiskCachePath);
        }
        if (sIsLogcatEnable) {
            LogcatUtils.updateLog4jConfig();
        }
        if (sIsSave2Disk) {
            LogUtils.updateLog4jConfig();
        }
    }

    /**
     * 设置是否对日志信息进行包装
     */
    public LogConfig setIsAutoWrapperMsg(boolean isAutoWrapperMsg) {
        LogConfig.sIsAutoWrapperMsg = isAutoWrapperMsg;
        return this;
    }

    /**
     * 设置是否打印内部的debug日志，日志信息可查看内部各功能的执行情况
     */
    public LogConfig setIsPrintDebugLog(boolean isPrintDebugLog) {
        LogConfig.sIsPrintDebugLog = isPrintDebugLog;
        return this;
    }

    /**
     * 设置日志文件大小限制，单位MB
     */
    public LogConfig setMaxLogFileSize(int maxLogFileSize) {
        LogConfig.sMaxLogFileSize = maxLogFileSize;
        return this;
    }

    static void checkInit() {
        if (!sIsInit) {
            throw new UnsupportedOperationException("before use the disk cache function, u should call LogUtils.init(context) first");
        }
    }

    private static String createDir(String path) {
        if (sIsPrintDebugLog) {
            LogUtils.d(LogConstValue.LOG_TAG, "LogConfig > createDir:" + path);
        }
        if (isExternalStorageWritable()) {
            String absolutePath =  Environment.getExternalStorageDirectory()+ File.separator
                    + path;
            if (!absolutePath.endsWith(File.separator)) {
                absolutePath += File.separator;
            }
            File dir = new File(absolutePath);
            if(!dir.exists()){
                dir.mkdirs();
            }
            if (getPathAvailableSize(absolutePath) > MINIMUM_DISK_SIZE) {
                return absolutePath;
            }
        }
        return sDiskCachePath;
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) &&
                Environment.getExternalStorageDirectory().canWrite();
    }

    private static long getPathAvailableSize(String path) {
        File dir = new File(path);
        if(!dir.exists()){
            return -1;
        }
        StatFs sf = new StatFs(path);
        long blockSize = 0;
        long availCount = 0;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = sf.getBlockSize();
            availCount = sf.getAvailableBlocks();
        } else {
            blockSize = sf.getBlockSizeLong();
            availCount = sf.getAvailableBlocksLong();
        }
        return blockSize * availCount;
    }
}
