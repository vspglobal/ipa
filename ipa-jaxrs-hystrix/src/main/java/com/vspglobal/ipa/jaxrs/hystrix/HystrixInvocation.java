package com.vspglobal.ipa.jaxrs.hystrix;

import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixRuntimeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

import java.util.Locale;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

/**
 * Created by casele on 4/2/15.
 */
public class HystrixInvocation implements Invocation {

   private final InvocationCommand command;
   private static Logger logger = LoggerFactory.getLogger(HystrixInvocation.class);

   private HystrixInvocation(InvocationCommand command) {
      this.command = command;
   }

   public static Builder builder(Invocation.Builder builder, Setter setter) {
      return new Builder(builder, setter);
   }
   public static Invocation build(Invocation invocation, Setter setter) {
      return new HystrixInvocation(new InvocationCommand(setter, invocation));
   }

   @Override
   public Invocation property(String name, Object value) {
      command.property(name,value);
      return this;
   }

   @Override
   public Response invoke() {
      try {
         return command.execute();
      } catch (Exception ex) {
         throw buildWebApplicationException(ex);
      }
   }

   @Override
   public <T> T invoke(Class<T> responseType) {
      try {
          return command.execute().readEntity(responseType);
       } catch (Exception ex) {
          throw buildWebApplicationException(ex);
       }
   }

   @Override
   public <T> T invoke(GenericType<T> responseType) {
      try {
          return command.execute().readEntity(responseType);
       } catch (Exception ex) {
          throw buildWebApplicationException(ex);
       }
   }

   @Override
   public Future<Response> submit() {
      return command.queue();
   }

   @Override
   public <T> Future<T> submit(Class<T> responseType) {
      return new EntityFuture<T>(command.queue(), responseType);
   }

   @Override
   public <T> Future<T> submit(GenericType<T> responseType) {
      return new EntityFuture<T>(command.queue(), responseType);
   }

   @Override
   public <T> Future<T> submit(InvocationCallback<T> callback) {
      return new ObservableEntityFuture<T>(command.observe(), callback);
   }

   public static class Builder implements Invocation.Builder {
      private final Invocation.Builder builder;
      private final Setter setter;

      Builder(Invocation.Builder builder, Setter setter) {
         this.builder = builder;
         this.setter = setter;
      }

      HystrixInvocation createCommand(final Invocation i) {
         InvocationCommand cmd = new InvocationCommand(setter, i);
         return new HystrixInvocation(cmd);
      }


      // BEGIN DELEGATES

      @Override
      public HystrixInvocation buildGet() {
         Invocation i = builder.buildGet();
         return createCommand(i);
      }

      @Override
      public HystrixInvocation buildPost(Entity<?> entity) {
         Invocation i = builder.buildPost(entity);
         return createCommand(i);
      }

      @Override
      public HystrixInvocation buildPut(Entity<?> entity) {
         Invocation i = builder.buildPut(entity);
         return createCommand(i);
      }

      @Override
      public HystrixInvocation buildDelete() {
         Invocation i = builder.buildDelete();
         return createCommand(i);
      }

      @Override
      public HystrixInvocation build(String methodName) {
         Invocation i = builder.build(methodName);
         return createCommand(i);
      }

      @Override
      public HystrixInvocation build(String method, Entity<?> entity) {
         Invocation i = builder.build(method, entity);
         return createCommand(i);
      }

      @Override
      public AsyncInvoker async() {
         return new AsyncInvocationCommand(this);
      }

      @Override
      public Builder accept(String... mediaTypes) {
         builder.accept(mediaTypes);
         return this;
      }

      @Override
      public Builder accept(MediaType... mediaTypes) {
         builder.accept(mediaTypes);
         return this;
      }

      @Override
      public Builder acceptLanguage(Locale... locales) {
         builder.acceptLanguage(locales);
         return this;
      }

      @Override
      public Builder acceptLanguage(String... locales) {
         builder.acceptLanguage(locales);
         return this;
      }

      @Override
      public Builder acceptEncoding(String... encodings) {
         builder.acceptEncoding(encodings);
         return this;
      }

      @Override
      public Builder cookie(Cookie cookie) {
         builder.cookie(cookie);
         return this;
      }

      @Override
      public Builder cookie(String name, String value) {
         builder.cookie(name, value);
         return this;
      }

      @Override
      public Builder cacheControl(CacheControl cacheControl) {
         builder.cacheControl(cacheControl);
         return this;
      }

      @Override
      public Builder header(String name, Object value) {
         builder.header(name,value);
         return this;
      }

      @Override
      public Builder headers(MultivaluedMap<String, Object> headers) {
         builder.headers(headers);
         return this;
      }

      @Override
      public Builder property(String name, Object value) {
         builder.property(name,value);
         return this;
      }

      @Override
      public Response get() {
         return buildGet().invoke();
      }

      @Override
      public <T> T get(Class<T> responseType) {
         return buildGet().invoke(responseType);
      }

      @Override
      public <T> T get(GenericType<T> responseType) {
         return buildGet().invoke(responseType);
      }

