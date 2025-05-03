# CustomRxJava — Реализация собственной реактивной библиотеки

## Описание проекта

CustomRxJava — это упрощённая, но полнофункциональная реактивная библиотека на Java, вдохновённая RxJava. Покрыты ключевые элементы реактивного программирования:

* операторы (`map`, `filter`, `flatMap`),
* управление потоками (`subscribeOn`, `observeOn`),
* отмена подписок (`Disposable`),
* обработка ошибок (`onError`),
* потокобезопасная подписка.

## Архитектура проекта

### 1. Базовые компоненты

* `Observable<T>` — поток данных с цепочкой операторов и `subscribe()`.
* `Observer<T>` — подписчик с `onNext`, `onError`, `onComplete`.
* `ObservableOnSubscribe<T>` — источник данных, используемый в `create()`.
* `Disposable`, `SimpleDisposable` — отмена и контроль состояния.

### 2. Операторы

* `MapObservable` — `Function<T, R>`.
* `FilterObservable` — `Predicate<T>`.
* `FlatMapObservable` — `Function<T, Observable<R>>`.

### 3. Управление потоками

* `Scheduler` — интерфейс.
* `IOThreadScheduler` — `CachedThreadPool`.
* `ComputationScheduler` — `FixedThreadPool`.
* `SingleThreadScheduler` — `SingleThreadExecutor`.

## Отмена подписки

После `dispose()` подписчик больше не получает событий. Это защищает от гонок и утечек в многопоточном контексте.

## Пример использования

```java
Observable.create(emitter -> {
    for (int i = 1; i <= 5; i++) {
        Thread.sleep(200);
        emitter.onNext(i);
    }
    emitter.onComplete();
})
.subscribeOn(new IOThreadScheduler())
.observeOn(new SingleThreadScheduler())
.map(x -> x * 10)
.filter(x -> x % 20 == 0)
.flatMap(x -> Observable.create(inner -> {
    inner.onNext(x + "-A");
    inner.onNext(x + "-B");
    inner.onComplete();
}))
.subscribe(new Observer<>() {
    public void onNext(Object item) {
        System.out.println("Получено: " + item);
    }
    public void onError(Throwable t) {
        System.err.println("Ошибка: " + t.getMessage());
    }
    public void onComplete() {
        System.out.println("Поток завершён");
    }
});
```

## Тестирование

* `ObservableTest` — простая подписка и поток данных.
* `OperatorsTest` — `map`, `filter`, `flatMap`, `subscribeOn`, `observeOn`, `onError`, `dispose`.

## Выводы

Проект демонстрирует:

* понимание паттерна наблюдателя,
* цепочки операторов в стиле Rx,
* многопоточность и Schedulers,
* подписки с отменой,
* изоляцию потоков.

CustomRxJava может быть основой для изучения Rx-подходов и дизайна собственных асинхронных API.
