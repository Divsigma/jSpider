package me.project.item;

import com.alibaba.fastjson.JSON;

public class PassageItem extends Item {

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
