package com.vspglobal.ipa.jaxrs.hystrix;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.concurrent.Future;

/**
 * Created by casele on 4/27/15.
 */
public class AsyncInvocationCommand implements AsyncInvoker{
    private final HystrixInvocation.Builder builder;

    public AsyncInvocationCommand(HystrixInvocation.Builder builder) {
        this.builder = builder;
    }

    @Override
    public Future<Response> get() {
        HystrixInvocation i =  builder.buildGet();
        return i.submit();
    }

    @Override
    public <T> Future<T> get(Class<T> responseType) {
        HystrixInvocation i =  builder.buildGet();
        return i.submit(responseType);
    }

    @Override
    public <T> Future<T> get(GenericType<T> responseType) {
        HystrixInvocation i =  builder.buildGet();
        return i.submit(responseType);
    }

    @Override
    public <T> Future<T> get(InvocationCallback<T> callback) {

        HystrixInvocation i =  builder.buildGet();
        return i.submit(callback);
    }

    @Override
    public Future<Response> put(Entity<?> entity) {
        HystrixInvocation i =  builder.buildPut(entity);
        return i.submit();
    }

    @Override
    public <T> Future<T> put(Entity<?> entity, Class<T> responseType) {
        HystrixInvocation i =  builder.buildPut(entity);
        return i.submit(responseType);
    }

    @Override
    public <T> Future<T> put(Entity<?> entity, GenericType<T> responseType) {
        HystrixInvocation i =  builder.buildPut(entity);
        return i.submit(responseType);
    }

    @Override
    public <T> Future<T> put(Entity<?> entity, InvocationCallback<T> callback) {
        HystrixInvocation i =  builder.buildPut(entity);
        return i.submit(callback);
    }

    @Override
    public Future<Response> post(Entity<?> entity) {
        HystrixInvocation i =  builder.buildPost(entity);
        return i.submit();
    }

    @Override
    public <T> Future<T> post(Entity<?> entity, Class<T> responseType) {
        HystrixInvocation i =  builder.buildPost(entity);
        return i.submit(responseType);
    }

    @Override
    public <T> Future<T> post(Entity<?> entity, GenericType<T> responseType) {
        HystrixInvocation i =  builder.buildPost(entity);
        return i.submit(responseType);
    }

    @Override
    public <T> Future<T> post(Entity<?> entity, InvocationCallback<T> callback) {
        HystrixInvocation i =  builder.buildPost(entity);
        return i.submit(callback);
    }

    @Override
    public Future<Response> delete() {
        HystrixInvocation i =  builder.buildDelete();
        return i.submit();
    }

    @Override
    public <T> Future<T> delete(Class<T> responseType) {
        HystrixInvocation i =  builder.buildDelete();
        return i.submit(responseType);
    }

    @Override
    public <T> Future<T> delete(GenericType<T> responseType) {
        HystrixInvocation i =  builder.buildDelete();
        return i.submit(responseType);
    }

    @Override
    public <T> Future<T> delete(InvocationCallback<T> callback) {
        HystrixInvocation i =  builder.buildDelete();
        return i.submit(callback);
    }

    @Override
    public Future<Response> head() {
        return method(HttpMethod.HEAD.name());
    }

    @Override
    public Future<Response> head(InvocationCallback<Response> callback) {
        return method(HttpMethod.HEAD.name(), callback);
    }

    @Override
    public Future<Response> options() {
        return method(HttpMethod.OPTIONS.name());
    }

    @Override
    public <T> Future<T> options(Class<T> responseType) {
        return method(HttpMethod.OPTIONS.name(), responseType);
    }

    @Override
    public <T> Future<T> options(GenericType<T> responseType) {
        return method(HttpMethod.OPTIONS.name(), responseType);
    }

    @Override
    public <T> Future<T> options(InvocationCallback<T> callback) {
        return method(HttpMethod.OPTIONS.name(), callback);
    }

    @Override
    public Future<Response> trace() {
        return method(HttpMethod.TRACE.name());
    }

    @Override
    public <T> Future<T> trace(Class<T> responseType) {
        return method(HttpMethod.TRACE.name(), responseType);
    }

    @Override
    public <T> Future<T> trace(GenericType<T> responseType) {
        return method(HttpMethod.TRACE.name(), responseType);
    }

    @Override
    public <T> Future<T> trace(InvocationCallback<T> callback) {
        return method(HttpMethod.TRACE.name(), callback);
    }

    @Override
    public Future<Response> method(String name) {
        HystrixInvocation i =  builder.build(name);
        return i.submit();
    }

    @Override
    public <T> Future<T> method(String name, Class<T> responseType) {
        HystrixInvocation i =  builder.build(name);
        return i.submit(responseType);
    }

    @Override
    public <T> Future<T> method(String name, GenericType<T> responseType) {
        HystrixInvocation i =  builder.build(name);
        return i.submit(responseType);
    }

    @Override
    public <T> Future<T> method(String name, InvocationCallback<T> callback) {
        HystrixInvocation i =  builder.build(name);
        return i.submit(callback);
    }

    @Override
    public Future<Response> method(String name, Entity<?> entity) {
        HystrixInvocation i =  builder.build(name,entity);
        return i.submit();
    }

    @Override
    public <T> Future<T> method(String name, Entity<?> entity, Class<T> responseType) {
        HystrixInvocation i =  builder.build(name,entity);
        return i.submit(responseType);
    }

    @Override
    public <T> Future<T> method(String name, Entity<?> entity, GenericType<T> responseType) {
        HystrixInvocation i =  builder.build(name,entity);
        return i.submit(responseType);
    }

    @Override
    public <T> Future<T> method(String name, Entity<?> entity, InvocationCallback<T> callback) {
        HystrixInvocation i =  builder.build(name,entity);
        return i.submit(callback);
    }
}
