package com.athou.renovace;

import android.content.Context;
import android.util.Log;

import java.io.File;

import okhttp3.Cache;

/**
 * Created by athou on 2016/12/26.
 */

public class RenovaceCache {

    public static final String Key_setCache = "renovace_set_cache";

    //set cahe times is 3 days
    public static final int maxStale = 60 * 60 * 24 * 3;
    // read from cache for 60 s
    public static final int maxAge = 60;
    //cache file's max size
    public static final int maxSize = 10 * 1024 * 1024;// 10 MB

    /**
     * CacheStrategy
     */
    public enum CacheStrategy {
        /**
         * if network is available and the request during max-age time({@link #maxAge}), so get cache first<br>
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

    /**
     * create the cache file to store the cache
     *
     * @param context the Context can get the CacheDir {@link Context#getCacheDir()}
     * @see #getCache(File)
     */
    public static Cache getCache(Context context) {
        return getCache(new File(context.getCacheDir(), "renovace-cache"));
    }

    /**
     * create the cache file to store the cache
     *
     * @param path the path will store cache
     * @see #getCache(File)
     */
    public static Cache getCache(String path) {
        return getCache(new File(path));
    }

    /**
     * create the cache file to store the cache, and max size is 10mb
     *
     * @param cacheFile the file will store cache
     */
    public static Cache getCache(File cacheFile) {
        Cache cache = null;
        try {
            cache = new Cache(cacheFile, maxSize);
        } catch (Exception e) {
            Log.e("cache", "Could not create Cache!");
        }
        return cache;
    }
}
