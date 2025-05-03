package com.leralee.threadpool.tasks;

import java.util.concurrent.Callable;

/**
 * @author valeriali
 * @project CustomThreadPool
 */
public class DemoCallableTask implements Callable<String> {
    private final int taskId;

    public DemoCallableTask(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public String call() throws Exception {
        String threadName = Thread.currentThread().getName();
        System.out.println("[" + threadName + "] Callable задача " + taskId + " началась.");
        try {
            // Имитация работы: задача выполняется 1 секунду
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[" + threadName + "] Callable задача " + taskId + " прервана.");
            throw e;
        }
        System.out.println("[" + threadName + "] Callable задача " + taskId + " завершилась.");
        return "Результат задачи " + taskId;
    }

    @Override
    public String toString() {
        return "DemoCallableTask{taskId=" + taskId + "}";
    }
}
