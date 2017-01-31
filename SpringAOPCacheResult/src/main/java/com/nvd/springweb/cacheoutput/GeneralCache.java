package com.nvd.springweb.cacheoutput;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneralCache<K, V> implements Cache<K, V> {

    private static final Logger LOGGER = Logger.getLogger(GeneralCache.class.getName());

    private volatile ConcurrentMap<K, Value<V>> store;

    private long resetTime = generateNextResetTime();

    public GeneralCache(ConcurrentMap<K, Value<V>> store, ScheduledExecutorService janitorScheduler, int janitorScheduleDelay) {
        this.store = store;
        janitorScheduler.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                recycle();
            }
        }, 1, janitorScheduleDelay, TimeUnit.SECONDS);
    }

    public V get(K key) {
        return checkExpireAndReturn(store.get(key));
    }

    public void put(K key, V value, int keepAlive) {
        store.put(key, new Value(value, keepAlive));
    }

    private long generateNextResetTime() {
        return System.currentTimeMillis() + (long) (60000 * 60 * (1 + Math.random()));
    }

    private void recycle() {
        if (System.currentTimeMillis() > resetTime) {
            resetTime = generateNextResetTime();
            store = new ConcurrentHashMap<K, Value<V>>();
            return;
        }
        for (Entry<K, Value<V>> entry : store.entrySet()) {
            if (entry.getValue().isExpired()) {
                if (store.remove(entry.getKey(), entry.getValue())) {
                    LOGGER.log(Level.FINE, "Cache janitor: removed entry {0}", entry.getKey());
                }
            }
        }
    }

    private V checkExpireAndReturn(Value<V> old) {
        if (old == null || old.isExpired()) {
            return null;
        }
        return old.getValue();
    }
}
