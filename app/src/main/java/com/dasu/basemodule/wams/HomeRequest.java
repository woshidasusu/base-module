package com.dasu.basemodule.wams;

import com.dasu.volley.DVolley;
import com.dasu.volley.VolleyListener;

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
                .responseInterceptor(new WamsResponseInterceptor())
                .enqueue(volleyListener);
    }

    public static void getBanner(Object tag, VolleyListener<BannerResEntity> volleyListener) {
        DVolley.url(WamsParams.apiBanner())
                .get()
                .tag(tag)
                .enqueue(volleyListener);
    }
}
