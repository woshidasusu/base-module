package com.dasu.basemodule.wams;

import com.dasu.volley.VolleyListener;

/**
 * Created by dasu on 2018/10/26.
 * 微信公众号：dasuAndroidTv
 * https://github.com/woshidasusu/DWanAndroid
 *
 * 主页模块统一对外的接口
 */
public class HomeController {
    public static void getArticleList(int page, Object tag, VolleyListener<HomeArticlesResEntity> volleyListener) {
        HomeRequest.getArticleList(page, tag, volleyListener);
    }

    public static void getBanner(Object tag, VolleyListener<BannerResEntity> volleyListener) {
        HomeRequest.getBanner(tag, volleyListener);
    }
}
