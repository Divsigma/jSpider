package me.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {

    private int status;

    private String charset;

    private String html;

    private Request request;

    private Map<String, String> headers;

    private byte[] body;

    public Response(Request request) {
        this.request = request;
        this.headers = new HashMap<>();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setHeaderField(String key, String value) {
        this.headers.put(key, value);
    }

    public String getHeaderField(String key) {
        return this.headers.get(key);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Request getRequest() {
        return request;
    }
}
