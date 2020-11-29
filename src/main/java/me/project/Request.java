package me.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    private String url;

    private Map<String, String> header;

    public Request(String url) {
        this.url = url;
        this.header = new HashMap<>();
    }

    public String getUrl() {
        return url;
    }

    public void setHeaderDomain(String key, String value) {
        this.header.put(key, value);
    }

    public Map<String, String> getHeader() {
        return header;
    }
}
