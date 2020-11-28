package me.project.passable;

import java.util.List;
import java.util.Map;

public class Response extends Passable {

    private Map<String, List<String>> header;

    private String encoding;

    private String html;

    public Response() {
    }

    public Map<String, List<String>> getHeader() {
        return header;
    }

    public void setHeader(Map<String, List<String>> header) {
        this.header = header;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        if(encoding == null) {
            this.encoding = "utf-8";
        } else {
            this.encoding = encoding;
        }
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
