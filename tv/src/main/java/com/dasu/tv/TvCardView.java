package com.dasu.tv;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Tv 上常见的卡片View
 * Created by suxq on 2018/7/10.
 */

public class TvCardView extends FrameLayout {



    public TvCardView(@NonNull Context context) {
        super(context);
    }

    public TvCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TvCardView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
