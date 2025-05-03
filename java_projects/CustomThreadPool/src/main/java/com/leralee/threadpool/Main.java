package com.leralee.threadpool;

import com.leralee.threadpool.executor.CustomExecutor;
import com.leralee.threadpool.executor.CustomRejectedExecutionHandler;
import com.leralee.threadpool.executor.CustomThreadFactory;
import com.leralee.threadpool.executor.CustomThreadPoolExecutor;
import com.leralee.threadpool.tasks.DemoCallableTask;
import com.leralee.threadpool.tasks.DemoTask;

import java.util.concurrent.*;

/**
 * @author valeriali
 * @project CustomThreadPool
 */
public class Main {
    public static void main(String[] args) {
        // Параметры пула потоков
        int corePoolSize = 2;
        int maxPoolSize = 4;
        int minSpareThreads = 1;
        long keepAliveTime = 5;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        // Ограниченная очередь для задач (например, размер 5)
        BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(5);

        // Создаем свою реализацию ThreadFactory с уникальными именами потоков
        CustomThreadFactory threadFactory = new CustomThreadFactory("MyPool");

        // Создаем обработчик отказов, который выбрасывает исключение, если пул не может принять задачу
        CustomRejectedExecutionHandler rejectedHandler = new CustomRejectedExecutionHandler();

        // Инициализируем наш кастомный пул потоков (реализация CustomExecutor)
        CustomExecutor executor = new CustomThreadPoolExecutor(
                corePoolSize, maxPoolSize, minSpareThreads, keepAliveTime, timeUnit,
                taskQueue, threadFactory, rejectedHandler
        );


        // Отправляем несколько задач типа DemoTask (реализация Runnable)
        for (int i = 1; i <= 7; i++) {
            executor.execute(new DemoTask(i));
        }

        // Отправляем задачу типа DemoCallableTask (реализация Callable), чтобы получить результат
        Future<String> future = executor.submit(new DemoCallableTask(1));
        try {
            String result = future.get();
            System.out.println("Результат callable задачи: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Завершаем работу пула потоков
        executor.shutdown();
    }
}
