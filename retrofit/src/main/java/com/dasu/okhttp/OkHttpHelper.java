package com.dasu.okhttp;

import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dasu on 2017/5/4.
 *
 * OkHttp提供类，用于提供OkHttpClient实例以及设置各种配置，及各种拦截器设置
 */

class OkHttpHelper {

    private final static long DEFAULT_CONNECT_TIMEOUT = 10;
    private final static long DEFAULT_WRITE_TIMEOUT = 15;
    private final static long DEFAULT_READ_TIMEOUT = 15;

    public static OkHttpClient getCacheOkHttpClient() {
        return getOkHttpClient(new CacheControlInterceptor());
    }

    public static OkHttpClient getNoCacheOkHttpClient() {
        return getOkHttpClient(new FromNetWorkControlInterceptor());
    }

    private static OkHttpClient getOkHttpClient(Interceptor cacheControl) {
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //设置超时时间
        httpClientBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
        //设置缓存
//        File httpCacheDirectory = new File(FileUtils.getExternalCacheDirectory(), "OkHttpCache");
//        httpClientBuilder.cache(new Cache(httpCacheDirectory, 100 * 1024 * 1024)); //100M
        //设置拦截器
        httpClientBuilder.addInterceptor(new LoggingInterceptor());
        httpClientBuilder.addInterceptor(cacheControl);
        httpClientBuilder.addNetworkInterceptor(cacheControl);
        return httpClientBuilder.build();
    }

    /**
     * 没有网络的情况下就从缓存中取
     * 有网络的情况则从网络获取
     */
    private static class CacheControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (true) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);
            if (true) {
                int maxAge = 60 * 60 * 2;//默认缓存两个小时
                String cacheControl = request.cacheControl().toString();
                if (TextUtils.isEmpty(cacheControl)) {
                    cacheControl = "public, max-age=" + maxAge;
                }
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", cacheControl)
                        .build();

            } else {
                int maxStale = 60 * 60 * 24 * 30;
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }

    /**
     * 强制从网络获取数据
     */
    private static class FromNetWorkControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();

            Response response = chain.proceed(request);

            return response;
        }
    }


    private static class UserAgentInterceptor implements Interceptor {
        private static final String USER_AGENT_HEADER_NAME = "User-Agent";
        private final String userAgentHeaderValue;

        UserAgentInterceptor(String userAgentHeaderValue) {
            //todo // FIXME: 2018/10/12 需要依赖support包
//            this.userAgentHeaderValue = Preconditions.checkNotNull(userAgentHeaderValue, "userAgentHeaderValue = null");
            this.userAgentHeaderValue = null;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request originalRequest = chain.request();
            final Request requestWithUserAgent = originalRequest.newBuilder()
                    .removeHeader(USER_AGENT_HEADER_NAME)
                    .addHeader(USER_AGENT_HEADER_NAME, userAgentHeaderValue)
                    .build();
            return chain.proceed(requestWithUserAgent);
        }
    }

    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
//            LogUtils.d(TAG, String.format(Locale.CHINA, "发送 request %s on %s%n%s",
//                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
//            LogUtils.d(TAG, String.format(Locale.CHINA, "收到 response for %s in %.1fms ，数据来自%s%n",
//                    response.request().url(), (t2 - t1) / 1e6d, (response.cacheResponse() != null ? "缓存" : "网络")));

            return response;
        }
    }

}
