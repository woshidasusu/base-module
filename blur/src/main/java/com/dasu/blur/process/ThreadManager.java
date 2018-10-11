package com.dasu.blur.process;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by suxq on 2018/10/11.
 */

public class ThreadManager {
    static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    public static void execTask(Runnable task) {
        EXECUTOR.execute(task);
    }
}
