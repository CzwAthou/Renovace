package com.pince.renovace2.cache;

/**
 * CacheStrategy
 *
 * @author athou
 * @date 2017/12/13
 */
public enum CacheStrategy {

    None("none"),
    /**
     * if network is available and the request during max-age time({@link HttpCache#maxAge}), so get cache first<br>
     * then if  network is unavailable, get data from cache.
     */
    CacheFirst("CacheFirst"),
    /**
     * if network is available, so get data from network whatever<br>
     * then if  network is unavailable, get data from cache.
     */
    NetWorkFirst("NetWorkFirst");

    String value;

    CacheStrategy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}