package com.dasu.utils;

import android.content.Context;
import android.os.Build;


/**
 * Created by dasu on 2018/5/14.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 */

public class DeviceUtils {
    private static final String TAG = "DeviceUtils";

    public static String getDeviceInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("device=").append(Build.DEVICE);
        sb.append(", model=").append(Build.MODEL);
        sb.append(", board=").append(Build.BOARD);
        sb.append(", brand=").append(Build.BRAND);
        sb.append(", product=").append(Build.PRODUCT);
        sb.append(", manufacturer=").append(Build.MANUFACTURER);
        sb.append(", androidOs=").append(Build.VERSION.RELEASE);
        sb.append(", androidApi=").append(Build.VERSION.SDK_INT);
        sb.append(", cpuAbi=").append(Build.CPU_ABI);
        sb.append(", cpuAbi2=").append(Build.CPU_ABI2);
        sb.append(", screenWidth=").append(ScreenUtils.getScreenWidth(context));
        sb.append(", screenHeight=").append(ScreenUtils.getScreenHeight(context));
        sb.append(", dpi=").append(context.getResources().getDisplayMetrics().densityDpi);
        sb.append(", 1dp=").append(context.getResources().getDisplayMetrics().density).append("px");
        return sb.toString();
    }
}
