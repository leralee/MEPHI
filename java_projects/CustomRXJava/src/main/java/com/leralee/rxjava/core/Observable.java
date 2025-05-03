package com.leralee.rxjava.core;


import com.leralee.rxjava.operators.*;
import com.leralee.rxjava.schedulers.Scheduler;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author valeriali
 * @project CustomRXJava
 */

public abstract class Observable<T> {

    // Фабрика для создания кастомного Observable
    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        return new ObservableCreate<>(source);
    }

    public <R> Observable<R> map(Function<T, R> mapper) {
        return new MapObservable<>(this, mapper);
    }

    public Observable<T> filter(Predicate<T> predicate) {
        return new FilterObservable<>(this, predicate);
    }

    public <R> Observable<R> flatMap(Function<T, Observable<R>> mapper) {
        return new FlatMapObservable<>(this, mapper);
    }

    public Observable<T> subscribeOn(Scheduler scheduler) {
        return new ObservableSubscribeOn<>(this, scheduler);
    }

    public Observable<T> observeOn(Scheduler scheduler) {
        return new ObservableObserveOn<>(this, scheduler);
    }

    public Disposable subscribe(Observer<T> observer) {
        SimpleDisposable disposable = new SimpleDisposable();

        Observer<T> safeObserver = new Observer<T>() {
            @Override
            public void onNext(T item) {
                if (!disposable.isDisposed()) {
                    observer.onNext(item);
                }
            }

            @Override
            public void onError(Throwable t) {
                if (!disposable.isDisposed()) {
                    observer.onError(t);
                }
            }

            @Override
            public void onComplete() {
                if (!disposable.isDisposed()) {
                    observer.onComplete();
                }
            }
        };

        subscribeActual(safeObserver);
        return disposable;
    }

    protected abstract void subscribeActual(Observer<T> observer);

    @SafeVarargs
    public static <T> Observable<T> just(T... items) {
        return create(emitter -> {
            for (T item : items) {
                emitter.onNext(item);
            }
            emitter.onComplete();
        });
    }
}
