package com.leralee.rxjava.operators;

import com.leralee.rxjava.core.Observable;
import com.leralee.rxjava.core.Observer;
import com.leralee.rxjava.schedulers.Scheduler;

/**
 * @author valeriali
 * @project CustomRXJava
 */
public class ObservableObserveOn<T> extends Observable<T> {

    private final Observable<T> source;
    private final Scheduler scheduler;

    public ObservableObserveOn(Observable<T> source, Scheduler scheduler) {
        this.source = source;
        this.scheduler = scheduler;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        source.subscribe(new Observer<T>() {
            @Override
            public void onNext(T item) {
                scheduler.execute(() -> observer.onNext(item));
            }

            @Override
            public void onError(Throwable t) {
                scheduler.execute(() -> observer.onError(t));
            }

            @Override
            public void onComplete() {
                scheduler.execute(observer::onComplete);
            }
        });
    }
}
