package com.dasu.blur;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by suxq on 2018/10/10.
 */

public class BlurConfig {

    public static final int MODE_RS = 0;     //RenderScript 模糊
    public static final int MODE_NATIVE = 1; //C层算法模糊
    public static final int MODE_FAST = 2;   //Java层算法1模糊
    public static final int MODE_STACK = 3;  //Java层算法2模糊

    static final int SOURCE_INVALID = -1;
    static final int SOURCE_ACTIVITY = 1;
    static final int SOURCE_VIEW = 2;
    static final int SOURCE_BITMAP = 3;

    private static final int DEFAULT_RADIUS = 10;
    private static final int DEFAULT_SAMPLING = 3;

    /**
     * 待模糊的 bitmap
     */
    WeakReference<Bitmap> bitmap;

    /**
     * 高斯模糊计算半径，半径越大越模糊
     */
    int radius = DEFAULT_RADIUS;
    /**
     * 原图缩小比例，3：表示长宽缩小3倍
     */
    int sampling = DEFAULT_SAMPLING;

    /**
     * 是否缓存
     */
    boolean cache = false;

    /**
     * 缓存的key
     */
    String cacheKey;

    /**
     * 模糊后作用的view
     */
    WeakReference<View> targetView;

    WeakReference<Context> context;

    OnBlurListener onBlurListener;

    /**
     * 渐进动画
     */
    boolean animAlpha = false;
    int animDuration = 300;

    int mode = MODE_RS;

    int sourceType = SOURCE_INVALID;
    WeakReference<Object> source;

    int width;
    int height;


    BlurConfig(int sourceType) {
        this.sourceType = sourceType;
    }

    public void doBlur() {
        doBlur(null);
    }

    public void doBlur(OnBlurListener listener) {
        BlurHelper.doBlur(this, listener);
    }

    public Bitmap doBlurSync() {
        return BlurHelper.doBlurSync(this);
    }

    public static class BlurConfigBuilder {

        private BlurConfig mBlurConfig;


        public BlurConfigBuilder(@NonNull View view) {
            mBlurConfig = new BlurConfig(SOURCE_VIEW);
            mBlurConfig.context = new WeakReference<Context>(view.getContext().getApplicationContext());
            mBlurConfig.source = new WeakReference<Object>(view);
            mBlurConfig.width = view.getMeasuredWidth();
            mBlurConfig.height = view.getMeasuredHeight();
        }

        public BlurConfigBuilder(@NonNull Context context, @NonNull Bitmap bitmap) {
            mBlurConfig = new BlurConfig(SOURCE_BITMAP);
            mBlurConfig.context = new WeakReference<Context>(context.getApplicationContext());
            mBlurConfig.source = new WeakReference<Object>(bitmap);
            mBlurConfig.width = bitmap.getWidth();
            mBlurConfig.height = bitmap.getHeight();
        }

        public BlurConfigBuilder(@NonNull Activity activity) {
            mBlurConfig = new BlurConfig(SOURCE_ACTIVITY);
            mBlurConfig.context = new WeakReference<Context>(activity.getApplicationContext());
            mBlurConfig.source = new WeakReference<Object>(activity);
            View view = activity.getWindow().getDecorView().getRootView();
            mBlurConfig.width = view.getMeasuredWidth();
            mBlurConfig.height = view.getMeasuredHeight();
        }

        public BlurConfigBuilder(@NonNull Context context, @DrawableRes final int resId) {
            mBlurConfig = new BlurConfig(SOURCE_BITMAP);
            mBlurConfig.context = new WeakReference<Context>(context.getApplicationContext());
            Bitmap bitmap = BlurHelper.getBitmap(context, resId);
            mBlurConfig.source = new WeakReference<Object>(bitmap);
            mBlurConfig.width = bitmap.getWidth();
            mBlurConfig.height = bitmap.getHeight();
        }

        public BlurConfigBuilder radius(int radius) {
            mBlurConfig.radius = radius;
            return this;
        }

        public BlurConfigBuilder sampling(int sampling) {
            mBlurConfig.sampling = sampling;
            return this;
        }

        public BlurConfigBuilder cache(String cacheKey) {
            mBlurConfig.cache = true;
            mBlurConfig.cacheKey = cacheKey;
            return this;
        }

        public BlurConfigBuilder animAlpha(int duration) {
            mBlurConfig.animAlpha = true;
            mBlurConfig.animDuration = duration;
            return this;
        }

        public BlurConfigBuilder animAlpha() {
            mBlurConfig.animAlpha = true;
            return this;
        }

        public BlurConfigBuilder mode(int mode) {
            mBlurConfig.mode = mode;
            return this;
        }

        public BlurConfigBuilder intoTarget(View targetView) {
            mBlurConfig.targetView = new WeakReference<View>(targetView);
            return this;
        }

        public BlurConfig build() {
            checkConfigValid();
            return mBlurConfig;
        }

        private void checkConfigValid() {
            if (mBlurConfig.width <= 0 || mBlurConfig.height <= 0) {
                throw new UnSupportBlurConfig("source width and height must be > 0");
            }
            switch (mBlurConfig.mode) {
                case MODE_FAST:
                case MODE_NATIVE:
                case MODE_STACK:
                case MODE_RS:
                    break;
                default:
                    throw new UnSupportBlurConfig("unknown mode: " + mBlurConfig.mode + ", u can go to BlurConfig check valid mode.");
            }
        }
    }

    public static class UnSupportBlurConfig extends RuntimeException {
        public UnSupportBlurConfig(String message) {
            super(message);
        }
    }
}
