package com.pince.renovace2.interceptor;

import com.pince.renovace2.Util.RenovaceLogUtil;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @author athou
 * @date 2016/12/16
 */

public class LogInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY
    }

    public LogInterceptor() {
        setLevel(RenovaceLogUtil.DEBUG ? Level.BODY : Level.NONE);
    }

    private volatile LogInterceptor.Level level = LogInterceptor.Level.NONE;

    /**
     * Changes the logs level
     */
    public LogInterceptor setLevel(LogInterceptor.Level level) {
        if (level == null) {
            throw new NullPointerException("level == null. Use Level.NONE instead.");
        }
        this.level = level;
        return this;
    }

    public LogInterceptor.Level getLevel() {
        return level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        LogInterceptor.Level level = this.level;

        Request request = chain.request();
        if (level == LogInterceptor.Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == LogInterceptor.Level.BODY;
        boolean logHeaders = logBody || level == LogInterceptor.Level.HEADERS;

        RequestBody requestBody = request.body();
        HttpUrl url = request.url();
        String method = request.method();
        String scheme = url.scheme();
        String host = url.host();
        String path = url.encodedPath();
        String wholePath = scheme + "://" + host + path;

        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage = "--> " + method + ' ' + wholePath + ' ' + protocol;
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        RenovaceLogUtil.logD(requestStartMessage);


        StringBuilder paramSb = new StringBuilder();
        for (int i = 0; i < url.querySize(); i++) {
            paramSb.append("\n");
            paramSb.append(url.queryParameterName(i) + "=" + url.queryParameterValue(i));
        }
        RenovaceLogUtil.logD(String.format("Params:{%s\n}", paramSb.toString()));

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    RenovaceLogUtil.logD("Content-Type: " + requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    RenovaceLogUtil.logD("Content-Length: " + requestBody.contentLength());
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    RenovaceLogUtil.logD(name + ": " + headers.value(i));
                }
            }

            if (!logBody || !hasRequestBody) {
                RenovaceLogUtil.logD("--> END " + method);
            } else if (bodyEncoded(request.headers())) {
                RenovaceLogUtil.logD("--> END " + method + " (encoded body omitted)");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                RenovaceLogUtil.logD("");
                if (isPlaintext(buffer)) {
                    RenovaceLogUtil.logD(buffer.readString(charset));
                    RenovaceLogUtil.logD("--> END " + method + " (" + requestBody.contentLength() + "-byte body)");
                } else {
                    RenovaceLogUtil.logD("--> END " + method + " (binary " + requestBody.contentLength() + "-byte body omitted)");
                }
            }
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            RenovaceLogUtil.logD("<-- HTTP FAILED: " + e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        RenovaceLogUtil.logD("<-- " + response.code() + ' ' + response.message() + ' '
                + wholePath + " (" + tookMs + "ms" + (!logHeaders ? ", "
                + bodySize + " body" : "") + ')');

        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                RenovaceLogUtil.logD(headers.name(i) + ": " + headers.value(i));
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                RenovaceLogUtil.logD("<-- END HTTP");
            } else if (bodyEncoded(response.headers())) {
                RenovaceLogUtil.logD("<-- END HTTP (encoded body omitted)");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8);
                    } catch (UnsupportedCharsetException e) {
                        RenovaceLogUtil.logD("");
                        RenovaceLogUtil.logD("Couldn't decode the response body; charset is likely malformed.");
                        RenovaceLogUtil.logD("<-- END HTTP");

                        return response;
                    }
                }

                if (!isPlaintext(buffer)) {
                    RenovaceLogUtil.logD("");
                    RenovaceLogUtil.logD("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                    return response;
                }

                if (contentLength != 0) {
                    RenovaceLogUtil.logD("");
                    RenovaceLogUtil.logD(buffer.clone().readString(charset));
                }

                RenovaceLogUtil.logD("<-- END HTTP (" + buffer.size() + "-byte body)");
            }
        }

        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}