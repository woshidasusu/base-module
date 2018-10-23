package com.dasu.volley.wrapper;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class VolleyQueueSingleton {
    /**
     * Number of network request dispatcher threads to start.
     */
    private static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 6;
    private RequestQueue mRequestQueue;
    private volatile static VolleyQueueSingleton sInstance;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public static VolleyQueueSingleton getInstance() {
        if (sInstance == null) {
            synchronized (VolleyQueueSingleton.class) {
                if (sInstance == null) {
                    sInstance = new VolleyQueueSingleton();
                }
            }
        }
        return sInstance;
    }

    private VolleyQueueSingleton() {
//        HttpsTrustManager.allowAllSSL();
        HurlStack stack = new SelfSignSslHurlStack();
        mRequestQueue = newAsyncRequestQueue(stack);
    }

    private RequestQueue newAsyncRequestQueue(HurlStack stack) {
        BasicNetwork network = new BasicNetwork(stack);
        //修改Volley的请求队列，构键新的线程池
        RequestQueue queue1 = new RequestQueue(new NoCache(), network, DEFAULT_NETWORK_THREAD_POOL_SIZE,
                new ExecutorDelivery(executorService));
        queue1.start();
        return queue1;
    }

    public void add(Request<?> request) {
        mRequestQueue.add(request);
    }

    public void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }
}