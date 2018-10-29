package com.dasu.volley;

/**
 * Created by dasu on 2018/10/26.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 * 接收到数据时的拦截预处理，拦截时机在 json 解析后，VolleyListener 回调之前，拦截运行在子线程中
 * 设计这个拦截器目的在于方便一些统一的处理工作，如解析相同的外层数据结构、判断接口访问状态、不同状态做一些相应处理等
 * 因此，实现该拦截器必须提供一个数据外层的统一结构，如：
 * {
 *     "data": T(泛型),
 *     "errorCode": 1,
 *     "errorMsg": "error"
 * }
 *
 * 如果不提供，那么 onSuccess 里会传入 null，如果提供了外层的数据结构，那么 json 将会按照这个来解析
 * 解析后，可根据需求在 onSuccess 里获取如 errorCode 等跟接口实际数据无关，表示接口访问状态的字段，做统一处理
 *
 * 如果拦截器处理后，不拦截后续流程，那么 VolleyListener 回调时，并不会将连同外层数据结构的整个实体类返回，
 * 而是只取出其中表示接口实际数据的如 data 字段的数据解析后返回，这样上层使用时，可只关注接口本身的 json 数据格式，
 * 建模时不用再拼凑外层的数据结构。
 */
public interface IResponseInterceptor<T extends ICommonResultStruct> {

    /**
     * 统一的 json 数据格式结构
     * @return
     */
    T getCommonResultStruct();

    /**
     * 请求成功，在正常通知 {@link VolleyListener#onSuccess(Object)} 回调之前，
     * 预先回调此方法，可用于统一处理一些同一服务器的底层处理。
     *
     * 此回调运行在子线程中
     * @param data
     * @return 返回 true，拦截后续VolleyListener#onSuccess的回调，返回 false，后续流程照常
     */
    boolean onSuccess(T data);

    /**
     * 请求失败，在正常通知 {@link VolleyListener#onError(int, String)} 回调之前，
     * 预先回调此方法，可用于统一处理一些同一服务器的底层处理。
     *
     * 此回调运行在子线程中
     * @param code
     * @param description
     * @return 返回 true，拦截后续VolleyListener#onSuccess的回调，返回 false，后续流程照常
     */
    boolean onError(int code, String description);
}
