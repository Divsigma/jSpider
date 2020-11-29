package me.project.middleware;

import me.project.Request;
import me.project.Response;

public class SetUserAgent implements Middleware {

    @Override
    public Object handleRequest(Request request) {
        System.out.println("Passing through Middleware: SetUserAgent");

        request.setHeaderDomain("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");

        return null;
    }

    @Override
    public Object handleResponse(Response response) {

        System.out.println("Passing back through Downloader Middleware: SetUserAgent");

        return null;
    }
}
