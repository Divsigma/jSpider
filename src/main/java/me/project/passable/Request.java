package me.project.passable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request extends Passable {

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
