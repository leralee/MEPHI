package com.leralee.threadpool.executor;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author valeriali
 * @project CustomThreadPool
 */
public class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        String message = "[Rejected] Задача " + r.toString() + " была отклонена из-за перегрузки пула!";
        System.out.println(message);
        throw new RejectedExecutionException(message);
    }
}