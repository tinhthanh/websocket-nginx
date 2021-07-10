package com.pal.websocketnginx.controller.ws.conn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class SessionDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger logger = Logger.getLogger(SessionDisconnectEventListener.class.getName());

    @Autowired
    private WSSessionCache sessionCache;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        WSSession wsSession = sessionCache.get(headerAccessor.getSessionId());
        if(wsSession != null) {
            wsSession.close();
            sessionCache.invalidate(headerAccessor.getSessionId());
        }
        logger.log(Level.INFO, "SessionDisconnectEvent > ClientIp={0}, SessionId={1}. Cache size={2}", new Object[]{wsSession.getClientIp(), headerAccessor.getSessionId(), sessionCache.size()});
    }
}
