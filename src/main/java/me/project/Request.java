package me.project;

import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Request implements Cloneable {

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

    public Request setCookieField(String key, String value) {
        this.cookies.put(key, value);
        return this;
    }

    public String getCookieField(String key) {
        return this.cookies.get(key);
    }

    public Request setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
        return this;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public String getCookieString() {

        if(cookies.size() == 0) {
            return null;
        }

        // does it need URLEncode ???
        // cookieString will be added in header by Middleware as a String
        StringJoiner joiner = new StringJoiner("; ");
        try {
            for(Map.Entry<String, String> entry : cookies.entrySet()) {
                if(entry.getValue().equals("")) {
                    joiner.add(entry.getKey());
                } else {
                    joiner.add(entry.getKey() + "=" + entry.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return joiner.toString();
    }

    public Request setBodyField(String key, String value) {
        this.body.put(key, value);
        return this;
    }

    public String getBodyField(String key) {
        return this.body.get(key);
    }

    // if I have already get bytes as UTF-8 in downloader,
    // do it need to encode body string according to `this.encoding` here ?
    // (I drop it out, it also works ...)
    // should I get bytes as `this.encoding` in downloader ?
    // (I try changing it to other charset, it also works ...)
    public String getBodyString() {
        StringJoiner joiner = new StringJoiner("&");
        try {
            for(Map.Entry<String, String> entry : body.entrySet()) {
                joiner.add(entry.getKey() + "=" + entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return joiner.toString();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}


