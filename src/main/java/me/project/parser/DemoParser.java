package me.project.parser;

import me.project.item.Item;
import me.project.Response;
import me.project.item.DemoItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DemoParser extends Parser {

    public DemoParser(String url) {
        this.url = url;
    }

    public DemoParser(String url, String encoding) {
        this.url = url;
        this.encoding = encoding;
    }

    @Override
    public Item process(Response response) {

        DemoItem item = new DemoItem();

        Document doc = Jsoup.parse(response.getHtml());

        item.setId(1);
        item.setPage(1111);
        // get title
        item.setTitle(doc.title());
        // get urls and images
        List<String> images = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        List<String> nextUrls = new ArrayList<>();
        Elements elements = doc.getElementById("list_article").getElementsByClass("list_article_item");
        for(Element ele : elements) {
            // return the value of the first element that has the specific attribute
            urls.add(ele.getElementsByTag("a").attr("href"));
            images.add(ele.getElementsByTag("img").attr("src"));
        }
        System.out.println("== Finish getting urls and images ==");
        item.setUrls(urls);
        item.setImages(images);

        nextUrls.add(urls.get(0));
        // item.setNextUrls(nextUrls);


        return item;

    }
}
