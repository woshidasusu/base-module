package com.dasu.log;

import android.content.Context;

/**
 * Created by suxq on 2018/9/11.
 *
 * Logcat工具类，执行shell命令：logcat -v time 采集日志
 * 因为涉及到磁盘读写文件，因此使用前需先初始化并且手动开启该功能（默认关闭）
 *
 * 用法示例：
 * <pre>
 *     LogUtils.init(context).setLogcatEnable(true);
 *
 *     LogcatUtils.start();//开始采集
 * </pre>
 *
 * 内部具体实现逻辑在{@link LogcatHelper}
 */
public final class LogcatUtils {

    private static LogcatHelper sLogcatHelper = new LogcatHelper();

    private LogcatUtils() {
        throw new UnsupportedOperationException("u can't instance it");
    }

    static {
        LogConfig.checkInit();
    }

    /**
     * 开始执行 logcat 命令，采集日志，功能默认关闭，如果要启用，需要在{@link LogConfig#setLogcatEnable(boolean)}开启该功能，否则即使调用了该方法也无效
     */
    public static void start() {
        sLogcatHelper.start();
    }

    /**
     * 停止采集
     */
    public static void stop() {
        sLogcatHelper.stop();
    }

    /**
     * kill掉logcat进程
     */
    public static void cleanup() {
        sLogcatHelper.cleanup();
    }

    /**
     * 将 log4j 生成的多份备份文件合并在一份中
     * @param context
     * @param dirPath 存放log4j文件的目录
     * @param fileName log4j生成的日志文件名
     * @return
     */
    public static String mergeLog4jFiles(Context context, String dirPath, String fileName) {
        return sLogcatHelper.mergeLog4jFiles(context, dirPath, fileName);
    }

    /**
     * 获取 logcat 通过 log4j 缓存到本地的文件名
     * @return
     */
    public static String getLog4jFileName() {
        return sLogcatHelper.sLogcatFile;
    }

    static void initLog4j() {
        sLogcatHelper.initLog4j();
    }

    static void updateLog4jConfig() {
        sLogcatHelper.updateLog4jConfig();
    }
}
