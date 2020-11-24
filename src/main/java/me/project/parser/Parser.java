package me.project.parser;

import me.project.item.Item;
import me.project.Response;

public abstract class Parser {

    protected String url;

    protected String encoding = "utf-8";

    abstract public Item process(Response response);

    public String getUrl() {
        return url;
    }
}
