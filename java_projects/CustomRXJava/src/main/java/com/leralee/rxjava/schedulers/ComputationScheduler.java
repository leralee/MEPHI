package com.leralee.rxjava.schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author valeriali
 * @project CustomRXJava
 */
public class ComputationScheduler implements Scheduler {
    private final ExecutorService executor;

    public ComputationScheduler(int threadCount) {
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }
}
