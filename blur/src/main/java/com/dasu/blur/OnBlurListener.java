package com.dasu.blur;

import android.graphics.Bitmap;

/**
 * Created by dasu on 2018/10/10.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 * 高斯模糊回调
 */

public interface OnBlurListener {

    void onBlurSuccess(Bitmap bitmap);

    void onBlurFailed();
}
