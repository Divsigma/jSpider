package me.project.parser;

import me.project.item.Item;
import me.project.Response;
import me.project.item.PassageItem;
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

        Item item = new PassageItem();

        Document doc = Jsoup.parse(response.getHtml());

        // get title
        item.setTitle(doc.title());
        // get urls
        List<String> urls = new ArrayList<String>();
        Elements elements = doc.getElementById("post_next_prev").getElementsByClass("p_n_p_prefix");
        for(Element ele : elements) {
            System.out.println(ele.toString());
            urls.add(ele.attr("href"));
        }
        item.setUrls(urls);

        return item;

    }
}
