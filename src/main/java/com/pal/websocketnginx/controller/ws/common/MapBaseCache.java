package com.pal.websocketnginx.controller.ws.common;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapBaseCache<K, V> implements BasicCache<K, V> {

    private final ConcurrentMap<K, V> cacheMap = new ConcurrentHashMap<>();

    @Override
    public int size() {
        return cacheMap.size();
    }

    @Override
    public void invalidateAll() {
        cacheMap.clear();
    }

    @Override
    public V get(K key) {
        return cacheMap.get(key);
    }

    @Override
    public ConcurrentMap<K, V> asMap() {
        return cacheMap;
    }

    @Override
    public V invalidate(K key) {
        return cacheMap.remove(key);
    }

    @Override
    public V put(K key, V value) {
        return cacheMap.put(key, value);
    }

    public V putIfAbsent(K key, V value) {
        return cacheMap.putIfAbsent(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        cacheMap.putAll(m);
    }

    @Override
    public Collection<V> values() {
        return cacheMap.values();
    }

    @Override
    public boolean isEmpty() {
        return cacheMap.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return cacheMap.keySet();
    }

}
