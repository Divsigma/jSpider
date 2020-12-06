package me.project.pipeline;

import me.project.item.Item;

public interface Pipeline {

    void process(Item item);  // 不用public？省略不写，即public abstract

}
