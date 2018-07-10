package com.dasu.tv.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * .9 图裁剪，裁掉周围透明区域
 * Created by suxq on 2018/4/28.
 */

public class CutNineDrawable extends Drawable {
    private Drawable mScaleFocusDrawable = null;

    public CutNineDrawable(Context context, int drawableId) {
        mScaleFocusDrawable = context.getResources().getDrawable(drawableId);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect padding = new Rect();
        Rect canvasRect = new Rect();
        Rect drawableRect = new Rect();
        mScaleFocusDrawable.getPadding(padding);
        canvas.getClipBounds(canvasRect);

        drawableRect.left = canvasRect.left - padding.left;
        drawableRect.top = canvasRect.top - padding.top;
        drawableRect.right = canvasRect.right + padding.right;
        drawableRect.bottom = canvasRect.bottom + padding.bottom;

        mScaleFocusDrawable.setBounds(drawableRect);
        mScaleFocusDrawable.draw(canvas);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
