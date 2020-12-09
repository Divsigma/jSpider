package me.project.middleware;

import me.project.Request;
import me.project.Response;
import me.project.utils.HttpConstant;

public class Redirect implements Middleware {

    @Override
    public Object handleRequest(Request request) {
        return null;
    }

    @Override
    public Object handleResponse(Response response) {
        System.err.println("Redirecting ...");
        if(response.getStatus() == HttpConstant.StatusCode.CODE_301
                || response.getStatus() == HttpConstant.StatusCode.CODE_302) {
            String url = response.getHeaderField("Location");
            Request request = response.getRequest();
            request.setUrl(url);
            return request;
        }
        return null;
    }
}
