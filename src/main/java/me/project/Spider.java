package me.project;

import me.project.item.Item;
import me.project.parser.Parser;
import me.project.pipeline.ConsolePipeline;
import me.project.pipeline.Pipeline;
import me.project.scheduler.QueueScheduler;
import me.project.scheduler.Scheduler;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.*;

/**
 * Spider Engine
 * 1. Spider push the Request created from url into Scheduler
 * 2. Downloader polls Request from Scheduler and get Response
 * 3. Response is then passed through a Parser, returning Items
 * 4. Item is then passed through Pipeline
 */
public class Spider implements Runnable {

    private Request index;

    private Scheduler scheduler = null;

    private List<String> downloaderMiddlewares = null;

    private Downloader downloader = null;

    private Parser parser = null;

    private List<Pipeline> pipelines = null;

    public Spider() {
        this.downloaderMiddlewares = new LinkedList<>();
        // should be initialized before run() is called
        this.pipelines = new LinkedList<>();
    }

    public Spider(Request index) {
        this();
        this.index = index;
    }

    public Spider setDownloaderMiddlewares(List<String> downloaderMiddlewares) {
        this.downloaderMiddlewares = downloaderMiddlewares;
        return this;
    }

    // to make Spider can be created and initialized in a chain
    public Spider scheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

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

    public void addRequest(Request request) {
        this.scheduler.push(request);
    }

    private void initComponent() {
        if(this.index == null) {

        }
        if(this.pipelines.size() == 0) {
            this.pipelines.add(new ConsolePipeline());
        }
        if(this.downloader == null) {
            this.downloader = new Downloader(downloaderMiddlewares);
        }
        if(this.scheduler == null) {
            this.scheduler = new QueueScheduler();
        }
    }

    @Override
    public void run() {

        initComponent();

        // construct Request from url of Parser and push it to Scheduler
        if(this.index != null) {
            this.scheduler.push(this.index);
        }

        while(!this.scheduler.empty()) {
            // obtain a Request from Scheduler
            Request request = this.scheduler.poll();

            // download and get Response from Downloader
            Object result = this.downloader.download(request);
            if(result == null) {
                continue;
            }
            if(result.getClass() == Request.class) {
                this.scheduler.push((Request) result);
                continue;
            }

            Response response = (Response) result;

            // process the Response and push new Request into scheduler
            System.out.println("== Pushing new Request ==");
            Item item = this.parser.process(response);
            if(item.getNextUrls() != null) {
                for(String url : item.getNextUrls()) {
                    this.scheduler.push(new Request(url));
                }
            }

            // process the Item in Pipelines
            for(Pipeline pipeline : this.pipelines) {
                pipeline.process(item);
            }

            System.out.println();

        }

        // release resources ?

    }
}
