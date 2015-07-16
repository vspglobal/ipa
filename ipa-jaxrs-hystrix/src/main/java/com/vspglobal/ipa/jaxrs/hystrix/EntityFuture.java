package com.vspglobal.ipa.jaxrs.hystrix;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by casele on 4/27/15.
 */
public class EntityFuture<EntityType> implements Future<EntityType> {
    private final Future<Response> response;
    private final Class<EntityType> entityType;

    EntityFuture(Future<Response> response, Class<EntityType> entityType) {
        this.entityType = entityType;
        this.response = response;
    }

    EntityFuture(Future<Response> response, GenericType<EntityType> entityType) {
        this.entityType = (Class<EntityType>) entityType.getRawType();
        this.response = response;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return response.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return response.isCancelled();
    }

    @Override
    public boolean isDone() {
        return response.isDone();
    }

    @Override
    public EntityType get() throws InterruptedException, ExecutionException {
        return readEntity(response.get());
    }

    @Override
    public EntityType get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return readEntity(response.get(timeout, unit));
    }


    private EntityType readEntity(Response response) throws ExecutionException {
        if (response == null)
            return null;



        try {
            if ((response.getStatus() == 200 || response.getStatus() == 201) && response.hasEntity())
                return response.readEntity(this.entityType);
            else {
                response.close();
                return null;
            }
        } catch (ProcessingException ex) {
            if (response != null) {
                response.close();
            }
            throw new ExecutionException("Unable to process entity of type [" + this.entityType + "]", ex);
        }

    }
}
