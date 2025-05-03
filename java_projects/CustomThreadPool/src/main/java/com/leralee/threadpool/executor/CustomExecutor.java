package com.leralee.threadpool.executor;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 * @author valeriali
 * @project CustomThreadPool
 */
public interface CustomExecutor extends Executor {
    /**
     * Выполняет заданную задачу.
     *
     * @param command задача для выполнения
     */
    @Override
    void execute(Runnable command);

    /**
     * Отправляет Callable задачу и возвращает объект Future, через который можно получить результат.
     *
     * @param <T> тип возвращаемого результата
     * @param task Callable задача
     * @return объект Future, представляющий результат выполнения задачи
     */
    <T> Future<T> submit(Callable<T> task);

    /**
     * Корректно завершает работу пула потоков, не принимая новых задач.
     */
    void shutdown();

    /**
     * Немедленно прекращает выполнение задач и возвращает список задач, ожидающих выполнения.
     *
     * @return список задач, которые не были выполнены
     */
    List<Runnable> shutdownNow();
}
