package com.nvd.springweb.cacheoutput;

public class Value<V> {

    private V value;

    private long timestamp;

    private long expiryTimestamp;

    public Value(V value, int keepAlive) {
        if (keepAlive < 0) {
            throw new IllegalArgumentException("Keep Alive must be greater than 0");
        }
        this.value = value;
        this.timestamp = System.currentTimeMillis();
        if (keepAlive == 0) {
            expiryTimestamp = Long.MAX_VALUE;
        } else {
            expiryTimestamp = timestamp + keepAlive * 1000;
        }
    }

    public V getValue() {
        return value;
    }

    public boolean isExpired() {
        return expiryTimestamp < System.currentTimeMillis();
    }
}
