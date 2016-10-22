package me.tatocaster.rxjavameetup;

import org.junit.Test;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

/**
 * Created by tatocaster on 10/23/16.
 */

public class MainActivityRxUnitTest {

    @Test
    public void testUsingTestSubscriber() {

        TestSubscriber<String> subscriber = new TestSubscriber<>();

        Observable<String> observable = Observable.just("Test Emitting");


        observable.subscribe(subscriber);


        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
        assertThat(subscriber.getOnNextEvents(), hasItem("Test Emitting"));
    }

}
