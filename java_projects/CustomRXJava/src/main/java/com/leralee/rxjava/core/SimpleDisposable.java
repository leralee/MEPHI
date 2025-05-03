package com.leralee.rxjava.core;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author valeriali
 * @project CustomRXJava
 */
public class SimpleDisposable implements Disposable {
    private final AtomicBoolean isDisposed = new AtomicBoolean(false);

    @Override
    public void dispose() {
        isDisposed.set(true);
    }

    @Override
    public boolean isDisposed() {
        return isDisposed.get();
    }
}
