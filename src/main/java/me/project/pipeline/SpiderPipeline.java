package me.project.pipeline;

import me.project.Request;
import me.project.Spider;
import me.project.item.Item;

public class SpiderPipeline implements Pipeline {

    private Spider destSpider;

    public SpiderPipeline destSpider(Spider destSpider) {
        this.destSpider = destSpider;
        return this;
    }

    @Override
    public void process(Item item) {

        if(item.getUrls() != null) {
            for(String url : item.getUrls()) {
                System.out.println("url: " + url);
                if(destSpider == null) {
                    System.out.println("destSpider is NULL !!!");
                }
                destSpider.addRequest(new Request(url));
            }
        }

    }
}
