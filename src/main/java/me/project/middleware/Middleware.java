package me.project.middleware;

import me.project.Request;
import me.project.Response;

public interface Middleware {

    Object handleRequest(Request request);

    Object handleResponse(Response response);

}
