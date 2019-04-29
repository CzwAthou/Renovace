package com.renovace.interceptor;

import com.renovace.header.HeaderKey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author athou
 * @date 2016/12/23
 */

public class RenovaceInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        List<String> headParams = parseHeadParams(request);
        Chain newChain = parseHeadTimeParams(chain, headParams);
        Map<String, String> headerMap = parseHeadMap(headParams);

        Request.Builder requestBuilder = request.newBuilder();
        //remove renovace_header
        requestBuilder.removeHeader(HeaderKey.Header);
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
        return newChain.proceed(request);
    }

    /**
     * 解析renovaceheader
     *
     * @param request
     * @return [catch=none,readTimeOut=200]
     */
    private List<String> parseHeadParams(Request request) {
        String header = request.header(HeaderKey.Header);
        if (header == null || header.equals("{}")) {
            //if empty header, return directly
            return null;
        }
        int len = header.length();
        header = header.substring(1, len - 1);
        return Arrays.asList(header.split(","));
    }

    /**
     * 解析读，写，连接超时，并从header中移除对应配置
     *
     * @param chain
     * @param nameAndValues
     * @return
     */
    private Chain parseHeadTimeParams(Chain chain, List<String> nameAndValues) {
        if (nameAndValues == null || nameAndValues.isEmpty()) {
            return chain;
        }
        Chain chainTemp = chain;
        List<String> willMove = new ArrayList<>();
        for (String nameAndValue : nameAndValues) {
            String[] temp = nameAndValue.split("=");
            if (temp.length == 2) {
                String key = temp[0].trim();
                String value = temp[1].trim();
                if (HeaderKey.ConnectTimeOut.equals(key)) {
                    try {
                        chainTemp = chainTemp.withConnectTimeout(Integer.parseInt(value), TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                    } finally {
                        willMove.add(nameAndValue);
                    }
                } else if (HeaderKey.WriteTimeOut.equals(key)) {
                    try {
                        chainTemp = chainTemp.withWriteTimeout(Integer.parseInt(value), TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                    } finally {
                        willMove.add(nameAndValue);
                    }
                } else if (HeaderKey.ReadTimeOut.equals(key)) {
                    try {
                        chainTemp = chainTemp.withReadTimeout(Integer.parseInt(value), TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                    } finally {
                        willMove.add(nameAndValue);
                    }
                }
            }
        }
        nameAndValues.removeAll(willMove);
        return chainTemp;
    }

    /**
     * read renovace_header from header to map
     *
     * @return
     */
    private Map<String, String> parseHeadMap(List<String> nameAndValues) {
        if (nameAndValues == null || nameAndValues.isEmpty()) {
            return null;
        }
        Map<String, String> headerMap = new HashMap<>();
        for (String nameAndValue : nameAndValues) {
            String[] temp = nameAndValue.split("=");
            if (temp.length == 2) {
                headerMap.put(temp[0].trim(), temp[1].trim());
            }
        }
        nameAndValues.clear();
        return headerMap;
    }
}