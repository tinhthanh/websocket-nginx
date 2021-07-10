package com.pal.websocketnginx.controller.ws.conn;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class SessionSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    private static final Logger logger = Logger.getLogger(SessionSubscribeEventListener.class.getName());

    @Autowired
    private WSSessionCache oddsSessionCache;

    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent)  {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
        String destination = headerAccessor.getDestination();
        WSSession wsSession = oddsSessionCache.get(headerAccessor.getSessionId());
        if(StringUtils.isNotBlank(destination)) {
            wsSession.addDestination(destination.intern());
            wsSession.setLastSendTimestamp(System.currentTimeMillis());
        }
        logger.log(Level.INFO, "SessionSubscribeEvent > ClientIp={0}, SessionId={1}, Destination={2}", new Object[]{wsSession.getClientIp(), headerAccessor.getSessionId(), headerAccessor.getDestination()});

    }
}
