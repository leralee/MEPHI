package com.leralee.rxjava;

import com.leralee.rxjava.core.Disposable;
import com.leralee.rxjava.core.Observable;
import com.leralee.rxjava.core.Observer;
import com.leralee.rxjava.schedulers.IOThreadScheduler;
import com.leralee.rxjava.schedulers.SingleThreadScheduler;

/**
 * @author valeriali
 * @project CustomRXJava
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("📦 Запуск демонстрации CustomRxJava...\n");

        Disposable disposable = Observable
                .<Integer>create(emitter -> {
                    System.out.println("[SOURCE] Поток: " + Thread.currentThread().getName());
                    for (int i = 1; i <= 5; i++) {
                        Thread.sleep(300); // имитация работы
                        emitter.onNext(i);
                    }
                    emitter.onComplete();
                })
                .subscribeOn(new IOThreadScheduler()) // запускаем источник в отдельном потоке
                .observeOn(new SingleThreadScheduler()) // переключаемся на Single для обработки
                .map(i -> {
                    System.out.println("[MAP] Поток: " + Thread.currentThread().getName());
                    return i * 10;
                })
                .filter(i -> i % 20 == 0)
                .flatMap(i -> Observable.create(em -> {
                    em.onNext(i + " - A");
                    em.onNext(i + " - B");
                    em.onComplete();
                }))
                .subscribe(new Observer<>() {
                    @Override
                    public void onNext(Object item) {
                        System.out.println("[Observer] Получено: " + item + " | Поток: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println("[Observer] Ошибка: " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("[Observer] Поток завершён");
                    }
                });

        // Ждём немного, потом отменим подписку
        Thread.sleep(1200);
        System.out.println("\n Отписываемся от потока...\n");
        disposable.dispose();

        // Даём время для завершения фоновых потоков
        Thread.sleep(2000);
        System.out.println("Демонстрация завершена.");
    }
}
