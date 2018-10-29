package com.dasu.volley;

import com.dasu.volley.wrapper.VolleyManager;

/**
 * Created by dasu on 2018/10/22.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 * Volley 组件入口
 *
 * adb shell setprop log.tag.Volley VERBOSE 开启 volley 内部日志
 */
public final class DVolley {

    /**
     * 发起接口访问请求
     */
    public static VolleyController.MethodStep url(String url) {
        return new VolleyController.MethodStep(url);
    }

    /**
     * 根据 TAG 取消请求
     * @param tag
     */
    public static void cancelRequests(Object tag) {
        VolleyManager.cancelRequests(tag);
    }

    /**
     * 设置通用的 params, headers
     * @return
     */
    public static VolleyController.GlobalConfigStep enterGlobalConfig() {
        return new VolleyController.GlobalConfigStep();
    }
}
