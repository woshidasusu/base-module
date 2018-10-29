package com.dasu.volley;

/**
 * Created by dasu on 2018/10/26.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 * 服务端接口返回的数据的外层统一结构抽象，例如 wanandroid 的 api：
 * {
 *     "data": T(泛型),
 *     "errorCode": 1,
 *     "errorMsg": "error"
 * }
 *
 * 该接口结合 {@link IResponseInterceptor} 一起使用，可达到统一处理接口的非数据字段的逻辑处理。
 */
public interface ICommonResultStruct<T> {
    /**
     * 返回统一结构中的代表 content data 的字段
      * @return
     */
    T getData();
}
