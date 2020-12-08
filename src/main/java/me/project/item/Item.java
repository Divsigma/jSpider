package me.project.item;

import com.alibaba.fastjson.JSON;
import me.project.Request;

import java.util.List;

public abstract class Item {

    protected Request request;

    protected List<String> urls;

    protected List<Request> nextRequests;

    public Request getRequest() {
        return request;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<Request> getNextRequest() {
        return nextRequests;
    }

    public void addNextRequest(Request request) {
        this.nextRequests.add(request);
    }

    @Override
    public String toString() {
    return JSON.toJSONString(this);
    }
}
