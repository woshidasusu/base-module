package com.dasu.basemodule.wams;

/**
 * Created by dasu on 2018/10/26.
 * 微信公众号：dasuAndroidTv
 * https://github.com/woshidasusu/DWanAndroid
 */
class WamsParams {

    public static final String HOST = "http://www.wanandroid.com/";

    public static String apiArticleList(int page) {
        return HOST + "article/list/" + page + "/json";
    }

    public static String apiBanner() {
        return HOST + "banner/json";
    }
}
