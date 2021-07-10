package com.pal.websocketnginx.controller.ws.controller;

import com.pal.websocketnginx.controller.ws.transfer.LastMessageCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("last-message")
public class LastMessageController {

    @Autowired
    private LastMessageCache lastMessageCache;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getLastMessage(@RequestParam String destination) {
        return lastMessageCache.get(destination);
    }

}
