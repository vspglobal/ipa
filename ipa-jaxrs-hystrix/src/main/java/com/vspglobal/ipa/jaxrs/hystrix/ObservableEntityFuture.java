package com.vspglobal.ipa.jaxrs.hystrix;

import rx.Observable;

import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Response;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by casele on 4/27/15.
 */
public class ObservableEntityFuture<T> implements Future<T> {
    private final Future<Response> future;

    public ObservableEntityFuture(Observable<Response> response, InvocationCallback<T> callback) {
        response.subscribe(new InvocationCallbackObserver(callback));
        this.future = response.toBlocking().toFuture();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        Response response = future.get();
        return narrow(response);
    }


    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        Response response = future.get(timeout, unit);
        return narrow(response);
    }

    private T narrow(Response response) {
        // TODO: this should support callbacks that expect entity not response
        return (T) response;
    }
}
