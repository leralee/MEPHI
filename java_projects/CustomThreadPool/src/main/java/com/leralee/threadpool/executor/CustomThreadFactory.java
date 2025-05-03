package com.leralee.threadpool.executor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author valeriali
 * @project CustomThreadPool
 */
public class CustomThreadFactory implements ThreadFactory {
    private final String poolName;
    private final AtomicInteger threadCount = new AtomicInteger(0);

    public CustomThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setName(poolName + "-worker-" + threadCount.incrementAndGet());
        System.out.println("[ThreadFactory] Создан новый поток: " + t.getName());
        return t;
    }
}
