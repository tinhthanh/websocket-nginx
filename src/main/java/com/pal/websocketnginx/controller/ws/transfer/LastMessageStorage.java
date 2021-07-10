package com.pal.websocketnginx.controller.ws.transfer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class LastMessageStorage {

    private static final Logger logger = Logger.getLogger(LastMessageStorage.class.getName());

    private static final String SERVER_GROUP_BLUE = "BLUE";
    private static final String WEBSOCKET_MESSAGE_TABLE_BLUE = "websocket_message_blue";
    private static final String WEBSOCKET_MESSAGE_TABLE_GREEN = "websocket_message";



    private boolean isBlue;

    @PostConstruct
    public void init() {
        logger.log(Level.INFO, "LastMessageStorage > init: This server belongs to {0} group", isBlue ? "BLUE" : "GREEN");
    }
    public String findLastSocketMessage(String destination) {
        try {
            logger.log(Level.INFO, "Error in findLastSocketMessage of destination=" + destination + ". Message=");
            return destination;
        } catch (Exception e) {
            logger.log(Level.INFO, "Error in findLastSocketMessage of destination=" + destination + ". Message=" + e.getMessage());
        }
        return "";
    }

    public void saveLastSocketMessages(Map<String, String> messages) {
        long start = System.currentTimeMillis();
        logger.log(Level.INFO, "saveLastSocketMessages updated {0} instances took {1} ms", new Object[]{10, (System.currentTimeMillis() - start)});
    }

    private String getWebsocketMessageTableName() {
        return isBlue ? WEBSOCKET_MESSAGE_TABLE_BLUE : WEBSOCKET_MESSAGE_TABLE_GREEN;
    }
}

