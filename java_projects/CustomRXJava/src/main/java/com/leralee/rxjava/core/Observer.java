package com.leralee.rxjava.core;

/**
 * @author valeriali
 * @project CustomRXJava
 */
public interface Observer <T> {
    void onNext(T item);
    void onError(Throwable t);
    void onComplete();
}
