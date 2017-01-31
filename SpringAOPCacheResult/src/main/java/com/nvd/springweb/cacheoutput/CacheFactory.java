package com.nvd.springweb.cacheoutput;

public interface CacheFactory {

    <K, V> Cache<K, V> create();
}
