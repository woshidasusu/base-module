package com.dasu.log;

import android.content.Context;

/**
 * Created by suxq on 2018/9/11.
 *
 * LogUtils日志工具类，支持以下功能：
 *      1.日志缓存到文件
 *      2.logcat抓取日志
 *      3.merge log4j生成的多份日志文件
 *==============================
 *
 * 用法示例1：单纯只用于普通日志工具类
 * <pre>
 *     LogUtils.d(TAG, "XXX");
 * </pre>
 * ps:没有调用LogUtils.init()，就只能用于普通日志工具类用途，即单纯将日志输出到控制台
 *===========================================================================
 *
 * 用法示例2：使用默认行为配置
 * <pre>
 *     LogUtils.init(context)
 *             .setInDebug(BuildConfig.DEBUG); //不调用的话，默认为debug的默认配置
 *</pre>
 * ps:debug默认配置:1.只输出全部级别日志到控制台；2.输出内部debug日志
 * release默认配置:1.输出INFO及以上级别的日志到控制台和磁盘文件中，磁盘文件默认sd/{包名}/目录下；2.不输出内部debug日志
 * =============================================================================================================================
 *
 * 用法示例3：修改默认行为配置
 *
 * <pre>
 *     LogUtils.init(context)
 *             .setInDebug(BuildConfig.DEBUG)
 *             .setIsSave2Disk(true)           //开启日志缓存到磁盘文件功能
 *             .setPrintLogLevel(Log.DEBUG)    //控制日志输出级别为DEBUG及以上
 *             .setDiskCachePath("com.dasu.log"); //修改日志文件的磁盘目录为：com.dasu.log
 *
 * ps:更多配置项参考{@link LogConfig}
 *
 * 内部实现逻辑在{@link LogHelper} 依赖于log4j 1.2x 版本
 */

public final class LogUtils {
    private static LogHelper sLogHelper = new LogHelper();

    private LogUtils() {
        throw new UnsupportedOperationException("u can't instance it");
    }

    public static LogConfig init(Context context) {
        return LogConfig.create(context);
    }

    public static void v(String tag, String msg) {
        sLogHelper.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        sLogHelper.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        sLogHelper.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        sLogHelper.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        sLogHelper.e(tag, msg);
    }

    public static void e(String tag, String msg, Exception e) {
        sLogHelper.e(tag, msg, e);
    }

    /**
     * 将 log4j 生成的多份备份文件合并在一份中
     * @param context
     * @param dirPath 存放log4j文件的目录
     * @param fileName log4j生成的日志文件名
     * @return
     */
    public static String mergeLog4jFiles(Context context, String dirPath, String fileName) {
        LogConfig.checkInit();
        return sLogHelper.mergeLog4jFiles(context, dirPath, fileName);
    }

    /**
     * 获取 logcat 通过 log4j 缓存到本地的文件名
     * @return
     */
    public static String getLog4jFileName() {
        LogConfig.checkInit();
        return sLogHelper.sLogFile;
    }

    static void initLog4j() {
        sLogHelper.initLog4j();
    }

    static void updateLog4jConfig() {
        sLogHelper.updateLog4jConfig();
    }
}
