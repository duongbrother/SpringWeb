package com.programcreek.helloworld.service;

import com.nvd.springweb.cacheoutput.aspect.CacheResult;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyService {

    private static final Logger logger = Logger.getLogger(MyService.class.getName());

    private Map<String, String> myMap;

    public MyService(Map<String, String> myMap) {
        this.myMap = myMap;
    }

    public String addElement(String key, String value) {
        return myMap.put(key, value);
    }

    public String printMap() {
        return myMap.toString();
    }

    @CacheResult(alive = 60)
    public String helloCacheResult() {
        logger.log(Level.INFO, "Invoking helloCacheResult...");
        return "Hello CacheResult";
    }
}
