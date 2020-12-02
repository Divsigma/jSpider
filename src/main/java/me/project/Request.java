package me.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    private String url;

    private String method;

    private String contentType;

    private String encoding;

    private Map<String, String> headers;

    private Map<String, String> cookies;

    public Request(String url) {
        this.url = url;
        this.headers = new HashMap<>();
    }

    public String getUrl() {
        return url;
    }

    public void setHeaderDomain(String key, String value) {
        this.headers.put(key, value);
    }

    public Map<String, String> getHeader() {
        return headers;
    }
}
