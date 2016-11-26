package me.tatocaster.rxjavameetup.rxbus;

import com.jakewharton.rxrelay.PublishRelay;
import com.jakewharton.rxrelay.Relay;

import rx.Observable;

/**
 * Created by tatocaster on 10/23/16.
 */

public class RxBus {
    private final Relay<Object, Object> _bus = PublishRelay.create().toSerialized();
    private static RxBus rxBus = null;

    public static RxBus getInstance() {
        if (rxBus == null) {
            rxBus = new RxBus();
        }
        return rxBus;
    }

    private RxBus() {

    }

    public void send(Object o) {
        _bus.call(o);
    }

    public Observable<Object> asObservable() {
        return _bus.asObservable();
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }
}
