package me.project.parser.Baike;

import me.project.Response;
import me.project.item.Baike.PassageItem;
import me.project.item.Item;
import me.project.parser.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PassageParser extends Parser {

    @Override
    public Item process(Response response) {

        PassageItem item = new PassageItem();

        Document doc = Jsoup.parse(response.getHtml());

        if(doc.getElementsByTag("title").size() == 0) {
            System.out.println("NOT THIS PASSAGE");
            return item;
        }

        item.setTitle(doc.getElementsByTag("title").get(0).html());

        return item;
    }
}
