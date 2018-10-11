package com.dasu.blur;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;

public class DBlur {

    public static BlurConfig.BlurConfigBuilder source(@NonNull Activity activity) {
        return new BlurConfig.BlurConfigBuilder(activity);
    }

    public static BlurConfig.BlurConfigBuilder source(@NonNull Context context, @NonNull Bitmap bitmap) {
        return new BlurConfig.BlurConfigBuilder(context, bitmap);
    }

    public static BlurConfig.BlurConfigBuilder source(@NonNull View view) {
        return new BlurConfig.BlurConfigBuilder(view);
    }

    public static BlurConfig.BlurConfigBuilder source(@NonNull Context context, @DrawableRes final int resId) {
        return new BlurConfig.BlurConfigBuilder(context, resId);
    }

    public static Bitmap getCacheBitmap(String cacheKey) {
        return BlurHelper.getCacheBitmap(cacheKey);
    }
}
