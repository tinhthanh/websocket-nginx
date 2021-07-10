package com.pal.websocketnginx.controller.ws.transfer;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class LastMessageCache {

    private static final Logger logger = Logger.getLogger(LastMessageCache.class.getName());

    @Autowired
    private LastMessageStorage lastMessageStorage;

    private LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(new LastMessageCacheLoader());

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public String get(String key) {
        try {
            return cache.get(key);
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Last message not found for destination: " + key);
        }
        return null;
    }

    public ConcurrentMap<String, String> asMap() {
        return cache.asMap();
    }

    private class LastMessageCacheLoader extends CacheLoader<String, String> {
        @Override
        public String load(String key) throws Exception {
            return lastMessageStorage.findLastSocketMessage(key);
        }
    }
}
