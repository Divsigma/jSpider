package me.project.parser;

import me.project.Item;
import me.project.Response;

public class Parser
{

    private String entryUrl;

    private String encoding = "utf-8";

    public Parser(String url, String encoding)
    {
        this.entryUrl = url;
        this.encoding = encoding;
    }

    public Item process(Response response) {
        Item item = new Item();
        return item;
    }

    public String getEntryUrl()
    {
        return entryUrl;
    }

    public String getEncoding()
    {
        return encoding;
    }

}
