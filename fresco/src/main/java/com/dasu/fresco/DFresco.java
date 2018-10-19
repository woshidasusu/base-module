package com.dasu.fresco;

import android.content.Context;
import android.net.Uri;

import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

/**
 * Created by dasu on 2018/10/19.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 * DFresco 组件访问入口
 */
public final class DFresco {
    private static FrescoHelper sFrescoHelper = new FrescoHelper();
    private static boolean sIsInit = false;

    /**
     * 使用默认的初始化配置
     * @param context
     */
    public static void init(Context context) {
        sIsInit = true;
        init(context, null);
    }

    /**
     * 自定义初始化配置
     * @param context
     * @param config
     */
    public static void init(Context context, ImagePipelineConfig config) {
        sIsInit = true;
        sFrescoHelper.init(context, config);
    }

    /**
     * 加载 res 内的资源图片
     * @param context
     * @param resId
     * @return
     */
    public static FrescoController.LoadStep source(Context context, int resId) {
        checkInit();
        return new FrescoController.LoadStep(context, resId);
    }

    /**
     * 加载磁盘中的图片
     * @param localFile
     * @return
     */
    public static FrescoController.LoadStep source(File localFile) {
        checkInit();
        return new FrescoController.LoadStep(localFile);
    }

    /**
     * 加载网络中的图片
     * @param url
     * @return
     */
    public static FrescoController.DownloadStep source(String url) {
        checkInit();
        return new FrescoController.DownloadStep(url);
    }

    public static FrescoController.LoadStep source(Uri uri) {
        checkInit();
        return new FrescoController.LoadStep(uri);
    }

    private static void checkInit() {
        if (!sIsInit) {
            throw new UnsupportedOperationException("before use fresco, u should call DFrsco.init().");
        }
    }
}
