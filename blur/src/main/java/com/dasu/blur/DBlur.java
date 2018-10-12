package com.dasu.blur;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by dasu on 2018/10/10.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 * 高斯模糊组件
 * 支持不同方式的高斯模糊，调用 #source() 后，根据 AndroidStudio 的代码提示查看后续支持的配置项，
 * 或者手动到 {@link BlurConfig} 查看即可
 *
 * 使用示例：
 * <pre>
 *    DBlur.source(this, R.drawable.background).intoTarget(imageView).animAlpha().build().doBlur();
 * <pre/>
 */
public class DBlur {

    /**
     * 对传入的 Activity 的视图做模糊
     */
    public static BlurConfig.Builder source(Activity activity) {
        return new BlurConfig.Builder(activity);
    }

    /**
     * 对指定的 Bitmap 做高斯模糊
     */
    public static BlurConfig.Builder source(Context context, Bitmap bitmap) {
        return new BlurConfig.Builder(context, bitmap);
    }

    /**
     * 对指定的 View 的视图做高斯模糊
     */
    public static BlurConfig.Builder source(View view) {
        return new BlurConfig.Builder(view);
    }

    /**
     * 对本地 drawable 资源图片做高斯模糊
     */
    public static BlurConfig.Builder source(Context context, final int resId) {
        return new BlurConfig.Builder(context, resId);
    }

    /**
     * 如果有设置 {@link BlurConfig.Builder#cache(String)} 的配置的话，高斯模糊结束后会将 bitmap
     * 缓存到内存中，外部自行在需要时根据 key 值获取，但缓存容器不大，LRU 缓存。
     *
     * @param cacheKey bitmap的缓存key
     * @return bitmap 已被回收的话，返回 null
     */
    public static Bitmap getCacheBitmap(String cacheKey) {
        return BlurHelper.getCacheBitmap(cacheKey);
    }
}
