package me.tatocaster.rxjavameetup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import me.tatocaster.rxjavameetup.rxbus.RxBus;
import me.tatocaster.rxjavameetup.rxbus.events.SimpleEvent;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Subscription mSubscriptionUsingCreate;
    private Observer mObserver;
    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RxBus.subscribe(new Action1() {
            @Override
            public void call(Object o) {
                if (o instanceof SimpleEvent)
                    Log.d(TAG, "call: " + ((SimpleEvent) o).greetingText);
            }
        });

        mCompositeSubscription = new CompositeSubscription();

        mObserver = new Observer<Object>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted() called");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: e = [" + e + "]");
            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "onNext() called with: o = [" + o + "]");
            }
        };

        mSubscriptionUsingCreate = usingCreate().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);


        Subscription subscriptionUsingJust = usingJust().subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted() called");
                RxBus.broadCast(new SimpleEvent("Observable consumed successfully"));
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: e = [" + e + "]");
            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "onNext() called with: o = [" + o + "]");
            }
        });
        mCompositeSubscription.add(subscriptionUsingJust);


        usingJust();


        usingCallable();

    }

    private Observable<Object> usingCreate() {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                if (!subscriber.isUnsubscribed()) subscriber.onNext("Hello Create operator");
                subscriber.onCompleted();
            }
        });
    }

    private Observable<String> usingJust() {
        return Observable.just("Hello Just Operator");
    }

    private void usingCallable() {
        Observable.fromCallable(new Func0<Object>() {
            @Override
            public Object call() {
                return "Hello fromCallable operator";
            }
        })
                .subscribe(mObserver);
    }


    @Override
    protected void onDestroy() {

        /*
        unsubscribe single sub
         */
        if (mSubscriptionUsingCreate != null) mSubscriptionUsingCreate.unsubscribe();

        /*
        composite subscriptions list
         */
        mCompositeSubscription.unsubscribe();
        super.onDestroy();
    }
}
