package com.leralee.rxjava.operators;

import com.leralee.rxjava.core.Observable;
import com.leralee.rxjava.core.Observer;
import com.leralee.rxjava.schedulers.Scheduler;

/**
 * @author valeriali
 * @project CustomRXJava
 */
public class ObservableSubscribeOn<T> extends Observable<T> {

    private final Observable<T> source;
    private final Scheduler scheduler;

    public ObservableSubscribeOn(Observable<T> source, Scheduler scheduler) {
        this.source = source;
        this.scheduler = scheduler;
    }

    @Override
    public void subscribeActual(Observer<T> observer) {
        scheduler.execute(() -> source.subscribe(observer));
    }
}
