package me.project.middleware;

import me.project.Request;
import me.project.Response;

public class SetCookie implements Middleware {

    @Override
    public Object handleRequest(Request request) {
        System.out.println("Passing through Downloader Middleware: SetCookie");

        request.setHeaderDomain("cookie", "_ga=GA1.2.156721159.1596030976; sc_is_visitor_unique=rx11857110.1596502132.");

        return null;
    }

    @Override
    public Object handleResponse(Response response) {

        System.out.println("Passing back through Downloader Middleware: SetCookie");

        return null;
    }
}
