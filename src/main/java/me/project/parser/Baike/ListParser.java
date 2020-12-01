package me.project.parser.Baike;

import me.project.Response;
import me.project.Spider;
import me.project.item.Item;
import me.project.item.Baike.ListItem;
import me.project.parser.Parser;
import me.project.pipeline.ConsolePipeline;
import me.project.pipeline.SpiderPipeline;
import me.project.scheduler.QueueScheduler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ListParser extends Parser {

    ListParser(String url) {
        this.url = url;
    }

    @Override
    public Item process(Response response) {
        ListItem item = new ListItem();

        System.out.println(response.getHtml());

        Document doc = Jsoup.parse(response.getHtml());

        item.setId(1);
        item.setPage(1);
        // get title
        item.setTitle(doc.title());
        // get urls and images
        List<String> images = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        // List<String> nextUrls = new ArrayList<>();
        Elements elements = doc.getElementById("list_article").getElementsByClass("list_article_item");
        for(Element ele : elements) {
            // return the value of the first element that has the specific attribute
            urls.add(ele.getElementsByTag("a").attr("href"));
            images.add(ele.getElementsByTag("img").attr("src"));
        }
        System.out.println("== Finish getting urls and images ==");
        item.setUrls(urls);
        item.setImages(images);

        // nextUrls.add(urls.get(0));
        // item.setNextUrls(nextUrls);


        return item;
    }

    public static void main(String[] args) {

        List<String> downloaderMiddlewares = new LinkedList<>(Arrays.asList(
                "me.project.middleware.SetCookie",
                "me.project.middleware.SetUserAgent"
        ));

        Spider passageSpider = new Spider()
                .parser(new PassageParser())
                .pipeline(new ConsolePipeline())
                .scheduler(new QueueScheduler())
                .setDownloaderMiddlewares(downloaderMiddlewares);

        new Spider()
                .parser(new ListParser("https://www.tuicool.com/ah/0/"))
                .pipeline(new ConsolePipeline())
                .pipeline(new SpiderPipeline().destSpider(passageSpider))
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
