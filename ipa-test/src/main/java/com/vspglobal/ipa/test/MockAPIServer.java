package com.vspglobal.ipa.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by casele on 4/23/15.
 */
public class MockAPIServer {
    private int port = 0;
    private Server server;
    private Map<String,Response> paths = new HashMap<String,Response>();
    private long delayInMillis;
    private Map<String,String> authorizationHeaders = new HashMap<String,String>();

    public MockAPIServer() {
    }

    public void start() throws Exception {
        if(server == null) {
            server = new Server(port);
            server.setHandler(new AbstractHandler() {
                @Override
                public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
                    handleRequest(s, request, httpServletRequest, httpServletResponse);
                }
            });
            server.start();
            port = ((ServerConnector)server.getConnectors()[0]).getLocalPort();
        }
    }
    public void stop() throws Exception{
        if(server != null) {
            server.stop();
            server.join();
            port = 0;
        }
    }

    public MockAPIServer registerStatus(String path, int status) {
        register(path, Response.status(status).build());
        return this;
    }
    public MockAPIServer registerEntity(String path, Object entity) {
        register(path, Response.ok(entity).type("application/json").build());
        return this;
    }
    public MockAPIServer registerEntity(String path, Object entity, MediaType contentType) {
        register(path, Response.ok(entity).type(contentType).build());
        return this;
    }
    public MockAPIServer withDelayInMillis(long delayInMillis) {
        this.delayInMillis = delayInMillis;
        return this;
    }
    public MockAPIServer register(String path, Response response) {
        this.paths.put(path, response);
        return this;
    }
    public URI resolve(String path) {
        return URI.create("http://localhost:"+port+"/").resolve(path);
    }
    public MockAPIServer expectAuthorization(String path, String authorizationHeader) {
        this.authorizationHeaders.put(path,authorizationHeader);
        return this;
    }


    private void handleRequest(String target, Request baseRequest,
        HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        try {
            Thread.currentThread().sleep(this.delayInMillis);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }

        for(Map.Entry<String,String> ah : this.authorizationHeaders.entrySet()) {
            if(target.matches(ah.getKey())) {
                String authorizationHeader = ah.getValue();
                String reqAuthz = request.getHeader("Authorization");
                if(reqAuthz == null || !reqAuthz.equals(authorizationHeader)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    baseRequest.setHandled(true);
                    return;
                }
            }

        }

        for(Map.Entry<String,Response> path : this.paths.entrySet()) {
            if(target.matches(path.getKey())) {
                Response resp = path.getValue();
                response.setStatus(resp.getStatus());
                if(resp.hasEntity()) {
                    ObjectMapper mapper = new ObjectMapper();
                    Object entity = resp.getEntity();
                    if(resp.getMediaType() != null)
                        response.setContentType(resp.getMediaType().toString());
                    else
                        response.setContentType("application/json");
                    mapper.writeValue(response.getWriter(), entity);
                }
                baseRequest.setHandled(true);

                return;
            }
        }

       // not found
        System.err.println("Uh-oh...no registered action for: "+target);
    }

    public int getPort() {
        return port;
    }

}
