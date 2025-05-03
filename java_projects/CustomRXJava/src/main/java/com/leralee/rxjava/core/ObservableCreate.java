package com.leralee.rxjava.core;

/**
 * @author valeriali
 * @project CustomRXJava
 */

public class ObservableCreate<T> extends Observable<T> {

    private final ObservableOnSubscribe<T> source;

    public ObservableCreate(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
    protected void subscribeActual(Observer<T> observer) {
        try {
            source.subscribe(observer); // запускаем пользовательский код
        } catch (Throwable t) {
            observer.onError(t);
        }
    }

}
