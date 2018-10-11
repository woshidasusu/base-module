package com.dasu.blur;

import android.graphics.Bitmap;

/**
 * Created by suxq on 2018/10/10.
 */

public interface OnBlurListener {

    void onBlurSuccess(Bitmap bitmap);

    void onBlurFailed();
}
