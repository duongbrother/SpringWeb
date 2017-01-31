package com.nvd.springweb.cacheoutput;

public interface Cache<K, V> {

    V get(K key);

    void put(K key, V value, int keepAlive);
}
