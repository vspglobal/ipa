package com.vspglobal.ipa.jaxrs.hystrix;

import rx.Observer;

import javax.ws.rs.client.InvocationCallback;

/**
 * Created by casele on 4/27/15.
 */
public class InvocationCallbackObserver<T> implements Observer<T> {
    private final InvocationCallback<T> callback;

    public InvocationCallbackObserver(InvocationCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        callback.failed(e);
    }

    @Override
    public void onNext(T v) {
        callback.completed(v);
    }
}
