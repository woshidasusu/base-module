package com.dasu.fresco;

/**
 * Created by dasu on 2018/10/19.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 * DFresco 配置文件可在 build.gradle 里写脚本生成
 * 然后在 build.gradle 中通过 buildConfigFiled 来设置配置项
 * 具体脚本可参考{@link PropertiesFile}
 */
public final class FrescoConstValue {
    /**
     * 配置文件名
     */
    public static final String BUILD_CONFIG_FILE = "build.properties";

    /**
     * 配置是否输出 fresco 的内部日志
     */
    public static final String IS_PRINT_FRESCO_LOG = "IS_PRINT_FRESCO_LOG";
    /**
     * 当上述开关打开时，配置日志输出的级别
     */
    public static final String FRESCO_LOG_LEVEL = "FRESCO_LOG_LEVEL";
}
