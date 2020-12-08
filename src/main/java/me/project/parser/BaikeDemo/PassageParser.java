package me.project.parser.BaikeDemo;

import me.project.Response;
import me.project.item.BaikeDemo.PassageItem;
import me.project.item.Item;
import me.project.parser.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PassageParser extends Parser {

    @Override
    public Item process(Response response) {

        System.err.println("Here in PassageParser ...");

        PassageItem item = new PassageItem(response.getRequest());

        Document doc = Jsoup.parse(response.getHtml());

        if(doc.getElementsByTag("title").size() == 0) {
            System.out.println("NOT THIS PASSAGE");
            return item;
        }

        item.setTitle(doc.getElementsByTag("title").get(0).html());
        item.setPage(Integer.parseInt(item.getRequest().getBodyField("page")));

        return item;
    }
}
