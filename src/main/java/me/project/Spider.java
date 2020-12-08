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

        // 1. Init index Request (or await status), Pipeline, Downloader
        // and Scheduler
        initComponent();

        // 2. Start crawling from index Request if it is not null
        if(this.index != null) {
            this.scheduler.push(this.index);
        }

        while(!this.scheduler.empty()) {
            // 3. Get a Request from Scheduler and download it, retrieving a Response or Request
            Request request = this.scheduler.poll();
            Object result = this.downloader.download(request);
            if(result.getClass() == Request.class) {
                this.scheduler.push((Request) result);
                continue;
            }
            Response response = (Response) result;

            // 4. Process the Response through Parser, retrieving an Item
            Item item = this.parser.process(response);

            // 5. Add next Request obtained from Item
            if(item.getNextRequest().size() != 0) {
                System.err.println("== Pushing new Request ==");
                for(Request nextRequest : item.getNextRequest()) {
                    try {
                        this.scheduler.push(nextRequest);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Deprecated Annotation:
                    // (1) One Item can generate multiple new Request from
                    //     the original one, so the copy should be a 'deep copy'.
                    // (2) But I only need to change the url of Request (currently),
                    //     which is a String.
                    //     String is a reference type and is immutably stored in
                    //     the constant pool of JVM (different String has different reference).
                    //     So a 'shallow copy' is enough.
                }
            }

            // 6. Process the Item in Pipelines
            for(Pipeline pipeline : this.pipelines) {
                pipeline.process(item);
            }

            System.out.println();

        }

        // release resources ?

    }
}
