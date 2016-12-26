package com.athou.renovace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by athou on 2016/12/23.
 */

public class RequestParams {

    protected ParamsWrapper paramsWrapper = null;
    protected HeaderWrapper headerWrapper = null;

    public static RequestParams emptyParams = new RequestParams();

    public RequestParams() {
        this(null);
    }

    public RequestParams(RenovaceCache.CacheStrategy cacheStrategy) {
        this.paramsWrapper = new ParamsWrapper();
        this.headerWrapper = new HeaderWrapper();

        if (cacheStrategy != null) {
            addHeader(RenovaceCache.Key_setCache, cacheStrategy.getValue());
        }
    }

    public void put(String key, String value) {
        this.paramsWrapper.put(key, value);
    }

    public void put(String key, int value) {
        this.paramsWrapper.put(key, String.valueOf(value));
    }

    public void put(String key, float value) {
        this.paramsWrapper.put(key, String.valueOf(value));
    }

    public void put(String key, long value) {
        this.paramsWrapper.put(key, String.valueOf(value));
    }

    public void put(String key, boolean value) {
        this.paramsWrapper.put(key, String.valueOf(value));
    }

    public void remove(String key) {
        this.paramsWrapper.remove(key);
    }

    public boolean has(String key) {
        return this.paramsWrapper.has(key);
    }

    public void addHeader(String key, String value) {
        this.headerWrapper.add(key, value);
    }

    public void addHead(Map<String, String> headers) {
        this.headerWrapper.add(headers);
    }

    public void removeHeader(String key) {
        this.headerWrapper.remove(key);
    }

    public boolean hasHeader(String key) {
        return this.headerWrapper.has(key);
    }

    public boolean isSetCache() {
        return hasHeader(RenovaceCache.Key_setCache);
    }

    public Map<String, String> getParams() {
        return paramsWrapper.paramsMap;
    }

    public Map<String, String> getHeader() {
        return headerWrapper.headMap;
    }

    @Override
    public String toString() {
        List arrayList = new ArrayList(getParams().entrySet());

        Collections.sort(arrayList, new Comparator() {
            public int compare(Object o1, Object o2) {  //  将HASHMAP中的数据排序
                Map.Entry obj1 = (Map.Entry) o1;
                Map.Entry obj2 = (Map.Entry) o2;
                return ((String) obj1.getKey()).compareTo((String) obj2.getKey());
            }
        });

        StringBuilder result = new StringBuilder();
        for (Iterator iter = arrayList.iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (result.length() > 0) {
                result.append("&");
            }
            result.append((String) entry.getKey());
            result.append("=");
            result.append((String) entry.getValue());
        }
        return result.toString();
    }

    class ParamsWrapper {
        private Map<String, String> paramsMap = null;

        public ParamsWrapper() {
            this.paramsMap = new ConcurrentHashMap<>();
        }

        public void put(String key, String value) {
            if (key != null && value != null) {
                this.paramsMap.put(key, value);
            }
        }

        public void put(String key, int value) {
            if (key != null) {
                this.paramsMap.put(key, String.valueOf(value));
            }
        }

        public void put(String key, float value) {
            if (key != null) {
                this.paramsMap.put(key, String.valueOf(value));
            }
        }

        public void put(String key, long value) {
            if (key != null) {
                this.paramsMap.put(key, String.valueOf(value));
            }
        }

        /**
         * When the boolean is true wil put "true", otherwise wil put "false".
         *
         * @param key
         * @param value true or false
         */
        public void put(String key, boolean value) {
            if (key != null) {
                this.paramsMap.put(key, String.valueOf(value));
            }
        }

        public void remove(String key) {
            this.paramsMap.remove(key);
        }

        public boolean has(String key) {
            return this.paramsMap.containsKey(key);
        }
    }

    class HeaderWrapper {
        private Map<String, String> headMap = null;

        public HeaderWrapper() {
            this.headMap = new ConcurrentHashMap<>();
        }

        public void add(String key, String value) {
            this.headMap.put(key, value);
        }

        public void add(Map<String, String> headers) {
            this.headMap.putAll(headers);
        }

        public void remove(String key) {
            this.headMap.remove(key);
        }

        public boolean has(String key) {
            return this.headMap.containsKey(key);
        }
    }
}
