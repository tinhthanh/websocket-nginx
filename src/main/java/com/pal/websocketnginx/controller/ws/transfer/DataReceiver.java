package com.pal.websocketnginx.controller.ws.transfer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/socket-receiver")
public class DataReceiver {

    private static final Logger logger = Logger.getLogger(DataReceiver.class.getName());

    @Autowired
    private DataDispatcherService dispatcherService;

    @Autowired
    private LastMessageCache lastMessageCache;

    @Autowired
    private LastMessageStorage lastMessageStorage;


    private volatile Map<String, String> lastMessageQueue = new HashMap<>();

    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public DeferredResult<String> receiveData(@RequestParam("destination") String destination, @RequestBody String data, HttpServletRequest request) {
        DeferredResult<String> result = new DeferredResult<>();
        lastMessageCache.put(destination, data);
        logger.log(Level.INFO, "DataReceiver > receiveData dest={0}, data={1}, source_ip={2}", new Object[]{destination, data, getTrueClientIPAddress(request)});
        dispatcherService.dispatch(destination, data);
        lastMessageQueue.put(destination, data);
        result.setResult("OK");
        return result;
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 5000)
    public void storeLastMessageToDB() {
        Map<String, String> messageMap = lastMessageQueue;
        lastMessageQueue = new HashMap<>();
        if (!messageMap.isEmpty() ) {
            lastMessageStorage.saveLastSocketMessages(messageMap);
        }
    }

    private String getTrueClientIPAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("TRUE-CLIENT-IP");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getHeader("X-FORWARDED-FOR");
        }
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        } else if (ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }
        return StringUtils.isBlank(ipAddress) ? StringUtils.EMPTY : ipAddress;
    }

    @PreDestroy
    public void stop() {
        lastMessageQueue.clear();
        logger.log(Level.INFO, "{0} stopped", DataReceiver.class.getSimpleName());
    }
}
