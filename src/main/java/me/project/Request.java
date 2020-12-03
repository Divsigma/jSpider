package me.project;

import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Request {

    private String url;

    private String method;

    private String contentType;

    private String encoding = "utf-8";

    private Map<String, String> headers;

    private Map<String, String> cookies;

    private Map<String, String> body;

    public Request() {
        this.headers = new HashMap<>();
        this.cookies = new HashMap<>();
        this.body = new HashMap<>();
    }

    public Request(String url) {
        this();
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Request setUrl(String url) {
        this.url = url;
        return this;
    }

    public Request setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getMethod() {
        return this.method;
    }

    public Request setHeaderField(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public Request setBodyField(String key, String value) {
        this.body.put(key, value);
        return this;
    }

    public String getBodyString() {
        StringJoiner joiner = new StringJoiner("&");
        try {
            for(Map.Entry<String, String> entry : body.entrySet()) {
                joiner.add(URLEncoder.encode(entry.getKey(), encoding) + "=" + URLEncoder.encode(entry.getValue(), encoding));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return joiner.toString();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public static abstract class ContentType {

        public static final String JSON = "application/json";

        public static final String FORM = "application/x-www-form-urlencoded";

    }

    public static abstract class Method {

        public static final String POST = "POST";

        public static final String GET = "GET";

    }

}


