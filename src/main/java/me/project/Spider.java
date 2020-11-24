package me.project;

import me.project.item.Item;
import me.project.parser.Parser;
import me.project.pipeline.ConsolePipeline;
import me.project.pipeline.Pipeline;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Spider Engine
 * 1. Spider push the Request created from url into Scheduler
 * 2. Downloader polls Request from Scheduler and get Response
 * 3. Response is then passed through a Parser, returning Items
 * 4. Item is then passed through Pipeline
 */
public class Spider implements Runnable {

    private Downloader downloader = null;

    private Parser parser = null;

    private List<Pipeline> pipelines = null;

    Spider() {
        if(this.downloader == null) {
            this.downloader = new Downloader();
        }
        if(this.pipelines == null) {
            this.pipelines = new LinkedList<Pipeline>();
            this.pipelines.add(new ConsolePipeline());
        }
    }


    // make Spider can be created and initialized in a chain
    public Spider parser(Parser parser) {
        this.parser = parser;
        return this;
    }

    public Spider pipeline(Pipeline pipeline) {
        this.pipelines.add(pipeline);
        return this;
    }

    private void initComponent() {
    }

    @Override
    public void run() {

        Response response = this.downloader.download(this.parser.getUrl());

        for (Map.Entry<String, List<String>> entry : response.getHeader().entrySet()) {
            System.out.print(entry.getKey() + ": ");
            for (String item : entry.getValue()) {
                System.out.print(item + " ");
            }
            System.out.println();
        }

        System.out.println(response.getHtml());

        Item item = this.parser.process(response);
        this.pipelines.get(0).process(item);

    }
}
