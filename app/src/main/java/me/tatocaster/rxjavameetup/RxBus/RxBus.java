package me.tatocaster.rxjavameetup.rxbus;

import me.tatocaster.rxjavameetup.rxbus.events.BaseEvent;
import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by tatocaster on 10/23/16.
 */

public class RxBus {
    private static RxBus instance = null;
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    private static RxBus getInstance() {
        if (instance == null) {
            instance = new RxBus();
        }
        return instance;
    }

    public static void broadCast(BaseEvent baseEvent) {
        getInstance().send(baseEvent);
    }

    public static void subscribe(final Action1 onNext) {
        getInstance().toObserverable().subscribe(onNext);
    }

    private void send(BaseEvent baseEvent) {
        _bus.onNext(baseEvent);
    }

    private Observable<Object> toObserverable() {
        return _bus;
    }
}
