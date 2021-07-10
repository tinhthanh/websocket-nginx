package com.pal.websocketnginx.controller.ws.common;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public interface BasicCache<K, V> {

    /**
     * present size of cache
     *
     * @return
     */
    public int size();

    /**
     * Removes all entries from the cache.
     */
    public void invalidateAll();

    /**
     * get the value for the key.
     *
     * @param key
     * @return
     */
    public V get(K key);

    /**
     * provide the map view of cache in current state. Can be used to iterate over the cache.
     *
     * @return
     */
    public ConcurrentMap<K, V> asMap();

    /**
     *
     * @param key Specify the key to be removed from the cache.
     *
     */
    public V invalidate(K key);

    /**
     * To populate some entry in cache
     *
     * @param key
     * @param value
     */
    public V put(K key, V value);

    /**
     * To populate some entry in cache
     *
     * @param key
     * @param value
     */
    public V putIfAbsent(K key, V value);

    /**
     * To populate all of the mappings from the specified map to the cache
     *
     * @param m
     */
    public void putAll(Map<? extends K, ? extends V> m);

    /**
     * @return all values of the cache
     */
    public Collection<V> values();

    /**
     * @return true if the cache is empty
     */
    public boolean isEmpty();

    /**
     * @return all keys of the cache
     */
    public Set<K> keySet();
}
