package com.leralee.rxjava.operators;

import com.leralee.rxjava.core.Observable;
import com.leralee.rxjava.core.Observer;

import java.util.function.Function;

/**
 * @author valeriali
 * @project CustomRXJava
 */
public class MapObservable<T, R> extends Observable<R> {

    private final Observable<T> source;
    private final Function<T, R> mapper;

    public MapObservable(Observable<T> source, Function<T, R> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    public void subscribeActual(Observer<R> observer) {
        source.subscribe(new Observer<T>() {
            @Override
            public void onNext(T item) {
                try {
                    R mappedItem = mapper.apply(item);
                    observer.onNext(mappedItem);
                } catch (Throwable t) {
                    observer.onError(t);
                }
            }

            @Override
            public void onError(Throwable t) {
                observer.onError(t);
            }

            @Override
            public void onComplete() {
                observer.onComplete();
            }
        });
    }
}