package com.pal.websocketnginx.controller.ws.conn;

import com.pal.websocketnginx.controller.ws.common.WSConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class SessionUnsubscribeListener implements ApplicationListener<SessionUnsubscribeEvent> {

    private static final Logger logger = Logger.getLogger(SessionUnsubscribeListener.class.getName());

    @Autowired
    private WSSessionCache sessionCache;

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent sessionUnsubscribeEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionUnsubscribeEvent.getMessage());
        String destination = headerAccessor.getDestination() == null ? String.valueOf(headerAccessor.getHeader(WSConstant.SIMP_SUBSCRIPTION_ID)) : headerAccessor.getDestination();
        WSSession wsSession = sessionCache.get(headerAccessor.getSessionId());
        if(StringUtils.isNotBlank(destination)) {
            wsSession.removeDestination(destination);
        }
        logger.log(Level.INFO, "SessionUnsubscribeEvent > ClientIp={0}, SessionId={1}, Destination={2}", new Object[]{wsSession.getClientIp(), headerAccessor.getSessionId(), destination});
    }
}
