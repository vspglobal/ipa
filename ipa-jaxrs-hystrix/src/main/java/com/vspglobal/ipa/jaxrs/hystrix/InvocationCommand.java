package com.vspglobal.ipa.jaxrs.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

/**
 * Created by casele on 4/2/15.
 */
public class InvocationCommand extends HystrixCommand<Response> {
   private final Invocation invocation;

   InvocationCommand(HystrixCommand.Setter setter, Invocation invocation) {
      super(setter);
      this.invocation = invocation;
   }

   public InvocationCommand property(String name, Object value) {
      if(value != null)
          invocation.property(name,value);
      return this;
   }

   @Override
   protected Response run() throws Exception {
      Response response = invocation.invoke();
      int status = response.getStatus();

      if (isSuccessful(status)) {
         return response;
      } else if(isClientError(status)) {
    	  throw new HystrixBadRequestException("Bad request. Client error.", new WebApplicationException(response.getStatus())); 
      } else {
         throw new WebApplicationException(response);
      }
   }
   
   private boolean isSuccessful(int status){
	   return status < 400; 
   }
   
   private boolean isClientError(int status){
	   return status >= 400 && status < 500; 
   }
}
