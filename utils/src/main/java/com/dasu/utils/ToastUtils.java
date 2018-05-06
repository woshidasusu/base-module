package com.dasu.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by suxq on 2018/5/5.
 */

public class ToastUtils {
    private static Toast sToast = null;

    public static void show(Context context, String text) {
        showText(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String text, int duration) {
        showText(context, text, duration);
    }

    public static void show(Context context, int textId) {
        showText(context, context.getText(textId).toString(), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int textId, int duration) {
        showText(context, context.getText(textId).toString(), duration);
    }

    private static void showText(Context context, String text, int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), text, duration);
        }
        sToast.setText(text);
        sToast.setDuration(duration);
        sToast.show();
    }
}
