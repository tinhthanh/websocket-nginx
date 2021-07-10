package com.pal.websocketnginx.controller.ws.conn;

import com.pal.websocketnginx.controller.ws.common.WSConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class SessionConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    private static final Logger logger = Logger.getLogger(SessionConnectEventListener.class.getName());

    @Autowired
    private WSSessionCache sessionCache;

    @Override
    public void onApplicationEvent(SessionConnectEvent sessionConnectEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionConnectEvent.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String servletSessionId = headerAccessor.getSessionAttributes().get(WSConstant.SERVLET_SESSION_ID).toString();
        String clientIp = String.valueOf(headerAccessor.getSessionAttributes().get(WSConstant.CLIENT_IP));
        WSSession wsSession = sessionCache.get(sessionId);
        wsSession.setServletSessionId(servletSessionId);
        wsSession.setClientIp(clientIp);
        logger.log(Level.INFO, "SessionConnectEvent > ClientIp={0}, SessionId={1}, ServletSessionId={2}", new Object[]{clientIp, sessionId, servletSessionId});
    }

}
