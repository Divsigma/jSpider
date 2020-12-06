package me.project.middleware;

import me.project.Request;
import me.project.Response;

public class Redirect implements Middleware {

    @Override
    public Object handleRequest(Request request) {
        return null;
    }

    @Override
    public Object handleResponse(Response response) {
        System.err.println("Redirecting ...");
        if(response.getStatus() == 301 || response.getStatus() == 302) {
            String url = response.getHeaderField("Location");
            Request request = response.getRequest();
            request.setUrl(url);
            return request;
        }
        return null;
    }
}
