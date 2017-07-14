package com.athou.renovace.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by athou on 2016/12/23.
 */

public class RenovaceInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Map<String, String> headerMap = parseHeadMap(request);

        Request.Builder requestBuilder = request.newBuilder();
        //remove renovace_header
        requestBuilder.removeHeader("renovace_header");
        //use headerMap to add new header
        if (headerMap != null && !headerMap.isEmpty()) {
            Iterator iterator = headerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                requestBuilder.addHeader((String) entry.getKey(), (String) entry.getValue());
            }
        }
        //build new request
        request = requestBuilder.build();
        return chain.proceed(request);
    }

    /**
     * read renovace_header from header to map
     * @param request
     * @return
     */
    private Map<String, String> parseHeadMap(Request request) {
        String header = request.header("renovace_header");
        if (header == null || header.equals("{}")) { //if empty header, return directly
            return null;
        }
        int len = header.length();
        header = header.substring(1, len - 1);
        String[] nameAndValues = header.split(",");
        Map<String, String> headerMap = new HashMap<>();
        for (int i = 0; i < nameAndValues.length; i++) {
            String[] temp = nameAndValues[i].split("=");
            if (temp.length == 2) {
                headerMap.put(temp[0].trim(), temp[1].trim());
            }
        }
        return headerMap;
    }
}