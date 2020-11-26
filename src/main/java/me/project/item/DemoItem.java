package me.project.item;

import com.alibaba.fastjson.JSON;

public class DemoItem extends Item {

    private int page = 0;

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
