package com.pal.websocketnginx.controller.ws.conn;


import com.pal.websocketnginx.controller.ws.common.WSConstant;
import com.pal.websocketnginx.controller.ws.transfer.LastMessageCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class WSController {

    private static final Logger logger = Logger.getLogger(WSController.class.getName());

    @Autowired
    private LastMessageCache lastMessageCache;

    @SubscribeMapping("/**")
    public String subscribe(SimpMessageHeaderAccessor headerAccessor) {
        String clientIp = String.valueOf(headerAccessor.getSessionAttributes().get(WSConstant.CLIENT_IP));
        String destination = headerAccessor.getDestination();
        String lastMessage = lastMessageCache.get(destination);
        logger.log(Level.INFO, "WSController > Initial message to ClientIp={0}, SessionId={1}, Destination={2}. Data: {3} ", new Object[]{clientIp, headerAccessor.getSessionId(), destination, lastMessage});
        return lastMessage;
    }

}
