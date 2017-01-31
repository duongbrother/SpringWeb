package com.nvd.springweb.cacheoutput;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;

public class GeneralCacheFactory implements CacheFactory {
    
    private static final Logger LOGGER = Logger.getLogger(GeneralCacheFactory.class.getName());
    
    private ScheduledExecutorService janitorScheduler;
    
    public GeneralCacheFactory() {
        try {
            Class classDetfaultThreadFactory = Class.forName("java.util.concurrent.Executors$DefaultThreadFactory");
            Constructor[] constructors = classDetfaultThreadFactory.getDeclaredConstructors();
            constructors[0].setAccessible(true);
            Object nameThreadFactory = constructors[0].newInstance();
            Field namePrefixField = classDetfaultThreadFactory.getDeclaredField("namePrefix");
            namePrefixField.setAccessible(true);
            namePrefixField.set(nameThreadFactory, "SimpleCacheFactory-");
            janitorScheduler = Executors.newScheduledThreadPool(10, ThreadFactory.class.cast(nameThreadFactory));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public <K, V> Cache<K, V> create() {
        ConcurrentHashMap<K, Value<V>> store = new ConcurrentHashMap<K, Value<V>>();
        return new GeneralCache<K, V>(store, janitorScheduler, 30);
    }
    
    @PreDestroy
    public void destroy() {
        try {
            janitorScheduler.shutdown();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error while shutting down Janitor Scheduler", e);
        }
    }
    
}
