package me.project;

import me.project.parser.Parser;

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

    private Pipeline pipeline = null;

    Spider(Parser parser) {
        this.parser = parser;
    }

    private void initComponent() {
        if(this.downloader == null) {
            this.downloader = new Downloader();
        }
        if(this.pipeline == null) {
            this.pipeline = new Pipeline();
        }
    }

    @Override
    public void run() {

        initComponent();

        Response response = this.downloader.download(this.parser.getEntryUrl());

        for (Map.Entry<String, List<String>> entry : response.getHeader().entrySet()) {
            System.out.print(entry.getKey() + ": ");
            for (String item : entry.getValue()) {
                System.out.print(item + " ");
            }
            System.out.println();
        }

        System.out.println(response.getHtml());

        //Item item = this.parser.process(response);
        //this.pipeline.process(item);

    }
}
