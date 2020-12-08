package me.project.item.BaikeDemo;

import me.project.Request;
import me.project.item.Item;

import java.util.ArrayList;

public class ListItem extends Item {

    private String title;

    public ListItem(Request request) {
        this.request = request;
        this.nextRequests = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
