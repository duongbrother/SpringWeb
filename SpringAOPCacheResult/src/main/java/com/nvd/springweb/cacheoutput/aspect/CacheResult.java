package com.nvd.springweb.cacheoutput.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CacheResult {
    /**
     * Time to keep an element be alive in cache
     * @return 
     */
    int alive() default 1;
    
    String[] args() default {};
    
}
