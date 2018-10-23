package com.dasu.volley;

import com.android.volley.Request;
import com.dasu.volley.wrapper.ImageFileRequest;
import com.dasu.volley.wrapper.VolleyManager;
import com.dasu.volley.wrapper.VolleyRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dasu on 2018/10/22.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 */
public class VolleyController {

    public static class MethodStep {
        protected VolleyRequest mVolleyRequest;
        private String mUrl;

        public MethodStep(String url) {
            mUrl = url;
            mVolleyRequest = new VolleyRequest(url);
        }

        /**
         * 发起一个 post 请求
         * @return
         */
        public MainStep post() {
            mVolleyRequest.setMethod(Request.Method.POST);
            return new MainStep(mVolleyRequest);
        }

        /**
         * 发起一个 get 请求
         * @return
         */
        public MainStep get() {
            mVolleyRequest.setMethod(Request.Method.GET);
            return new MainStep(mVolleyRequest);
        }

        /**
         * 下载图片文件
         * @return
         */
        public ImageFileStep asImageFile() {
            return new ImageFileStep(mUrl);
        }
    }

    public static class MainStep {
        protected VolleyRequest mVolleyRequest;

        public MainStep(VolleyRequest volleyRequest) {
            mVolleyRequest = volleyRequest;
        }

        public MainStep addParam(String key, String value) {
            mVolleyRequest.addParam(key, value);
            return this;
        }

        public MainStep addParams(HashMap<String, String> params) {
            mVolleyRequest.addParams(params);
            return this;
        }

        public MainStep addHeader(String key, String value) {
            mVolleyRequest.addHeader(key, value);
            return this;
        }

        public MainStep addHeaders(HashMap<String, String> headers) {
            mVolleyRequest.addHeaders(headers);
            return this;
        }

        public MainStep tag(Object tag) {
            mVolleyRequest.setTag(tag);
            return this;
        }

        /**
         * 加入队列中，等待执行
         * @param listener
         * @param <T>
         */
        public <T> void enqueue(VolleyListener<T> listener) {
            mVolleyRequest.createRequest(listener);
        }
    }

    public static class ImageFileStep {
        private ImageFileRequest mImageFileRequest;
        public ImageFileStep(String url) {
            mImageFileRequest = new ImageFileRequest(url);
        }

        public void downloadTo(File imageFile) {
            downloadTo(imageFile, null);
        }

        /**
         * 将图片下载到本地
         * @param imageFile
         */
        public void downloadTo(File imageFile, VolleyListener<? extends String> listener) {
            mImageFileRequest.setFilePath(imageFile.getAbsolutePath());
            mImageFileRequest.download(listener);
        }
    }

    public static class GlobalConfigStep {
        public GlobalConfigStep globalParam(String key, String value) {
            VolleyManager.addParam(key, value);
            return this;
        }

        public GlobalConfigStep globalParams(Map<String, String> params) {
            VolleyManager.addParams(params);
            return this;
        }

        public GlobalConfigStep globalHeader(String key, String value) {
            VolleyManager.addHeader(key, value);
            return this;
        }

        public GlobalConfigStep globalHeaders(Map<String, String> headers) {
            VolleyManager.addHeaders(headers);
            return this;
        }
    }

}
