package me.project.parser.BaikeDemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.project.Request;
import me.project.Response;
import me.project.Spider;
import me.project.item.Item;
import me.project.item.BaikeDemo.ListItem;
import me.project.parser.Parser;
import me.project.pipeline.ConsolePipeline;
import me.project.scheduler.QueueScheduler;

import java.util.*;

public class ListParser extends Parser {

    private Spider nextSpider;

    public ListParser(Spider nextSpider) {
        this.nextSpider = nextSpider;
    }

    @Override
    public Item process(Response response) {

        System.err.println("Here in ListParser ...");

        ListItem item = new ListItem();

        System.out.println(response.getHtml());

        JSONObject data = (JSONObject) JSON.parse(response.getHtml());

        item.setTitle("list");
        // item.setId(1);
        // item.setPage(0);
        for(Object obj : (List) data.get("lemmaList")) {
            JSONObject ele = (JSONObject) obj;

            Request request = new Request()
                    .setMethod(Request.Method.GET)
                    .setUrl((String) ele.get("lemmaUrl"));

            nextSpider.addRequest(request);
        }

        return item;
    }

    public static void main(String[] args) {

        List<String> downloaderMiddlewares = new LinkedList<>(Arrays.asList(
                "me.project.middleware.SetCookie",
                "me.project.middleware.SetUserAgent",
                "me.project.middleware.Redirect"
        ));

        Request index = new Request()
                .setUrl("https://baike.baidu.com/wikitag/api/getlemmas")
                .setMethod(Request.Method.POST)
                .setHeaderField("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .setBodyField("limit", "24")
                .setBodyField("timeout", "3000")
                .setBodyField("filterTags", "[]")
                .setBodyField("tagId", "76606")
                .setBodyField("fromLemma", "false")
                .setBodyField("contentLength", "40")
                .setBodyField("page", "0");

        Spider passageSpider = new Spider()
                .parser(new PassageParser())
                .pipeline(new ConsolePipeline())
                .scheduler(new QueueScheduler())
                .setDownloaderMiddlewares(downloaderMiddlewares);

        new Spider(index)
                .parser(new ListParser(passageSpider))
                //.pipeline(new ConsolePipeline())
                .setDownloaderMiddlewares(downloaderMiddlewares)
                .run();

        /*
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        passageSpider.run();

    }

}
