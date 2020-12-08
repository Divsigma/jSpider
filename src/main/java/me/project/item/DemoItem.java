package me.project.item;

import com.alibaba.fastjson.JSON;

public class DemoItem extends Item {

    private int id;

    private int page = 0;

    private String title;

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
