package com.leralee.rxjava;

import com.leralee.rxjava.core.Observable;
import com.leralee.rxjava.core.Observer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author valeriali
 * @project CustomRXJava
 */
public class ObservableTest {

    @Test
    void testBasicEmission() {
        List<Integer> result = new ArrayList<>();

        Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onComplete();
        }).subscribe(new Observer<>() {
            @Override
            public void onNext(Object item) {
                result.add((Integer) item);
            }

            @Override
            public void onError(Throwable t) {}

            @Override
            public void onComplete() {}
        });

        assertEquals(List.of(1, 2, 3), result);
    }
}