      @Override
      public Response put(Entity<?> entity) {
         return buildPut(entity).invoke();
      }

      @Override
      public <T> T put(Entity<?> entity, Class<T> responseType) {
         return buildPut(entity).invoke(responseType);
      }

      @Override
      public <T> T put(Entity<?> entity, GenericType<T> responseType) {
         return buildPut(entity).invoke(responseType);
      }

      @Override
      public Response post(Entity<?> entity) {
         return buildPost(entity).invoke();
      }

      @Override
      public <T> T post(Entity<?> entity, Class<T> responseType) {
         return buildPost(entity).invoke(responseType);
      }

      @Override
      public <T> T post(Entity<?> entity, GenericType<T> responseType) {
         return buildPost(entity).invoke(responseType);
      }

      @Override
      public Response delete() {
         return buildDelete().invoke();
      }

      @Override
      public <T> T delete(Class<T> responseType) {
         return buildDelete().invoke(responseType);
      }

      @Override
      public <T> T delete(GenericType<T> responseType) {
         return buildDelete().invoke(responseType);
      }

      @Override
      public Response head() {
         return method(HttpMethod.HEAD.name());
      }

      @Override
      public Response options() {
         return method(HttpMethod.OPTIONS.name());
      }

      @Override
      public <T> T options(Class<T> responseType) {
         return method(HttpMethod.OPTIONS.name(),responseType);
      }

      @Override
      public <T> T options(GenericType<T> responseType) {
         return method(HttpMethod.OPTIONS.name(),responseType);
      }

      @Override
      public Response trace() {
         return method(HttpMethod.TRACE.name());
      }

      @Override
      public <T> T trace(Class<T> responseType) {
         return method(HttpMethod.TRACE.name(), responseType);
      }

      @Override
      public <T> T trace(GenericType<T> responseType) {
         return method(HttpMethod.TRACE.name(), responseType);
      }

      @Override
      public Response method(String name) {
         return build(name).invoke();
      }

      @Override
      public <T> T method(String name, Class<T> responseType) {
         return build(name).invoke(responseType);
      }

      @Override
      public <T> T method(String name, GenericType<T> responseType) {
         return build(name).invoke(responseType);
      }

      @Override
      public Response method(String name, Entity<?> entity) {
         return build(name,entity).invoke();
      }

      @Override
      public <T> T method(String name, Entity<?> entity, Class<T> responseType) {
         return build(name,entity).invoke(responseType);
      }

      @Override
      public <T> T method(String name, Entity<?> entity, GenericType<T> responseType) {
         return build(name,entity).invoke(responseType);
      }
   }

   private WebApplicationException buildWebApplicationException(Exception originalEx) {
      WebApplicationException rtn = null;

      Throwable ex = originalEx;

      String commandName = command.getCommandGroup().name()+":"+command.getCommandKey().name();

      // try to find an interesting exception...
      while(ex != null && rtn == null) {
         if(ex instanceof WebApplicationException) {
            rtn = (WebApplicationException) ex;
            logger.warn("HystrixInvocation caused WebApplicationException cmd="+commandName+" http_status="+rtn.getResponse().getStatus()+" failure_type=WebApplicationException message="+ex.getMessage());
         } else if(ex instanceof TimeoutException) {
            rtn =  new WebApplicationException(ex, Response.Status.GATEWAY_TIMEOUT);
            logger.warn("HystrixInvocation caused WebApplicationException cmd="+commandName+" http_status="+rtn.getResponse().getStatus()+" failure_type=TimeoutException message="+ex.getMessage());
         } else if(ex instanceof HystrixRuntimeException) {
            switch(((HystrixRuntimeException)ex).getFailureType()) {
               case BAD_REQUEST_EXCEPTION:
            	   rtn = new WebApplicationException(ex, Response.Status.BAD_REQUEST);
            	   break; 
               case COMMAND_EXCEPTION:
                  break;
               case TIMEOUT:
                  rtn =  new WebApplicationException(ex, Response.Status.GATEWAY_TIMEOUT);
                  break;
               case SHORTCIRCUIT:
                  rtn =  new WebApplicationException(ex, Response.Status.SERVICE_UNAVAILABLE);
                  break;
               case REJECTED_THREAD_EXECUTION:
               case REJECTED_SEMAPHORE_EXECUTION:
               case REJECTED_SEMAPHORE_FALLBACK:
                  rtn =  new WebApplicationException(ex, Response.Status.SERVICE_UNAVAILABLE);
                  break;
            }
            if(rtn != null)
                logger.warn("HystrixInvocation caused WebApplicationException cmd="+commandName+" http_status="+rtn.getResponse().getStatus()+" failure_type="+((HystrixRuntimeException) ex).getFailureType()+" message="+ex.getMessage());
         }

         ex = ex.getCause();
      }

      if(rtn == null) {
         rtn = new WebApplicationException(originalEx);
         logger.warn("HystrixInvocation caused WebApplicationException cmd="+commandName+" http_status="+rtn.getResponse().getStatus()+" failure_type="+originalEx.getClass().getSimpleName()+" message="+originalEx.getMessage());
      }


      return rtn;
   }

}
