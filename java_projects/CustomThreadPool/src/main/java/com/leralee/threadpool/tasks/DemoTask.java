package com.leralee.threadpool.tasks;

import com.leralee.threadpool.executor.CustomExecutor;
import com.leralee.threadpool.executor.CustomRejectedExecutionHandler;
import com.leralee.threadpool.executor.CustomThreadFactory;
import com.leralee.threadpool.executor.CustomThreadPoolExecutor;

import java.util.concurrent.*;

/**
 * @author valeriali
 * @project CustomThreadPool
 */
public class DemoTask implements Runnable {
    private final int taskId;

    public DemoTask(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("[" + threadName + "] Задача " + taskId + " началась.");
        try {
            // Имитация работы: задача выполняется 1 секунду
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[" + threadName + "] Задача " + taskId + " прервана.");
        }
        System.out.println("[" + threadName + "] Задача " + taskId + " завершилась.");
    }

    @Override
    public String toString() {
        return "DemoTask{taskId=" + taskId + "}";
    }
}