package com.dasu.basemodule.wams;

import android.os.SystemClock;
import android.util.Log;

import com.dasu.volley.DVolley;
import com.dasu.volley.VolleyListener;
import com.google.gson.Gson;

/**
 * Created by dasu on 2018/10/26.
 * 微信公众号：dasuAndroidTv
 * https://github.com/woshidasusu/DWanAndroid
 */
class HomeRequest {

    public static void getArticleList(int page, Object tag, final VolleyListener<HomeArticlesResEntity> volleyListener) {

               DVolley.url(WamsParams.apiArticleList(page))
                .get()
                .tag(tag)
                .enqueue(new VolleyListener<WamsResEntity<Object>>() {
                    @Override
                    public void onSuccess(WamsResEntity<Object> data) {
                        long time = SystemClock.uptimeMillis();
                        Gson gson = new Gson();
                        
                        Log.e("!!!!!!!!", "1time: " + (SystemClock.uptimeMillis() - time) + "ms");
                    }

                    @Override
                    public void onError(int code, String description) {

                    }
                });
    }

    public static void getBanner(Object tag, VolleyListener<BannerResEntity> volleyListener) {
        DVolley.url(WamsParams.apiBanner())
                .get()
                .tag(tag)
                .enqueue(volleyListener);
    }
}
