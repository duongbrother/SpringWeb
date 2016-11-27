package com.programcreek.helloworld.service;

import java.util.Map;

public class MyService {

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
}
