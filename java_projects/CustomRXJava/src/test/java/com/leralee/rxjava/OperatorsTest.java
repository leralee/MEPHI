package com.leralee.rxjava;

import com.leralee.rxjava.core.Disposable;
import com.leralee.rxjava.core.Observable;
import com.leralee.rxjava.core.Observer;
import com.leralee.rxjava.schedulers.IOThreadScheduler;
import com.leralee.rxjava.schedulers.SingleThreadScheduler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author valeriali
 * @project CustomRXJava
 */
public class OperatorsTest {

    @Test
    void testMapOperator() {
        List<Integer> result = new ArrayList<>();

        Observable.<Integer>create(emitter -> {
                    emitter.onNext(1);
                    emitter.onNext(2);
                    emitter.onComplete();
                })
                .map(x -> x * 10)
                .subscribe(new Observer<>() {

                    @Override
                    public void onNext(Integer item) {
                        result.add(item);
                    }

                    @Override
                    public void onError(Throwable t) {}

                    @Override
                    public void onComplete() {}
                });

        assertEquals(List.of(10, 20), result);
    }

    @Test
    void testFilterOperator() {
        List<Integer> result = new ArrayList<>();

        Observable.create(emitter -> {
                    emitter.onNext(1);
                    emitter.onNext(2);
                    emitter.onNext(3);
                    emitter.onComplete();
                })
                .filter(x -> (Integer) x % 2 == 0)
                .subscribe(new Observer<>() {
                    @Override
                    public void onNext(Object item) {
                        result.add((Integer) item);
                    }

                    @Override
                    public void onError(Throwable t) {}

                    @Override
                    public void onComplete() {}
                });

        assertEquals(List.of(2), result);
    }

    @Test
    void testFlatMapOperator() {
        List<String> result = new ArrayList<>();

        Observable.just(1, 2)
                .flatMap(i -> Observable.just(i + "-A", i + "-B"))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onNext(String item) {
                        result.add(item);
                    }

                    @Override
                    public void onError(Throwable t) {}

                    @Override
                    public void onComplete() {}
                });

        assertEquals(List.of("1-A", "1-B", "2-A", "2-B"), result);
    }

    @Test
    void testSubscribeOnObserveOn() throws InterruptedException {
        List<String> threads = new ArrayList<>();

        Observable.just(10)
                .subscribeOn(new IOThreadScheduler())
                .observeOn(new SingleThreadScheduler())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onNext(Integer item) {
                        threads.add(Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable t) {}

                    @Override
                    public void onComplete() {}
                });

        Thread.sleep(500); // дождаться выполнения
        assertFalse(threads.isEmpty());
    }

    @Test
    void testDisposeStopsEmission() throws InterruptedException {
        List<Integer> result = new ArrayList<>();

        Disposable disposable = Observable.create(emitter -> {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(100);
                emitter.onNext(i);
            }
            emitter.onComplete();
        }).subscribe(new Observer<>() {
            @Override
            public void onNext(Object item) {
                result.add((Integer) item);
            }

            @Override
            public void onError(Throwable t) {}

            @Override
            public void onComplete() {}
        });

        Thread.sleep(200); // получить 2 значения
        disposable.dispose(); // отписка
        Thread.sleep(500); // подождать
        assertTrue(result.size() <= 2);
    }

    @Test
    void testOnErrorCalled() {
        AtomicBoolean errorHandled = new AtomicBoolean(false);

        Observable.create(emitter -> {
            throw new RuntimeException("Boom");
        }).subscribe(new Observer<>() {
            @Override
            public void onNext(Object item) {}

            @Override
            public void onError(Throwable t) {
                errorHandled.set(true);
            }

            @Override
            public void onComplete() {}
        });

        assertTrue(errorHandled.get());
    }
}
