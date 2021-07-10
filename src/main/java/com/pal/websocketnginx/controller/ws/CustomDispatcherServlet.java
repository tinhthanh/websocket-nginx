package com.pal.websocketnginx.controller.ws;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class CustomDispatcherServlet extends DispatcherServlet {
    private static final Logger logger = Logger.getLogger(CustomDispatcherServlet.class.getName());

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.doDispatch(request, response);
        String location = response.getHeader("Sec-WebSocket-Location");
        if(location != null && !location.isEmpty()) {
            String origin = response.getHeader("Origin");
            if(origin != null && origin.startsWith("https")) {
                response.setHeader("Sec-WebSocket-Location", location.replace("ws://", "wss://"));
            }
        }
    }
}