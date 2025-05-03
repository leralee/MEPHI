package com.leralee.rxjava.operators;

import com.leralee.rxjava.core.Observable;
import com.leralee.rxjava.core.Observer;

import java.util.function.Predicate;

/**
 * @author valeriali
 * @project CustomRXJava
 */
public class FilterObservable<T> extends Observable<T> {

    private final Observable<T> source;
    private final Predicate<T> predicate;

    public FilterObservable(Observable<T> source, Predicate<T> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    protected void subscribeActual(Observer<T> observer) {
        source.subscribe(new Observer<T>() {
            @Override
            public void onNext(T item) {
                try {
                    if (predicate.test(item)) {
                        observer.onNext(item);
                    }
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
