package com.programcreek.helloworld.service;

import org.springframework.beans.factory.annotation.Autowired;

public class SercondService {
    @Autowired
    private MyService myService;
    
    public String invokeCacheMethod() {
        return myService.helloCacheResult();
    }
}
