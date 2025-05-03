package com.leralee.rxjava.schedulers;

/**
 * @author valeriali
 * @project CustomRXJava
 */
public interface Scheduler {
    void execute(Runnable task);
}
