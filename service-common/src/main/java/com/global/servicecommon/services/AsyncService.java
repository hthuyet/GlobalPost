package com.global.servicecommon.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ThuyetLV
 */
@Service
public class AsyncService {
    private static final int THREAD_POOL_SIZE = 10;
    private ExecutorService executorService;

    public AsyncService() {
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }
    public void execute(Runnable runnable) {
        executorService.submit(runnable);
    }
}
