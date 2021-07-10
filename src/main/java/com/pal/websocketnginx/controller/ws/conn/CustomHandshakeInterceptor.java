package com.pal.websocketnginx.controller.ws.conn;

import com.pal.websocketnginx.controller.ws.common.WSConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;


import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger logger = Logger.getLogger(CustomHandshakeInterceptor.class.getName());

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();
            logger.log(Level.INFO, "CustomHandshakeInterceptor > beforeHandshake: ClientIP={0}, ServletSessionId={1}", new Object[]{getTrueClientIPAddress(servletRequest), session.getId()});
            attributes.put(WSConstant.SERVLET_SESSION_ID, session.getId());
            attributes.put(WSConstant.CLIENT_IP, getTrueClientIPAddress(servletRequest));
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();
            logger.log(Level.INFO, "CustomHandshakeInterceptor > afterHandshake: ClientIP={0}, ServletSessionId={1}", new Object[]{getTrueClientIPAddress(servletRequest), session.getId()});
        }
    }

    private String getTrueClientIPAddress(ServletServerHttpRequest request) {
        String ipAddress = request.getServletRequest().getHeader("TRUE-CLIENT-IP");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getServletRequest().getHeader("X-FORWARDED-FOR");
        }
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddress().getAddress().getHostAddress();
        } else if (ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }
        return StringUtils.isBlank(ipAddress) ? StringUtils.EMPTY : ipAddress;
    }

}
