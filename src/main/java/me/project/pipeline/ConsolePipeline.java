package me.project.pipeline;

import me.project.item.Item;

public class ConsolePipeline implements Pipeline {

    @Override
    public void process(Item item) {

        System.out.println(item.toString());

    }
}
