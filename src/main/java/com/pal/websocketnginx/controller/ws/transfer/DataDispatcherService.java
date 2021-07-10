package com.pal.websocketnginx.controller.ws.transfer;

import com.pal.websocketnginx.controller.ws.common.WSConstant;
import com.pal.websocketnginx.controller.ws.conn.WSSession;
import com.pal.websocketnginx.controller.ws.conn.WSSessionCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class DataDispatcherService {

    private static final Logger logger = Logger.getLogger(DataDispatcherService.class.getName());

    private static final int TIMEOUT = 30 * 1000; //30s

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private WSSessionCache sessionCache;

    @Autowired
    private LastMessageCache lastMessageCache;

    public void dispatch(String destination, String data) {
        List<WSSession> sessions = sessionCache.asMap().values().stream().filter(session -> session.getDestinations().contains(destination)).collect(Collectors.toList());
        if (destination.startsWith(WSConstant.USER_SUBSCRIPTION_ID_PREFIX)) {
            String dest = destination.substring(WSConstant.USER_SUBSCRIPTION_ID_PREFIX.length() - 1);

            for (WSSession session : sessions) {
                long start = System.currentTimeMillis();
                messagingTemplate.convertAndSendToUser(session.getSessionId(), dest, data, createHeaders(session.getSessionId()));
                logger.log(Level.INFO, "DataDispatcherService > dispatch convertAndSendToUser to ClientIp={0}, SessionId={1}, Destination={2}, Data={3}. Duration: {4}",
                        new Object[]{session.getClientIp(), session.getSessionId(), dest, data, System.currentTimeMillis() - start});
            }
        } else {
            long start = System.currentTimeMillis();
            messagingTemplate.convertAndSend(destination, data);
            long timestamp = System.currentTimeMillis();
            for (WSSession session : sessions) {
                session.setLastSendTimestamp(timestamp);
            }
            logger.log(Level.INFO, "DataDispatcherService > dispatch convertAndSend to Destination={0}, Data={1}. Duration: {2}", new Object[]{destination, data, System.currentTimeMillis() - start});
        }
    }

    public void dispatchLastMessage(String destination) {
        String lastData = lastMessageCache.get(destination);
        if (lastData != null) {
            dispatch(destination, lastData);
        }
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    @Scheduled(fixedDelay = 5000, initialDelay = 5000)
    public void sendKeepaliveMessage() {
        long timestamp = System.currentTimeMillis();
        String keepAliveMessage = "ping:" + timestamp;
        Set<String> keptAliveDests = new HashSet<>();
        for (WSSession session : sessionCache.asMap().values()) {
            try {
                if (!session.getDestinations().isEmpty() && session.getLastSendTimestamp() > 0 && (timestamp - session.getLastSendTimestamp()) >= TIMEOUT) {
                    String dest = session.getDestinations().iterator().next();
                    if (!keptAliveDests.contains(dest)) {
                        messagingTemplate.convertAndSend(dest, keepAliveMessage);
                        keptAliveDests.add(dest);
                    }
                    session.setLastSendTimestamp(timestamp);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Cannot send keep alive message to client " + session, e);
            }
        }
    }
}
