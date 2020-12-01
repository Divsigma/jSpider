package me.project.item;

import com.alibaba.fastjson.JSON;

import java.util.List;

public abstract class Item {

    private int id = 0;

    private int page = 0;

    private String title;

    private List<String> urls;

    private List<String> images;

    private List<String> nextUrls;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getNextUrls() {
        return nextUrls;
    }

    public void setNextUrls(List<String> nextUrls) {
        this.nextUrls = nextUrls;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
