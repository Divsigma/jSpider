package me.project.item.BaikeDemo;

import me.project.Request;
import me.project.item.Item;

import java.util.ArrayList;

public class PassageItem extends Item {

    private int page;

    private String title;

    public PassageItem(Request request) {
        this.request = request;
        this.nextRequests = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
