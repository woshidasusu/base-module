package com.dasu.basemodule.wams;

import android.util.Log;

import com.dasu.volley.IResponseInterceptor;

/**
 * Created by dasu on 2018/10/26.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 */
public class WamsResponseInterceptor implements IResponseInterceptor<WamsResEntity> {

    @Override
    public WamsResEntity getCommonResultStruct() {
        return new WamsResEntity();
    }

    @Override
    public boolean onSuccess(WamsResEntity data) {
        Log.e("!!!!!!!!!!", "预处理：" + data);
        return false;
    }

    @Override
    public boolean onError(int code, String description) {
        Log.e("????????????", "" + code);
        return false;
    }
}
