package me.project;

import me.project.item.Item;
import me.project.parser.Parser;
import me.project.pipeline.ConsolePipeline;
import me.project.pipeline.Pipeline;

import java.util.*;

/**
 * Spider Engine
 * 1. Spider push the Request created from url into Scheduler
 * 2. Downloader polls Request from Scheduler and get Response
 * 3. Response is then passed through a Parser, returning Items
 * 4. Item is then passed through Pipeline
 */
public class Spider implements Runnable {

    private List<String> downloaderMiddleware = null;

    private Downloader downloader = null;

    private Parser parser = null;

    private List<Pipeline> pipelines = null;

    Spider() {
        this.downloaderMiddleware = new LinkedList<>();
        this.pipelines = new LinkedList<>();
    }

    public Spider setDownloaderMiddleware(List<String> downloaderMiddleware) {
        this.downloaderMiddleware = downloaderMiddleware;
        return this;
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

    public Spider setPipelines(List<Pipeline> pipelines) {
        this.pipelines = pipelines;
        return this;
    }

    private void initComponent() {
        if(this.pipelines.size() == 0) {
            this.pipelines.add(new ConsolePipeline());
        }
        if(this.downloader == null) {
            this.downloader = new Downloader(downloaderMiddleware);
        }
    }

    @Override
    public void run() {

        initComponent();

        String url = this.parser.getUrl();

        // construct Request from url of Parser
        Request request = new Request(url);

        // download and get Response from Downloader
        Response response = this.downloader.download(request);

        System.out.println("== Response Header ==");
        for (Map.Entry<String, List<String>> entry : response.getHeader().entrySet()) {
            System.out.print(entry.getKey() + ": ");
            for (String item : entry.getValue()) {
                System.out.print(item + " ");
            }
            System.out.println();
        }


        Item item = this.parser.process(response);

        this.pipelines.get(0).process(item);

        // release resources ?

    }
}
