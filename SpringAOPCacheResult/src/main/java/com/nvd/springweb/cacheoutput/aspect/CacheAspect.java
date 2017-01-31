package com.nvd.springweb.cacheoutput.aspect;

import com.nvd.springweb.cacheoutput.Cache;
import com.nvd.springweb.cacheoutput.CacheFactory;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
public class CacheAspect {

    private static final Logger LOGGER = Logger.getLogger(CacheAspect.class.getName());

    private Cache<String, Object> cache;

    private ConcurrentMap<String, Lock> invocationLock = new ConcurrentHashMap<String, Lock>();

    @Autowired
    void setCacheFactory(CacheFactory cacheFactory) {
        cache = cacheFactory.create();
    }

    @Around(value = "@annotation(cacheResult)", argNames = "cacheResult")
    public Object checkCache(final ProceedingJoinPoint pjp, final CacheResult cacheResult) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String key = "";
        if (cacheResult.args() != null && cacheResult.args().length > 0) {
            key = getKey(signature, pjp.getArgs(), cacheResult);
        }
        if (key == null || key.isEmpty()) {
            key = keygen(signature, pjp.getArgs());
        }
        Object val = cache.get(key);
        if (val != null) {
            return val;
        }
        Lock lck = new ReentrantLock();
        Lock invLock = invocationLock.putIfAbsent(key, lck);
        try {
            if (invLock == null) {
                invLock = lck;
            }
            invLock.lock();
            val = cache.get(key);
            if (val != null) {
                return val;
            }
            try {
                val = pjp.proceed();
                if (val != null) {
                    cache.put(key, val, cacheResult.alive());
                }
            } finally {
                invocationLock.remove(key, invLock);
            }
        } finally {
            invLock.unlock();
        }
        return val;
    }

    private String getKey(MethodSignature signature, Object[] arguments, CacheResult cacheResult) {
        String lockKey = "";
        if (signature != null && cacheResult.args().length > 0) {
            StringBuilder sb = new StringBuilder(signature.toString());
            String[] names = signature.getParameterNames();
            for (String param : cacheResult.args()) {
                String[] keyParams = param.split("\\.");
                Object value = arguments[ArrayUtils.indexOf(names, keyParams[0])];
                if (keyParams.length > 1) {
                    for (int i = 1; i < keyParams.length; i++) {
                        value = getFieldValue(value, keyParams[i]);
                    }
                }
                if (value != null || value instanceof String && StringUtils.isNotBlank((String) value)) {
                    sb.append(",").append(String.valueOf(value));
                }
            }

            lockKey = sb.toString();
        }
        return lockKey;
    }

    private String keygen(MethodSignature signature, Object[] param) {
        StringBuilder sb = new StringBuilder(signature.toString());
        for (Object value : param) {
            sb.append(",");
            sb.append(value == null ? "" : value.toString());
        }
        return sb.toString();
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        if (obj == null) {
            return null;
        }
        if (StringUtils.isBlank(fieldName)) {
            return null;
        }
        Class klazz = obj.getClass();
        while (klazz != null) {
            try {
                Field field = klazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Can not get field " + fieldName, e);
            }
        }
        return null;
    }
}
