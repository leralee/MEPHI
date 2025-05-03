package com.leralee.rxjava.operators;

import com.leralee.rxjava.core.Observable;
import com.leralee.rxjava.core.Observer;

import java.util.function.Function;

/**
 * @author valeriali
 * @project CustomRXJava
 */
public class FlatMapObservable<T, R> extends Observable<R> {

    private final Observable<T> source;
    private final Function<T, Observable<R>> mapper;

    public FlatMapObservable(Observable<T> source, Function<T, Observable<R>> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    public void subscribeActual(Observer<R> finalObserver) {
        source.subscribe(new Observer<T>() {
            @Override
            public void onNext(T item) {
                try {
                    Observable<R> innerObservable = mapper.apply(item);
                    innerObservable.subscribe(new Observer<R>() {
                        @Override
                        public void onNext(R r) {
                            finalObserver.onNext(r); // передаём элементы из вложенного Observable
                        }

                        @Override
                        public void onError(Throwable t) {
                            finalObserver.onError(t); // ошибка во вложенном Observable
                        }

                        @Override
                        public void onComplete() {
                            // Мы ничего не делаем при завершении внутреннего Observable — общее завершение будет отдельно
                        }
                    });
                } catch (Throwable t) {
                    finalObserver.onError(t);
                }
            }

            @Override
            public void onError(Throwable t) {
                finalObserver.onError(t);
            }

            @Override
            public void onComplete() {
                finalObserver.onComplete();
            }
        });
    }
}
