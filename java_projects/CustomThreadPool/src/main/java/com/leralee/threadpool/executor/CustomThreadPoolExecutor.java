package com.leralee.threadpool.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author valeriali
 * @project CustomThreadPool
 */
public class CustomThreadPoolExecutor implements CustomExecutor {
    private final int corePoolSize;
    private final int maxPoolSize;
    private final int minSpareThreads;
    private final long keepAliveTime;
    private final TimeUnit timeUnit;
    private final BlockingQueue<Runnable> taskQueue;
    private final ThreadFactory threadFactory;
    private final RejectedExecutionHandler rejectedHandler;

    // Для отслеживания текущего числа рабочих потоков
    private final AtomicInteger poolSize = new AtomicInteger(0);
    // Храним наших работников для управления ими
    private final ConcurrentLinkedQueue<Worker> workers = new ConcurrentLinkedQueue<>();
    // Флаг завершения работы пула
    private volatile boolean isShutdown = false;

    public CustomThreadPoolExecutor(int corePoolSize, int maxPoolSize, int minSpareThreads,
                                    long keepAliveTime, TimeUnit timeUnit,
                                    BlockingQueue<Runnable> taskQueue,
                                    ThreadFactory threadFactory,
                                    RejectedExecutionHandler rejectedHandler) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.minSpareThreads = minSpareThreads;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.taskQueue = taskQueue;
        this.threadFactory = threadFactory;
        this.rejectedHandler = rejectedHandler;
    }

    @Override
    public void execute(Runnable command) {
        if (isShutdown) {
            // Если пул уже завершён, обрабатываем задачу через обработчик отказов
            rejectedHandler.rejectedExecution(command, null);
            return;
        }
        if (getFreeThreads() < minSpareThreads && poolSize.get() < maxPoolSize) {
            System.out.println("[Pool] minSpareThreads не соблюдено, создаем поток.");
            addWorker(command);
            return;
        }
        // Если количество рабочих потоков меньше corePoolSize, сразу запускаем нового работника с задачей
        if (poolSize.get() < corePoolSize) {
            addWorker(command);
            return;
        }
        // Если попытка добавить задачу в очередь неудачна (очередь заполнена),
        if (!taskQueue.offer(command)) {
            if (poolSize.get() < maxPoolSize) {
                addWorker(command);
            } else {
                // Если ни очередь не может принять задачу, ни можно создать новый поток – отказываем в выполнении
                rejectedHandler.rejectedExecution(command, null);
            }
        }
    }

    private int getFreeThreads() {
        return (int) workers.stream().filter(Worker::isIdle).count();
    }

    private void addWorker(Runnable firstTask) {
        Worker worker = new Worker(firstTask);
        Thread thread = threadFactory.newThread(worker);
        if (thread != null) {
            workers.add(worker);
            poolSize.incrementAndGet();
            thread.start();
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        FutureTask<T> futureTask = new FutureTask<>(task);
        execute(futureTask);
        return futureTask;
    }

    @Override
    public void shutdown() {
        isShutdown = true;
        // После shutdown не принимаем новые задачи
    }

    @Override
    public List<Runnable> shutdownNow() {
        isShutdown = true;
        // Прерываем все рабочие потоки
        for (Worker worker : workers) {
            worker.thread.interrupt();
        }
        // Возвращаем оставшиеся задачи из очереди
        List<Runnable> remainingTasks = new ArrayList<>();
        taskQueue.drainTo(remainingTasks);
        return remainingTasks;
    }

    // Внутренний класс-работник, который постоянно извлекает задачи из очереди и выполняет их
    private final class Worker implements Runnable {
        private Runnable firstTask;
        private Thread thread;
        private volatile boolean idle = false;

        public Worker(Runnable firstTask) {
            this.firstTask = firstTask;
        }

        public boolean isIdle() {
            return idle;
        }

        @Override
        public void run() {
            thread = Thread.currentThread();
            Runnable task = this.firstTask;
            this.firstTask = null;
            try {
                while (task != null || (task = getTask()) != null) {
                    task.run();
                    System.out.println("[Worker] " + thread.getName() + " executes " + task);
                    task = null;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                // Когда работник завершается, уменьшаем счетчик и удаляем его из списка
                poolSize.decrementAndGet();
                workers.remove(this);
                System.out.println("[Worker] " + thread.getName() + " terminated.");
            }
        }

        // Метод для получения задачи из очереди
        private Runnable getTask() throws InterruptedException {
            idle = true;
            Runnable task = taskQueue.poll(keepAliveTime, timeUnit);
            if (task == null) {
                System.out.println("[Worker] " + thread.getName() + " idle timeout, stopping.");
            }
            idle = false;
            try {
                // Если число потоков превышает corePoolSize,
                // ждем за keepAliveTime, иначе блокируемся до появления задачи
                if (poolSize.get() > corePoolSize) {
                    return taskQueue.poll(keepAliveTime, timeUnit);
                } else {
                    return taskQueue.take();
                }
            } catch (InterruptedException e) {
                // Если прерывание – возвращаем null для выхода из цикла
                return null;
            }
        }
    }
}
