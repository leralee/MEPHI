package com.leralee.rxjava.core;

/**
 * @author valeriali
 * @project CustomRXJava
 */
@FunctionalInterface
public interface ObservableOnSubscribe<T> {
    void subscribe(Observer<T> observer) throws InterruptedException;
}
