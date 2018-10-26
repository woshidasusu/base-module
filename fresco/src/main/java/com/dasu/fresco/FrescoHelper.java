package com.dasu.fresco;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import com.facebook.common.internal.Supplier;
import com.facebook.common.logging.FLog;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dasu on 2018/10/19.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 * Fresco 初始化配置工作
 *
 * adb shell setprop log.tag.DFresco VERBOSE 开启 fresco 内部日志
 */
class FrescoHelper implements ComponentCallbacks2{
    static final String TAG = "DFresco";
    private boolean isVLoggable = Log.isLoggable("DFresco", Log.VERBOSE);
    private boolean isDLoggable = Log.isLoggable("DFresco", Log.DEBUG);

    public void init(Context context, ImagePipelineConfig config) {
        if(config == null) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

            ImagePipelineConfig.Builder builder = ImagePipelineConfig.newBuilder(context)
                    .setResizeAndRotateEnabledForNetwork(true)
                    .setBitmapsConfig(Bitmap.Config.ARGB_8888)
                    .setBitmapMemoryCacheParamsSupplier(new MyBitmapMemoryCacheParamsSupplier(activityManager))
                    .setDownsampleEnabled(true);

            if (isVLoggable || isDLoggable) {
                Set<RequestListener> requestListeners = new HashSet<>();
                requestListeners.add(new RequestLoggingListener());
                builder.setRequestListeners(requestListeners);
                int level = isVLoggable ? FLog.VERBOSE : FLog.DEBUG;
                FLog.setMinimumLoggingLevel(level);
            }

            config = builder.build();
        }

        context.getApplicationContext().registerComponentCallbacks(this);
        Fresco.initialize(context, config);
    }

    @Override
    public void onTrimMemory(int level) {
        Log.w(TAG, "------------onTrimMemory level = " + level + " ----------------");
        try {
            if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) { // 60：后台，内存紧张
                clearMemoryCaches();
            } else if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL) {//15:前台，内存紧张
                clearMemoryCaches();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //nothing to do
    }

    @Override
    public void onLowMemory() {
        Log.w(TAG, "--------------onLowMemory----------------");
        clearMemoryCaches();
    }

    public void clearMemoryCaches() {
        ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
    }

    private static class MyBitmapMemoryCacheParamsSupplier implements Supplier<MemoryCacheParams> {
        private static final int MAX_CACHE_ENTRIES = 56;  //内存缓存中图片的最大数量
        private static final int MAX_CACHE_ASHM_ENTRIES = 256;
        private static final int MAX_CACHE_EVICTION_SIZE = 10 * ByteConstants.MB;  //内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位
        private static final int MAX_CACHE_EVICTION_ENTRIES = 5; //内存缓存中准备清除的总图片的最大数量
        private static final int MAX_CACHE_ENTRY_SIZE = 5 * ByteConstants.MB;  //内存缓存中单个图片的最大大小，字节为单位
        private final ActivityManager mActivityManager;

        public MyBitmapMemoryCacheParamsSupplier(ActivityManager activityManager) {
            mActivityManager = activityManager;
        }

        @Override
        public MemoryCacheParams get() {
            //fresco 在 api 21 以下是将图片缓存在 ashm 中，此时不做限制，默认配置，当大于 21 版本，缓存在 java 内存堆时做如下配置
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return new MemoryCacheParams(
                        getMaxCacheSize(), //内存缓存中总图片的最大大小,以字节为单位
                        MAX_CACHE_ENTRIES,
                        MAX_CACHE_EVICTION_SIZE,
                        MAX_CACHE_EVICTION_ENTRIES,
                        MAX_CACHE_ENTRY_SIZE);
            } else {
                return new MemoryCacheParams(
                        getMaxCacheSize(),
                        MAX_CACHE_ASHM_ENTRIES,
                        Integer.MAX_VALUE,
                        Integer.MAX_VALUE,
                        Integer.MAX_VALUE);
            }
        }

        private int getMaxCacheSize() {
            final int maxMemory =
                    Math.min(mActivityManager.getMemoryClass() * ByteConstants.MB, Integer.MAX_VALUE);
            if (maxMemory < 32 * ByteConstants.MB) {
                return 4 * ByteConstants.MB;
            } else if (maxMemory < 64 * ByteConstants.MB) {
                return 6 * ByteConstants.MB;
            } else {
                // We don't want to use more ashmem on Gingerbread for now, since it doesn't respond well to
                // native memory pressure (doesn't throw exceptions, crashes app, crashes phone)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    return 8 * ByteConstants.MB;
                } else {
                    return maxMemory / 4;
                }
            }
        }
    }
}
