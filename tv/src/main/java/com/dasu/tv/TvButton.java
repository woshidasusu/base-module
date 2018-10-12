package com.dasu.tv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by dasu on 2018/7/10.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 */

@SuppressLint("AppCompatCustomView")
public class TvButton extends Button {
    public TvButton(Context context) {
        super(context);
    }

    public TvButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TvButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
