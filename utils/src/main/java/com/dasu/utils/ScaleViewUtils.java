package com.dasu.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

/**
 * Created by suxq on 2018/5/5.
 */

public class ScaleViewUtils {
    private static final float DEFAULT_SCALE_VALUE = 1.1f;
    private static final int DEFAULT_SCALE_DURATION = 300;

    public static void scaleUp(View view) {
        scaleUp(view, DEFAULT_SCALE_VALUE);
    }

    public static void scaleUp(final View view, final float scaleValue) {
        view.bringToFront();
        view.animate().scaleX(scaleValue).scaleY(scaleValue).setDuration(DEFAULT_SCALE_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        view.setScaleX(scaleValue);
                        view.setScaleY(scaleValue);
                    }
                })
                .start();
    }

    public static void scaleDown(final View view) {
        view.animate().scaleX(1).scaleY(1).setDuration(DEFAULT_SCALE_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        view.setScaleX(1);
                        view.setScaleY(1);
                    }
                })
                .start();
    }
}