package com.dasu.blur;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.dasu.blur.process.BlurProcess;
import com.dasu.blur.process.JavaBlurProcess;
import com.dasu.blur.process.NativeBlurProcess;
import com.dasu.blur.process.RSBlurProcess;
import com.dasu.blur.process.StackBlurProcess;

import java.lang.ref.WeakReference;

/**
 * Created by suxq on 2018/10/10.
 */

class BlurTask implements Runnable {

    private BlurConfig mBlurConfig;
    private OnBlurListener mBlurListener;

    public BlurTask(BlurConfig blurConfig, OnBlurListener listener) {
        mBlurConfig = blurConfig;
        mBlurListener = listener;
    }

    @Override
    public void run() {
        try {
            Bitmap bitmap = exec();
            if (bitmap != null) {
                if (mBlurListener != null) {
                    mBlurListener.onBlurSuccess(bitmap);
                }
            } else {
                if (mBlurListener != null) {
                    mBlurListener.onBlurFailed();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (mBlurListener != null) {
                mBlurListener.onBlurFailed();
            }
        }
    }

    public Bitmap exec() {
        long time = SystemClock.uptimeMillis();
        if (isEmptyBitmap(mBlurConfig.bitmap)) {
            Log.e("DBlur", "BlurTask doBlur error, cause bitmap is empty");
            return null;
        }
        
        Bitmap source = mBlurConfig.bitmap.get();
        
        int width = mBlurConfig.width / mBlurConfig.sampling;
        int height = mBlurConfig.height / mBlurConfig.sampling;

        if (hasZero(width, height)) {
            Log.e("DBlur", "doBlur error, cause mBlurConfig.width/height is 0");
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / (float) mBlurConfig.sampling, 1 / (float) mBlurConfig.sampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
        PorterDuffColorFilter filter =
                new PorterDuffColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
        paint.setColorFilter(filter);
        canvas.drawBitmap(source, 0, 0, paint);

        BlurProcess blurProcess;
        switch (mBlurConfig.mode) {
            case BlurConfig.MODE_FAST:
                blurProcess = new JavaBlurProcess();
                Log.w("DBlur", "BlurTask begin blur, u choose the mode: MODE_FAST");
                break;
            case BlurConfig.MODE_NATIVE:
                blurProcess = new NativeBlurProcess();
                Log.w("DBlur", "BlurTask begin blur, u choose the mode: MODE_NATIVE");
                break;
            case BlurConfig.MODE_STACK:
                blurProcess = new StackBlurProcess();
                Log.w("DBlur", "BlurTask begin blur, u choose the mode: MODE_STACK");
                break;
            case BlurConfig.MODE_RS:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                        && mBlurConfig.context != null
                        && mBlurConfig.context.get() != null) {
                    blurProcess = new RSBlurProcess(mBlurConfig.context.get());
                    Log.w("DBlur", "BlurTask begin blur, u choose the mode: MODE_RS");
                } else {
                    blurProcess = new StackBlurProcess();
                    Log.w("DBlur", "BlurTask begin blur, u choose the mode: MODE_RS, but api < 18 or context is null, so change ues MODE_STACK");
                }
                break;
            default:
                blurProcess = new StackBlurProcess();
                Log.w("DBlur", "BlurTask begin blur, u choose the default mode: MODE_STACK");
                break;
        }

        bitmap = blurProcess.blur(bitmap, mBlurConfig.radius);


        if (mBlurConfig.sampling == 1) {
            Log.w("DBlur", "BlurTask finish blur, cast " + (SystemClock.uptimeMillis() - time) + "ms");
            return bitmap;
        } else {
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, mBlurConfig.width, mBlurConfig.height, true);
            Log.w("DBlur", "BlurTask finish blur, cast " + (SystemClock.uptimeMillis() - time) + "ms");
            bitmap.recycle();
            bitmap = null;
            return scaled;
        }
    }

    private boolean hasZero(int... args) {
        for (int num : args) {
            if (num == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmptyBitmap(final WeakReference<Bitmap> src) {
        return src == null || src.get() == null || src.get().getWidth() == 0 || src.get().getHeight() == 0;
    }
}
