package com.dasu.blur;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.dasu.blur.process.ThreadManager;

import java.lang.ref.WeakReference;

/**
 * Created by suxq on 2018/10/10.
 */

class BlurHelper {
    private static LruCache<String, WeakReference<Bitmap>> sBitmapCaches = new LruCache<>(5);
    private static Handler sUiHandler = new Handler(Looper.getMainLooper());

    static void doBlur(final BlurConfig blurConfig, final OnBlurListener listener) {
        ThreadManager.execTask(new Runnable() {
            @Override
            public void run() {
                calculateSourceBitmap(blurConfig);
                final BlurTask blurTask = new BlurTask(blurConfig, new OnBlurListener() {
                    @Override
                    public void onBlurSuccess(final Bitmap bitmap) {
                        sUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                autoHandleSuccess(bitmap, blurConfig);
                                if (listener != null) {
                                    listener.onBlurSuccess(bitmap);
                                }
                            }
                        });
                    }

                    @Override
                    public void onBlurFailed() {
                        sUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.onBlurFailed();
                                }
                            }
                        });
                    }
                });
                ThreadManager.execTask(blurTask);
            }
        });
    }

    private static void autoHandleSuccess(final Bitmap bitmap, final BlurConfig blurConfig) {
        if (blurConfig.cache && !TextUtils.isEmpty(blurConfig.cacheKey)) {
            addCacheBitmap(blurConfig.cacheKey, bitmap);
        }
        if (blurConfig.targetView != null && blurConfig.targetView.get() != null) {
            View target = blurConfig.targetView.get();
            if (target instanceof ImageView) {
                ((ImageView)target).setImageBitmap(bitmap);
            } else {
                Drawable drawable = new BitmapDrawable(target.getContext().getResources(), bitmap);
                target.setBackground(drawable);
            }
            if (blurConfig.animAlpha) {
                animate(target, blurConfig.animDuration);
            }
        }
    }

    static Bitmap doBlurSync(final BlurConfig blurConfig) {
        calculateSourceBitmap(blurConfig);
        BlurTask blurTask = new BlurTask(blurConfig, null);
        Bitmap bitmap = blurTask.exec();
        autoHandleSuccess(bitmap, blurConfig);
        return bitmap;
    }

    private static void calculateSourceBitmap(BlurConfig blurConfig) {
        Bitmap bitmap = null;
        if (blurConfig.source != null && blurConfig.source.get() != null) {
            switch (blurConfig.sourceType) {
                case BlurConfig.SOURCE_ACTIVITY:
                    bitmap = getViewBitmap(((Activity)blurConfig.source.get()).getWindow().getDecorView().getRootView());
                    break;
                case BlurConfig.SOURCE_VIEW:
                    bitmap = getViewBitmap((View)blurConfig.source.get());
                    break;
                case BlurConfig.SOURCE_BITMAP:
                    bitmap = (Bitmap)blurConfig.source.get();
                    break;
            }
        }
        if (bitmap != null) {
            blurConfig.bitmap = new WeakReference<Bitmap>(bitmap);
        }
    }

    private static Bitmap getViewBitmap(View view) {
        long startTime = SystemClock.uptimeMillis();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        long endTime = SystemClock.uptimeMillis();
        Log.w("DBlur","BlurHelper call view.draw() for get view's bitmap, cast " + (endTime - startTime) + "ms");
        return bitmap;
    }

    private static void animate(View v, int duration) {
        AlphaAnimation alpha = new AlphaAnimation(0f, 1f);
        alpha.setDuration(duration);
        v.startAnimation(alpha);
    }

    static Bitmap getCacheBitmap(String cacheKey) {
        WeakReference<Bitmap> weakReference = sBitmapCaches.get(cacheKey);
        if (weakReference != null && weakReference.get() != null) {
            Bitmap bitmap = weakReference.get();
            sBitmapCaches.remove(cacheKey);
            return bitmap;
        }
        return null;
    }

    static void addCacheBitmap(String cacheKey, Bitmap bitmap) {
        if (sBitmapCaches.get(cacheKey) == null) {
            WeakReference<Bitmap> weakReference = new WeakReference<Bitmap>(bitmap);
            sBitmapCaches.put(cacheKey, weakReference);
        }
    }

    static Bitmap getBitmap(@NonNull Context context, @DrawableRes final int resId) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
